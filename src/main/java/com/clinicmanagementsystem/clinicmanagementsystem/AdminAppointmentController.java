

package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AdminAppointmentController {

    @FXML
    private RadioButton filterAll;
    @FXML
    private TextField txtSearchEmail;
    @FXML
    private Button btnSearchName;
    @FXML
    private RadioButton filterByPatient;
    @FXML
    private RadioButton filterByDoctor;
    @FXML
    private TableView<Appointment> appointmentsTable;
    @FXML
    private TableColumn<Appointment, String> colPatientName;
    @FXML
    private TableColumn<Appointment, String> colDoctorName;
    @FXML
    private TableColumn<Appointment, String> colSpecialty;
    @FXML
    private TableColumn<Appointment, LocalDateTime> colDate;
    @FXML
    private TableColumn<Appointment, Integer> colQueueNumber;
    @FXML
    private TableColumn<Appointment, String> colStatus;

    @FXML
    public void initialize() {

        colPatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        colSpecialty.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
        colQueueNumber.setCellValueFactory(new PropertyValueFactory<>("queueNumber"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colDate.setCellFactory(column -> new TableCell<>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(formatter.format(item));
                }
            }
        });
        System.out.println(3);

        loadAppointmentsData("");
    }

    private void loadAppointmentsData(String filterEmail) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT " +
                "  u.name AS patient_name, " +
                "  du.name AS doctor_name, " +
                "  spt.name AS specialty, " +
                "  s.day AS appointment_day, " +
                "  a.created_at AS appointment_date, " +
                "  a.queue_number, " +
                "  a.status " +
                "FROM appointments a " +
                "JOIN users u ON a.patient_id = u.user_id " +
                "JOIN doctors d ON a.doctor_id = d.doctor_id " +
                "JOIN users du ON d.user_id = du.user_id " +
                "JOIN schedules s ON a.schedule_id = s.schedule_id " +
                "JOIN specialties spt ON d.specialty_id = spt.specialty_id " +
                "WHERE u.email_address LIKE ? ";


        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, "%" + filterEmail + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                LocalDateTime appointmentDate = rs.getTimestamp("appointment_date").toLocalDateTime();

                Appointment appointment = new Appointment(
                        rs.getString("patient_name"),
                        rs.getString("doctor_name"),
                        rs.getString("specialty"),
                        appointmentDate,
                        rs.getInt("queue_number"),
                        rs.getString("status"),
                        rs.getString("appointment_day")
                );

                appointments.add(appointment);
            }

            appointmentsTable.setItems(FXCollections.observableArrayList(appointments));
        } catch (SQLException e) {
            e.printStackTrace();
            showError("Database Error", "Failed to load appointments: " + e.getMessage());
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void filterAppointments(ActionEvent event) {
        String email = txtSearchEmail.getText();
        loadAppointmentsData(email);
    }

    public void addAppointment(ActionEvent event) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            List<String> patientEmails = new ArrayList<>();
            String fetchPatientsQuery = "SELECT email_address FROM users WHERE role = 'Patient'";
            try (PreparedStatement stmt = connection.prepareStatement(fetchPatientsQuery);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patientEmails.add(rs.getString("email_address"));
                }
            }
            String selectedPatientEmail = showSelectionDialog("Select Patient Email", patientEmails);
            if (selectedPatientEmail == null) {
                return;
            }

            List<String> specialties = new ArrayList<>();
            String fetchSpecialtiesQuery = "SELECT name FROM specialties";
            try (PreparedStatement stmt = connection.prepareStatement(fetchSpecialtiesQuery);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    specialties.add(rs.getString("name"));
                }
            }

            String selectedSpecialty = showSelectionDialog("Select Specialty", specialties);
            if (selectedSpecialty == null) {
                return;
            }

            List<String> doctors = new ArrayList<>();
            String fetchDoctorsQuery = """
            SELECT u.name 
            FROM doctors d
            JOIN users u ON d.user_id = u.user_id
            JOIN specialties s ON d.specialty_id = s.specialty_id
            WHERE s.name = ?
        """;
            try (PreparedStatement stmt = connection.prepareStatement(fetchDoctorsQuery)) {
                stmt.setString(1, selectedSpecialty);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        doctors.add(rs.getString("name"));
                    }
                }
            }

            String selectedDoctor = showSelectionDialog("Select Doctor", doctors);
            if (selectedDoctor == null) {
                return;
            }

            List<String> availableDays = new ArrayList<>();
            String fetchDaysQuery = """
            SELECT s.day
            FROM schedules s
            JOIN doctors d ON s.doctor_id = d.doctor_id
            JOIN users u ON d.user_id = u.user_id
            WHERE u.name = ? AND s.remain_limit > 0
        """;
            try (PreparedStatement stmt = connection.prepareStatement(fetchDaysQuery)) {
                stmt.setString(1, selectedDoctor);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        availableDays.add(rs.getString("day"));
                    }
                }
            }

            String selectedDay = showSelectionDialog("Select Day", availableDays);
            if (selectedDay == null) {
                return;
            }

            String addAppointmentQuery = """
            INSERT INTO appointments (patient_id, doctor_id, schedule_id, queue_number, status)
            VALUES (?, ?, ?, ?, 'Pending')
        """;
            try (PreparedStatement stmt = connection.prepareStatement(addAppointmentQuery)) {
                int patientId = getPatientIdByEmail(selectedPatientEmail, connection);
                int doctorId = getDoctorIdByName(selectedDoctor, connection);
                int scheduleId = getScheduleIdByDayAndDoctor(selectedDay, doctorId, connection);

                stmt.setInt(1, patientId);
                stmt.setInt(2, doctorId);
                stmt.setInt(3, scheduleId);
                stmt.setInt(4, getNextQueueNumber(scheduleId, connection));

                stmt.executeUpdate();
                showAlert("Success", "Appointment successfully added.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to add appointment: " + e.getMessage());
        }
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void editAppointment(ActionEvent event) {

    }

    public void deleteAppointment(ActionEvent event) {

    }

    private int getNextQueueNumber(int scheduleId, Connection connection) throws SQLException {
        String query = """
        SELECT appointment_limit, remain_limit
        FROM schedules
        WHERE schedule_id = ?
    """;

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, scheduleId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int appointmentLimit = rs.getInt("appointment_limit");
                    int remainLimit = rs.getInt("remain_limit");
                    return appointmentLimit - remainLimit + 1;
                }
            }
        }

        throw new SQLException("Unable to calculate the next queue number.");
    }


    private int getPatientIdByEmail(String email, Connection connection) throws SQLException {
        String query = "SELECT user_id FROM users WHERE email_address = ? AND role = 'Patient'";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                }
            }
        }
        throw new SQLException("Patient not found for the given email.");
    }

    private int getScheduleIdByDayAndDoctor(String day, int doctorId, Connection connection) throws SQLException {
        String query = """
        SELECT schedule_id 
        FROM schedules 
        WHERE doctor_id = ? AND day = ? AND remain_limit > 0
    """;
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            stmt.setString(2, day);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("schedule_id");
                }
            }
        }
        throw new SQLException("No available schedule found for the selected day and doctor.");
    }


    private String showSelectionDialog(String title, List<String> options) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(options.get(0), options);
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText("Choose an option:");
        return dialog.showAndWait().orElse(null);
    }

    private int getDoctorIdByName(String doctorName, Connection connection) throws SQLException {
        String query = "SELECT doctor_id FROM doctors WHERE name = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, doctorName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("doctor_id");
                }
            }
        }
        throw new SQLException("Doctor not found.");
    }

    private int getScheduleIdByDate(String date, Connection connection) throws SQLException {
        String query = "SELECT schedule_id FROM schedules WHERE date = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, date);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("schedule_id");
                }
            }
        }
        throw new SQLException("Schedule not found.");
    }

    public void showspecialtyCrud(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(AdminDashboardController.class.getResource("FXML/SpecialtyPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 690, 600);
        SpecialtyPageController controller = fxmlLoader.getController();
        controller.initialize();
        stage.setTitle("Clinic Management System");
        stage.setScene(scene);
        stage.show();
    }

    public void showDoctorCrud(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(AdminDashboardController.class.getResource("FXML/CRUD-Doctors-Admin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 690, 600);
        CRUDDoctorsAdminController controller = fxmlLoader.getController();
        controller.initialize();
        stage.setTitle("Clinic Management System");
        stage.setScene(scene);
        stage.show();
    }

    public void showSchedulesCrud(ActionEvent event) {
    }

    public void showAppointmentCrud(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(AdminDashboardController.class.getResource("FXML/Appointment-Admin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 690, 600);
        AdminAppointmentController controller = fxmlLoader.getController();
        stage.setTitle("Clinic Management System");
        stage.setScene(scene);
        stage.show();
    }

    public void logout(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(AdminDashboardController.class.getResource("FXML/LoginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 690, 600);
        LoginPageController controller = fxmlLoader.getController();
        stage.setTitle("Clinic Management System");
        stage.setScene(scene);
        stage.show();
    }
}
