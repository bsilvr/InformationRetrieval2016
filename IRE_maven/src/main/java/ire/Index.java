/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.util.HashMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Index {
    
    HashMap<Integer, Dictionary> dict;
    SortedSet<String> words;
            
    public Index(){
        dict = new HashMap<>();
        words = new TreeSet<>();
    }
    
    public synchronized void addTerm(String term, int doc){
        // adiciona um termo no indice em que apareceu no doc.
        // se ja existir adicionar a posting list.
        if(dict.containsKey(term.hashCode())){
            Dictionary d = dict.get(term.hashCode());
            d.addDocument(doc);
            dict.replace(term.hashCode(), d);
            return;
        }
        words.add(term);
        dict.put(term.hashCode(), new Dictionary(term, doc));
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
    
    public SortedSet<String> getSortedWords(){
        return words;
    }
    
}
