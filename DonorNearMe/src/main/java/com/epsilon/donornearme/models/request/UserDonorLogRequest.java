package com.epsilon.donornearme.models.request;

public class UserDonorLogRequest {
    String receipentId;
    String donorId;
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceipentId() {
        return receipentId;
    }

    public void setReceipentId(String receipentId) {
        this.receipentId = receipentId;
    }

    public String getDonorId() {
        return donorId;
    }

    public void setDonorId(String donorId) {
        this.donorId = donorId;
    }
}
