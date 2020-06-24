package com.donornearme.UserRegistery.model.response;

import java.util.List;
import java.util.Map;

public class GetBloodGroupsResponse {
    Map<String, List<String>> bloodGroupsList;

    public Map<String, List<String>> getBloodGroupsList() {
        return bloodGroupsList;
    }

    public void setBloodGroupsList(Map<String, List<String>> bloodGroupsList) {
        this.bloodGroupsList = bloodGroupsList;
    }
}
