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
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class Buffer {
    private int nDocs;
    private final DocumentContent[] buffer;
    private int nBuffer = 0;
    
    public Buffer(int nDocs){
        this.nDocs = nDocs;
        buffer = new DocumentContent[nDocs];
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
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(CsvProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        nBuffer--;
        DocumentContent c = buffer[nBuffer];
        notifyAll();
        //System.err.println(nBuffer);
        return c;
    }
    
}
