package com.mobilesutra.iiser_tirupati.Model;


import com.mobilesutra.iiser_tirupati.Config.IISERApp;

/**
 * Created by Sony on 20-02-2016.
 */
public class DTOResponse {

    String response_code = "",response_message = "";

    public DTOResponse(String response_code, String response_message) {
        this.response_code = response_code;
        this.response_message = response_message;
    }

    public String getResponse_code() {
        IISERApp.log("get_dto","get_dto"+this.response_code);
        return this.response_code;

    }

    public void setResponse_code(String response_code) {
        IISERApp.log("set_dto","set_dto"+response_code);
        this.response_code = response_code;
    }

    public String getResponse_message() {

        return response_message;
    }

    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }

    public DTOResponse() {
    }
}
