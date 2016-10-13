/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.DocumentProcessors.ArffProcessor;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class DocumentProcessor {
    
    private ArrayList<Document> documents;
    private Iterator<Document> documentsIterator;
    
    public DocumentProcessor(){
        documents = new ArrayList<>();
        // criar pasta para guardar os documentos em formato unico
    }
    
    public void processDocument(CorpusFile cfile){
        // Ver a extensao e enviar o path para a função adequada.
        if(cfile.getExtension().equals("arff")){
            documents.addAll(ArffProcessor.process(cfile));
        }
        
        documentsIterator = documents.iterator();
    }
    
    public ArrayList<Document> getDocuments() {
        return documents;
    }
    
    public Document getNextDocument(){
        if(documentsIterator.hasNext()) {
            return documentsIterator.next();
        }
        return null;
    }
    
}
