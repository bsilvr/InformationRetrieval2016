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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bernardo
 */
public class TI_Manager  extends Thread{
    String[] stopWordsArray;
    DocumentContent buf;
    TI_Worker thrd;
    DocumentProcessor docProc;
    Tokenizer tokenizer;
    Indexer indexer;
   
    public TI_Manager(DocumentProcessor docProc, String[] stopWordsArray){
        this.docProc = docProc;
        this.stopWordsArray = stopWordsArray;
        this.indexer = new Indexer();
    }
    
    @Override
    public void run() {
        try {
            sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TI_Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        TI_Worker[] thread_pool = new TI_Worker[100];
        buf = docProc.getNextDocument();
        while(buf != null){
            for(int i = 0; i < 100; i++){
                if (buf == null){
                    break;
                }
                thread_pool[i] = new TI_Worker(buf, indexer, stopWordsArray);
                thread_pool[i].start();
                buf = docProc.getNextDocument();
            }
            for(int i = 0; i < 100; i++){
                try {
                    thread_pool[i].join();
                } catch (InterruptedException ex) {
                    Logger.getLogger(TI_Manager.class.getName()).log(Level.SEVERE, null, ex);
                }
                thread_pool[i] = null;
            }
                
            //System.out.println(Thread.activeCount());
        }
        System.out.println("Manageer out");
    }
    
}
