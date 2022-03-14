package com.example.hamarekisan;

public class User {
public String fullname, address, email, phNo;
private String imageUrl;
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

    public String getFullname() {
        return fullname;
    }

    public String getRegno() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setRegno(String regno) {
        this.address = regno;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhNo(String phNo) {
        this.phNo = phNo;
    }

}
