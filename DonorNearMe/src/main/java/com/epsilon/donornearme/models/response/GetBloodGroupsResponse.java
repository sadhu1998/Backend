package com.epsilon.donornearme.models.response;

import java.util.List;
import java.util.Map;

public class GetBloodGroupsResponse {
    Map<String, List<String>> bloodGroupsList;
    String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, List<String>> getBloodGroupsList() {
        return bloodGroupsList;
    }

    public void setBloodGroupsList(Map<String, List<String>> bloodGroupsList) {
        this.bloodGroupsList = bloodGroupsList;
    }
}
