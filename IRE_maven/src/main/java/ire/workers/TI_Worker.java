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
import java.util.regex.Pattern;

/**
 * Worker class to tokenize and index a document
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class TI_Worker extends Thread{
    private final Pattern pattern = Pattern.compile("\\W");
    private final Pattern space_char = Pattern.compile("\\s+");
    private final Pattern as_space = Pattern.compile("[-()]");
    
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
        Tokenizer tokenizer;
        String[] tokens;
        while(doc != null){            
            content = docProc.getDocumentContent(doc);

            tokenizer = new Tokenizer(stopWordsArray, pattern, space_char, as_space);
            tokens =  tokenizer.tokenize(content, doc);
            
            indexer.indexToken(tokens, doc.getDocId());
            
            doc = docProc.getNextDocument();  
        }
        doc = null;
        content = null;
        tokenizer = null;
        tokens = null;
    }
    
}
