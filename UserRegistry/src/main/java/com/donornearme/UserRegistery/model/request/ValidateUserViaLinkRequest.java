package com.donornearme.UserRegistery.model.request;

public class ValidateUserViaLinkRequest {
    String mailid;
    String otp;

    public String getMailid() {
        return mailid;
    }

    public void setMailid(String mailid) {
        this.mailid = mailid;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
