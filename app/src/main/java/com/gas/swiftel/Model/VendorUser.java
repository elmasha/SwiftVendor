package com.gas.swiftel.Model;


public class VendorUser {

    private String first_name,last_name , Location, Email, ID_no , ShopName, Password , User_ID, Shop_No, mobile;
    private long Trips;
    private long Earnings;
    private double lat,lng;

    public VendorUser() {
        //empty constractor..
    }

    public VendorUser(String first_name, String last_name,
                      String location, String email, String ID_no,
                      String shopName, String password,
                      String user_ID, String shop_No, String mobile,
                      long trips, long earnings, double lat, double lng) {
        this.first_name = first_name;
        this.last_name = last_name;
        Location = location;
        Email = email;
        this.ID_no = ID_no;
        ShopName = shopName;
        Password = password;
        User_ID = user_ID;
        Shop_No = shop_No;
        this.mobile = mobile;
        Trips = trips;
        Earnings = earnings;
        this.lat = lat;
        this.lng = lng;
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
    public String getEmail() {
        return Email;
    }
    public void setEmail(String email) {
        Email = email;
    }
    public String getID_no() {
        return ID_no;
    }
    public void setID_no(String ID_no) {
        this.ID_no = ID_no;
    }
    public String getShopName() {
        return ShopName;
    }
    public void setShopName(String shopName) {
        ShopName = shopName;
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String password) {
        Password = password;
    }
    public String getUser_ID() {
        return User_ID;
    }
    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }
    public String getShop_No() {
        return Shop_No;
    }
    public void setShop_No(String shop_No) {
        Shop_No = shop_No;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
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

}
