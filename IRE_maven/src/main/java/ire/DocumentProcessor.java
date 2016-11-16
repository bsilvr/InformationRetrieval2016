/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.DocumentProcessors.CsvProcessor;
import ire.DocumentProcessors.Processor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class DocumentProcessor {
    
    private final ArrayList<Document> documents;
    private int currentDoc;
    private int ndocs;
    private int docsToProcess;
    final String doc_path = "doc_dict.txt";
    private Processor proc;
    private Buffer buffer;
    
    public DocumentProcessor(int nBuffer){
        documents = new ArrayList<>();
        currentDoc = 0;
        ndocs = 0;
        docsToProcess = 0;
        this.buffer = new Buffer(nBuffer);
    }
    
    public void processDocument(CorpusFile cfile){
        // Ver a extensao e enviar o path para a função adequada.
        /*if(cfile.getExtension().equals("arff")){
            ArrayList<Document> tmp = ArffProcessor.identify(cfile);
            synchronized(documents){
                documents.addAll(tmp);
                docsToProcess += tmp.size();
                notifyAll();
            }
        }
        else*/ 
        if(cfile.getExtension().equals("csv")){
            proc = new CsvProcessor(buffer);
            ArrayList<Document> tmp = proc.process(cfile);
            synchronized(documents){
                documents.addAll(tmp);
                docsToProcess += tmp.size();
            }
        }
    }
    
    public Document[] getDocuments() {
        return documents.toArray(new Document[0]);
    }
    
    public DocumentContent getNextDocument(){
        return proc.getDocument();
    }
    
    public void setFinish(){
        buffer.setFinish();
    }
       
    public int getSize(){
        return documents.size();
    }
    
    public void writeDocuments(){
        Document[] documentsArray = documents.toArray(new Document[0]);
        Arrays.sort(documentsArray, (Document a, Document b) -> a.compareTo(b));
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

    public synchronized void finishedProcess() {
        ndocs = docsToProcess;
        notifyAll();
    }
    
}
