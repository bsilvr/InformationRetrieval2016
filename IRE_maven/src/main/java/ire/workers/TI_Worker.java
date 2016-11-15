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
    
    private DocumentProcessor docProc;
    private Indexer indexer;
    private String[] stopWordsArray;
    private int indexCount = 0;
    private int docCount = 0;

    public TI_Worker(DocumentProcessor docProc, String[] stopWordsArray){
        this.docProc = docProc;
        this.stopWordsArray = stopWordsArray;
        this.indexer = new Indexer();
    }

    @Override
    public void run() {
        DocumentContent doc = docProc.getNextDocument();
        Tokenizer tokenizer = new Tokenizer(stopWordsArray, pattern, space_char, as_space);
        String[] tokens;
        while(doc != null){            
            tokens =  tokenizer.tokenize(doc.getContent());
            //Calcular pesos palavras e passar ao indexer para dar merge ao indice global.
            indexer.indexToken(tokens, doc.getDocId());
            
            
            
            /*if(doc.getDocId() == 10000){
                //docCount++;
                //writeIndex();
                
                //indexer = new Indexer();
                //System.gc();
                
            }*/
            System.out.println(doc.getDocId());
            
            doc = docProc.getNextDocument(); 
        }
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
