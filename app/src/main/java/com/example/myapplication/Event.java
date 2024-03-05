package com.example.myapplication;

public class Event {
    private String dateTime;
    private String id;
    private int status;
    private int newUse;

    private String color;


    private String type;

    private String weather;
    private String occasion;

    public Event(){

    }

    public Event(String dateTime){
        this.dateTime = dateTime;
    }

    public Event(String id, String dateTime, int newUse, String type){
        this.id = id;
        this.dateTime = dateTime;
        this.newUse = newUse;
        this.type = type;
    }

    public Event(String id, String dateTime, String type){
        this.id = id;
        this.dateTime = dateTime;
        this.type = type;
    }

    public String getId(){
        return id;
    }

    public String getDateTime(){
        return dateTime;
    }

    public int getStatus(){
        return status;
    }
    public void setStatusFalse(){
        this.status = 0;
    }

    public String getWeather(){
        return weather;
    }
    public void setWeather(String weather){
        this.weather = weather;
    }

    public void setOccasion(String occasion){
        this.occasion = occasion;
    }

    public String getColor(){return color;}

    public void setNewUseFalse(){
        this.newUse=0;
    }
    public int getNewUse() { return newUse; }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
