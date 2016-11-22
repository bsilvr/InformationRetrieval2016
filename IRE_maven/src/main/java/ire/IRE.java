/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.DocumentProcessors.ArffProcessor;
import ire.workers.DP_Worker;
import ire.workers.TI_Manager;
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
        // configurações
        int nthreads_dp = 1;
        int nthreads_ti = 100;
        
        String dir = "stacksample";
        String stopWordsFile = "stopwords_en.txt";
        
        //Max documents in ram
        int nBuffer = 100;
        
        // String documentMappingPath = "";
        // boolean stemming = true;
        // boolean removeStopWords = true;
        
        ////////////////////////////////////////////////
        double startTime = System.currentTimeMillis();
        double endTime;
        double totalTime;
        
        CorpusReader corpus = new CorpusReader();
        DocumentProcessor docProc = new DocumentProcessor(nBuffer);
        
        // Guardar stopwords numa Array List
        File stopWords = new File(stopWordsFile);
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
        
        //Launching threads
        DP_Worker[] thread_pool_dp = new DP_Worker[nthreads_dp];
  
        for(int i = 0; i < nthreads_dp; i++){
            thread_pool_dp[i] = new DP_Worker(corpus, docProc);
            thread_pool_dp[i].start();
        }
        
        TI_Manager timnger = new TI_Manager(nthreads_ti, docProc, stopWordsArray);
        timnger.start();

        for(int i = 0; i < nthreads_dp; i++){
            try {
                thread_pool_dp[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(IRE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            timnger.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(IRE.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        endTime = System.currentTimeMillis();
        totalTime = (endTime - startTime)/1000;
        System.out.println("Finished Indexing: "+totalTime+" seconds.");
        
        Indexer indexer = new Indexer();
        indexer.mergeIndex();
        
        endTime = System.currentTimeMillis();
        totalTime = (endTime - startTime)/1000;
        System.out.println("Finished Indexing: "+totalTime+" seconds.");
    }
    
}