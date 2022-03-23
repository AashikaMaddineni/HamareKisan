package com.example.hamarekisan;

public class User {
public String fullname, address, email, phNo;
private String imageUrl;
private String AboutYou;
public User(){

}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public User(String imageUrl){
    this.imageUrl=imageUrl;
}
    public User(String fullname, String email) {
        this.fullname = fullname;
        this.email = email;
    }

public User(String fullname, String address, String email, String phNo){
    this.fullname=fullname;
    this.address=address;
    this.email=email;
    this.phNo=phNo;
}

    public User(String fullname, String address, String email, String phNo, String AboutYou){
        this.fullname=fullname;
        this.address=address;
        this.email=email;
        this.phNo=phNo;
        this.AboutYou=AboutYou;
    }

    public String getFullname() {
        return fullname;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getAboutYou() {
        return phNo;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

    public void setAboutYou(String phNo) {
        this.phNo = phNo;
    }
}
