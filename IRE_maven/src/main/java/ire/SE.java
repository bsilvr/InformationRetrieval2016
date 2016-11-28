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
        
        int numResults = 10;
        boolean debug = false;
        String wordsPath = "indexes/other/words";
        String docsPath = "indexes/other/docs";
        String indexPath = "indexes/final_index_";
        String filesMapPath = "indexes/other/files";
        String stopWordsFile = "stopwords_en.txt";
        
        Searcher searcher = new Searcher(indexPath, stopWordsFile, numResults, debug);
        searcher.loadWords(wordsPath);
        searcher.loadDocs(docsPath, filesMapPath);
        
        String query = "abc";
        Result[] results = searcher.search(query);
        
        
       
        
    }
}
