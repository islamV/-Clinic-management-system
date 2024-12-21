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
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MakeReportController {

    @FXML
    private Button backButton;

    @FXML
    private Label makeLabel;

    @FXML
    private TextArea reportArea;

    @FXML
    private Button submitButton;

    private int appointmentID;
    private int doctorId;

    void setAppointmentId(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    @FXML
    private void submitbutton() {
        String reportContent = reportArea.getText();

        if (reportContent == null || reportContent.trim().isEmpty()) {
            showAlert("Please enter a report.");
            return;
        }

        String insertQuery = """
            INSERT INTO reports ( doctor_id, appointment_id, report_content, created_at)
            VALUES ( ?, ?, ?, ?)
        """;

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stat = con.prepareStatement(insertQuery)) {

            // Set values for the query
            stat.setInt(1, doctorId);
            stat.setInt(2, appointmentID);
            stat.setString(3, reportContent);
            stat.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            int rowsAffected = stat.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Report submitted successfully.");
                reportArea.clear();

                // Update the appointment status to 'Completed'
                try (PreparedStatement updateAppointment = con.prepareStatement(
                        "UPDATE appointments SET status = ? WHERE appointment_id = ?"
                )) {
                    updateAppointment.setString(1, "Completed");
                    updateAppointment.setInt(2, appointmentID);
                    updateAppointment.executeUpdate();
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
