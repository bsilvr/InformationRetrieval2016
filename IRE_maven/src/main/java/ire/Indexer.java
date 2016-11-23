/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.Objects.Index;
import java.util.HashMap;

/**
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Indexer {
    
    private static Index index;
    private int writeThreshold;
    private static Runtime runtime = Runtime.getRuntime();
    private static int mb = 1024*1024;
    private boolean debug;
    
    public Indexer(){
        if(index == null){
            index = new Index();
        }
        writeThreshold = ((int)(runtime.maxMemory()/mb))*100;
        this.debug = false;
    }
    
    public Indexer(int threshold, boolean debug, String basefolder){
        if(index == null){
            index = new Index(basefolder, debug);
        }
        writeThreshold = threshold;
        this.debug = debug;
    }
    
    public Indexer(String basefolder, boolean debug){
        if(index == null){
            index = new Index(basefolder, debug);
        }
        writeThreshold = ((int)(runtime.maxMemory()/mb))*100;
        this.debug = debug;
    }
    
    public Indexer(String basefolder){
        if(index == null){
            index = new Index(basefolder, false);
        }
        writeThreshold = ((int)(runtime.maxMemory()/mb))*100;
        this.debug = false;
    }
    
    public Indexer(boolean debug){
        if(index == null){
            index = new Index(false);
        }
        writeThreshold = ((int)(runtime.maxMemory()/mb))*100;
        this.debug = debug;
    }
    
    public void indexToken(int docId, HashMap<String,Double> weight, double doc_weight){
        for(HashMap.Entry<String, Double> entry : weight.entrySet()){
            index.addTerm(entry.getKey(), docId, weight.get(entry.getKey())/doc_weight);
        }

        if ((docId % writeThreshold == 0 && docId != 0) ){
            if(debug){
                System.out.println("Free Memory:" + runtime.freeMemory() / mb);
            }
            index.writeIndex();
        }
    }
    
    public void indexToken(int docId, String[] tokens){
        for(String entry : tokens){
            index.addTerm(entry, docId, 0);
        }

        if ((docId % writeThreshold == 0 && docId != 0) ){
            if(debug){
                System.out.println("Free Memory:" + runtime.freeMemory() / mb);
            }
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
