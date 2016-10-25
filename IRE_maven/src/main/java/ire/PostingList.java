/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class PostingList {
    
    private SortedSet<Integer> docs;
    
    public PostingList(){
        this.docs = new TreeSet<>();
    }
    
    public void addPosting(int doc){
        docs.add(doc);
    }
    
    public boolean contains(int doc){
        return docs.contains(doc);
    }
    
    public int postingListSize(){
         return docs.size();
    }
    
    public void removePosting(int doc){
        docs.remove(doc);
    }
    
    public Integer[] getAllPostings(){
        return docs.toArray(new Integer[0]);
    }
    
    @Override
    public String toString(){
        String s = "";
        for (int i : docs){
            s += i + "-";
        }
        s = s.substring(0, s.length()-1);
        return s;
    }
}
