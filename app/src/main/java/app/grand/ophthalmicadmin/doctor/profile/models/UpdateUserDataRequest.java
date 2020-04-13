package app.grand.ophthalmicadmin.doctor.profile.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateUserDataRequest {
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("phone")
    @Expose
    private String phone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
