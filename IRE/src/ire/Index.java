/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.util.HashMap;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Index {
    
    HashMap<Integer, Dictionary> dict;
    
    
    public Index(){
        dict = new HashMap<>();
    }
    
    public void addTerm(String term, int doc){
        // adiciona um termo no indice em que apareceu no doc.
        // se ja existir adicionar a posting list.
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
    
}
