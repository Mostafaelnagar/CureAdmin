package app.grand.ophthalmicadmin.admin.doctors.models;

import app.grand.ophthalmicadmin.auth.model.UserData;

public class AddDoctorRequest extends UserData {
    private String degree;
    private String patientNumber;
    private String department;
    private String password;

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
