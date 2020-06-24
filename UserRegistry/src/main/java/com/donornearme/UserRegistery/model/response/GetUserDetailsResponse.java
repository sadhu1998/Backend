package com.donornearme.UserRegistery.model.response;

import java.util.List;
import java.util.Map;

public class GetUserDetailsResponse {
    List<Map<String, Object>> userDetails;

    public List<Map<String, Object>> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<Map<String, Object>> userDetails) {
        this.userDetails = userDetails;
    }
}
