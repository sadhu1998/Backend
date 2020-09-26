package com.epsilon.donornearme.models.request;

import java.io.File;

public class GetUserDetailsRequest {
    String mailid;
    
    public String getMailid() {
        return mailid;
    }

    public void setMailid(String mailid) {
        this.mailid = mailid;
    }
}
