package com.donornearme.UserRegistery.model.response;

public class ForgotPasswordResponse {
    String status;
    String mailid;

    public String getMailid() {
        return mailid;
    }

    public void setMailid(String mailid) {
        this.mailid = mailid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
