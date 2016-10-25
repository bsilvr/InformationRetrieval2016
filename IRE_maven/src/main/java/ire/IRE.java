/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.DocumentProcessors.ArffProcessor;
import ire.workers.DP_Worker;
import ire.workers.TI_Worker;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
        String dir = "corpus-RI";
        
        double startTime = System.currentTimeMillis();
        
        Runtime runtime = Runtime.getRuntime();
        
        int nthreads = runtime.availableProcessors() * 2;
        
        CorpusReader corpus = new CorpusReader();
        DocumentProcessor docProc = new DocumentProcessor();
        //Indexer indexer = new Indexer();
        Index idx = new Index();
        
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
        corpus.readDir(dir);
        
        // Dividir e guardar numa array list todos os documentos encontrados nos ficheiros
//        CorpusFile file = corpus.getNextFile();
//        while(file != null){
//            docProc.processDocument(file);
//            file = corpus.getNextFile();
//        }

        DP_Worker[] thread_pool_dp = new DP_Worker[nthreads];
        
        for(int i = 0; i < nthreads; i++){
            thread_pool_dp[i] = new DP_Worker(corpus, docProc);
            thread_pool_dp[i].start();
        }
        
        for(int i = 0; i < nthreads; i++){
            try {
                thread_pool_dp[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(IRE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        docProc.finishReadingDocs();
        
        
        
        TI_Worker[] thread_pool = new TI_Worker[nthreads];
        
        for(int i = 0; i < nthreads; i++){
            thread_pool[i] = new TI_Worker(docProc, idx, stopWordsArray);
            thread_pool[i].start();
        }
        
        for(int i = 0; i < nthreads; i++){
            try {
                thread_pool[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(IRE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        

        //NumberFormat format = NumberFormat.getInstance();

        //long allocatedMemory = runtime.totalMemory();
        //long freeMemory = runtime.freeMemory();

        
        //System.out.println("Allocated memory before write index: " + format.format((allocatedMemory-freeMemory) / 1024)+"Mb");
        
        
        Indexer indexer = new Indexer(idx);
        indexer.writeIndex();
        docProc.writeDocuments();
               
        //freeMemory = runtime.freeMemory();

        
        //System.out.println("Allocated memory after write index: " + format.format((allocatedMemory-freeMemory) / 1024)+"Mb");
        double endTime = System.currentTimeMillis();
        double totalTime = (endTime - startTime)/1000;
        System.out.println("Runtime: "+totalTime+" seconds.");
    }
    
}