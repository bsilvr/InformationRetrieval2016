/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.util.HashMap;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Indexer {
    
    private static Index index = new Index();
    private int writeThreshold;
    
    
    public Indexer(){
        writeThreshold=100000;
        
    }
    
    public Indexer(int threshold){
        writeThreshold = threshold;
    }
    
    public void indexToken(int docId, HashMap<String,Double> weight, double doc_weight){
        for(HashMap.Entry<String, Double> entry : weight.entrySet()){
            index.addTerm(entry.getKey(), docId, weight.get(entry.getKey())/doc_weight);
        }

        if (docId % writeThreshold == 0 && docId != 0){
            index.writeIndex();
        }
    }
    
    public void writeLast(){
        index.writeIndex();
    }
    
    public void mergeIndex(){
        index.mergeIndex();
        
    }
    
}
