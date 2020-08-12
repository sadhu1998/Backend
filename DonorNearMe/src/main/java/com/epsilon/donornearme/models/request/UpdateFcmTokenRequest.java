package com.epsilon.donornearme.models.request;

public class UpdateFcmTokenRequest {
    String mailid;
    String fcmToken;

    public String getMailid() {
        return mailid;
    }

    public void setMailid(String mailid) {
        this.mailid = mailid;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
