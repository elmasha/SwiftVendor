package com.gas.swiftel.Model;

import java.util.Date;

public class Accessories {

    private String Item_name,Item_Desc,User_ID,Item_Image;
    private int Item_Price;
    private Date timestamp;


    public Accessories() {
    }

    public Accessories(String item_name, String item_Desc, String user_ID, String item_Image,
                       int item_Price, Date timestamp) {
        Item_name = item_name;
        Item_Desc = item_Desc;
        User_ID = user_ID;
        Item_Image = item_Image;
        Item_Price = item_Price;
        this.timestamp = timestamp;
    }


    public String getItem_name() {
        return Item_name;
    }

    public void setItem_name(String item_name) {
        Item_name = item_name;
    }

    public String getItem_Desc() {
        return Item_Desc;
    }

    public void setItem_Desc(String item_Desc) {
        Item_Desc = item_Desc;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getItem_Image() {
        return Item_Image;
    }

    public void setItem_Image(String item_Image) {
        Item_Image = item_Image;
    }

    public int getItem_Price() {
        return Item_Price;
    }

    public void setItem_Price(int item_Price) {
        Item_Price = item_Price;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
