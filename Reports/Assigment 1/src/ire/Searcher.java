/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Searcher {
    Index index;
    
    public Searcher(){
        index = new Index();
    }
    
    public Searcher(Index idx){
        index = idx;
    }
    
    public Dictionary searchTerm(String term){
        return index.searchTerm(term);
    }
    
}