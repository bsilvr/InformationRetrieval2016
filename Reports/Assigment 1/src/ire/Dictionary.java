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
public class Dictionary {
    String term;
    int nDocs;
    Posting postingList;
    
    public Dictionary(String term){
        this.term = term;
        this.nDocs = 0;
        this.postingList = new Posting();
    }
    
    public Dictionary(String term, int doc){
        this.term = term;
        this.nDocs = 1;
        this.postingList = new Posting();
        this.postingList.addPosting(doc);
    }
    
    public void addDocument(int doc){
        this.postingList.addPosting(doc);
    }
    
    public void removeDocument(int doc){
        this.postingList.removePosting(doc);
    }
    
    public Dictionary getTerm(){
        return this;
    }
}
