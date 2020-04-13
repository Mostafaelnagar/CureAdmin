package app.grand.ophthalmicadmin.doctor.models;

import com.google.gson.annotations.SerializedName;

public class DiagnosisRequest {
    @SerializedName("note")
    private String note;
    @SerializedName("medicine")
    private String medicine;
    @SerializedName("rays")
    private String rays;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public String getRays() {
        return rays;
    }

    public void setRays(String rays) {
        this.rays = rays;
    }
}
