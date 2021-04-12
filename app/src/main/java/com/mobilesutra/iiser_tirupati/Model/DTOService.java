package com.mobilesutra.iiser_tirupati.Model;

/**
 * Created by kalyani on 20-04-2016.
 */
public class DTOService {

    public DTOService(int str_response_code, String str_response_mesaage) {
        this.str_response_code = str_response_code;
        this.str_response_mesaage = str_response_mesaage;
    }

    int str_response_code = 0;
    String str_response_body = "";
    String str_exception = "";
    String str_response_mesaage="";

    public DTOService(int str_response_code, String str_response_body, String str_exception,String str_response_mesaage) {
        this.str_response_code = str_response_code;
        this.str_response_body = str_response_body;
        this.str_exception = str_exception;
        this.str_response_mesaage = str_response_mesaage;
    }

    public DTOService() {
    }


    public String getStr_response_mesaage() {
        return str_response_mesaage;
    }

    public void setStr_response_mesaage(String str_response_mesaage) {
        this.str_response_mesaage = str_response_mesaage;
    }

    public int getStr_response_code() {
        return str_response_code;
    }

    public String getStr_response_body() {
        return str_response_body;
    }

    public String getStr_exception() {
        return str_exception;
    }

    public void setStr_response_code(int str_response_code) {
        this.str_response_code = str_response_code;
    }

    public void setStr_response_body(String str_response_body) {
        this.str_response_body = str_response_body;
    }

    public void setStr_exception(String str_exception) {
        this.str_exception = str_exception;
    }
}
