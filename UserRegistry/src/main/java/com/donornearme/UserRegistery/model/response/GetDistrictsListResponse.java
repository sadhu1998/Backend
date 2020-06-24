package com.donornearme.UserRegistery.model.response;

import java.util.List;
import java.util.Map;

public class GetDistrictsListResponse {
    Map<String, List<String>> districtsList;

    public Map<String, List<String>> getDistrictsList() {
        return districtsList;
    }

    public void setDistrictsList(Map<String, List<String>> districtsList) {
        this.districtsList = districtsList;
    }
}
