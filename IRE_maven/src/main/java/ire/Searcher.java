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
    private final int numDocs;
    
    private final String indexPath;
    
    public Searcher(String indexPath, String stopWordsFile, int numResults, boolean debug){
        documents = new HashMap<>();
        index = new HashMap<>();
        filesMapping = new DualHashBidiMap();
        words = new DualHashBidiMap();
        results = null;
        tokenizer = new Tokenizer(loadStopWords(stopWordsFile), debug);
        numDocs = 3000000;
        
        this.indexPath = indexPath;
        
    }
    
    public Result[] search(String query){
        String [] tokens = tokenizer.tokenize(query);
        double idf = calculateIdf(tokens);
        HashMap<String, Double> queryWeights = calculateWeight(tokens, idf);
        results = calculateResult(queryWeights);
        
        return results;
    }
    
    private Result[] calculateResult(HashMap<String, Double> queryWeights){
        
        Comparator<Result> comparator = (Result a, Result b) -> a.compareTo(b);
        ArrayList<Result> scores = new ArrayList<>();
        for(HashMap.Entry<String, Double> entry : queryWeights.entrySet()){ 
            HashMap<Integer, Double> postList = getPostingList(entry.getKey());
            for(HashMap.Entry<Integer, Double> e : postList.entrySet()){
                double tmpScore = entry.getValue()*e.getValue();
                int result = scores.indexOf(e.getKey());
                if(result != -1){
                   scores.get(result).addScore(tmpScore);
                }
                else{
                    scores.add(new Result(filesMapping.getKey(documents.get(e.getKey()).getLeft()), documents.get(e.getKey()).getRight(),  e.getKey(),tmpScore));
                }
            }
        }
        return results;
    }
    
    private double calculateIdf(String [] tokens){
        return 0;
    }
    
    private HashMap<Integer, Double> getPostingList(String term){
        
        int termId;
        if(words.containsKey(term)){
            termId = words.get(term);
        }
        else return null;
        
        loadIndex(term.charAt(0));
        
        if(index.containsKey(termId)){
            return index.get(termId);
        }
        return null;
    }
    
    private HashMap<String, Double> calculateWeight(String[] tokens, double idf){
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
                sum += Math.pow(tmp, 2);
                tmp = tmp*idf;
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
