/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.util.HashMap;

/**
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Indexer {
    
    private static Index index = new Index();
    private int writeThreshold;
    private static Runtime runtime = Runtime.getRuntime();
    private static int mb = 1024*1024;
    
    public Indexer(){
        writeThreshold = ((int)(runtime.maxMemory()/mb))*100;
        System.out.println(writeThreshold);
    }
    
    public Indexer(int threshold){
        writeThreshold = threshold;
    }
    
    public void indexToken(int docId, HashMap<String,Double> weight, double doc_weight){
        for(HashMap.Entry<String, Double> entry : weight.entrySet()){
            index.addTerm(entry.getKey(), docId, weight.get(entry.getKey())/doc_weight);
        }

        if ((docId % writeThreshold == 0 && docId != 0) ){
            
            System.out.println("Free Memory:" 
                    + runtime.freeMemory() / mb);
		
            index.writeIndex();
        }
    }
    
    public void addDocument(String filePath, int docid, int line){
        index.addDocument(filePath, docid, line);
    }
    
    public void writeLast(){
        index.writeIndex();
        index.writeWords();
        index.writeDocuments();
    }
    
    public void mergeIndex(){
        index.mergeIndex();
        
    }
    
    public void loadWords(){
        index.loadWords();
        
    }
    
    public void dumpWords(){
        index.dumpWords();
        
    }
    
}
