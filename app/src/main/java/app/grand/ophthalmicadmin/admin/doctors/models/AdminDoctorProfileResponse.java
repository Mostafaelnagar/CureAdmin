package app.grand.ophthalmicadmin.admin.doctors.models;

import com.google.gson.annotations.SerializedName;

public class AdminDoctorProfileResponse {
    @SerializedName("degree")
    private String degree;
    @SerializedName("department_id")
    private String department_id;
    @SerializedName("department")
    private String department;
    @SerializedName("patient_number")
    private String patient_number;
    @SerializedName("working_days")
    private String working_days;

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(String department_id) {
        this.department_id = department_id;
    }

    public String getPatient_number() {
        return patient_number;
    }

    public void setPatient_number(String patient_number) {
        this.patient_number = patient_number;
    }

    public String getWorking_days() {
        return working_days;
    }

    public void setWorking_days(String working_days) {
        this.working_days = working_days;
    }
}
