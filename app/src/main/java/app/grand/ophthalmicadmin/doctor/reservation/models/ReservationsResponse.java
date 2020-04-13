package app.grand.ophthalmicadmin.doctor.reservation.models;

import com.google.gson.annotations.SerializedName;

import app.grand.ophthalmicadmin.auth.model.UserData;


public class ReservationsResponse {
    @SerializedName("day")
    private String day;
    @SerializedName("doctor")
    private UserData doctor;
    @SerializedName("note")
    private String note;
    @SerializedName("status")
    private String status;
    @SerializedName("patient")
    private UserData patient;
    @SerializedName("time")
    private String time;
    @SerializedName("doctor_id")
    private String doc_id;
    @SerializedName("medicine")
    private String medicine;
    @SerializedName("date")
    private String date;
    private int count;
    @SerializedName("user_id")
    private String user_id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public UserData getDoctor() {
        return doctor;
    }

    public void setDoctor(UserData doctor) {
        this.doctor = doctor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserData getPatient() {
        return patient;
    }

    public void setPatient(UserData patient) {
        this.patient = patient;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
