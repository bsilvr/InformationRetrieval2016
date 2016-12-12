/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.workers;

import ire.DocumentProcessor;
import ire.Indexer;
import ire.Objects.DocumentContent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class TI_Manager extends Thread{
    private final boolean debug;
    private final boolean stemming;
    private final boolean stopWords;
    private final boolean bolleanIndex;
    private final String[] stopWordsArray;
    private final DocumentProcessor docProc;
    private final Indexer indexer;
    private final int nthreads;
    private DocumentContent buf;

    public TI_Manager(int nthreads, DocumentProcessor docProc, String[] stopWordsArray, boolean debug, String indexBaseFolder, boolean stemming, boolean stopWords, boolean bolleanIndex){
        this.docProc = docProc;
        this.stopWordsArray = stopWordsArray;
        this.indexer = new Indexer(indexBaseFolder, debug);
        this.nthreads = nthreads;
        this.debug = debug;
        this.stemming = stemming;
        this.stopWords = stopWords;
        this.bolleanIndex = bolleanIndex;
    }
    
    @Override
    public void run() {
        try {
            sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TI_Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        TI_Worker[] thread_pool = new TI_Worker[nthreads];
        buf = docProc.getNextDocument();
        while(buf != null){
            for(int i = 0; i < nthreads; i++){
                if (buf == null){
                    break;
                }
                thread_pool[i] = new TI_Worker(buf, indexer, stopWordsArray, debug, stemming, stopWords, bolleanIndex);
                thread_pool[i].start();
                buf = docProc.getNextDocument();
            }
            if (buf == null){
                break;
            }
            for(int i = 0; i < nthreads; i++){
                try {
                    thread_pool[i].join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(TI_Manager.class.getName()).log(Level.SEVERE, null, ex);
                }
                thread_pool[i] = null;
            }
        }
        if(debug){
            System.out.println("Manager has Finished execution...");
        }
        indexer.writeLast();
    }
    
}
