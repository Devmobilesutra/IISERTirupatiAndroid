package com.mobilesutra.iiser_tirupati.Model;

/**
 * Created by Bhavesh Chaudhari on 24-Jan-20.
 */
public class DTO_Faculty_Attendence {
    String rollNumber;
    String percentage;

    public DTO_Faculty_Attendence(String rollNumber, String percentage) {
        this.rollNumber = rollNumber;
        this.percentage = percentage;
    }


    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }



}
