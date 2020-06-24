package com.donornearme.UserRegistery.model.response;

import java.util.List;
import java.util.Map;

public class GetCitiesListResponse {
    Map<String, List<String>> citiesList;

    public Map<String, List<String>> getCitiesList() {
        return citiesList;
    }

    public void setCitiesList(Map<String, List<String>> citiesList) {
        this.citiesList = citiesList;
    }
}
