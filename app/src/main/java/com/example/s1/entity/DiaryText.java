package com.example.s1.entity;

import org.litepal.crud.DataSupport;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Vector;


/**
 * Created by Administrator on 2017/10/31.
 */
public class DiaryText extends DataSupport{
    private String diaryText;//日记内容
    private int   id;//主键
    private String date;//写日记的时间
    private static int id2=0;

    public DiaryText(){
        id=id2;
        id2++;
    }
    public DiaryText(String diaryText,String date){
        this.diaryText=diaryText;
        id=id2;
        id2++;
        this.date=date;
    }
    public String getDate() {
        return date;
    }

    public String getDiaryText() {
        return diaryText;
    }

    public int getId() {
        return id;
    }

    public void setDate(String date) {

        this.date = date;
    }

    public void setDiaryText(String diaryText) {
        this.diaryText = diaryText;
    }

    public void setId(int id) {
        this.id = id;
    }

}
