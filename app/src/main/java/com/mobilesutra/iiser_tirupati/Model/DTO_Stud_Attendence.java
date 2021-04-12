package com.mobilesutra.iiser_tirupati.Model;

/**
 * Created by Bhavesh Chaudhari on 17-Jan-20.
 */
public class DTO_Stud_Attendence {
    String date;
    String course_code;
    String stud_present;
    String lec_present;

    public DTO_Stud_Attendence(String date, String course_code, String stud_present, String lec_present) {
        this.date = date;
        this.course_code = course_code;
        this.stud_present = stud_present;
        this.lec_present = lec_present;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCourse_code() {
        return course_code;
    }

    public void setCourse_code(String course_code) {
        this.course_code = course_code;
    }

    public String getStud_present() {
        return stud_present;
    }

    public void setStud_present(String stud_present) {
        this.stud_present = stud_present;
    }

    public String getLec_present() {
        return lec_present;
    }

    public void setLec_present(String lec_present) {
        this.lec_present = lec_present;
    }


}
