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
import java.sql.SQLException;

public class SchedulesController {

    @FXML
    private TableView<Schedule> tableView;

    @FXML
    private TableColumn<Schedule, Integer> scheduleIdColumn;

    @FXML
    private TableColumn<Schedule, String> dayColumn;

    private ObservableList<Schedule> scheduleList;

    @FXML
    public void initialize() {
        // Initialize columns
        scheduleIdColumn.setCellValueFactory(new PropertyValueFactory<>("scheduleId"));
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));

        // Load data into the table
        loadSchedulesFromDatabase();
    }

    private void loadSchedulesFromDatabase() {
        scheduleList = FXCollections.observableArrayList();
        DatabaseConnection databaseConnection = new DatabaseConnection();

        String query = "SELECT s.schedule_id, s.day " +
                "FROM schedules s " +
                "JOIN doctors d ON s.doctor_id = d.doctor_id " +
                "JOIN users u ON d.user_id = u.user_id " +
                "WHERE d.user_id = ?;";

        try (Connection connection = databaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Bind the parameter before executing the query
            preparedStatement.setInt(1, userData.id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int scheduleId = resultSet.getInt("schedule_id");
                    String day = resultSet.getString("day");
                    scheduleList.add(new Schedule(scheduleId, day));
                }

                // Set the items for the TableView
                tableView.setItems(scheduleList);
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
