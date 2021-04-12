package com.mobilesutra.iiser_tirupati.Model;

/**
 * Created by kalyani on 20/04/2016.
 */
public class DTO_TimeTable {
    public String str_time=null,str_venue=null;
    public String str_sub_name=null;
    public String str_teacher_name=null,str_lesson=null;

    public DTO_TimeTable(String str_time,String str_venue,String str_sub_name,String str_teacher_name,String str_lesson)
    {
        this.str_time=str_time;
        this.str_venue=str_venue;
        this.str_sub_name=str_sub_name;
        this.str_teacher_name=str_teacher_name;
        this.str_lesson=str_lesson;
    }
    public String getStr_time() {
        return str_time;
    }

    public void setStr_time(String str_time) {
        this.str_time = str_time;
    }

    public String getStr_venue() {
        return str_venue;
    }

    public void setStr_venue(String str_venue) {
        this.str_venue = str_venue;
    }

    public String getStr_sub_name() {
        return str_sub_name;
    }

    public void setStr_sub_name(String str_sub_name) {
        this.str_sub_name = str_sub_name;
    }

    public String getStr_teacher_name() {
        return str_teacher_name;
    }

    public void setStr_teacher_name(String str_teacher_name) {
        this.str_teacher_name = str_teacher_name;
    }

    public String getStr_lesson() {
        return str_lesson;
    }

    public void setStr_lesson(String str_lesson) {
        this.str_lesson = str_lesson;
    }


}
