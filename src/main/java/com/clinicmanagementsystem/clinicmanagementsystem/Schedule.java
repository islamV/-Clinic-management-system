package com.clinicmanagementsystem.clinicmanagementsystem;

public class Schedule {
    private int scheduleId;
    private String day;

    public Schedule(int scheduleId, String day) {
        this.scheduleId = scheduleId;
        this.day = day;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
