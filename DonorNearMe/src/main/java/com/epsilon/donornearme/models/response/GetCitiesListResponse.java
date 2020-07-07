package com.epsilon.donornearme.models.response;

import java.util.List;
import java.util.Map;

public class GetCitiesListResponse {
    Map<String, List<String>> citiesList;
    String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, List<String>> getCitiesList() {
        return citiesList;
    }

    public void setCitiesList(Map<String, List<String>> citiesList) {
        this.citiesList = citiesList;
    }
}
