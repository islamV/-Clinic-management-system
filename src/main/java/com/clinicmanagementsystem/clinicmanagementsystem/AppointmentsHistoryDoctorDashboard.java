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

public class AppointmentsHistoryDoctorDashboard {


    @FXML
    private TableView<Appointment> tableView;
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdColumn;
    @FXML
    private TableColumn<Appointment, Integer> queueNumberColumn;
    @FXML
    private TableColumn<Appointment, String> statusColumn;
    @FXML
    private TableColumn<Appointment, String> patientNameColumn;
    @FXML
    private TableColumn<Appointment, String> doctorNameColumn;
    @FXML
    private TableColumn<Appointment, String> scheduleDayColumn;
    @FXML
    private TableColumn<Appointment, String> createdAtColumn;

    private final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Set up the columns
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        queueNumberColumn.setCellValueFactory(new PropertyValueFactory<>("queueNumber"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        scheduleDayColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleDay"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

        // Load data from the database
        loadAppointmentsFromDatabase();
    }

    private void loadAppointmentsFromDatabase() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        String query = """
                SELECT\s
                   a.appointment_id,\s
                   a.queue_number,\s
                   a.status,\s
                   p.name AS patient_name,\s
                   u.name AS doctor_name\s ,
                  schedule.day As schedule_day\s ,
                a.created_at AS created_at
               FROM\s
                   appointments a
               JOIN\s
                   users p ON a.patient_id = p.user_id
               JOIN\s
                   schedules schedule ON a.schedule_id = schedule.schedule_id
               JOIN\s
        
                   doctors d ON a.doctor_id = d.doctor_id
               JOIN\s
                   users u ON d.user_id = u.user_id
                WHERE\s
                    d.user_id = ?;
                """;



        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameter for the patient ID
          preparedStatement.setInt(1, userData.id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int appointmentId = resultSet.getInt("appointment_id");
                    int queueNumber = resultSet.getInt("queue_number");
                    String status = resultSet.getString("status");
                    String patientName = resultSet.getString("patient_name");
                    String doctorName = resultSet.getString("doctor_name");
                    String scheduleDay = resultSet.getString("schedule_day");
                    String createdAt = resultSet.getString("created_at");

                    // Add to the appointment list
                    appointmentList.add(new Appointment(appointmentId, patientName, doctorName, queueNumber, status, scheduleDay, createdAt));
                }

                // Update the table view
                tableView.setItems(appointmentList);
            }

        } catch (SQLException e) {
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
