/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.workers;

import ire.DocumentContent;
import ire.DocumentProcessor;
import ire.Indexer;
import ire.Tokenizer;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Worker class to tokenize and index a document
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class TI_Worker extends Thread{
    private String basefolder = "indexes/";
    
    private final Pattern pattern = Pattern.compile("\\W");
    private final Pattern space_char = Pattern.compile("\\s+");
    private final Pattern as_space = Pattern.compile("[-()]");
    
    private DocumentContent doc;
    private Indexer indexer;
    private String[] stopWordsArray;
    private int indexCount = 0;
    private int docCount = 0;

    public TI_Worker(DocumentContent doc, String[] stopWordsArray){
        this.doc = doc;
        this.stopWordsArray = stopWordsArray;
        this.indexer = new Indexer();
    }

    @Override
    public void run() {
       
        Tokenizer tokenizer = new Tokenizer(stopWordsArray, pattern, space_char, as_space);
        String[] tokens;
        tokens =  tokenizer.tokenize(doc.getContent());
            
        //Calcular pesos palavras e passar ao indexer para dar merge ao indice global.
        HashMap<Integer,Integer> counts = new HashMap<>();
        int count = 0;
        for (int i = 0; i < tokens.length; i++){

            if(!counts.containsKey(tokens[i].hashCode())){
                counts.put(tokens[i].hashCode(), 1);   

            }

            else{
                count = counts.get(tokens[i].hashCode());
                count++;
                counts.replace(tokens[i].hashCode(), count);
            }

        }
        double sum = 0;
        HashMap<Integer,Double> weights = new HashMap<>();
        for(HashMap.Entry<Integer, Integer> entry : counts.entrySet()){ 
            sum += Math.pow(entry.getValue(), 2);
            weights.put(entry.getKey(), 1+Math.log(counts.get(entry.getKey())));
        }
        double doc_length = Math.sqrt(sum);

        
        indexer.indexToken(tokens, doc.getDocId(), weights, doc_length);
        //System.out.println(doc.getDocId());

    }
    
    public void writeIndex(){
        ObjectOutputStream oos = null;
        try {
            String filename = basefolder + this.getId() + "_" + indexCount;
            oos = new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(indexer.getIndex());
            oos.close();
            indexCount++;
        } catch (IOException ex) {
            Logger.getLogger(TI_Worker.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                oos.close();
            } catch (IOException ex) {
                Logger.getLogger(TI_Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
