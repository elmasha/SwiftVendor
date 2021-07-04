package com.gas.swiftel.Model;


import java.util.Date;

public class Vendor_Arround {

    private String Vendor_name,Vendor_image, Client_ID, Shop_Name, Shop_No, Vendor_ID;
    private Date timestamp;
    private double lat,lng;
    private String Activation_fee;

    public Vendor_Arround() {
    }


    public Vendor_Arround(String vendor_name, String vendor_image, String client_ID, String shop_Name, String shop_No,
                          String vendor_ID, Date timestamp, double lat, double lng, String activation_fee) {
        Vendor_name = vendor_name;
        Vendor_image = vendor_image;
        Client_ID = client_ID;
        Shop_Name = shop_Name;
        Shop_No = shop_No;
        Vendor_ID = vendor_ID;
        this.timestamp = timestamp;
        this.lat = lat;
        this.lng = lng;
        Activation_fee = activation_fee;
    }

    public String getVendor_name() {
        return Vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        Vendor_name = vendor_name;
    }

    public String getVendor_image() {
        return Vendor_image;
    }

    public void setVendor_image(String vendor_image) {
        Vendor_image = vendor_image;
    }

    public String getClient_ID() {
        return Client_ID;
    }

    public void setClient_ID(String client_ID) {
        Client_ID = client_ID;
    }

    public String getShop_Name() {
        return Shop_Name;
    }

    public void setShop_Name(String shop_Name) {
        Shop_Name = shop_Name;
    }

    public String getShop_No() {
        return Shop_No;
    }

    public void setShop_No(String shop_No) {
        Shop_No = shop_No;
    }

    public String getVendor_ID() {
        return Vendor_ID;
    }

    public void setVendor_ID(String vendor_ID) {
        Vendor_ID = vendor_ID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getActivation_fee() {
        return Activation_fee;
    }

    public void setActivation_fee(String activation_fee) {
        Activation_fee = activation_fee;
    }


}
