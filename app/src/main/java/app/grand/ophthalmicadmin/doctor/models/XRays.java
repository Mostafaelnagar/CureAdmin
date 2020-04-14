package app.grand.ophthalmicadmin.doctor.models;

import com.google.gson.annotations.SerializedName;

import app.grand.ophthalmicadmin.auth.model.UserData;

public class XRays extends UserData {
    @SerializedName("patient_id")
    private String patient_id;
    @SerializedName("x_ray_image")
    private String x_ray_image;
    @SerializedName("x_ray_name")
    private String x_ray_name;

    public String getPatient_id() {
        return patient_id;
    }

    public void setPatient_id(String patient_id) {
        this.patient_id = patient_id;
    }

    public String getX_ray_image() {
        return x_ray_image;
    }

    public void setX_ray_image(String x_ray_image) {
        this.x_ray_image = x_ray_image;
    }

    public String getX_ray_name() {
        return x_ray_name;
    }

    public void setX_ray_name(String x_ray_name) {
        this.x_ray_name = x_ray_name;
    }
}
