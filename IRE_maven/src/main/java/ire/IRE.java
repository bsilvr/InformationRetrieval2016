/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.DocumentProcessors.ArffProcessor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class IRE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        double startTime = System.currentTimeMillis();
        
        CorpusReader corpus = new CorpusReader();
        DocumentProcessor docProc = new DocumentProcessor();
        Indexer indexer = new Indexer();
        
        // Guardar stopwords numa Array List
        File stopWords = new File("stopwords_en.txt");
        ArrayList<String> stopWordsList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(stopWords))) {
            for(String line; (line = br.readLine()) != null; ) {
                stopWordsList.add(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(ArffProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        String[] stopWordsArray = stopWordsList.toArray(new String[0]);
        
        // Listar ficheiros, e respetivas extenções, de um determinado diretorio
        corpus.readDir("sample_corpus");
        
        // Dividir e guardar numa array list todos os documentos encontrados nos ficheiros
        CorpusFile file = corpus.getNextFile();
        while(file != null){
            docProc.processDocument(file);
            file = corpus.getNextFile();
        }
        docProc.finishReadingDocs();
        
        // Tokenizar cada documento e indexar cada token 
        Document doc = docProc.getNextDocument();
        String content = "";
        while(doc != null){
            content = docProc.getDocumentContent(doc);
            Tokenizer tokenizer = new Tokenizer(stopWordsArray);
            String[] tokens =  tokenizer.tokenize(content, doc);
            
            indexer.indexToken(tokens, doc.getDocId());
            
            doc = docProc.getNextDocument();
            
        }
        
        
        // 
        Runtime runtime = Runtime.getRuntime();

        NumberFormat format = NumberFormat.getInstance();

        StringBuilder sb = new StringBuilder();
        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        
        System.out.println("Allocated memory before write index: " + format.format((allocatedMemory-freeMemory) / 1024)+"Mb");
        
        indexer.writeIndex();
        docProc.writeDocuments();
        
        format = NumberFormat.getInstance();

        sb = new StringBuilder();
        maxMemory = runtime.maxMemory();
        allocatedMemory = runtime.totalMemory();
        freeMemory = runtime.freeMemory();

        
        System.out.println("Allocated memory after write index: " + format.format((allocatedMemory-freeMemory) / 1024)+"Mb");
        double endTime   = System.currentTimeMillis();
        double totalTime = (endTime - startTime)/1000;
        System.out.println("Runtime: "+totalTime+" seconds.");
    }
    
}