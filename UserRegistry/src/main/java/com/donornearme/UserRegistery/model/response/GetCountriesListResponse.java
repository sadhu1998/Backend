package com.donornearme.UserRegistery.model.response;

import java.util.List;
import java.util.Map;

public class GetCountriesListResponse {
    Map<String, List<String>> countriesList;

    public Map<String, List<String>> getCountriesList() {
        return countriesList;
    }

    public void setCountriesList(Map<String, List<String>> countriesList) {
        this.countriesList = countriesList;
    }
}
