package com.gas.swiftel.Model;

import com.google.firebase.firestore.FieldValue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Notifications {

    private String  Name, User_ID, type, Order_iD;
    private Date timestamp;

    public Notifications() {
    }

    public Notifications(String name, String user_ID, String type, String order_iD, Date timestamp) {
        Name = name;
        User_ID = user_ID;
        this.type = type;
        Order_iD = order_iD;
        this.timestamp = timestamp;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOrder_iD() {
        return Order_iD;
    }

    public void setOrder_iD(String order_iD) {
        Order_iD = order_iD;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
