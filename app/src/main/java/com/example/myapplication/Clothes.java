package com.example.myapplication;

public class Clothes {

    private String id, type;
    private int status;
    public Clothes(String id, String type) {
        this.id = id;
        this.type = type;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public boolean isStatusTrue() {
        return status==1;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
