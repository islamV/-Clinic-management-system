package com.clinicmanagementsystem.clinicmanagementsystem;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class PatientHomePageController {

    @FXML
    private Button historyAppointmentButton;

    @FXML
    private Button makeAppointmentButton;

    @FXML
    private Button logout_btn;


    public void historyButton(ActionEvent event) throws IOException {
        FXMLLoader patientLoader = new FXMLLoader(getClass().getResource("FXML/AppointmentsHistoryPage.fxml"));
        Parent patientRoot = patientLoader.load();
        Stage patientStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the stage from the event source
        patientStage.setScene(new Scene(patientRoot));
        patientStage.show();
    }
    public void makeButton(ActionEvent event) throws IOException {
        FXMLLoader patientLoader = new FXMLLoader(getClass().getResource("FXML/MakeAppointmentPage.fxml"));
        Parent patientRoot = patientLoader.load();
        Stage patientStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the stage from the event source
        patientStage.setScene(new Scene(patientRoot));
        patientStage.show();
    }

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
                Stage currentStage = (Stage) historyAppointmentButton.getScene().getWindow();
                currentStage.setScene(new Scene(root));
                currentStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}