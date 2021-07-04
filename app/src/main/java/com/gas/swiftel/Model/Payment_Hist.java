package com.gas.swiftel.Model;


import java.util.Date;

public class Payment_Hist {

   private String Name,Amount,PhoneNo,Type,User_ID,User_name,Payment_ID;
   private long Trips;
   private Date timestamp;

    public Payment_Hist() {
    }

    public Payment_Hist(String name, String amount, String phoneNo, String type, String user_ID,
                        String user_name, String payment_ID, long trips, Date timestamp) {
        Name = name;
        Amount = amount;
        PhoneNo = phoneNo;
        Type = type;
        User_ID = user_ID;
        User_name = user_name;
        Payment_ID = payment_ID;
        Trips = trips;
        this.timestamp = timestamp;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getUser_name() {
        return User_name;
    }

    public void setUser_name(String user_name) {
        User_name = user_name;
    }

    public String getPayment_ID() {
        return Payment_ID;
    }

    public void setPayment_ID(String payment_ID) {
        Payment_ID = payment_ID;
    }

    public long getTrips() {
        return Trips;
    }

    public void setTrips(long trips) {
        Trips = trips;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
