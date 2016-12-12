/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.DocumentProcessors.CsvProcessor;
import ire.Objects.MemoryIndex;
import ire.Objects.Result;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.tuple.Pair;
import org.nustaq.serialization.FSTObjectInput;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Searcher {
    private HashMap<Integer, Pair<Integer,Integer>> documents;
    private BidiMap<String,Integer> filesMapping;
    private BidiMap<String, Integer> words;
    private Result[] results;
    private final Tokenizer tokenizer;
    private final MemoryIndexManager indexManager;
    private final int numResults;
        
    public Searcher(String indexPath, String stopWordsFile, int numResults, int maxIndex, boolean debug){
        documents = new HashMap<>();
        filesMapping = new DualHashBidiMap();
        words = new DualHashBidiMap();
        results = null;
        tokenizer = new Tokenizer(loadStopWords(stopWordsFile), debug);
        this.numResults = numResults;
        this.indexManager = new MemoryIndexManager(maxIndex, indexPath);        
    }
    
    public Result[] search(String query){
        String [] tokens = tokenizer.tokenize(query);
        HashMap<String, Double> idfs = calculateIdf(tokens);
        HashMap<String, Double> queryWeights = calculateWeight(tokens, idfs);
        return hash2array(calculateResult(queryWeights));
    }
    
    private Result[] hash2array(HashMap<Integer, Double> scores){
        Comparator<Result> comparator = (Result a, Result b) -> a.compareTo(b);
        SortedSet<Result> resul = new TreeSet<>(comparator);
        for(HashMap.Entry<Integer, Double> e : scores.entrySet()){ 
            
            resul.add(new Result(filesMapping.getKey(documents.get(e.getKey()).getLeft()), documents.get(e.getKey()).getRight(),  e.getKey(),e.getValue()));

        }
        results = resul.toArray(new Result[0]);
        return Arrays.copyOfRange(results, 0, numResults);
    }
            
    public Result[] getResultsPage(int page){
        if(numResults*page > results.length || page < 1){
            return null;
        }
        return Arrays.copyOfRange(results, numResults*(page-1), numResults*page);
    }
    
    public int getPageCount(){
        return results.length/numResults;
    }

    private HashMap<Integer, Double> calculateResult(HashMap<String, Double> queryWeights){
        
        
        HashMap<Integer, Double> scores = new HashMap<>();
        for(HashMap.Entry<String, Double> entry : queryWeights.entrySet()){ 
            HashMap<Integer, Double> postList = getPostingList(entry.getKey());
            if(postList == null){
                continue;
            }
            for(HashMap.Entry<Integer, Double> e : postList.entrySet()){
                double tmpScore = entry.getValue()*e.getValue();
                
                if(scores.containsKey(e.getKey())){
                   tmpScore += scores.get(e.getKey());
                   scores.put(e.getKey(), tmpScore);
                }
                else{
                    scores.put(e.getKey(), tmpScore);
                    
                }
            }
        }
        return scores;
    }
    
    private HashMap<String, Double> calculateIdf(String [] tokens){
        HashMap<String, Double> idfs = new HashMap<>();
        for(String s : tokens){
            HashMap<Integer, Double> postList = getPostingList(s);
            if(postList == null){
                continue;
            }
            idfs.put(s, Math.log(documents.size()/postList.size()));
        }
        return idfs;
    }
    
    private HashMap<Integer, Double> getPostingList(String term){
        
        int termId;
        if(words.containsKey(term)){
            termId = words.get(term);
        }
        else return null;
        // Classe para guardar indices em memoria
        //TODO
        MemoryIndex i = indexManager.loadIndex(term.charAt(0));
        HashMap<Integer, HashMap<Integer,Double>> index = i.getIndex();
        if(index.containsKey(termId)){
            return index.get(termId);
        }
        return null;
    }
    
    private HashMap<String, Double> calculateWeight(String[] tokens, HashMap<String, Double> idfs){
        //Calcular pesos palavras e passar ao indexer para dar merge ao indice global.
            HashMap<String,Integer> counts = new HashMap<>();
            
            int count;
            for (String token : tokens) {
                if (!counts.containsKey(token)) {
                    counts.put(token, 1);
                } else {
                    count = counts.get(token);
                    count++;
                    counts.replace(token, count);
                }
            }
            double sum = 0;
            double tmp = 0;
            HashMap<String,Double> weights = new HashMap<>();
            for(HashMap.Entry<String, Integer> entry : counts.entrySet()){ 
                tmp = 1+Math.log(counts.get(entry.getKey()));
                tmp = tmp*idfs.get(entry.getKey());
                sum += Math.pow(tmp, 2);
                
                weights.put(entry.getKey(), tmp);
            }
            double doc_length = Math.sqrt(sum);
            
            for(HashMap.Entry<String, Double> entry : weights.entrySet()){ 
                
                weights.put(entry.getKey(), entry.getValue()/doc_length);
            }
            return weights;
    }
    
    
    public void loadWords(String wordsPath){
        try {
            FSTObjectInput in = new FSTObjectInput(new FileInputStream(wordsPath));
            words = (BidiMap<String, Integer>)in.readObject();
            in.close();
            
        } catch (IOException | ClassNotFoundException ex) {
        }
    }
    
    public void loadDocs(String docsPath, String filesMapPath){
        try {
            FSTObjectInput in = new FSTObjectInput(new FileInputStream(docsPath));
            documents = (HashMap<Integer, Pair<Integer,Integer>>)in.readObject();
            in.close();
            
            in = new FSTObjectInput(new FileInputStream(filesMapPath));
            filesMapping = (BidiMap<String,Integer>)in.readObject();
            in.close();
            
        } catch (IOException | ClassNotFoundException ex) {
        }
    }
    
    private String[] loadStopWords(String stopWordsFile){
        File stopWords = new File(stopWordsFile);
        ArrayList<String> stopWordsList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(stopWords))) {
            for(String line; (line = br.readLine()) != null; ) {
                stopWordsList.add(line);
            }
        } catch (IOException ex) {
        }
        return stopWordsList.toArray(new String[0]);
    }
    
   
    public String getContent(Result r){
        int startLine = r.getStartLine();
        try {
            StringBuilder currentDoc = new StringBuilder();
            File cf = new File(r.getFilePath());
            Charset utf8charset = Charset.forName("UTF-8");
            CSVParser parser;
            int nLine = 0;
            parser = CSVParser.parse(cf, utf8charset, CSVFormat.DEFAULT);
            for (CSVRecord csvRecord : parser) {
                if(nLine == startLine){ 
                    currentDoc.append(csvRecord.get(5));
                    try{ 
                        currentDoc.append(" ");
                        currentDoc.append(csvRecord.get(6));
                    }catch(ArrayIndexOutOfBoundsException e) {
                    }
                    return currentDoc.toString();
                }
                nLine++;
            }
        } catch (IOException ex) {
            Logger.getLogger(CsvProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
