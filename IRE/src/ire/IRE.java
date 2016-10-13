/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

/**
 *
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class IRE {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        CorpusReader corpus = new CorpusReader();
        
        corpus.readDir("sample_corpus");
        
        DocumentProcessor docProc = new DocumentProcessor();
        
        CorpusFile file = corpus.getNextFile();
        while(file != null){
            docProc.processDocument(file);
            file = corpus.getNextFile();
        }
    }
    
}
