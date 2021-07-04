package com.gas.swiftel.Model;

import java.util.Date;

public class VendorsShop {

    public String Gas_Name,Gas_Desc,Gas_Kgs,User_ID,Gas_Image;
    public int Gas_Price;
    public int Gas_RefillPrice;
    public Date timestamp;


    public VendorsShop(){
        //emptyConstructor
    }


    public VendorsShop(String gas_Name, String gas_Desc, String gas_Kgs,
                       String user_ID, String gas_Image, int gas_Price, int gas_RefillPrice, Date timestamp) {
        Gas_Name = gas_Name;
        Gas_Desc = gas_Desc;
        Gas_Kgs = gas_Kgs;
        User_ID = user_ID;
        Gas_Image = gas_Image;
        Gas_Price = gas_Price;
        Gas_RefillPrice = gas_RefillPrice;
        this.timestamp = timestamp;
    }

    public String getGas_Name() {
        return Gas_Name;
    }

    public void setGas_Name(String gas_Name) {
        Gas_Name = gas_Name;
    }

    public String getGas_Desc() {
        return Gas_Desc;
    }

    public void setGas_Desc(String gas_Desc) {
        Gas_Desc = gas_Desc;
    }

    public String getGas_Kgs() {
        return Gas_Kgs;
    }

    public void setGas_Kgs(String gas_Kgs) {
        Gas_Kgs = gas_Kgs;
    }

    public String getUser_ID() {
        return User_ID;
    }

    public void setUser_ID(String user_ID) {
        User_ID = user_ID;
    }

    public String getGas_Image() {
        return Gas_Image;
    }

    public void setGas_Image(String gas_Image) {
        Gas_Image = gas_Image;
    }

    public int getGas_Price() {
        return Gas_Price;
    }

    public void setGas_Price(int gas_Price) {
        Gas_Price = gas_Price;
    }

    public int getGas_RefillPrice() {
        return Gas_RefillPrice;
    }

    public void setGas_RefillPrice(int gas_RefillPrice) {
        Gas_RefillPrice = gas_RefillPrice;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
