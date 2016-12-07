/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.Objects.Index;
import ire.Objects.Result;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
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
import org.apache.commons.lang3.tuple.Pair;
import org.nustaq.serialization.FSTObjectInput;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Searcher {
    private HashMap<Integer, Pair<Integer,Integer>> documents;
    private HashMap<Integer, HashMap<Integer,Double>> index;
    private BidiMap<String,Integer> filesMapping;
    private BidiMap<String, Integer> words;
    private Result[] results;
    private final Tokenizer tokenizer;
    private final int numResults;
    
    private final String indexPath;
    
    public Searcher(String indexPath, String stopWordsFile, int numResults, boolean debug){
        documents = new HashMap<>();
        index = new HashMap<>();
        filesMapping = new DualHashBidiMap();
        words = new DualHashBidiMap();
        results = null;
        tokenizer = new Tokenizer(loadStopWords(stopWordsFile), debug);
        this.numResults = numResults;
        
        this.indexPath = indexPath;
        
    }
    
    public Result[] search(String query){
        String [] tokens = tokenizer.tokenize(query);
        HashMap<String, Double> idfs = calculateIdf(tokens);
        HashMap<String, Double> queryWeights = calculateWeight(tokens, idfs);
        results = hash2array(calculateResult(queryWeights));
        
        return results;
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
            
                        

    private HashMap<Integer, Double> calculateResult(HashMap<String, Double> queryWeights){
        
        
        HashMap<Integer, Double> scores = new HashMap<>();
        for(HashMap.Entry<String, Double> entry : queryWeights.entrySet()){ 
            HashMap<Integer, Double> postList = getPostingList(entry.getKey());
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
            idfs.put(s, Math.log(documents.size()/getPostingList(s).size()));
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
        loadIndex(term.charAt(0));
        
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
    
    private void loadIndex(char initial){
        try {
            FSTObjectInput in = new FSTObjectInput(new FileInputStream(indexPath + initial));
            index = (HashMap<Integer, HashMap<Integer,Double>>)in.readObject();
            in.close();
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadWords(String wordsPath){
        try {
            FSTObjectInput in = new FSTObjectInput(new FileInputStream(wordsPath));
            words = (BidiMap<String, Integer>)in.readObject();
            in.close();
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
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
    
    
}
