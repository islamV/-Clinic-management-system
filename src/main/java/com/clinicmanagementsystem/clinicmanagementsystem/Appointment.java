package com.clinicmanagementsystem.clinicmanagementsystem;

import javafx.beans.property.*;

public class Appointment {
    private final IntegerProperty appointmentId;
    private final IntegerProperty queueNumber;
    private final StringProperty status;
    private final StringProperty patientName;
    private final StringProperty doctorName;
    private final StringProperty scheduleDay;
    private final StringProperty specialty;
    private final StringProperty day;

    // Primary constructor
    public Appointment(String patientName, String doctorName, String specialty,
                       int queueNumber, String status, String day) {
        this.appointmentId = new SimpleIntegerProperty(0);
        this.patientName = new SimpleStringProperty(patientName);
        this.doctorName = new SimpleStringProperty(doctorName);
        this.specialty = new SimpleStringProperty(specialty);
        this.queueNumber = new SimpleIntegerProperty(queueNumber);
        this.status = new SimpleStringProperty(status);
        this.day = new SimpleStringProperty(day);
        this.scheduleDay = new SimpleStringProperty("");
    }

    // Secondary constructor for minimal appointment creation
    public Appointment(int appointmentId, String patientName, int queueNumber) {
        this.appointmentId = new SimpleIntegerProperty(appointmentId);
        this.patientName = new SimpleStringProperty(patientName);
        this.doctorName = new SimpleStringProperty("");
        this.specialty = new SimpleStringProperty("");
        this.queueNumber = new SimpleIntegerProperty(queueNumber);
        this.status = new SimpleStringProperty("");
        this.day = new SimpleStringProperty("");
        this.scheduleDay = new SimpleStringProperty("");
    }

    // Getters and setters for all properties
    public IntegerProperty appointmentIdProperty() {
        return appointmentId;
    }

    public int getAppointmentId() {
        return appointmentId.get();
    }

    public void setAppointmentId(int value) {
        appointmentId.set(value);
    }

    public IntegerProperty queueNumberProperty() {
        return queueNumber;
    }

    public int getQueueNumber() {
        return queueNumber.get();
    }

    public void setQueueNumber(int value) {
        queueNumber.set(value);
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getStatus() {
        return status.get();
    }

    public void setStatus(String value) {
        status.set(value);
    }

    public StringProperty patientNameProperty() {
        return patientName;
    }

    public String getPatientName() {
        return patientName.get();
    }

    public void setPatientName(String value) {
        patientName.set(value);
    }

    public StringProperty doctorNameProperty() {
        return doctorName;
    }

    public String getDoctorName() {
        return doctorName.get();
    }

    public void setDoctorName(String value) {
        doctorName.set(value);
    }

    public StringProperty scheduleDayProperty() {
        return scheduleDay;
    }

    public String getScheduleDay() {
        return scheduleDay.get();
    }

    public void setScheduleDay(String value) {
        scheduleDay.set(value);
    }

    public StringProperty specialtyProperty() {
        return specialty;
    }

    public String getSpecialty() {
        return specialty.get();
    }

    public void setSpecialty(String value) {
        specialty.set(value);
    }

    public StringProperty dayProperty() {
        return day;
    }

    public String getDay() {
        return day.get();
    }

    public void setDay(String value) {
        day.set(value);
    }

    @Override
    public String toString() {
        return String.format("Appointment{patientName=%s, doctorName=%s, queueNumber=%d}",
                getPatientName(), getDoctorName(), getQueueNumber());
    }
}
