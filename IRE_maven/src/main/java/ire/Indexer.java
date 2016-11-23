/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.Objects.DocumentList;
import ire.Objects.Index;
import java.util.HashMap;

/**
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Indexer {
    
    private static Index index;
    private static DocumentList documents;
    
    private int writeThreshold;
    private static Runtime runtime = Runtime.getRuntime();
    private static int mb = 1024*1024;
    private boolean debug;
    
    public Indexer(){
        if(index == null){
            index = new Index();
        }
        if(documents == null){
            documents = new DocumentList();
        }
        writeThreshold = ((int)(runtime.maxMemory()/mb))*100;
        this.debug = false;
    }
    
    public Indexer(int threshold, boolean debug, String basefolder){
        if(index == null){
            index = new Index(basefolder, debug);
        }
        if(documents == null){
            documents = new DocumentList();
        }
        writeThreshold = threshold;
        this.debug = debug;
    }
    
    public Indexer(String basefolder, boolean debug){
        if(index == null){
            index = new Index(basefolder, debug);
        }
        if(documents == null){
            documents = new DocumentList();
        }
        writeThreshold = ((int)(runtime.maxMemory()/mb))*100;
        this.debug = debug;
    }
    
    public Indexer(String basefolder){
        if(index == null){
            index = new Index(basefolder, false);
        }
        if(documents == null){
            documents = new DocumentList();
        }
        writeThreshold = ((int)(runtime.maxMemory()/mb))*100;
        this.debug = false;
    }
    
    public Indexer(boolean debug){
        if(index == null){
            index = new Index(false);
        }
        if(documents == null){
            documents = new DocumentList();
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
        documents.addDocument(filePath, docid, line);
        if ((docid % writeThreshold == 0 && docid != 0) ){
            if(debug){
                System.out.println("Writing Documens");
            }
            documents.writeDocuments();
        }
    }
    
    public void writeLast(){
        index.writeIndex();
        index.writeWords();
        documents.writeDocuments();
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
