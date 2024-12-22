package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ReportDetailsController {

    @FXML
    private Label reportIdLabel;

    @FXML
    private Label appointmentIdLabel;

    @FXML
    private Label doctorNameLabel;

    @FXML
    private Label patientNameLabel; // Added field

    @FXML
    private Label createdAtLabel;

    @FXML
    private TextArea reportContentLabel;

    /**
     * Sets the details of the report to the UI components.
     *
     * @param report The selected report whose details need to be displayed.
     */
    public void setReportDetails(Report report) {
        reportIdLabel.setText(String.valueOf(report.getReportId()));
        appointmentIdLabel.setText(String.valueOf(report.getAppointmentId()));
        doctorNameLabel.setText(report.getDoctorName());
        patientNameLabel.setText(report.getPatientName()); // Set patient name
        createdAtLabel.setText(report.getCreatedAt() != null ? report.getCreatedAt().toString() : "N/A");
        reportContentLabel.setText(report.getReportContent());
    }
    @FXML
    private void handleCloseButton(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }
}
