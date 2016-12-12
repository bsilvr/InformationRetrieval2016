package ire;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import ire.Objects.Cache;
import ire.Objects.Result;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    @FXML private Button back;
    @FXML private Button open;
    @FXML private Label pageCount;
    
    private int page;
    private Searcher searcher;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        page = 1;
        Cache cache = new Cache();
        String query = cache.getQuery();
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
        
        pageCount.setText("Page: " + page + "/"+ searcher.getPageCount());
        docId.setCellValueFactory(new PropertyValueFactory<Result, String>("docId"));
        file.setCellValueFactory(new PropertyValueFactory<Result, String>("filePath"));
        score.setCellValueFactory(new PropertyValueFactory<Result, String>("score"));

        tableView.getItems().setAll(results);
        
        
    }
    
    @FXML
    private void handleNext(ActionEvent event) throws IOException {
        page+=1;
        Result[] results = searcher.getResultsPage(page);
        if (results != null){
            
            pageCount.setText("Page: " + page + "/"+ searcher.getPageCount());
            docId.setCellValueFactory(new PropertyValueFactory<Result, String>("docId"));
            file.setCellValueFactory(new PropertyValueFactory<Result, String>("filePath"));
            score.setCellValueFactory(new PropertyValueFactory<Result, String>("score"));
            tableView.getItems().setAll(results);
        }
    }
    
    @FXML
    private void handlePrevious(ActionEvent event) throws IOException {
        page-=1;
        Result[] results = searcher.getResultsPage(page);
        if (results != null){

            pageCount.setText("Page: " + page + "/"+ searcher.getPageCount());
            docId.setCellValueFactory(new PropertyValueFactory<Result, String>("docId"));
            file.setCellValueFactory(new PropertyValueFactory<Result, String>("filePath"));
            score.setCellValueFactory(new PropertyValueFactory<Result, String>("score"));
            tableView.getItems().setAll(results);
        }
    }
    
    
    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        Stage stage ;
        Parent root;
         
        //get reference to the button's stage         
        stage=(Stage) back.getScene().getWindow();
        //load up OTHER FXML document
        root = FXMLLoader.load(getClass().getResource("/fxml/Scene.fxml"));
           
     
        //create a new scene with root and set the stage
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    private void handleOpen(ActionEvent event) throws IOException {
        Cache cache = new Cache();
        Result result = tableView.getSelectionModel().getSelectedItem();
        cache.setContent(searcher.getContent(result));
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Content.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("ABC");
            stage.setScene(new Scene(root1));  
            stage.show();
    }
    
    
}
