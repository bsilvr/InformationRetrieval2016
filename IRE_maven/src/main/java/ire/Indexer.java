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
    private String index_path;
    
    private static Index index = new Index();
    
    public Indexer(){
        index_path = "index.txt";
    }
    
    public Indexer(String path){
        index_path = path;
    }
    
    public void indexToken(int docId, HashMap<String,Double> weight, double doc_weight){
        for(HashMap.Entry<String, Double> entry : weight.entrySet()){
            index.addTerm(entry.getKey(), docId, weight.get(entry.getKey())/doc_weight);
        }
        //if(docId%100000 ==0){
            System.out.println(docId);
        //}
        if (docId%400000==0 && docId != 0){
            index.writeIndex();
        }
    }
    
    public Index getIndex(){
        return index;
    }
}
