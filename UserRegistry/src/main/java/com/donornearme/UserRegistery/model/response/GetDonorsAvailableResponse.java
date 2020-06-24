package com.donornearme.UserRegistery.model.response;

import java.util.List;
import java.util.Map;

public class GetDonorsAvailableResponse {
    List<Map<String, Object>> donorsList;

    public List<Map<String, Object>> getDonorsList() {
        return donorsList;
    }

    public void setDonorsList(List<Map<String, Object>> donorsList) {
        this.donorsList = donorsList;
    }
}
