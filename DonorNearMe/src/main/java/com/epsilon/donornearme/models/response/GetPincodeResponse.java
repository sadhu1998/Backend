package com.epsilon.donornearme.models.response;

public class GetPincodeResponse {
    String pincode;
    String status;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
