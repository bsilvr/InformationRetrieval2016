/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.util.ArrayList;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Posting {
    
    private ArrayList<Integer> docs;
    
    public Posting(){
        this.docs = new ArrayList<>();
    }
    
    public void addPosting(int doc){
        docs.add(doc);
    }
    
    public int postingListSize(){
         return docs.size();
    }
    
    public void removePosting(int doc){
        docs.remove(doc);
    }
    
    public ArrayList getAllPostings(){
        return docs;
    }
}
