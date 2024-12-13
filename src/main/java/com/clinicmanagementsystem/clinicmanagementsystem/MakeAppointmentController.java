package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    private int doctorId;
    private int specialtyId;
    private int schedulId;
    private ObservableList<String> Doctors;
    private ObservableList<String> specialties;
    private ObservableList<String> schedules;
    public void initialize() {
        loadSpecialties();
        loadDoctors();
        loadschedules();
    }
    public void doctorButton(){
        String[] value = doctorsComboBox.getValue().split(" ");
        doctorId = Integer.parseInt(value[1]);
    }
    public void specialtyButton(){
        String[] value = specialtyComboBox.getValue().split(" ");
        specialtyId = Integer.parseInt(value[1]);
    }
    public void schedulButton(){
        schedulId = Integer.parseInt(schedulesComboBox.getValue());
    }

    private void loadSpecialties() {
        specialties = FXCollections.observableArrayList();
        String query = "SELECT * FROM specification";
        try{
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement stat = con.prepareStatement(query);
             ResultSet re = stat.executeQuery();
            while (re.next()) {
                specialties.add(re.getInt("id")+" "+re.getString("name"));
            }
            specialtyComboBox.setItems(specialties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDoctors() {
        Doctors = FXCollections.observableArrayList();
        String query = "SELECT * FROM doctors where specilty_id = ?";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement stat = con.prepareStatement(query);
            if(specialtyId!=-1){
                stat.setInt(1,specialtyId);
                ResultSet re = stat.executeQuery();
                while (re.next()) {
                    Doctors.add(re.getInt("doctor_id")+" "+re.getString("name"));
                }
                doctorsComboBox.setItems(Doctors);
            }else{
                Alert alert =new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please select specailty first.");
                alert.show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadschedules() {
        schedules = FXCollections.observableArrayList();
        String query = null;
        try {
            Connection con = DatabaseConnection.getConnection();
            query ="SELECT * FROM schedules where doctor_id = ?";
            PreparedStatement stat = con.prepareStatement(query);
            if(doctorId!=-1){
                stat.setInt(1,doctorId);
                ResultSet re = stat.executeQuery();
                while (re.next()) {
                    schedules.add(re.getString("schedule_id"));
                }
                schedulesComboBox.setItems(schedules);
            }else{
                Alert alert =new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please select doctor first.");
                alert.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void submitButton(){
        String query = "insert into appointment" +
                "(appointment_id, patient_id, doctor_id, schedule_id, queue_number, status)"+
                "values (?,?,?,?,?,?)";
        try{
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement stat = con.prepareStatement(query);
            if(specialtyId!=-1&&doctorId!=-1&&schedulId!=-1){
                int x = (int) Math.random()*10000;
                stat.setInt(1,x);
                stat.setInt(2, Data.id);
                stat.setInt(3, doctorId);
                stat.setInt(4,schedulId);
                stat.setInt(5, x);
                stat.setString(6,"Pending");
                int re = stat.executeUpdate();
                if(re>0){
                    Alert alert =new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Make Appointment successfully. Your Queue Number is: "+x);
                    alert.show();
                }else {
                    Alert alert =new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("can't Make Appointment successfully.");
                    alert.show();
                }
            }else{
                Alert alert =new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Please select fields before submit.");
                alert.show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
