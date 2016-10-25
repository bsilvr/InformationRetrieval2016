/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.DocumentProcessors.ArffProcessor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class DocumentProcessor {
    
    private ArrayList<Document> documents;
    private Iterator<Document> documentsIterator;
    
    final String doc_path = "doc_dict.txt";    
    
    public DocumentProcessor(){
        documents = new ArrayList<>();
        // criar pasta para guardar os documentos em formato unico
    }
    
    public void processDocument(CorpusFile cfile){
        // Ver a extensao e enviar o path para a função adequada.
        if(cfile.getExtension().equals("arff")){
            documents.addAll(ArffProcessor.identify(cfile));
        }
    }
    
    public void finishReadingDocs(){
        documentsIterator = documents.iterator();
    }
    
    public ArrayList<Document> getDocuments() {
        return documents;
    }
    
    public synchronized Document getNextDocument(){
        if(documentsIterator.hasNext()) {
            return documentsIterator.next();    
        }
        return null;
    }
    
    public String getDocumentContent(Document doc){
        if (doc.getFilePath().endsWith(".arff")){
            return ArffProcessor.process(doc);
        } 
        return null;
    }
    
    public void writeDocuments(){
        File fl = new File(doc_path);
        PrintWriter writer;
        try {
            writer = new PrintWriter(fl, "UTF-8");
            
            for(Document i: documents){
                
                writer.println(i.getDocId() +";"+ i.getFilePath() +";"+ i.getOriginalDocId());
            }
            
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
