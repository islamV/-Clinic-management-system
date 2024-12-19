package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class DoctorDashboardController {
    @FXML
    private Button logout_btn;

    @FXML
    private Button make_reports_btn;

    @FXML
    private Button view_reports_btn;

    @FXML
    private Button view_schedule_btn;

    public void makeReports() throws IOException {
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("FXML/ViewAppointmentsToMakeReport.fxml"));
        Parent Root = Loader.load();
        Stage patientStage = (Stage) make_reports_btn.getScene().getWindow();
        patientStage.setScene(new Scene(Root));
        patientStage.show();
    }

    public void viewReports(){

    }

    public void viewSchedule(){

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
                Stage currentStage = (Stage) make_reports_btn.getScene().getWindow();
                currentStage.setScene(new Scene(root));
                currentStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
