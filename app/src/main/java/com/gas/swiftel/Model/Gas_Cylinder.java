package com.gas.swiftel.Model;

public class Gas_Cylinder {

    private String Cylinder_image,name,kg;

    public Gas_Cylinder() {
    //empty
    }

    public String getCylinder_image() {
        return Cylinder_image;
    }

    public void setCylinder_image(String cylinder_image) {
        Cylinder_image = cylinder_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKg() {
        return kg;
    }

    public void setKg(String kg) {
        this.kg = kg;
    }
}
