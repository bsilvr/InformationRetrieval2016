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
    
    private SortedSet<Post> docs;
    
    public PostingList(){
        this.docs = new TreeSet<>();
    }
    
    public void addPosting(int doc){
        Post p = new Post(doc);
        docs.add(p);
    }
    
    public void addPostWeight(int doc, double weight){
        Post p = new Post(doc, weight);
        docs.add(p);
    }
    
    public boolean contains(int doc){
        Post p = new Post(doc);
        return docs.contains(p);
    }
    
    public int postingListSize(){
         return docs.size();
    }
    
    public void removePosting(int doc){
        Post p = new Post(doc);
        docs.remove(p);
    }
    
    @Override
    public String toString(){
        Post[] postings = docs.toArray(new Post[0]);
        StringBuilder s = new StringBuilder();
        for(int i = 0; i < postings.length; i++){
            s.append(postings[i].getNdoc());
            s.append(",");
        }
        return s.substring(0, s.length()-1);
    }
}
