/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class Index implements Serializable{
    
    //HashMap<Integer, HashMap<Integer,Double>> dict;
    //HashMap<Integer, String> words;
    private int countID = 0;
    private int termID;
    
    HashMap<Integer, HashMap<Integer,Double>> dict;
    HashMap<String, Integer> words;
    HashMap<Integer,Double> posts;

    // words passar a dar a key para o outro hashmap
            
    public Index(){
        //dict = new HashMap<>();
        //words = new HashMap<>();
        posts = new HashMap<>();
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
            HashMap<Integer,Double> posts = new HashMap<>();
            posts.put(doc, weight);
            dict.put(termID, posts);
        }

        posts.clear();
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
    
}
