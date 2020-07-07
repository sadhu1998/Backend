package com.epsilon.donornearme.models.response;

import java.util.List;
import java.util.Map;

public class GetDistrictsListResponse {
    Map<String, List<String>> districtsList;
    String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, List<String>> getDistrictsList() {
        return districtsList;
    }

    public void setDistrictsList(Map<String, List<String>> districtsList) {
        this.districtsList = districtsList;
    }
}
