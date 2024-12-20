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
import java.sql.*;

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
    }

    private void loadReportsData() {
        DatabaseConnection databaseConnection = new DatabaseConnection();


        String query = """
                SELECT r.report_id, r.appointment_id, r.doctor_id, u.name AS doctor_name, 
                       r.report_content, r.created_at
                FROM reports r
                INNER JOIN users u ON r.doctor_id = u.user_id
                """;

        try (Connection connection = databaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int reportId = resultSet.getInt("report_id");
                int appointmentId = resultSet.getInt("appointment_id");
                int doctorId = resultSet.getInt("doctor_id");
                String doctorName = resultSet.getString("doctor_name");
                String reportContent = resultSet.getString("report_content");
                Timestamp createdAt = resultSet.getTimestamp("created_at");

                reportsList.add(new Report(reportId, appointmentId, doctorId, doctorName, reportContent, createdAt));
            }

            reportsTable.setItems(reportsList);

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
}
