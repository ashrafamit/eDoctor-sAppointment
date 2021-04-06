package com.ashrafamit.edoctorsappointment;


public class deal {
    private  String name,chamber,uid,category;

    public deal() {
    }

    public deal(String name, String chamber, String uid, String category) {
        this.name = name;
        this.chamber = chamber;
        this.uid = uid;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChamber() {
        return chamber;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
