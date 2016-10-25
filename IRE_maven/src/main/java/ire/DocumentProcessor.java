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
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class DocumentProcessor {
    
    private SortedSet<Document> documents;
    private Document[] documentsArray;
    private int currentDoc = 0;
    
    final String doc_path = "doc_dict.txt";    
    
    public DocumentProcessor(){
        documents = new TreeSet<>((Document a, Document b) -> a.compareTo(b));
        // criar pasta para guardar os documentos em formato unico
    }
    
    public void processDocument(CorpusFile cfile){
        // Ver a extensao e enviar o path para a função adequada.
        if(cfile.getExtension().equals("arff")){
            ArrayList<Document> tmp = ArffProcessor.identify(cfile);
            synchronized(documents){
                documents.addAll(tmp);
            }
        }
    }
    
    public void finishReadingDocs(){
        documentsArray = documents.toArray(new Document[0]);
        documents = null;
    }
    
    public Document[] getDocuments() {
        return documents.toArray(new Document[0]);
    }
    
    public synchronized Document getNextDocument(){
        if(currentDoc == documentsArray.length) {
            return null;
        }
        return documentsArray[currentDoc++];
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
            
            for(int i = 0; i < documentsArray.length; i++){
                if(documentsArray[i] == null){
                    System.err.println("Null Document" + i);
                    continue;
                }
                writer.println(documentsArray[i].getDocId() +";"+ documentsArray[i].getFilePath() +";"+ documentsArray[i].getOriginalDocId());
            }
            
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(Indexer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
