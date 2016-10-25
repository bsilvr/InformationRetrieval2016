/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Indexer {
    final String index_path = "index.txt";
    
    
    private Index index;
    
    public Indexer(){
        index = new Index();
    }
    
    public Indexer(Index idx){
        index = idx;
    }
    
    public void indexToken(String[] tokens, int docId){
        for (String token : tokens){
            index.addTerm(token, docId);
        }
        
    }
    
    public void writeIndex(){
        File fl = new File(index_path);
        PrintWriter writer;
        try {
            writer = new PrintWriter(fl, "UTF-8");
            
            for(String i: index.getSortedWords()){
                Dictionary d = index.get(i.hashCode());
                
                writer.println(d.getTerm()+"-"+d.getnDocs()+"-"+d.getPostingList().toString());
            }
            
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
