package com.donornearme.UserRegistery.model.response;

import java.util.List;
import java.util.Map;

public class GetStatesListResponse {
    Map<String, List<String>> statesList;

    public Map<String, List<String>> getStatesList() {
        return statesList;
    }

    public void setStatesList(Map<String, List<String>> statesList) {
        this.statesList = statesList;
    }
}
