package com.epsilon.donornearme.models.response;

public class AuthenticationResponse {
    String mailid;
    String status;
    String username;
    String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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
