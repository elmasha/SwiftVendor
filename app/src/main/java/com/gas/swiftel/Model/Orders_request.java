package com.gas.swiftel.Model;

import java.util.Date;

public class Orders_request {

    private String Vendor_ID,Item_image,Name,Item_desc,Category,User_id,
            Customer_name,Customer_No,User_image,exCylinder;
    private double lat,lng;
    private int Order_status;
    private String Quantity;
    private String Payment_method,Rated,doc_id,Vendor_Name,Shop_Name,Shop_No,mpesaReceipt,Price;
    private Date timestamp;

    public Orders_request() {
        //empty--
    }

    public Orders_request(String vendor_ID, String item_image, String name, String item_desc,
                          String category, String user_id, String customer_name, String customer_No,
                          String user_image, String exCylinder, double lat, double lng, int order_status,
                          String quantity, String payment_method, String rated,
                          String doc_id, String vendor_Name, String shop_Name, String shop_No,
                          String mpesaReceipt, String price, Date timestamp) {
        Vendor_ID = vendor_ID;
        Item_image = item_image;
        Name = name;
        Item_desc = item_desc;
        Category = category;
        User_id = user_id;
        Customer_name = customer_name;
        Customer_No = customer_No;
        User_image = user_image;
        this.exCylinder = exCylinder;
        this.lat = lat;
        this.lng = lng;
        Order_status = order_status;
        Quantity = quantity;
        Payment_method = payment_method;
        Rated = rated;
        this.doc_id = doc_id;
        Vendor_Name = vendor_Name;
        Shop_Name = shop_Name;
        Shop_No = shop_No;
        this.mpesaReceipt = mpesaReceipt;
        Price = price;
        this.timestamp = timestamp;
    }


    public String getVendor_ID() {
        return Vendor_ID;
    }

    public void setVendor_ID(String vendor_ID) {
        Vendor_ID = vendor_ID;
    }

    public String getItem_image() {
        return Item_image;
    }

    public void setItem_image(String item_image) {
        Item_image = item_image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getItem_desc() {
        return Item_desc;
    }

    public void setItem_desc(String item_desc) {
        Item_desc = item_desc;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getUser_id() {
        return User_id;
    }

    public void setUser_id(String user_id) {
        User_id = user_id;
    }

    public String getCustomer_name() {
        return Customer_name;
    }

    public void setCustomer_name(String customer_name) {
        Customer_name = customer_name;
    }

    public String getCustomer_No() {
        return Customer_No;
    }

    public void setCustomer_No(String customer_No) {
        Customer_No = customer_No;
    }

    public String getUser_image() {
        return User_image;
    }

    public void setUser_image(String user_image) {
        User_image = user_image;
    }

    public String getExCylinder() {
        return exCylinder;
    }

    public void setExCylinder(String exCylinder) {
        this.exCylinder = exCylinder;
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

    public int getOrder_status() {
        return Order_status;
    }

    public void setOrder_status(int order_status) {
        Order_status = order_status;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getPayment_method() {
        return Payment_method;
    }

    public void setPayment_method(String payment_method) {
        Payment_method = payment_method;
    }

    public String getRated() {
        return Rated;
    }

    public void setRated(String rated) {
        Rated = rated;
    }

    public String getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(String doc_id) {
        this.doc_id = doc_id;
    }

    public String getVendor_Name() {
        return Vendor_Name;
    }

    public void setVendor_Name(String vendor_Name) {
        Vendor_Name = vendor_Name;
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

    public String getMpesaReceipt() {
        return mpesaReceipt;
    }

    public void setMpesaReceipt(String mpesaReceipt) {
        this.mpesaReceipt = mpesaReceipt;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

