package com.donornearme.UserRegistery.model.response;

import java.util.List;
import java.util.Map;

public class GetTownsListResponse {
    Map<String, List<String>> townsList;

    public Map<String, List<String>> getTownsList() {
        return townsList;
    }

    public void setTownsList(Map<String, List<String>> townsList) {
        this.townsList = townsList;
    }
}
