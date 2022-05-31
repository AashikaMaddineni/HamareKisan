package com.example.hamarekisan;

public class Prediction {

    String Prediction;
    String Image;
    String Confidence;
    String Date;
    String Uid;
    String UploadType;

    public Prediction(){

    }

    public Prediction(String Prediction, String Image, String Confidence, String Date, String UploadType, String Uid ){
        this.Prediction=Prediction;
        this.Image=Image;
        this.Confidence=Confidence;
        this.Date=Date;
        this.Uid=Uid;
        this.UploadType=UploadType;
    }

    public String getUploadType() {
        return UploadType;
    }

    public void setUploadType(String uploadType) {
        UploadType = uploadType;
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