/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.workers;

import ire.CorpusFile;
import ire.CorpusReader;
import ire.DocumentProcessor;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class DP_Worker extends Thread{
    CorpusReader corpus;
    DocumentProcessor docProc;
    
    public DP_Worker(CorpusReader corpus, DocumentProcessor docProc){
        this.corpus = corpus;
        this.docProc = docProc;
    }
    
    @Override
    public void run() {
        
        CorpusFile file = corpus.getNextFile();
        while(file != null){
            docProc.processDocument(file);
            file = corpus.getNextFile();
        }
        docProc.setFinish();
    }
}
