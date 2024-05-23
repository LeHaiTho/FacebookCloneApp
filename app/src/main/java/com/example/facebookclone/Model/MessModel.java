package com.example.facebookclone.Model;

public class MessModel {
    private  String uId, message;
    private long timeStamp;

    public MessModel() {
    }

    public MessModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }

    public MessModel(String uId, String message, long timeStamp) {
        this.uId = uId;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
