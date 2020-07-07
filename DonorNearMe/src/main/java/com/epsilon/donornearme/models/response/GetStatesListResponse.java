package com.epsilon.donornearme.models.response;

import java.util.List;
import java.util.Map;

public class GetStatesListResponse {
    Map<String, List<String>> statesList;
    String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Map<String, List<String>> getStatesList() {
        return statesList;
    }

    public void setStatesList(Map<String, List<String>> statesList) {
        this.statesList = statesList;
    }
}
