/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.clinicmanagementsystem.clinicmanagementsystem;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * FXML Controller class
 *
 * @author Eng. Mohamed
 */
public class LoginPageController implements Initializable {

    
    @FXML
    private Hyperlink dont_hyperlink;

    @FXML
    private TextField email_txtfld;

    @FXML
    private Button login_btn;

    @FXML
    private PasswordField password_txtfld;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
