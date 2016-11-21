/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class Index implements Serializable{
    private final String basefolder = "indexes/";
    
    private int indexCount = 0;
    private int countID = 0;
    private int termID;
    
    HashMap<Integer, HashMap<Integer,Double>> dict;
    final HashMap<String, Integer> words;
    private HashMap<Integer,Double> posts;

    // words passar a dar a key para o outro hashmap
            
    public Index(){
        //dict = new HashMap<>();
        //words = new HashMap<>();
        dict = new HashMap<>();
        words = new HashMap<>();
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
    
    public HashMap<String, Integer> getSortedWords(){
        return words;
    }
    
    public synchronized void writeIndex(){
        // Write to file
        System.err.println("Current Words Size: " + words.size());
        System.out.println("Writing....");
        ObjectOutputStream oos = null;
        try {
            String filename = basefolder + "index_" + indexCount;
            indexCount++;
            oos = new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(dict);
            oos.close();
        } catch (IOException ex) {
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
            }
        }
        
        // Create new
        dict = null;
        System.gc();
        dict = new HashMap<>();
    }
    
}
