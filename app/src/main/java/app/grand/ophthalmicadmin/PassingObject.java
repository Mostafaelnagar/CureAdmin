package app.grand.ophthalmicadmin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import app.grand.ophthalmicadmin.auth.model.UserData;
import app.grand.ophthalmicadmin.doctor.reservation.models.ReservationsResponse;


public class PassingObject implements Serializable {
    private UserData objectClass;
    private String object;
    private ReservationsResponse reservationsResponse;
    private int code;

    public PassingObject() {
    }

    public PassingObject(int code) {
        this.code = code;
    }

    public PassingObject(String object, int code) {
        this.object = object;
        this.code = code;
    }

    public PassingObject(ReservationsResponse reservationsResponse) {
        this.reservationsResponse = reservationsResponse;
    }

    public PassingObject(String object) {
        this.object = object;
    }

    public PassingObject(UserData objectClass, String object) {
        this.objectClass = objectClass;
        this.object = object;
    }

    public PassingObject(UserData objectClass) {
        this.objectClass = objectClass;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public UserData getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(UserData objectClass) {
        this.objectClass = objectClass;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public ReservationsResponse getReservationsResponse() {
        return reservationsResponse;
    }

    public void setReservationsResponse(ReservationsResponse reservationsResponse) {
        this.reservationsResponse = reservationsResponse;
    }
}