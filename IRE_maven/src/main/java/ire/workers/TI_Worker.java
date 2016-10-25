/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.workers;

import ire.Document;
import ire.DocumentProcessor;
import ire.Index;
import ire.Indexer;
import ire.Tokenizer;

/**
 * Worker class to tokenize and index a document
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class TI_Worker extends Thread{
    DocumentProcessor docProc;
    Indexer indexer;
    String[] stopWordsArray;
    
    public TI_Worker(DocumentProcessor docProc, Index idx, String[] stopWordsArray){
        this.docProc = docProc;
        this.stopWordsArray = stopWordsArray;
        indexer = new Indexer(idx);
    }

    @Override
    public void run() {
        Document doc = docProc.getNextDocument();
        String content = "";
        while(doc != null){
            content = docProc.getDocumentContent(doc);
            Tokenizer tokenizer = new Tokenizer(stopWordsArray);
            String[] tokens =  tokenizer.tokenize(content, doc);
            
            indexer.indexToken(tokens, doc.getDocId());
            
            doc = docProc.getNextDocument();
            
        }
    }
    
}
