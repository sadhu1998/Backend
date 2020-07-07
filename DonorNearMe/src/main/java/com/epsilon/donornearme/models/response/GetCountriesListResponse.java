package com.epsilon.donornearme.models.response;

import java.util.List;
import java.util.Map;

public class GetCountriesListResponse {
    Map<String, List<String>> countriesList;
    String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, List<String>> getCountriesList() {
        return countriesList;
    }

    public void setCountriesList(Map<String, List<String>> countriesList) {
        this.countriesList = countriesList;
    }
}
