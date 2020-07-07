package com.epsilon.donornearme.models.response;

import java.util.List;
import java.util.Map;

public class GetUserDetailsResponse {
    List<Map<String, Object>> userDetails;
    String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Map<String, Object>> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<Map<String, Object>> userDetails) {
        this.userDetails = userDetails;
    }
}
