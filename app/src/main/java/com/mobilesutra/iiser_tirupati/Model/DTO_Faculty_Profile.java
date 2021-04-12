package com.mobilesutra.iiser_tirupati.Model;

/**
 * Created by kalyani on 02/04/2016.
 */
public class DTO_Faculty_Profile {

    public String str_faculty_id = null;
    public String str_faculty_name = null;
    public String str_faculty_degree = null;
    public String str_faculty_designation = null;
    public String str_faculty_research = null;
    public String str_faculty_email_id = null;
    public String str_faculty_mobile_no = null;
    public String str_faculty_photo_url = null;
    public String str_faculty_personal_page = null;
    public String subject ="";


    public DTO_Faculty_Profile(String str_faculty_id, String str_faculty_name,  String str_faculty_degree,
                               String str_faculty_designation, String str_faculty_research,
                               String str_faculty_email_id, String str_faculty_mobile_no,
                               String str_faculty_photo_url, String str_faculty_personal_page) {

        this.str_faculty_id = str_faculty_id;
        this.str_faculty_name = str_faculty_name;
        this.str_faculty_degree = str_faculty_degree;
        this.str_faculty_designation = str_faculty_designation;
        this.str_faculty_research = str_faculty_research;
        this.str_faculty_email_id = str_faculty_email_id;
        this.str_faculty_mobile_no = str_faculty_mobile_no;
        this.str_faculty_photo_url = str_faculty_photo_url;
        this.str_faculty_personal_page = str_faculty_personal_page;
    }

    public DTO_Faculty_Profile(String str_faculty_id, String str_faculty_name, String str_faculty_designation, String subject,
                               String str_faculty_email_id, String str_faculty_mobile_no, String str_faculty_personal_page,
                               String str_faculty_photo_url) {

        this.str_faculty_id = str_faculty_id;
        this.str_faculty_name = str_faculty_name;
        this.subject = subject;

        this.str_faculty_research = subject;
        this.str_faculty_degree = "--";

        this.str_faculty_designation = str_faculty_designation;
        this.str_faculty_email_id = str_faculty_email_id;
        this.str_faculty_mobile_no = str_faculty_mobile_no;
        this.str_faculty_photo_url = str_faculty_photo_url;
        this.str_faculty_personal_page = str_faculty_personal_page;
    }

    public String getStr_faculty_id() {
        return str_faculty_id;
    }

    public void setStr_faculty_id(String str_faculty_id) {
        this.str_faculty_id = str_faculty_id;
    }

    public String getStr_faculty_name() {
        return str_faculty_name;
    }

    public void setStr_faculty_name(String str_faculty_name) {
        this.str_faculty_name = str_faculty_name;
    }

    public String getStr_faculty_degree() {
        return str_faculty_degree;
    }

    public void setStr_faculty_degree(String str_faculty_degree) {
        this.str_faculty_degree = str_faculty_degree;
    }

    public String getStr_faculty_designation() {
        return str_faculty_designation;
    }

    public void setStr_faculty_designation(String str_faculty_designation) {
        this.str_faculty_designation = str_faculty_designation;
    }

    public String getStr_faculty_research() {
        return str_faculty_research;
    }

    public void setStr_faculty_research(String str_faculty_research) {
        this.str_faculty_research = str_faculty_research;
    }

    public String getStr_faculty_email_id() {
        return str_faculty_email_id;
    }

    public void setStr_faculty_email_id(String str_faculty_email_id) {
        this.str_faculty_email_id = str_faculty_email_id;
    }

    public String getStr_faculty_mobile_no() {
        return str_faculty_mobile_no;
    }

    public void setStr_faculty_mobile_no(String str_faculty_mobile_no) {
        this.str_faculty_mobile_no = str_faculty_mobile_no;
    }

    public String getStr_faculty_photo_url() {
        return str_faculty_photo_url;
    }

    public void setStr_faculty_photo_url(String str_faculty_photo_url) {
        this.str_faculty_photo_url = str_faculty_photo_url;
    }

    public String getStr_faculty_personal_page() {
        return str_faculty_personal_page;
    }

    public void setStr_faculty_personal_page(String str_faculty_personal_page) {
        this.str_faculty_personal_page = str_faculty_personal_page;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
