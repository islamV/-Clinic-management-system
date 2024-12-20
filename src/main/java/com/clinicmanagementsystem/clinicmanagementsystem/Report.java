package com.clinicmanagementsystem.clinicmanagementsystem;

import java.sql.Timestamp;

public class Report {
    private int reportId;
    private int appointmentId;
    private int doctorId;
    private String doctorName;
    private String reportContent;
    private Timestamp createdAt;

    // Constructor
    public Report(int reportId, int appointmentId, int doctorId, String doctorName, String reportContent, Timestamp createdAt) {
        this.reportId = reportId;
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.reportContent = reportContent;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getReportContent() {
        return reportContent;
    }

    public void setReportContent(String reportContent) {
        this.reportContent = reportContent;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
