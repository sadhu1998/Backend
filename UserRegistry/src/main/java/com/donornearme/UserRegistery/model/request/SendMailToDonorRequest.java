package com.donornearme.UserRegistery.model.request;

public class SendMailToDonorRequest {
    String mailid;
    String message;

    public String getMailid() {
        return mailid;
    }

    public void setMailid(String mailid) {
        this.mailid = mailid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
