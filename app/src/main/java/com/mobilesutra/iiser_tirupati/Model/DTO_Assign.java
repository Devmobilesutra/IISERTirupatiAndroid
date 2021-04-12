package com.mobilesutra.iiser_tirupati.Model;

/**
 * Created by kalyani on 14/04/2016.
 */
public class DTO_Assign {
    public String str_assign_title=null;
    public String str_sub_name=null;
    public String str_submission_date=null;
    public String str_submission_month=null;
    String img_url = "";


    public String getStr_teacher_name() {
        return str_teacher_name;
    }

    public void setStr_teacher_name(String str_teacher_name) {
        this.str_teacher_name = str_teacher_name;
    }

    public String str_teacher_name=null;

    public DTO_Assign(String str_assign_title,String str_sub_name,String str_submission_month,
                      String str_submission_date,String str_teacher_name)
    {
        this.str_assign_title=str_assign_title;
        this.str_sub_name=str_sub_name;
        this.str_submission_date=str_submission_date;
        this.str_submission_month=str_submission_month;
        this.str_teacher_name=str_teacher_name;


    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getStr_submission_month() {
        return str_submission_month;
    }

    public void setStr_submission_month(String str_submission_month) {
        this.str_submission_month = str_submission_month;
    }

    public String getStr_assign_title() {
        return str_assign_title;
    }

    public void setStr_assign_title(String str_assign_title) {
        this.str_assign_title = str_assign_title;
    }

    public String getStr_sub_name() {
        return str_sub_name;
    }

    public void setStr_sub_name(String str_sub_name) {
        this.str_sub_name = str_sub_name;
    }

    public String getStr_submission_date() {
        return str_submission_date;
    }

    public void setStr_submission_date(String str_submission_date) {
        this.str_submission_date = str_submission_date;
    }


}
