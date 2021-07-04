package com.gas.swiftel.Model;

public class Data {

    public String user,title,location,sent;
    public Integer icon;
    public long Active_shop;

    public Data() {
    }

    public Data(String user, String title, String location, String sent, Integer icon) {
        this.user = user;
        this.title = title;
        this.location = location;
        this.sent = sent;
        this.icon = icon;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSent() {
        return sent;
    }

    public void setSent(String sent) {
        this.sent = sent;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }
}
