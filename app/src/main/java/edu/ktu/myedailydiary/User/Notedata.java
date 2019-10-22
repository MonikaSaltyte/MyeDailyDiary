package edu.ktu.myedailydiary.User;


import java.util.Date;

public class Notedata {
    public String nId;
    public String date;
    public String title ;
    public String desc;


    public Notedata( ) {

    }

    public Notedata(String nId, String date, String title, String desc) {
        this.nId = nId;
        this.date = date;
        this.title = title;
        this.desc = desc;

    }

    public String getnId(){return nId;}

    public void setnId(String nId) {this.nId=nId;}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


   /* public String getFullString() {
        return date + " " + title + " " + desc;
    }*/

    /*public String getId() {return id; }

    public void setId(String id) {this.id = id;}*/






}