package com.example.hamarekisan;

public class Prediction {

    String title;
    String image;
    String prediction;
    String date;

    public Prediction(){

    }

    public Prediction(String title, String image, String prediction, String date ){
        if (title.trim().equals("")) {
            title = "No Name";
        }
        this.date=date;
        this.title=title;
        this.image=image;
        this.prediction=prediction;

    }
    public Prediction(String image){
        this.image=image;
    }

    // date
    public String getDate(){
        return date;
    }
    public void setDate(String date){
        this.date=date;
    }

    //title
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    //image
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    //prediction
    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }
}