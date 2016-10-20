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
        // TODO code application logic here
        
        CorpusReader corpus = new CorpusReader();
        DocumentProcessor docProc = new DocumentProcessor();
        Indexer indexer = new Indexer();
        
        File stopWords = new File("stopwords_en.txt");
        ArrayList<String> stopWordsList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(stopWords))) {
            for(String line; (line = br.readLine()) != null; ) {
                stopWordsList.add(line);
            }
        } catch (IOException ex) {
            Logger.getLogger(ArffProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        corpus.readDir("sample_corpus");
        
        CorpusFile file = corpus.getNextFile();
        while(file != null){
            docProc.processDocument(file);
            file = corpus.getNextFile();
        }
        
        Document doc = docProc.getNextDocument();
        while(doc != null){
            
            Tokenizer tokenizer = new Tokenizer(stopWordsList.toArray(new String[0]));
            tokenizer.tokenize(doc);
            
            Token token = tokenizer.getNextToken();
            while(token != null){
                //System.out.println(token.getTerm());
                indexer.indexToken(token);
                
                token = tokenizer.getNextToken();
            }
            
            doc = docProc.getNextDocument();
        }
        indexer.writeIndex();
        System.err.println("DONE!");
    }
    
}
