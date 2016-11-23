/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.workers;

import ire.DocumentContent;
import ire.Indexer;
import ire.Tokenizer;
import java.util.HashMap;

/**
 * Worker class to tokenize and index a document
 * 
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class TI_Worker extends Thread{
    private boolean debug;
    private DocumentContent doc;
    private Indexer indexer;
    private Tokenizer tokenizer;

    public TI_Worker(DocumentContent doc, Indexer indexer, String[] stopWordsArray, boolean debug){
        this.doc = doc;
        this.indexer = indexer;
        this.tokenizer = new Tokenizer(stopWordsArray);
        this.debug = debug;
    }

    @Override
    public void run() {
       
        String[] tokens;
        tokens = tokenizer.tokenize(doc.getContent());
            
        //Calcular pesos palavras e passar ao indexer para dar merge ao indice global.
        HashMap<String,Integer> counts = new HashMap<>();
        int count;
        for (String token : tokens) {
            if (!counts.containsKey(token)) {
                counts.put(token, 1);
            } else {
                count = counts.get(token);
                count++;
                counts.replace(token, count);
            }
        }
        double sum = 0;
        double tmp = 0;
        HashMap<String,Double> weights = new HashMap<>();
        for(HashMap.Entry<String, Integer> entry : counts.entrySet()){ 
            tmp = 1+Math.log(counts.get(entry.getKey()));
            sum += Math.pow(tmp, 2);
            weights.put(entry.getKey(), tmp);
        }
        double doc_length = Math.sqrt(sum);

        
        indexer.indexToken(doc.getDocId(), weights, doc_length);
        indexer.addDocument(doc.getFilePath(), doc.getDocId(), doc.getStartLine());
        
        if(debug){
            if(doc.getDocId()%10000 == 0){
                System.out.println(doc.getDocId());
            }
        }
        
        
        doc = null;
        indexer = null;
        tokenizer = null;
        tokens = null;
        counts = null;
        weights = null;
    }
}
