package com.example.myapplication;

public class Information {
    private String Frequency;
    private String Id;

    public Information(){

    }
    public Information(String id, String frequency){
        this.Id = id;
        this.Frequency = frequency;

    }

    public String getId(){
        return Id;
    }

    public void setId(String id){
        this.Id=id;
    }

    public String getFrequency(){
        return Frequency;
    }

    public void setFrequency(String frequency){
        this.Frequency = frequency;
    }
}
