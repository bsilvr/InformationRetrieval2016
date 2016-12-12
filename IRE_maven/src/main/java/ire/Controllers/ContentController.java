/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ire.Controllers;

import ire.Objects.Cache;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bernardo Ferreira <bernardomrferreira@ua.pt>
 * @author Bruno Silva <brunomiguelsilva@ua.pt>
 */
public class ContentController implements Initializable {

    @FXML private Button close;
    @FXML private TextArea content;


    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Cache cache = new Cache();
        
        content.setText(cache.getContent());
    }    
    
    @FXML
    private void handleClose(ActionEvent event) throws IOException {
        Stage stage = (Stage) close.getScene().getWindow();
        // do what you have to do
        stage.close();
    }
    
}
