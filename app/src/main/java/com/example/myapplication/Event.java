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

    private boolean isSelected;


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

    public String getDate() {
        String date;
        String parsing_dateTime = this.dateTime;

        // Split the dateTime string based on space and take the first part
        date = parsing_dateTime.split(" ")[0];

        return date;
    }

    public String getTime() {
        String time;
        String parsing_dateTime = this.dateTime;

        // Split the dateTime string based on space and take the second part
        String[] dateTimeParts = parsing_dateTime.split(" ");
        String timePart = dateTimeParts[1];

        // Further split the timePart based on colon to get the time
        String[] timeParts = timePart.split(":");
        time = timeParts[0] + ":" + timeParts[1] + ":" + timeParts[2];

        return time;
    }

    public int getStatus(){
        return status;
    }
    public void setStatusFalse(){
        this.status = 0;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public void setNewUseFalse(){
        this.newUse=0;
    }
    public int getNewUse() { return newUse; }

    public boolean isStatusTrue() {
        return status==1;
    }

    public String getType() {
        return type;
    }

    public String getColor(){return color;}

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


}

