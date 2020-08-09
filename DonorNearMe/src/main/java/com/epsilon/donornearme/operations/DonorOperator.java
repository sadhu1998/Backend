package com.epsilon.donornearme.operations;

import com.epsilon.donornearme.Common;
import com.epsilon.donornearme.models.request.GetAllDonorsCountRequest;
import com.epsilon.donornearme.models.request.GetBloodGroupsRequest;
import com.epsilon.donornearme.models.request.GetDonorsAvailableRequest;
import com.epsilon.donornearme.models.request.UserDonorLogRequest;
import com.epsilon.donornearme.models.response.GetAllDonorsCountResponse;
import com.epsilon.donornearme.models.response.GetBloodGroupsResponse;
import com.epsilon.donornearme.models.response.GetDonorsAvailableResponse;
import com.epsilon.donornearme.models.response.UserDonorLogResponse;
import com.epsilon.donornearme.dao.DonorQueries;

import java.util.List;
import java.util.Map;

public class DonorOperator extends CommonOperator {
    DonorQueries donorQueries = new DonorQueries();
    public GetDonorsAvailableResponse getDataRequested(GetDonorsAvailableRequest getDonorsAvailableRequest) throws Exception {
        GetDonorsAvailableResponse getDonorsAvailableResponse = new GetDonorsAvailableResponse();
        logger.info(Common.FETCHING_REQUESTED_DETAILS);
        String sql = donorQueries.getAvailableDonorsList(getDonorsAvailableRequest);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> requests_map = sqlRenderer.runSelectQuery(sql);
        getDonorsAvailableResponse.setDonorsList(requests_map);
        return getDonorsAvailableResponse;
    }

    public GetAllDonorsCountResponse getAllDonorsCount(GetAllDonorsCountRequest getAllDonorsCountRequest) throws Exception {
        GetAllDonorsCountResponse getAllDonorsCountResponse = new GetAllDonorsCountResponse();
        String sql = donorQueries.getAllDonorsCount();
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> donor_count_map = sqlRenderer.runSelectQuery(sql);
        getAllDonorsCountResponse.setCount(donor_count_map.get(0).get(Common.COUNT).toString());
        return getAllDonorsCountResponse;
    }

    public GetBloodGroupsResponse getAllBloodGroupsList(GetBloodGroupsRequest getBloodGroupsRequest) throws Exception {
        GetBloodGroupsResponse getBloodGroupsResponse = new GetBloodGroupsResponse();
        String sql = donorQueries.getBloodGroupsListSql(getBloodGroupsRequest);
        Map<String, List<String>> groups_map = sqlRenderer.runSelectQueryReturnMapOfList(sql);
        getBloodGroupsResponse.setBloodGroupsList(groups_map);
        return getBloodGroupsResponse;
    }


    public UserDonorLogResponse insertIntoRequestsLog(UserDonorLogRequest userDonorLogRequest) {
        UserDonorLogResponse userDonorLogResponse = new UserDonorLogResponse();
        userDonorLogResponse.setReceipentId(userDonorLogRequest.getReceipentId());
        userDonorLogResponse.setDonorId(userDonorLogRequest.getDonorId());
        String sql = donorQueries.insertIntoBloodRequestsTable(userDonorLogRequest);
        logger.info(sql);
        sqlRenderer.runInsertQuery(sql);
        userDonorLogResponse.setStatus("Request Succesfull");
        return userDonorLogResponse;
    }
}
