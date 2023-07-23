package com.manuni.sunwayadmin;

public class Users {
    private String email,fullName,online,phoneNumber,profileImage,timestamp,uid,balance;

    public Users() {
    }

    public Users(String email, String fullName, String online, String phoneNumber, String profileImage, String timestamp, String uid,String balance) {
        this.email = email;
        this.fullName = fullName;
        this.online = online;
        this.phoneNumber = phoneNumber;
        this.profileImage = profileImage;
        this.timestamp = timestamp;
        this.uid = uid;
        this.balance = balance;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
