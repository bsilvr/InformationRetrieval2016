/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Index implements Serializable{
    
    HashMap<Integer, ArrayList<Post>> dict;
    HashMap<Integer, String> words;
            
    public Index(){
        dict = new HashMap<>();
        words = new HashMap<>();
    }
    
    public void addTerm(String term, int doc){
        // adiciona um termo no indice em que apareceu no doc.
        // se ja existir adicionar a posting list.
        int hash = term.hashCode();
        Dictionary d = dict.get(hash);
        if(d != null){  
            d.addDocument(doc);
            dict.replace(hash, d);
            return;
        }
        words.put(hash, term);
        dict.put(hash, new Dictionary(doc));
        //System.out.println(dict.size());
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
    
    public Dictionary get(int key){
        return dict.get(key);
    }
    
    public Set<Integer> keySet(){
        return dict.keySet();
    }
    
    public HashMap<Integer,String> getSortedWords(){
        return words;
    }
    
}
