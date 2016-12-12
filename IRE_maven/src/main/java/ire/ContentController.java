/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author bernardo
 */
public class ContentController implements Initializable {

    @FXML private Button close;
    @FXML private WebView web;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        web = new WebView();
        final WebEngine webEngine = web.getEngine();

        
        webEngine.load("http://www.oracle.com/products/index.html");
    }    
    
    @FXML
    private void handleClose(ActionEvent event) throws IOException {
        Stage stage = (Stage) close.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    
}
