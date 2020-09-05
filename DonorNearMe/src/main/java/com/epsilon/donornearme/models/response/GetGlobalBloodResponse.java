package com.epsilon.donornearme.models.response;

import java.util.List;
import java.util.Map;

public class GetGlobalBloodResponse {
    List<Map<String,Object>> requestsList;

    public List<Map<String, Object>> getRequestsList() {
        return requestsList;
    }

    public void setRequestsList(List<Map<String, Object>> requestsList) {
        this.requestsList = requestsList;
    }
}
