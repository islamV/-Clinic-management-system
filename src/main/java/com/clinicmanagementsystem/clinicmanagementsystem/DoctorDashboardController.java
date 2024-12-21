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

public class DoctorDashboardController {
    @FXML
    private Button logout_btn;

    @FXML
    private Button make_reports_btn;

    @FXML
    private Button view_reports_btn;

    @FXML
    private Button view_schedule_btn;
    @FXML
    private Button appointments_btn;

    public void makeReports() throws IOException {
        FXMLLoader Loader = new FXMLLoader(getClass().getResource("FXML/ViewAppointmentsToMakeReport.fxml"));
        Parent Root = Loader.load();
        Stage patientStage = (Stage) make_reports_btn.getScene().getWindow();
        patientStage.setScene(new Scene(Root));
        patientStage.show();
    }

    public void viewReports(ActionEvent event) throws IOException {
        FXMLLoader patientLoader = new FXMLLoader(getClass().getResource("FXML/doctorReports.fxml"));
        Parent patientRoot = patientLoader.load();
        Stage patientStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the stage from the event source
        patientStage.setScene(new Scene(patientRoot));
        patientStage.show();
    }

    public void viewSchedule(ActionEvent event) throws IOException {
        FXMLLoader patientLoader = new FXMLLoader(getClass().getResource("FXML/doctorSchedules.fxml"));
        Parent patientRoot = patientLoader.load();
        Stage patientStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the stage from the event source
        patientStage.setScene(new Scene(patientRoot));
        patientStage.show();
    }
    public void Appointments(ActionEvent event) throws IOException {
        FXMLLoader patientLoader = new FXMLLoader(getClass().getResource("FXML/AppointmentsHistoryDoctorDashboard.fxml"));
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
                Stage currentStage = (Stage) make_reports_btn.getScene().getWindow();
                currentStage.setScene(new Scene(root));
                currentStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
