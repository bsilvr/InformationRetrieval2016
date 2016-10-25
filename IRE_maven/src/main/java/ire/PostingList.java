/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.util.Iterator;
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
    
    public int[] getAllPostings(){
        int[] ret = new int[docs.size()];
        Iterator<Integer> iterator = docs.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next();
        }
        return ret;
    }
    
    @Override
    public String toString(){
        int[] postings = getAllPostings();
        String s = "";
        for (int i = 0; i < postings.length; i++){
            s += postings[i] + ",";
        }
        s = s.substring(0, s.length()-1);
        return s;
    }
}
