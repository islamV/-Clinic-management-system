package com.clinicmanagementsystem.clinicmanagementsystem;

import java.util.List;

public class Doctor {
    private int id;
    private String name;
    private int age;
    private String speciality;
    private String gender;
    private String phoneNumber;
    private String email;

    public Doctor(int id, String name, int age, String speciality, String gender, String phoneNumber, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.speciality = speciality;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getSpeciality() {
        return speciality;
    }
    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}

