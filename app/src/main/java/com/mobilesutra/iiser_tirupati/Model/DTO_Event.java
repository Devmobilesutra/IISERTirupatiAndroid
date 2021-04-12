package com.mobilesutra.iiser_tirupati.Model;

/**
 * Created by kalyani on 04/04/2016.
 */
public class DTO_Event {

    public String str_event_id=null;
    public String str_event_description=null;
    public String str_event_pdf_link=null;
    public String str_event_month=null;
    public String str_event_day=null;
    public String str_event_title=null;
    public String str_event_date=null;
    public String str_event_venue=null;
    public String getStr_event_date;

    public DTO_Event(String str_event_id, String str_event_month, String str_event_day, String str_event_title,
                     String str_event_description, String str_event_date, String str_event_pdf_link, String str_event_venue)
    {
        this.str_event_id=str_event_id;
        this.str_event_month=str_event_month;
        this.str_event_day=str_event_day;
        this.str_event_title=str_event_title;
        this.str_event_description=str_event_description;
        this.str_event_date=str_event_date;
        this.str_event_pdf_link=str_event_pdf_link;
        this.str_event_venue=str_event_venue;

    }
    public String getStr_event_id() {
        return str_event_id;
    }

    public void setStr_event_id(String str_event_id) {
        this.str_event_id = str_event_id;
    }

    public String getStr_event_description() {
        return str_event_description;
    }

    public void setStr_event_description(String str_event_description) {
        this.str_event_description = str_event_description;
    }

    public String getStr_event_pdf_link() {
        return str_event_pdf_link;
    }

    public void setStr_event_pdf_link(String str_event_pdf_link) {
        this.str_event_pdf_link = str_event_pdf_link;
    }
    public String getStr_event_month() {
        return str_event_month;
    }

    public void setStr_event_month(String str_event_month) {
        this.str_event_month = str_event_month;
    }

    public String getStr_event_day() {
        return str_event_day;
    }

    public void setStr_event_day(String str_event_day) {
        this.str_event_day = str_event_day;
    }

    public String getStr_event_title() {
        return str_event_title;
    }

    public void setStr_event_title(String str_event_header) {
        this.str_event_title = str_event_header;
    }

    public String getStr_event_date() {
        return str_event_date;
    }

    public void setStr_event_date(String str_event_organisr) {
        this.str_event_date = str_event_organisr;
    }

    public String getStr_event_venue() {
        return str_event_venue;
    }

    public void setStr_event_venue(String str_event_venue) {
        this.str_event_venue = str_event_venue;
    }


}
