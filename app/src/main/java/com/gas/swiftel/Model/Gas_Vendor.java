package com.gas.swiftel.Model;

public class Gas_Vendor {

    private String first_name,last_name,Location, mobile, Email, ShopName, ID_no, Shop_No,User_ID,User_Image,device_token,Activation_fee;
    private int Shop_status;
    private long Trips,Earnings,Cash_Trips;
    private double Rating;
    private double lat,lng;
    private double Avg_Rate;


    public Gas_Vendor() {
        //empty
    }


    public Gas_Vendor(String first_name, String last_name,
                      String location, String mobile, String email,
                      String shopName, String ID_no, String shop_No,
                      String user_ID, String user_Image,
                      String device_token, String  activation_fee, int shop_status,
                      long trips, long earnings, long cash_Trips, double rating, double lat, double lng, double avg_Rate) {
        this.first_name = first_name;
        this.last_name = last_name;
        Location = location;
        this.mobile = mobile;
        Email = email;
        ShopName = shopName;
        this.ID_no = ID_no;
        Shop_No = shop_No;
        User_ID = user_ID;
        User_Image = user_Image;
        this.device_token = device_token;
        Activation_fee = activation_fee;
        Shop_status = shop_status;
        Trips = trips;
        Earnings = earnings;
        Cash_Trips = cash_Trips;
        Rating = rating;
        this.lat = lat;
        this.lng = lng;
        Avg_Rate = avg_Rate;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getID_no() {
        return ID_no;
    }

    public void setID_no(String ID_no) {
        this.ID_no = ID_no;
    }

    public String getShop_No() {
        return Shop_No;
    }

    public void setShop_No(String shop_No) {
        Shop_No = shop_No;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getUser_Image() {
        return User_Image;
    }

    public void setUser_Image(String user_Image) {
        User_Image = user_Image;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String  getActivation_fee() {
        return Activation_fee;
    }

    public void setActivation_fee(String  activation_fee) {
        Activation_fee = activation_fee;
    }

    public int getShop_status() {
        return Shop_status;
    }

    public void setShop_status(int shop_status) {
        Shop_status = shop_status;
    }

    public long getTrips() {
        return Trips;
    }

    public void setTrips(long trips) {
        Trips = trips;
    }

    public long getEarnings() {
        return Earnings;
    }

    public void setEarnings(long earnings) {
        Earnings = earnings;
    }

    public long getCash_Trips() {
        return Cash_Trips;
    }

    public void setCash_Trips(long cash_Trips) {
        Cash_Trips = cash_Trips;
    }

    public double getRating() {
        return Rating;
    }

    public void setRating(double rating) {
        Rating = rating;
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

    public double getAvg_Rate() {
        return Avg_Rate;
    }

    public void setAvg_Rate(double avg_Rate) {
        Avg_Rate = avg_Rate;
    }
}
