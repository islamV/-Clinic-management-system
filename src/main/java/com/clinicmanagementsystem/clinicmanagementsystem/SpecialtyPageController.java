package com.clinicmanagementsystem.clinicmanagementsystem;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Eng. Mohamed
 */
public class SpecialtyPageController {

    @FXML
    private TextField specialty_txtfld;

    @FXML
    private Button add_specialty_btn;

    @FXML
    private Button remove_specialty_btn;

    @FXML
    private TableView<String> specialty_table;

    @FXML
    private TableColumn<String, String> specialty_column;

    private Connection con;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private ObservableList<String> specialties;

    public void addSpecialty() {
        String specialty = specialty_txtfld.getText().trim();

        Alert alert;
        if (specialty.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Specialty name cannot be empty.");
            alert.showAndWait();
            return;
        }

        try {
            String sql = "INSERT INTO specialties (name) VALUES (?)";
            con = DatabaseConnection.getConnection();
            prepare = con.prepareStatement(sql);
            prepare.setString(1, specialty);
            prepare.executeUpdate();

            specialties.add(specialty);
            specialty_txtfld.clear();
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Specialty added successfully.");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to add specialty. It might already exist.");
            alert.showAndWait();
        }
    }

    public void removeSpecialty() {
        String selectedSpecialty = specialty_table.getSelectionModel().getSelectedItem();

        if (selectedSpecialty == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a specialty to remove.");
            alert.showAndWait();
            return;
        }


        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation Message");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to remove the specialty: " + selectedSpecialty + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    String sql = "DELETE FROM specialties WHERE name = ?";
                    con = DatabaseConnection.getConnection();
                    prepare = con.prepareStatement(sql);
                    prepare.setString(1, selectedSpecialty);
                    prepare.executeUpdate();

                    specialties.remove(selectedSpecialty);

                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success Message");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Specialty removed successfully.");
                    successAlert.showAndWait();
                } catch (SQLException e) {
                    e.printStackTrace();

                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to remove specialty.");
                    errorAlert.showAndWait();
                }
            } else {
                System.out.println("Specialty removal canceled.");
            }
        });
    }



    public void loadSpecialtiesFromDatabase() {
        try {
            specialties.clear();
            String sql = "SELECT name FROM specialties";
            con = DatabaseConnection.getConnection();
            statement = con.createStatement();
            result = statement.executeQuery(sql);

            while (result.next()) {
                specialties.add(result.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        specialties = FXCollections.observableArrayList();
        specialty_column.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue()));
        specialty_table.setItems(specialties);

        try {
            loadSpecialtiesFromDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
