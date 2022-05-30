package com.example.hamarekisan;

public class Prediction {

    String Prediction;
    String Image;
    String Confidence;
    String Date;
    String Uid;

    public Prediction(){

    }

    public Prediction(String Prediction, String Image, String Confidence, String Date , String Uid ){
        this.Prediction=Prediction;
        this.Image=Image;
        this.Confidence=Confidence;
        this.Date=Date;
        this.Uid=Uid;
    }

    public String getPrediction() {
        return Prediction;
    }

    public void setPrediction(String prediction) {
        Prediction = prediction;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getConfidence() {
        return Confidence;
    }

    public void setConfidence(String confidence) {
        Confidence = confidence;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}