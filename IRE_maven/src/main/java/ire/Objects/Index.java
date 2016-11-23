/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.Objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

/**
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Index implements Serializable{
    private static int fileID = 0;
    private final String basefolder;
    private boolean debug;
    private int indexCount = 0;
    private int countID = 0;
    private int termID;
    
    private HashMap<String,Integer> filesMapping;
    private BidiMap<Integer, Pair<Integer,Integer>> documents;
    private HashMap<Integer, HashMap<Integer,Double>> dict;
    private BidiMap<String, Integer> words;
    private HashMap<Integer,Double> posts;
    private HashMap<Integer, HashMap<Integer,Double>>[] alphabeticIndex;
            
    public Index(){
        filesMapping = new HashMap<>();
        documents = new DualHashBidiMap();
        dict = new HashMap<>();
        words = new DualHashBidiMap();
        basefolder = "indexes";
        debug = false;
    }
    
    public Index(String bf, boolean debug){
        filesMapping = new HashMap<>();
        documents = new DualHashBidiMap();
        dict = new HashMap<>();
        words = new DualHashBidiMap();
        basefolder = bf;
        this.debug = debug;
    }
    
    public Index(String bf){
        filesMapping = new HashMap<>();
        documents = new DualHashBidiMap();
        dict = new HashMap<>();
        words = new DualHashBidiMap();
        basefolder = bf;
        this.debug = false;
    }
    
    public Index(boolean debug){
        filesMapping = new HashMap<>();
        documents = new DualHashBidiMap();
        dict = new HashMap<>();
        words = new DualHashBidiMap();
        basefolder = "indexes";
        this.debug = debug;
    }
    
    public synchronized void addTerm(String term, int doc, double weight){
        // adiciona um termo no indice em que apareceu no doc.
        // se ja existir adicionar a posting list.
        if(words.containsKey(term)){ 
            termID = words.get(term);
        }
        else{
            words.put(term, countID);
            termID = countID;
            countID++;
        }
        
        if(dict.containsKey(termID)){ 
            dict.get(termID).put(doc,weight);
        }
        else{
            posts = new HashMap<>();
            posts.put(doc, weight);
            dict.put(termID, posts);
        }
    }
    
    public synchronized void addDocument(String filePath, int docID, int line){
        int fileID;
        if(!filesMapping.containsKey(filePath)){
            fileID = getFileID();
            filesMapping.put(filePath, fileID);
        }
        else{
            fileID = filesMapping.get(filePath);
        }
        documents.put(docID, new ImmutablePair<>(fileID,line));
    }
    
    public synchronized void writeIndex(){
        try {
            // Write to file
            if(debug){
                System.out.println("Writing intermediary index to disk....");
            }
            
            String filename = basefolder + "/tmp/index_" + indexCount;
            indexCount++;
            
            FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(filename));
            out.writeObject(dict, HashMap.class);
            out.close();
            
            // Create new index
            dict = new HashMap<>();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void writeWords(){
        try {
            String filename = basefolder + "/other/words";
            
            FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(filename));
            out.writeObject(words);
            out.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void writeDocuments(){
        try {
            String filename = basefolder + "/other/documents";
            FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(filename));
            out.writeObject(documents);
            out.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String filename = basefolder + "/other/fileMap";
            FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(filename));
            out.writeObject(filesMapping);
            out.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void mergeIndex(){
        ArrayList<String> files = readDir();
        writeMergeIndex();
        
        for(String f : files){
           
            HashMap<Integer, HashMap<Integer,Double>> tmp = readObject(f);
            HashMap[] indexPart = consumeIndexPart(tmp);
            mergeToDisk(indexPart);
            if (debug){
                System.err.println("Merged " + f);
            }
            File file = new File(basefolder + "/tmp/" + f);
            file.delete();
        }
        
    }
    
    public void loadWords(){
        try {
            FSTObjectInput in = new FSTObjectInput(new FileInputStream(basefolder + "/other/words"));
            words = (BidiMap<String, Integer>)in.readObject();
            in.close();
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void dumpWords(){
        for(HashMap.Entry<String, Integer> entry : words.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
    
    private ArrayList<String> readDir(){
        File folder = new File(basefolder);
        ArrayList<String> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if(fileEntry.getName().equals(".DS_Store")){
                continue;
            }
            files.add(fileEntry.getName());
        }
        return files;
    }
    
    private int getAlphabeticOrder(char character){
        if (character < 97 || character > 122){
            return 26;
        }
        return (int)character-97;
    }
    
    private HashMap<Integer, HashMap<Integer,Double>> readObject(String filename){
        HashMap<Integer, HashMap<Integer,Double>> result = null;
        try {
            FSTObjectInput in = new FSTObjectInput(new FileInputStream(basefolder + "/tmp/" + filename));
            Object res = in.readObject(HashMap.class);
            result = (HashMap<Integer, HashMap<Integer,Double>>) res;
            in.close();
            
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
    private HashMap[] consumeIndexPart(HashMap<Integer, HashMap<Integer,Double>> map){
        alphabeticIndex = new HashMap[27];
        for(HashMap.Entry<Integer, HashMap<Integer,Double>> entry : map.entrySet()){ 
            String word = words.getKey(entry.getKey());
            
            int idx = getAlphabeticOrder(word.charAt(0));
            if(alphabeticIndex[idx] == null){
                alphabeticIndex[idx] =  new HashMap<>();
            } 

            alphabeticIndex[idx].put(entry.getKey(), entry.getValue());
        }
        return alphabeticIndex;
    }
    
    private void mergeToDisk(HashMap[] indexes){
        int count = 97;
        try {
            for(HashMap<Integer, HashMap<Integer,Double>> entry : indexes){
                if(count==123){
                    count =35;
                }
                String filename = basefolder + "final_index_" + (char)count;
                
                FSTObjectInput in = new FSTObjectInput(new FileInputStream(filename));
                HashMap<Integer, HashMap<Integer,Double>> letter = (HashMap)in.readObject();
                in.close();
                
                for(HashMap.Entry<Integer, HashMap<Integer,Double>> e : entry.entrySet()){
                    if(letter == null){ 
                    letter =  new HashMap<>(); 
                    }  
                    if (letter.containsKey(e.getKey())){ 
                        letter.get(e.getKey()).putAll(e.getValue()); 
                    } 
                    else{ 
                        letter.put(e.getKey(), e.getValue()); 
                    }
                }

                FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(filename));
                out.writeObject(letter);
                out.close();
                count++;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void writeMergeIndex(){
        alphabeticIndex = new HashMap[27];
        int count = 97;
        try {
            for(HashMap<Integer, HashMap<Integer,Double>> entry : alphabeticIndex){
         
                if(count == 123){
                    count = 35;
                }
                String filename = basefolder + "final_index_" + (char)count;

                FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(filename));
                entry = new HashMap<>();
                out.writeObject(entry);
                out.close();
                count++;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static synchronized int getFileID(){
        return Index.fileID++;
    }
}
