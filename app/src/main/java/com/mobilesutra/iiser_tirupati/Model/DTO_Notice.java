package com.mobilesutra.iiser_tirupati.Model;

/**
 * Created by kalyani on 06/05/2016.
 */
public class DTO_Notice {


    public String str_notice_id=null;
    public String str_notice_title=null;
    public String str_notice_description=null;
    public String str_notice_pdf_link=null;
    public String str_expiry_date=null;
    public String str_day=null;
    public String str_month=null;


    public DTO_Notice(String str_notice_id, String str_notice_title,
                      String str_notice_description, String str_notice_pdf_link, String str_expiry_date,String str_day,
                      String str_month) {
        this.str_notice_id = str_notice_id;
        this.str_notice_title = str_notice_title;
        this.str_notice_description = str_notice_description;
        this.str_notice_pdf_link = str_notice_pdf_link;
        this.str_expiry_date = str_expiry_date;
        this.str_day = str_day;
        this.str_month = str_month;
    }
    public String getStr_day() {
        return str_day;
    }

    public void setStr_day(String str_day) {
        this.str_day = str_day;
    }

    public String getStr_month() {
        return str_month;
    }

    public void setStr_month(String str_month) {
        this.str_month = str_month;
    }


    public String getStr_notice_id() {
        return str_notice_id;
    }

    public void setStr_notice_id(String str_notice_id) {
        this.str_notice_id = str_notice_id;
    }

    public String getStr_notice_title() {
        return str_notice_title;
    }

    public void setStr_notice_title(String str_notice_title) {
        this.str_notice_title = str_notice_title;
    }

    public String getStr_notice_description() {
        return str_notice_description;
    }

    public void setStr_notice_description(String str_notice_description) {
        this.str_notice_description = str_notice_description;
    }

    public String getStr_notice_pdf_link() {
        return str_notice_pdf_link;
    }

    public void setStr_notice_pdf_link(String str_notice_pdf_link) {
        this.str_notice_pdf_link = str_notice_pdf_link;
    }

    public String getStr_expiry_date() {
        return str_expiry_date;
    }

    public void setStr_expiry_date(String str_expiry_date) {
        this.str_expiry_date = str_expiry_date;
    }


}
