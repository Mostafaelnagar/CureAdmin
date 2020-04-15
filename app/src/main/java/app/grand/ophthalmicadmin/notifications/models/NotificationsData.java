package app.grand.ophthalmicadmin.notifications.models;

import com.google.gson.annotations.SerializedName;

public class NotificationsData {

    @SerializedName("title")
    private String title;

    @SerializedName("body")
    private String body;

    @SerializedName("reserve_id")
    private String reserve_id;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("to")
    private String to;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getReserve_id() {
        return reserve_id;
    }

    public void setReserve_id(String reserve_id) {
        this.reserve_id = reserve_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}