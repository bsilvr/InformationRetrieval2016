/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.workers.DP_Worker;

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
        String dir = "stacksample";
        int nthreads_dp = 10;
        int nthreads_ti = 10;
        
        ////////////////////////////////////////////////
        double startTime = System.currentTimeMillis();
        double endTime;
        double totalTime;
        
        CorpusReader corpus = new CorpusReader();
        DocumentProcessor docProc = new DocumentProcessor();
        Index idx = new Index();
        
        //Launching threads
        DP_Worker[] thread_pool_dp = new DP_Worker[nthreads_dp];
        
        for(int i = 0; i < nthreads_dp; i++){
            thread_pool_dp[i] = new DP_Worker(corpus, docProc);
            thread_pool_dp[i].start();
        }

        // Listar ficheiros, e respetivas extenções, de um determinado diretorio
        corpus.readDir(dir);
        
        endTime = System.currentTimeMillis();
        totalTime = (endTime - startTime)/1000;
        System.out.println("Finished Reading Directory: "+totalTime+" seconds.");
/*
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
        
        TI_Worker[] thread_pool = new TI_Worker[nthreads_ti];
        for(int i = 0; i < nthreads_ti; i++){
            thread_pool[i] = new TI_Worker(docProc, idx, stopWordsArray);
            thread_pool[i].start();
        }
        
        for(int i = 0; i < nthreads_dp; i++){
            try {
                thread_pool_dp[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(IRE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        docProc.finishedProcess();
        
        endTime = System.currentTimeMillis();
        totalTime = (endTime - startTime)/1000;
        System.out.println("Finished Processed Documents: "+totalTime+" seconds.");
        
        for(int i = 0; i < nthreads_ti; i++){
            try {
                thread_pool[i].join();
            } catch (InterruptedException ex) {
                Logger.getLogger(IRE.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        endTime = System.currentTimeMillis();
        totalTime = (endTime - startTime)/1000;
        System.out.println("Finished Indexing: "+totalTime+" seconds.");
        
        Indexer indexer = new Indexer(idx);
        indexer.writeIndex();
        docProc.writeDocuments();
               
        endTime = System.currentTimeMillis();
        totalTime = (endTime - startTime)/1000;
        System.out.println("Finished writing index to file: "+totalTime+" seconds.");*/
    }
    
}