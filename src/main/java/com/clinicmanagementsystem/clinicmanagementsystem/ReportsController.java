package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class ReportsController {

    @FXML
    private TableView<Report> reportsTable;

    @FXML
    private TableColumn<Report, Integer> reportIdColumn;

    @FXML
    private TableColumn<Report, Integer> appointmentIdColumn;

    @FXML
    private TableColumn<Report, String> doctorNameColumn;

    @FXML
    private TableColumn<Report, String> reportContentColumn;

    @FXML
    private TableColumn<Report, String> createdAtColumn;

    private ObservableList<Report> reportsList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up table columns
        reportIdColumn.setCellValueFactory(new PropertyValueFactory<>("reportId"));
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        reportContentColumn.setCellValueFactory(new PropertyValueFactory<>("reportContent"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        // Load data into the table
        loadReportsData();

        // Add a listener for row clicks
        reportsTable.setOnMouseClicked(event -> handleRowClick());
    }

    private void loadReportsData() {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        String query = """
        
                SELECT
            r.report_id,
            r.appointment_id,
            u.name AS doctor_name,
            p.name AS patient_name, -- Assuming "p.name" is the patient's name in the database
            r.created_at,
            r.report_content
        FROM
            reports r
        JOIN
            appointments a ON r.appointment_id = a.appointment_id
        JOIN
            doctors d ON a.doctor_id = d.doctor_id
        JOIN
            users p ON a.patient_id = p.user_id
        JOIN
            users u ON d.user_id = u.user_id
        WHERE
            d.user_id = ?;
                                """;

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, userData.id); // Assuming `userData.id` provides the doctor ID

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int reportId = resultSet.getInt("report_id");
                    int appointmentId = resultSet.getInt("appointment_id");
                    String doctorName = resultSet.getString("doctor_name");
                    String patientName = resultSet.getString("patient_name");
                    String reportContent = resultSet.getString("report_content");

                    Timestamp createdAt = resultSet.getTimestamp("created_at");

                    reportsList.add(new Report(reportId, appointmentId, doctorName, patientName, reportContent, createdAt));
                }

                reportsTable.setItems(reportsList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/Doctor-Dashboard.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void handleRowClick() {
        Report selectedReport = reportsTable.getSelectionModel().getSelectedItem();
        if (selectedReport != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/ReportDetails.fxml"));
                Parent root = loader.load();

                // Pass the selected report details to the new controller
                ReportDetailsController controller = loader.getController();
                controller.setReportDetails(selectedReport);

                // Load the new page
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Report Details");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
