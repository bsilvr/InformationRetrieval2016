/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.DocumentProcessors.CsvProcessor;
import ire.DocumentProcessors.Processor;

/**
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class DocumentProcessor {

    private Processor proc;
    private Buffer buffer;
    
    public DocumentProcessor(int nBuffer){
        this.buffer = new Buffer(nBuffer);
    }
    
    public void processDocument(CorpusFile cfile){
        // Ver a extensao e enviar o path para a função adequada.
        //if(cfile.getExtension().equals("arff")){
            //proc = new ArffProcessor(buffer);
            //proc.process(cfile);
        //}
        //else 
        if(cfile.getExtension().equals("csv")){
            proc = new CsvProcessor(buffer);
            proc.process(cfile);
        }
    }
    
    public DocumentContent getNextDocument(){
        return proc.getDocument();
    }
    
    public void setFinish(){
        buffer.setFinish();
    }
}
