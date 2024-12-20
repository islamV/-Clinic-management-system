package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.util.Optional;

public class LogoutMethod {


    //Copy the method, Unhash lines 28,29,30 and replace test_txtfld with any component id in your current stage
    public void logout() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to signout?");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get().equals(ButtonType.OK)) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/LoginPage.fxml"));
                Parent root = loader.load();
                //Stage currentStage = (Stage) test_txtfld.getScene().getWindow();
                //currentStage.setScene(new Scene(root));
                //currentStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
