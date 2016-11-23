/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.DocumentProcessors.CsvProcessor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Buffer {
    private final int nDocs;
    private final DocumentContent[] buffer;
    private int nBuffer = 0;
    private boolean finish = false;
    
    public Buffer(int nDocs){
        this.nDocs = nDocs;
        buffer = new DocumentContent[nDocs];
        finish = false;
    }
    
    public synchronized void addItem(DocumentContent d){
        while(nBuffer >= nDocs){
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(CsvProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (nBuffer < nDocs){
            buffer[nBuffer] = d;
            nBuffer++;
            notifyAll();
        }
    }
    
    public synchronized DocumentContent getItem(){
        while(nBuffer <= 0){
            if (finish){
                return null;
            }
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(CsvProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        nBuffer--;
        DocumentContent c = buffer[nBuffer];
        notifyAll();
        return c;
    }
    
    public synchronized void setFinish(){ 
        finish=true;
        notifyAll();
    }
    
}
