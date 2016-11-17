/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.workers;

import ire.Buffer;
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

/**
 *
 * @author bernardo
 */
public class TI_Manager  extends Thread{
    
    String[] stopWordsArray;
    DocumentContent buf;
    TI_Worker thrd;
    DocumentProcessor docProc;
   
    public TI_Manager(DocumentProcessor docProc, String[] stopWordsArray){
        this.docProc = docProc;
        this.stopWordsArray = stopWordsArray;
    }
    
    @Override
    public void run() {
        try {
            sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TI_Manager.class.getName()).log(Level.SEVERE, null, ex);
        }
        buf = docProc.getNextDocument();
        while(buf != null){
            if(Thread.activeCount() < 500){
                thrd = new TI_Worker(buf, stopWordsArray);
                thrd.start();
                buf = docProc.getNextDocument();
            }
            
            //System.out.println(Thread.activeCount());
        }
        System.out.println("Manageer out");
    }
    
}
