package com.mobilesutra.iiser_tirupati.Model;

/**
 * Created by kalyani on 22/04/2016.
 */
public class DTO_ExamSchedule {


    public String str_exam_id = null;
    public String str_exam_day = null;
    public String str_exam_month = null;
    public String str_exam_weekdays = null;
    public String str_exam_date = null;
    public String str_exam_title = null;
    public String str_exam_sub_name = null, str_exam_time = null, str_exam_supervisr_name = null, str_exam_venue = null;

    public DTO_ExamSchedule(String str_exam_id, String str_exam_day, String str_exam_month, String str_exam_title,
                            String str_exam_sub_name, String str_exam_time, String str_exam_date, String str_exam_weekdays,
                            String str_exam_supervisr_name, String str_exam_venue) {
        this.str_exam_id = str_exam_id;
        this.str_exam_day = str_exam_day;
        this.str_exam_title = str_exam_title;
        this.str_exam_month = str_exam_month;
        this.str_exam_sub_name = str_exam_sub_name;
        this.str_exam_time = str_exam_time;
        this.str_exam_date = str_exam_date;
        this.str_exam_weekdays = str_exam_weekdays;
        this.str_exam_supervisr_name = str_exam_supervisr_name;
        this.str_exam_venue = str_exam_venue;

    }

    public String getStr_exam_weekdays() {
        return str_exam_weekdays;
    }

    public void setStr_exam_weekdays(String str_exam_weekdays) {
        this.str_exam_weekdays = str_exam_weekdays;
    }

    public String getStr_exam_date() {
        return str_exam_date;
    }

    public void setStr_exam_date(String str_exam_date) {
        this.str_exam_date = str_exam_date;
    }

    public String getStr_exam_id() {
        return str_exam_id;
    }

    public void setStr_exam_id(String str_exam_id) {
        this.str_exam_id = str_exam_id;
    }

    public String getStr_exam_day() {
        return str_exam_day;
    }

    public void setStr_exam_day(String str_exam_date) {
        this.str_exam_day = str_exam_date;
    }

    public String getStr_exam_month() {
        return str_exam_month;
    }

    public void setStr_exam_month(String str_exam_month) {
        this.str_exam_month = str_exam_month;
    }

    public String getStr_exam_title() {
        return str_exam_title;
    }

    public void setStr_exam_title(String str_exam_title) {
        this.str_exam_title = str_exam_title;
    }

    public String getStr_exam_sub_name() {
        return str_exam_sub_name;
    }

    public void setStr_exam_sub_name(String str_exam_sub_name) {
        this.str_exam_sub_name = str_exam_sub_name;
    }

    public String getStr_exam_time() {
        return str_exam_time;
    }

    public void setStr_exam_time(String str_exam_time) {
        this.str_exam_time = str_exam_time;
    }

    public String getStr_exam_supervisr_name() {
        return str_exam_supervisr_name;
    }

    public void setStr_exam_supervisr_name(String str_exam_supervisr_name) {
        this.str_exam_supervisr_name = str_exam_supervisr_name;
    }

    public String getStr_exam_venue() {
        return str_exam_venue;
    }

    public void setStr_exam_venue(String str_exam_venue) {
        this.str_exam_venue = str_exam_venue;
    }

}
