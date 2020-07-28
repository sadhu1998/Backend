package com.epsilon.donornearme.models.response;

public class UserDonorLogResponse {
    String receipentId;
    String donorId;
    String status;
    String error;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
