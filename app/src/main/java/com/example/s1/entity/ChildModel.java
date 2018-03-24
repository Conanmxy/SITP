package com.example.s1.entity;

import org.litepal.crud.DataSupport;

/**
 *
 * Created by yang.dong on 2015/10/22.
 */
public class ChildModel extends DataSupport{
    private int id;
    private String date;
    private String time;
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
