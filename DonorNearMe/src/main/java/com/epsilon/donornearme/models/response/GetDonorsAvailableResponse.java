package com.epsilon.donornearme.models.response;

import java.util.List;
import java.util.Map;

public class GetDonorsAvailableResponse {
    List<Map<String, Object>> donorsList;
    String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Map<String, Object>> getDonorsList() {
        return donorsList;
    }

    public void setDonorsList(List<Map<String, Object>> donorsList) {
        this.donorsList = donorsList;
    }
}
