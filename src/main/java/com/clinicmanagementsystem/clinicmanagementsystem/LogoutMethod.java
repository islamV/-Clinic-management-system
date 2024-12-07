package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LogoutMethod {


    //Copy the method, Unhash lines 16,17,18 and replace test_txtfld with any component id in your current stage
    public void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/clinicmanagementsystem/clinicmanagementsystem/FXML/LoginPage.fxml"));
            Parent root = loader.load();
            //Stage currentStage = (Stage) test_txtfld.getScene().getWindow();
            //currentStage.setScene(new Scene(root));
            //currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
