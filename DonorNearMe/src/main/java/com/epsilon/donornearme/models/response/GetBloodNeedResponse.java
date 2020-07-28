package com.epsilon.donornearme.models.response;

import java.util.List;
import java.util.Map;

public class GetBloodNeedResponse {
    String mailid;
    List<Map<String, Object>> requestsList;
    String error;
    String status;

    public String getMailid() {
        return mailid;
    }

    public void setMailid(String mailid) {
        this.mailid = mailid;
    }

    public List<Map<String, Object>> getRequestsList() {
        return requestsList;
    }

    public void setRequestsList(List<Map<String, Object>> requestsList) {
        this.requestsList = requestsList;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
