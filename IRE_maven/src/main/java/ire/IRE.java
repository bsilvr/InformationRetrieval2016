/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

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
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class IRE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    
        // Settings
        
        // Number of threads reading a file in the dir
        int nthreads_dp = 1;
        
        // Number of threads tokenizing and indexing a document
        int nthreads_ti = 100;
        
        // Directory to index
        String dir = "stacksample";
        
        // Stop words file
        String stopWordsFile = "stopwords_en.txt";
        
        //Max documents in ram
        int nBuffer = 100;
        
        // Extensions to ignore when reading dir
        String [] ignoreExtensions = {".pdf", ".docx", ".txt"};
        
        
        // String documentMappingPath = "";
        
        // Whether the tokenizer should stemm words or not
        // boolean stemming = true;
        
        // Whether the tokenizer should remove stop words or not
        // boolean removeStopWords = true;
        
        /////////////////////////////////////////////////////////////////////////////////
        int mb = 1024*1024;
		
        //Getting the runtime reference from system
        Runtime runtime = Runtime.getRuntime();

        System.out.println("##### Heap utilization statistics [MB] #####");

        //Print used memory
        System.out.println("Used Memory:" 
                + (runtime.totalMemory() - runtime.freeMemory()) / mb);

        //Print free memory
        System.out.println("Free Memory:" 
                + runtime.freeMemory() / mb);

        //Print total available memory
        System.out.println("Total Memory:" + runtime.totalMemory() / mb);

        //Print Maximum available memory
        System.out.println("Max Memory:" + runtime.maxMemory() / mb);
        
        /////////////////////////////////////////////////////////////////////////////////
        double startTime = System.currentTimeMillis();
        double endTime;
        double totalTime;
        
        CorpusReader corpus = new CorpusReader(ignoreExtensions);
        DocumentProcessor docProc = new DocumentProcessor(nBuffer);
        
        // Guardar stopwords numa Array List
        File stopWords = new File(stopWordsFile);
        ArrayList<String> stopWordsList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(stopWords))) {
            for(String line; (line = br.readLine()) != null; ) {
                stopWordsList.add(line);
            }
        } catch (IOException ex) {
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
        System.out.println("Finished Merging Index: "+totalTime+" seconds.");
    }
    
}