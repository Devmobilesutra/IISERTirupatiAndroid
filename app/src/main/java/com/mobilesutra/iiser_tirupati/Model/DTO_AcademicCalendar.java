package com.mobilesutra.iiser_tirupati.Model;

/**
 * Created by kalyani on 13/05/2016.
 */
public class DTO_AcademicCalendar {
    public String str_calendar_id=null;
    public String str_calendar_date=null;
    public String str_calendar_month=null;
    public String str_calendar_weekday=null;
    public String str_calendar_weekdate=null;
    public String str_calendar_title=null;
    public String str_holiday=null;

    public DTO_AcademicCalendar(String str_calendar_id, String str_calendar_date,
                                String str_calendar_month, String str_calendar_weekday, String str_calendar_weekdate, String str_calendar_title) {
        this.str_calendar_id = str_calendar_id;
        this.str_calendar_date = str_calendar_date;
        this.str_calendar_month = str_calendar_month;
        this.str_calendar_weekday = str_calendar_weekday;
        this.str_calendar_weekdate = str_calendar_weekdate;
        this.str_calendar_title = str_calendar_title;
    }

    public DTO_AcademicCalendar(String str_calendar_id, String str_calendar_date,
                                String str_calendar_month, String str_calendar_weekday, String str_calendar_weekdate, String str_calendar_title,String str_holiday) {
        this.str_calendar_id = str_calendar_id;
        this.str_calendar_date = str_calendar_date;
        this.str_calendar_month = str_calendar_month;
        this.str_calendar_weekday = str_calendar_weekday;
        this.str_calendar_weekdate = str_calendar_weekdate;
        this.str_calendar_title = str_calendar_title;
        this.str_holiday = str_holiday;
    }
    public String getStr_calendar_id() {
        return str_calendar_id;
    }

    public void setStr_calendar_id(String str_calendar_id) {
        this.str_calendar_id = str_calendar_id;
    }

    public String getStr_calendar_date() {
        return str_calendar_date;
    }

    public void setStr_calendar_date(String str_calendar_date) {
        this.str_calendar_date = str_calendar_date;
    }

    public String getStr_calendar_month() {
        return str_calendar_month;
    }

    public String getStr_holiday() {
        return str_holiday;
    }

    public void setStr_calendar_month(String str_calendar_month) {
        this.str_calendar_month = str_calendar_month;
    }

    public String getStr_calendar_weekday() {
        return str_calendar_weekday;
    }

    public void setStr_calendar_weekday(String str_calendar_weekday) {
        this.str_calendar_weekday = str_calendar_weekday;
    }

    public String getStr_calendar_weekdate() {
        return str_calendar_weekdate;
    }

    public void setStr_calendar_weekdate(String str_calendar_weekdate) {
        this.str_calendar_weekdate = str_calendar_weekdate;
    }

    public String getStr_calendar_title() {
        return str_calendar_title;
    }

    public void setStr_calendar_title(String str_calendar_title) {
        this.str_calendar_title = str_calendar_title;
    }
}
