package com.gas.swiftel.Model;

public class AccessModel {

    private String Accessory_image,name;

    public AccessModel() {
    }

    public AccessModel(String accessory_image, String name) {
        Accessory_image = accessory_image;
        this.name = name;
    }


    public String getAccessory_image() {
        return Accessory_image;
    }

    public void setAccessory_image(String accessory_image) {
        Accessory_image = accessory_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
