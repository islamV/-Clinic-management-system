package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class MakeAppointmentController {
    @FXML
    private Button doctorSaveButton;

    @FXML
    private ComboBox<String> doctorsComboBox;

    @FXML
    private Button schedulSaveButton;

    @FXML
    private ComboBox<String> schedulesComboBox;

    @FXML
    private ComboBox<String> specialtyComboBox;

    @FXML
    private Button specialtySaveButton;

    @FXML
    private Button submitAppointment;
    @FXML
    private void handleBackButton(ActionEvent event)throws IOException {
        FXMLLoader patientLoader = new FXMLLoader(getClass().getResource("FXML/patient-home-page.fxml"));
        Parent patientRoot = patientLoader.load();
        Stage patientStage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Get the stage from the event source
        patientStage.setScene(new Scene(patientRoot));
        patientStage.show();
    }
    private int doctorId = -1;
    private int specialtyId = -1;
    private int schedulId = -1;
    private ObservableList<String> Doctors;
    private ObservableList<String> specialties;
    private ObservableList<String> schedules;

    public void initialize() {
        loadSpecialties();
    }

    public void doctorButton() {
        if (doctorsComboBox.getValue() != null) {
            String[] value = doctorsComboBox.getValue().split(" ");
            doctorId = Integer.parseInt(value[0]);
            loadSchedules();
        } else {
            showAlert("Please select a doctor.");
        }
    }

    public void specialtyButton() {
        if (specialtyComboBox.getValue() != null) {
            String[] value = specialtyComboBox.getValue().split(" ");
            specialtyId = Integer.parseInt(value[0]);
            loadDoctors();
        } else {
            showAlert("Please select a specialty.");
        }
    }

    public void schedulButton() {
        if (schedulesComboBox.getValue() != null) {
            schedulId = Integer.parseInt(schedulesComboBox.getValue());
        } else {
            showAlert("Please select a schedule.");
        }
    }

    private void loadSpecialties() {
        specialties = FXCollections.observableArrayList();
        String query = "SELECT specialty_id, name FROM specification";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stat = con.prepareStatement(query);
             ResultSet re = stat.executeQuery()) {

            while (re.next()) {
                specialties.add(re.getInt("specialty_id") + " " + re.getString("name"));
            }
            specialtyComboBox.setItems(specialties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDoctors() {
        if (specialtyId == -1) {
            showAlert("Please select a specialty first.");
            return;
        }
        Doctors = FXCollections.observableArrayList();
        String query = "SELECT doctor_id FROM doctors WHERE specialty_id = ?";
        String qToFetchDoctorName = "SELECT name FROM users WHERE user_id = (SELECT user_id FROM doctors WHERE doctor_id = ?)";
        String limitQuery = "SELECT appointment_limit FROM doctors WHERE doctor_id = ?";
        try (Connection con = DatabaseConnection.getConnection()) {
            try (PreparedStatement stat = con.prepareStatement(query)) {
                stat.setInt(1, specialtyId);
                try (ResultSet re = stat.executeQuery()) {
                    while (re.next()) {
                        int docId = re.getInt("doctor_id");
                        try (PreparedStatement limitStat = con.prepareStatement(limitQuery)) {
                            limitStat.setInt(1, docId);
                            try (ResultSet limitRe = limitStat.executeQuery()) {
                                if (limitRe.next()) {
                                    int appointmentLimit = limitRe.getInt("appointment_limit");
                                    if (appointmentLimit <= 0) {
                                        continue;
                                    }
                                }
                            }
                        }
                        try (PreparedStatement nameStat = con.prepareStatement(qToFetchDoctorName)) {
                            nameStat.setInt(1, docId);
                            try (ResultSet re2 = nameStat.executeQuery()) {
                                if (re2.next()) {
                                    String docName = re2.getString("name");
                                    Doctors.add(docId + " " + docName);
                                }
                            }
                        }
                    }
                }
            }
            doctorsComboBox.setItems(Doctors);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error loading doctors: " + e.getMessage());
        }
    }


    private void loadSchedules() {
        if (doctorId == -1) {
            showAlert("Please select a doctor first.");
            return;
        }

        schedules = FXCollections.observableArrayList();
        String query = "SELECT schedule_id FROM schedules WHERE doctor_id = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stat = con.prepareStatement(query)) {

            stat.setInt(1, doctorId);
            try (ResultSet re = stat.executeQuery()) {
                while (re.next()) {
                    schedules.add(re.getString("schedule_id"));
                }
            }
            schedulesComboBox.setItems(schedules);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submitButton() {
        if (specialtyId == -1 || doctorId == -1 || schedulId == -1) {
            showAlert("Please select all fields before submitting.");
            return;
        }

        String query = "INSERT INTO appointments (patient_id, doctor_id, schedule_id, queue_number, status) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stat = con.prepareStatement(query)) {
            int num= (int)Math.random()*100;
            stat.setInt(1, userData.id); // Example patient_id; replace with actual logic.
            stat.setInt(2, doctorId);
            stat.setInt(3, schedulId);
            stat.setInt(4, num);
            stat.setString(5, "Pending");

            int result = stat.executeUpdate();

            if (result > 0) {
                showAlert("Appointment made successfully. Your Queue Number is: " + num);
                String limitQuery = "SELECT appointment_limit FROM doctors WHERE doctor_id = ?";
                try (PreparedStatement limitStat = con.prepareStatement(limitQuery)) {
                    limitStat.setInt(1, doctorId);
                    try (ResultSet limitRe = limitStat.executeQuery()) {
                        if (limitRe.next()) {
                            int appointmentLimit = limitRe.getInt("appointment_limit");
                            appointmentLimit--;
                            String updateLimit = "update doctors set appointment_limit = ? where doctor_id = ?";
                            try (PreparedStatement updateStat = con.prepareStatement(updateLimit)) {
                                updateStat.setInt(1, appointmentLimit);
                                updateStat.setInt(2, doctorId);
                                ResultSet updateRe = limitStat.executeQuery();
                            }
                        }
                    }
                }
            } else {
                showAlert("Failed to make an appointment.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.show();
    }
}
