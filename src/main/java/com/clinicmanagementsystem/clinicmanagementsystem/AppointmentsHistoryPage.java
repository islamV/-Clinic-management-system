package com.clinicmanagementsystem.clinicmanagementsystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AppointmentsHistoryPage {
    @FXML
    private void handleBackButton(ActionEvent event)throws IOException {
        FXMLLoader patientLoader = new FXMLLoader(getClass().getResource("FXML/patient-home-page.fxml"));
        Parent patientRoot = patientLoader.load();
        Stage patientStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the stage from the event source
        patientStage.setScene(new Scene(patientRoot));
        patientStage.show();
    }


}
