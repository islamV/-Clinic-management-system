package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Appointment {
    private  IntegerProperty appointmentId;
    private  IntegerProperty queueNumber;
    private  StringProperty status;
    private  StringProperty patientName;
    private  StringProperty doctorName;
    private  StringProperty scheduleDay ;
    private  StringProperty createdAt ;

    public Appointment(int appointmentId, int queueNumber, String status, String patientName, String doctorName , String scheduleDay , String createdAt) {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.queueNumber = new SimpleIntegerProperty(queueNumber);
        this.status = new SimpleStringProperty(status);
        this.patientName = new SimpleStringProperty(patientName);
        this.doctorName = new SimpleStringProperty(doctorName);
        this.scheduleDay = new SimpleStringProperty(scheduleDay);
        this.createdAt = new SimpleStringProperty(createdAt);

    }
    public Appointment(int appointmentId, String patientName) {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.queueNumber = new SimpleIntegerProperty(0); // Default value
        this.status = new SimpleStringProperty("0"); // Default value
        this.patientName = new SimpleStringProperty(patientName);
        this.doctorName = new SimpleStringProperty("Unknown"); // Default value
        this.scheduleDay = new SimpleStringProperty("Unknown"); // Default value
        this.createdAt = new SimpleStringProperty("Unknown"); // Default value
    }

    public Appointment(int appointmentId, String patientName, Object o) {

    }

    public Appointment(String patientName, String doctorName, String specialty, LocalDateTime appointmentDate, int queueNumber, String status, String appointmentDay) {
    }


    public int getAppointmentId() {
        return appointmentId.get();
    }

    public IntegerProperty appointmentIdProperty() {
        return appointmentId;
    }

    public int getQueueNumber() {
        return queueNumber.get();
    }

    public IntegerProperty queueNumberProperty() {
        return queueNumber;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getScheduleDay() {
        return scheduleDay.get();
    }

    public StringProperty scheduleDayProperty() {
        return scheduleDay;
    }


    public String getPatientName() {
        return patientName.get();
    }

    public StringProperty patientNameProperty() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName.get();
    }

    public StringProperty createdAtProperty() {
        return createdAt;
    }
    public String getCreatedAt() {
        return createdAt.get();
    }



}
