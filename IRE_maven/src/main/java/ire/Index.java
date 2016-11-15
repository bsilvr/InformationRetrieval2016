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
    
    HashMap<Integer, HashMap<Integer,Double>> dict;
    HashMap<Integer, String> words;
            
    public Index(){
        dict = new HashMap<>();
        words = new HashMap<>();
    }
    
    public void addTerm(String term, int doc, double weight){
        // adiciona um termo no indice em que apareceu no doc.
        // se ja existir adicionar a posting list.
        int hash = term.hashCode();
        
        words.put(hash, term);
        HashMap<Integer,Double> posts = dict.get(hash);
        //Post post = new Post(doc, weight);

        if(posts == null){
            posts= new HashMap<>();
            posts.put(doc,weight);
            
            dict.put(hash, posts);
        }
        else{
            //posts.add(post);
            posts.put(doc,weight);
        }
        
    }
    
    public Dictionary searchTerm(String term){
        //retorna informação sobre o termo encontrado
        return null;
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
    
    public HashMap<Integer,String> getSortedWords(){
        return words;
    }
    
}
