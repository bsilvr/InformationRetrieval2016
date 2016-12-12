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
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

/**
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Index implements Serializable{
    private final String basefolder;
    private final boolean debug;
    private int indexCount = 0;
    private int countID = 0;
    private int termID;
    
    private HashMap<Integer, HashMap<Integer,Double>> dict;
    private BidiMap<String, Integer> words;
    private HashMap<Integer,Double> posts;
            
    public Index(){
        dict = new HashMap<>();
        words = new DualHashBidiMap();
        basefolder = "indexes";
        debug = false;
        
        // Create folders they dont exist
        new File(basefolder+"/tmp").mkdirs();
        new File(basefolder+"/other").mkdirs();
    }
    
    public Index(String bf, boolean debug){
        dict = new HashMap<>();
        words = new DualHashBidiMap();
        basefolder = bf;
        this.debug = debug;
        
        // Create folders they dont exist
        new File(basefolder+"/tmp").mkdirs();
        new File(basefolder+"/other").mkdirs();
    }
    
    public Index(String bf){
        dict = new HashMap<>();
        words = new DualHashBidiMap();
        basefolder = bf;
        this.debug = false;
        
        // Create folders they dont exist
        new File(basefolder+"/tmp").mkdirs();
        new File(basefolder+"/other").mkdirs();
    }
    
    public Index(boolean debug){
        dict = new HashMap<>();
        words = new DualHashBidiMap();
        basefolder = "indexes";
        this.debug = debug;
        
        // Create folders they dont exist
        new File(basefolder+"/tmp").mkdirs();
        new File(basefolder+"/other").mkdirs();
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

    public synchronized void writeIndex(){
        try {
            // Write to file
            if(debug){
                System.out.println("Writing intermediary index to disk....");
            }
            
            String filename = basefolder + "/tmp/index_" + indexCount;
            indexCount++;
            
            try (FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(filename))) {
                out.writeObject(dict, HashMap.class);
            }
            
            // Create new index
            dict = new HashMap<>();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void writeWords(){
        System.out.println(words.size());
        try {
            String filename = basefolder + "/other/words";
            
            try (FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(filename))) {
                out.writeObject(words);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void mergeIndex(){
        ArrayList<String> files = readDir();
        writeMergeIndex();
        
        files.stream().map((f) -> {
            mergeToDisk(consumeIndexPart(readObject("/tmp/" + f)));
            return f;
        }).map((f) -> {
            if (debug){
                System.err.println("Merged " + f);
            }
            return f;
        }).map((f) -> {
            new File(basefolder + "/tmp/" + f).delete();
            return f;
        }).forEach((_item) -> {
            System.gc();
        }); 
    }
    
    public void loadWords(){
        try (FSTObjectInput in = new FSTObjectInput(new FileInputStream(basefolder + "/other/words"))) {
            words = (BidiMap<String, Integer>)in.readObject();
            
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void dumpWords(){
        words.entrySet().stream().forEach((entry) -> {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        });
    }
    
    private ArrayList<String> readDir(){
        File folder = new File(basefolder + "/tmp");
        ArrayList<String> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if(fileEntry.getName().equals(".DS_Store") || fileEntry.isDirectory()){
                continue;
            }
            files.add(fileEntry.getName());
        }
        folder = null;
        return files;
    }
    
    private int getAlphabeticOrder(char character){
        if (character < 97 || character > 122){
            return 26;
        }
        return (int)character-97;
    }
    
    private HashMap<Integer, HashMap<Integer,Double>> readObject(String filename){
        try {
            Object res;
            try (FSTObjectInput in = new FSTObjectInput(new FileInputStream(basefolder + filename))) {
                res = in.readObject(HashMap.class);
            }
            return (HashMap<Integer, HashMap<Integer,Double>>) res;
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private HashMap[] consumeIndexPart(HashMap<Integer, HashMap<Integer,Double>> map){
        HashMap<Integer, HashMap<Integer,Double>>[] alphabeticIndex = new HashMap[27];
        String word;
        for(HashMap.Entry<Integer, HashMap<Integer,Double>> entry : map.entrySet()){ 
            word = words.getKey(entry.getKey());
            
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
            FSTObjectInput in;
            FSTObjectOutput out;
            HashMap<Integer, HashMap<Integer,Double>> letter;
            for(HashMap<Integer, HashMap<Integer,Double>> entry : indexes){
                if(count==123){
                    count =35;
                }
                String filename = basefolder + "/final_index_" + (char)count;
                
                in = new FSTObjectInput(new FileInputStream(filename));
                letter = (HashMap)in.readObject();
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

                out = new FSTObjectOutput(new FileOutputStream(filename));
                out.writeObject(letter);
                out.close();
                count++;
                System.gc();
            }
            in = null;
            out = null;
            letter = null;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void writeMergeIndex(){
        HashMap<Integer, HashMap<Integer,Double>> alphabeticIndex = new HashMap<>();
        int count = 97;
        try {
            FSTObjectOutput out;
            for(int i = 0; i < 27; i++){
         
                if(count == 123){
                    count = 35;
                }
                String filename = basefolder + "/final_index_" + (char)count;

                out = new FSTObjectOutput(new FileOutputStream(filename));
                out.writeObject(alphabeticIndex);
                out.close();
                count++;
            }
            out = null;
            alphabeticIndex = null;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
