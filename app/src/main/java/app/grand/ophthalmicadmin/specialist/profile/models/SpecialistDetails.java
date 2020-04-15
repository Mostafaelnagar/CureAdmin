package app.grand.ophthalmicadmin.specialist.profile.models;

import com.google.gson.annotations.SerializedName;

public class SpecialistDetails {
    @SerializedName("lab_name")
    private String lab_name;
    @SerializedName("lab_number")
    private String lab_number;

    public String getLab_name() {
        return lab_name;
    }

    public void setLab_name(String lab_name) {
        this.lab_name = lab_name;
    }

    public String getLab_number() {
        return lab_number;
    }

    public void setLab_number(String lab_number) {
        this.lab_number = lab_number;
    }
}
