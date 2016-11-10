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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Indexer {
    private String index_path;
    
    private static Index index = new Index();
    
    public Indexer(){
        index_path = "index.txt";
    }
    
    public Indexer(String path){
        index_path = path;
    }
    
    public void indexToken(String[] tokens, int docId){
        HashMap<Integer,Integer> weight = new HashMap<>();
        for (int i = 0; i < tokens.length; i++){
            index.addTerm(tokens[i], docId);
            
            //Calcular term frequency sem guardar no index
            if(weight.containsKey(tokens[i].hashCode())){
                int count = weight.get(tokens[i].hashCode());
                count++;
                weight.replace(tokens[i].hashCode(), count);
            }
            else{
                weight.put(tokens[i].hashCode(), 1);
            }
        }
        // ja temos o term frequency
        
        // calcular document lenght
        /*float sum = 0;
        for(HashMap.Entry<Integer, Integer> entry : weight.entrySet()){ 
            sum += Math.pow(entry.getValue(), 2);
        }
        double doc_length = Math.sqrt(sum);
        
        Set<String> mySet = new HashSet<>(Arrays.asList(tokens));
        Iterator it = mySet.iterator();
        double wNorm = 0;
        while(it.hasNext()) {
            String tmp = (String)it.next();
            wNorm = (1+Math.log(weight.get(tmp.hashCode()))) / doc_length;
            index.dict.get(tmp.hashCode()).getPostingList().addPostWeight(docId, wNorm);
        }*/
        
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
