package com.epsilon.donornearme.models.response;

import java.util.List;
import java.util.Map;

public class GetTownsListResponse {
    Map<String, List<String>> townsList;
    String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
    public Map<String, List<String>> getTownsList() {
        return townsList;
    }

    public void setTownsList(Map<String, List<String>> townsList) {
        this.townsList = townsList;
    }
}
