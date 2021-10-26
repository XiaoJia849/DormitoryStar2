package com.example.dormitorystar;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

public class Done extends LitePalSupport {
    String user_id,date;
    int bed_id;
    boolean done;

    public Done() {
    }

    public Done(String user_id, String date, int bed_id, boolean done) {
        this.user_id = user_id;
        this.date = date;
        this.bed_id = bed_id;
        this.done = done;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getBed_id() {
        return bed_id;
    }

    public void setBed_id(int bed_id) {
        this.bed_id = bed_id;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
