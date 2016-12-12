/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import ire.Objects.Result;

/**
 *
 * @author bernardo
 */
public class SE {
    
    public static void main(String [] args){
        
        int maxIndex = 10;
        int numResults = 100;
        boolean debug = false;
        String wordsPath = "indexes/other/words";
        String docsPath = "indexes/other/documents_final";
        String indexPath = "indexes/final_index_";
        String filesMapPath = "indexes/other/fileMapping";
        String stopWordsFile = "stopwords_en.txt";
        
        Searcher searcher = new Searcher(indexPath, stopWordsFile, numResults, maxIndex, debug);
        searcher.loadWords(wordsPath);
        searcher.loadDocs(docsPath, filesMapPath);
        
        String query = "java class tostring";
        Result[] results = searcher.search(query);
        for(Result a  : results){
            System.out.println(a);
        }
                   
    }
}
