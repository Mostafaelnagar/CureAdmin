package app.grand.ophthalmicadmin.admin.adminSepcialist.models;

import app.grand.ophthalmicadmin.auth.model.UserData;

public class AddSpecialistRequest extends UserData {
    private String labName;
    private String labNumber;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getLabNumber() {
        return labNumber;
    }

    public void setLabNumber(String labNumber) {
        this.labNumber = labNumber;
    }
}
