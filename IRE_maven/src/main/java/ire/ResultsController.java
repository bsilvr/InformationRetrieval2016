package ire;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ire.Objects.Query;
import ire.Objects.Result;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author bernardo
 */
public class ResultsController implements Initializable {

    private static String query;
    
    @FXML private TableView<Result> tableView;
    @FXML private TableColumn<Result, String> docId;
    @FXML private TableColumn<Result, String> file;
    @FXML private TableColumn<Result, String> score;
    @FXML private Button next;
    @FXML private Button previous;
    
    private int page;
    private Searcher searcher;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        page = 1;
        Query qry = new Query();
        String query = qry.getQuery();
        int numResults = 10;
        int maxIndex = 10;
        boolean debug = false;
        String wordsPath = "indexes/other/words";
        String docsPath = "indexes/other/documents_final";
        String indexPath = "indexes/final_index_";
        String filesMapPath = "indexes/other/fileMapping";
        String stopWordsFile = "stopwords_en.txt";
        
        
        searcher = new Searcher(indexPath, stopWordsFile, numResults, maxIndex, debug);
        searcher.loadWords(wordsPath);
        searcher.loadDocs(docsPath, filesMapPath);
        
        Result[] results = searcher.search(query);
        for(Result a  : results){
            System.out.println(a);
        }
        docId.setCellValueFactory(new PropertyValueFactory<Result, String>("docId"));
        file.setCellValueFactory(new PropertyValueFactory<Result, String>("filePath"));
        score.setCellValueFactory(new PropertyValueFactory<Result, String>("score"));

        tableView.getItems().setAll(results);
   
    }
    
    @FXML
    private void handleNext(ActionEvent event) throws IOException {
        page+=1;
        Result[] results = searcher.getResultsPage(page);
        for(Result a  : results){
            System.out.println(a);
        }
        docId.setCellValueFactory(new PropertyValueFactory<Result, String>("docId"));
        file.setCellValueFactory(new PropertyValueFactory<Result, String>("filePath"));
        score.setCellValueFactory(new PropertyValueFactory<Result, String>("score"));

        tableView.getItems().setAll(results);
    }
    
    @FXML
    private void handlePrevious(ActionEvent event) throws IOException {
    
    }
    
    
}
