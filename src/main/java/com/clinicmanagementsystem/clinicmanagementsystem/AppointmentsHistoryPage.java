package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppointmentsHistoryPage {
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
    private TableColumn<Appointment, LocalDateTime> appointmentDateColumn;

    private final ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @FXML
    private void initialize() {
        // Set up the columns
        appointmentIdColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        queueNumberColumn.setCellValueFactory(new PropertyValueFactory<>("queueNumber"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        patientNameColumn.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        doctorNameColumn.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        scheduleDayColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleDay"));
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        // Configure date column formatting
        appointmentDateColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(DATE_FORMATTER.format(item));
                }
            }
        });

        // Load data from the database
        loadAppointmentsFromDatabase();
    }

    private void loadAppointmentsFromDatabase() {
        String query = """
            SELECT
                a.appointment_id,
                a.queue_number,
                a.status,
                p.name AS patient_name,
                u.name AS doctor_name,
                s.day AS schedule_day,
                a.created_at AS appointment_date,
                spec.name AS specialty
            FROM
                appointments a
            JOIN
                users p ON a.patient_id = p.user_id
            JOIN
                schedules s ON a.schedule_id = s.schedule_id
            JOIN
                doctors d ON a.doctor_id = d.doctor_id
            JOIN
                users u ON d.user_id = u.user_id
            JOIN
                specialties spec ON d.specialty_id = spec.specialty_id
            ORDER BY
                a.created_at DESC
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            appointmentList.clear();

            while (rs.next()) {
                LocalDateTime appointmentDate = rs.getTimestamp("appointment_date").toLocalDateTime();

                Appointment appointment = new Appointment(
                        rs.getString("patient_name"),
                        rs.getString("doctor_name"),
                        rs.getString("specialty"),
                        appointmentDate,
                        rs.getInt("queue_number"),
                        rs.getString("status"),
                        rs.getString("schedule_day")
                );
                appointment.setAppointmentId(rs.getInt("appointment_id"));

                appointmentList.add(appointment);
            }

            tableView.setItems(appointmentList);

        } catch (SQLException e) {
            showError("Database Error", "Failed to load appointments: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/patient-home-page.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showError("Navigation Error", "Failed to load patient home page: " + e.getMessage());
            e.printStackTrace();
        }
    }
}