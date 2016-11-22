/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.nustaq.serialization.FSTObjectInput;
import org.nustaq.serialization.FSTObjectOutput;

public class Index implements Serializable{
    private final String basefolder = "indexes/";
    
    private int indexCount = 0;
    private int countID = 0;
    private int termID;
    
    HashMap<Integer, HashMap<Integer,Double>> dict;
    private BidiMap<String, Integer> words;
    private HashMap<Integer,Double> posts;
    private HashMap<Integer, HashMap<Integer,Double>>[] alphabeticIndex;
    // words passar a dar a key para o outro hashmap
            
    public Index(){
        //dict = new HashMap<>();
        //words = new HashMap<>();
        dict = new HashMap<>();
        words = new DualHashBidiMap();
    }
    
    public synchronized void addTerm(String term, int doc, double weight){
        // adiciona um termo no indice em que apareceu no doc.
        // se ja existir adicionar a posting list.
        if(words.containsKey(term)){ 
            termID = words.get(term);
        }
        else{
            //System.out.println(term);
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
        
        // if free memory < 10mb
        /*if (Runtime.getRuntime().freeMemory() < 10485760){
            writeIndex();
        }
        else*/
    }
    
    public void removeTerm(String Term){
        // remove termo do indice
    }
    
    public void removeDocument(String Term, int doc){
        // remove documento da posting list para aquele termo
    }
    
    public int size(){
        return dict.size();
    }

    
    public Set<Integer> keySet(){
        return dict.keySet();
    }
    
    public BidiMap<String, Integer> getSortedWords(){
        return words;
    }
    
    public synchronized void writeIndex(){
        try {
            // Write to file
            System.err.println("Current Words Size: " + words.size());
            System.out.println("Writing....");
            
            String filename = basefolder + "index_" + indexCount;
            indexCount++;
            
            FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(filename));
            out.writeObject(dict, HashMap.class);
            out.close(); // required !
            
            // Create new
            
            dict = new HashMap<>();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized void writeWords(){
        try {
            String filename = basefolder + "words";
            
            FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(filename));
            out.writeObject(words);
            out.close(); // required !

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
           
            HashMap<Integer, HashMap<Integer,Double>> tmp = myreadMethod(f);
            HashMap[] indexPart = consumeIndexPart(tmp);
            mergeToDisk(indexPart);
            System.err.println("Merged " + f);
            File file = new File(basefolder+f);
            file.delete();
        }
        
    }
    private ArrayList<String> readDir(){
        File folder = new File(basefolder);
        ArrayList<String> files = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if(fileEntry.getName().equals(".DS_Store") || fileEntry.getName().equals("words")){
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
    
    private HashMap<Integer, HashMap<Integer,Double>> myreadMethod(String filename){
        HashMap<Integer, HashMap<Integer,Double>> result = null;
        try {
            FSTObjectInput in = new FSTObjectInput(new FileInputStream(basefolder + filename));
            Object res = in.readObject(HashMap.class);
            result = (HashMap<Integer, HashMap<Integer,Double>>) res;
            
            
            in.close(); // required !
            
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
            // Write to file
            for(HashMap<Integer, HashMap<Integer,Double>> entry : indexes){
                if(count==123){
                    count =35;
                }
                String filename = basefolder + "final_index_" + (char)count;
                
                FSTObjectInput in = new FSTObjectInput(new FileInputStream(filename));
                HashMap<Integer, HashMap<Integer,Double>> letter = (HashMap)in.readObject();
                in.close(); // required !
                
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
                out.close(); // required !
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
            // Write to file
            for(HashMap<Integer, HashMap<Integer,Double>> entry : alphabeticIndex){
         
                if(count==123){
                    count =35;
                }
                String filename = basefolder + "final_index_" + (char)count;

                FSTObjectOutput out = new FSTObjectOutput(new FileOutputStream(filename));
                entry = new HashMap<>();
                out.writeObject(entry);
                out.close(); // required !
                count++;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void loadWords(){
        HashMap<Integer, HashMap<Integer,Double>> result = null;
        try {
            FSTObjectInput in = new FSTObjectInput(new FileInputStream(basefolder + "words"));
            words = (BidiMap<String, Integer>)in.readObject();
            in.close(); // required !
            
        } catch (IOException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void dumpWords(){
        for(HashMap.Entry<String, Integer> entry : words.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        
    }
    
}
