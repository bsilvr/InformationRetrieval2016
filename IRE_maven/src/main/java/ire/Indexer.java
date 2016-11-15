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
public class Indexer {
    private String index_path;
    
    private Index index = new Index();
    
    public Indexer(){
        index_path = "index.txt";
    }
    
    public Indexer(String path){
        index_path = path;
    }
    
    public void indexToken(String[] tokens, int docId, HashMap<Integer,Double> weight, double doc_weight){
        for (int i = 0; i < tokens.length; i++){
            index.addTerm(tokens[i], docId, weight.get(tokens[i].hashCode())/doc_weight);
        }
    }
    
    public Index getIndex(){
        return index;
    }
    
    /*public void writeIndex(){
        File fl = new File(index_path);
        PrintWriter writer;
        try {
            writer = new PrintWriter(fl, "UTF-8");
            
            for(String i: index.getSortedWords()){
                Dictionary d = index.get(i.hashCode());
                
                writer.println(i+"-"+d.getnDocs()+"-"+d.getPostingList().toString());
            }
            
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }*/
}
