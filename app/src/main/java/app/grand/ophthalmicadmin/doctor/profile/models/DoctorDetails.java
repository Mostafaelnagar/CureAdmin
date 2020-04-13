package app.grand.ophthalmicadmin.doctor.profile.models;

import com.google.gson.annotations.SerializedName;

public class DoctorDetails {
    @SerializedName("master")
    private String master;
    @SerializedName("patient_number")
    private String patient_number;

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getPatient_number() {
        return patient_number;
    }

    public void setPatient_number(String patient_number) {
        this.patient_number = patient_number;
    }
}
