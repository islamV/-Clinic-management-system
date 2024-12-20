package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MakeReportController {

    @FXML
    private Button backButton;

    @FXML
    private Label makeLabel;

    @FXML
    private TextArea reportArea;

    @FXML
    private Button submitButton;

    private  int appointmentID;


    void setAppointmentId(int appointmentID) {
        this.appointmentID = appointmentID;

    }

    @FXML
    private void submitbutton() {
        String reportContent = reportArea.getText();

        if (reportContent == null || reportContent.trim().isEmpty()) {
            showAlert("Please enter a report.");
            return;
        }

        int doctorID = userData.id;
        String insertQuery = "Update reports set report_content = ? where appointment_id =?";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stat = con.prepareStatement(insertQuery)) {

            stat.setInt(2, appointmentID);
            stat.setString(1, reportContent);

            int rs = stat.executeUpdate();
            if (rs > 0) {
                showAlert("Report submitted successfully.");
                reportArea.clear();
                try{
                    PreparedStatement stut = con.prepareStatement("UPDATE appointments SET status = ? WHERE appointment_id = ?");
                    stut.setString(1, "Completed");
                    stut.setInt(2, appointmentID);
                    stut.executeUpdate();
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            } else {
                showAlert("Unable to submit the report.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("An error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void backbutton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/ViewAppointmentsToMakeReport.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            showAlert(e.getMessage());

            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}
