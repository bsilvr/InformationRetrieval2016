package ire;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ire.Objects.Query;
import ire.Objects.Result;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author bernardo
 */
public class ResultsController implements Initializable {

    private static String query;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        Query qry = new Query();
        String query = qry.getQuery();
        int numResults = 10;
        boolean debug = false;
        String wordsPath = "indexes/other/words";
        String docsPath = "indexes/other/documents_final";
        String indexPath = "indexes/final_index_";
        String filesMapPath = "indexes/other/fileMapping";
        String stopWordsFile = "stopwords_en.txt";
        
        Searcher searcher = new Searcher(indexPath, stopWordsFile, numResults, debug);
        searcher.loadWords(wordsPath);
        searcher.loadDocs(docsPath, filesMapPath);
        
        Result[] results = searcher.search(query);
        for(Result a  : results){
            System.out.println(a);
        }
    }
    
    
}
