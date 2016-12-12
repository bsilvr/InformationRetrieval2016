/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.workers;

import ire.CorpusReader;
import ire.DocumentProcessor;
import ire.Objects.CorpusFile;

/**
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class DP_Worker extends Thread{
    private final boolean debug;
    private final CorpusReader corpus;
    private final DocumentProcessor docProc;
    
    public DP_Worker(CorpusReader corpus, DocumentProcessor docProc, boolean debug){
        this.corpus = corpus;
        this.docProc = docProc;
        this.debug = debug;
    }
    
    public DP_Worker(CorpusReader corpus, DocumentProcessor docProc){
        this.corpus = corpus;
        this.docProc = docProc;
        this.debug = false;
    }
    
    @Override
    public void run() {
        
        CorpusFile file = corpus.getNextFile();
        while(file != null){
            docProc.processDocument(file);
            file = corpus.getNextFile();
            if(debug){
                System.out.println("Reading document " + file.getPath());
            }
        }
        if(debug){
            System.out.println("Document Process thread finished...");
        }
        docProc.setFinish();
    }
}
