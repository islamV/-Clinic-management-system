package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.scene.control.cell.CheckBoxTableCell;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewAppointmentsToMakeReportController {

    @FXML
    private TableView<Appointment> appointmentsTable;

    @FXML
    private TableColumn<Appointment, Boolean> selectColumn;

    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;

    @FXML
    private TableColumn<Appointment, String> patientNameColumn;

    @FXML
    private Button generateReportButton;

    @FXML
    private Button clearButton;

    private final ObservableList<Appointment> appointments = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));

        appointmentsTable.setItems(appointments);

        generateReportButton.setDisable(true);

        loadAppointmentsWithoutReports();

        appointmentsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> generateReportButton.setDisable(newSelection == null)
        );
    }

    private void loadAppointmentsWithoutReports() {
        appointments.clear();

        String sql = """
                SELECT 
                    a.appointment_id, 
                    u.name AS patient_name
                FROM 
                    appointments a
                INNER JOIN 
                    users u ON a.patient_id = u.user_id
                LEFT JOIN 
                    reports r ON a.appointment_id = r.appointment_id
                WHERE 
                    (r.report_content IS NULL OR r.report_content = '')
                    AND a.status = 'Completed';
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                appointments.add(new Appointment(
                        rs.getInt("appointment_id"),
                        rs.getString("patient_name"),
                        0
                ));
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error loading appointments: " + e.getMessage());
        }
    }

    @FXML
    private void handleGenerateReport() {
        Appointment selectedAppointment = appointmentsTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            showAlert(Alert.AlertType.WARNING, "No Appointment Selected",
                    "Please select an appointment to generate a report.");
            return;
        }

        try {
            int appointmentID = selectedAppointment.getAppointmentId();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("MakeReport.fxml"));
            MakeReportController controller = new MakeReportController(appointmentID);
            loader.setController(controller);

            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Make Report");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error",
                    "Error opening report window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClear() {
        appointmentsTable.getSelectionModel().clearSelection();
        loadAppointmentsWithoutReports();
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
