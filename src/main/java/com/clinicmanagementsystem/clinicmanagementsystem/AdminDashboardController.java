package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AdminDashboardController {

    public Button btnManageAppointments;
    public Button btnManageDoctors;
    public Button btnLogout;
    public Label lblAdminUsername;

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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to signout?");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get().equals(ButtonType.OK)) {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("FXML/LoginPage.fxml"));
                Parent root = loader.load();
                Stage currentStage = (Stage) lblAdminUsername.getScene().getWindow();
                currentStage.setScene(new Scene(root));
                currentStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
