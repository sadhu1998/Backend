package com.donornearme.UserRegistery.model.response;

public class ValidateOTPResponse {
    String mailid;
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMailid() {
        return mailid;
    }

    public void setMailid(String mailid) {
        this.mailid = mailid;
    }
}
