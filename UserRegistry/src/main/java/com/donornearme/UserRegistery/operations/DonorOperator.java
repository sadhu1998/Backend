package com.donornearme.UserRegistery.operations;

import com.donornearme.UserRegistery.Common;
import com.donornearme.UserRegistery.models.request.GetAllDonorsCountRequest;
import com.donornearme.UserRegistery.models.request.GetBloodGroupsRequest;
import com.donornearme.UserRegistery.models.request.GetDonorsAvailableRequest;
import com.donornearme.UserRegistery.models.response.GetAllDonorsCountResponse;
import com.donornearme.UserRegistery.models.response.GetBloodGroupsResponse;
import com.donornearme.UserRegistery.models.response.GetDonorsAvailableResponse;

import java.util.List;
import java.util.Map;

public class DonorOperator extends CommonOperator {
    public GetDonorsAvailableResponse getDataRequested(GetDonorsAvailableRequest getDonorsAvailableRequest) throws Exception {
        GetDonorsAvailableResponse getDonorsAvailableResponse = new GetDonorsAvailableResponse();
        logger.info(Common.FETCHING_REQUESTED_DETAILS);
        String sql = sqlUtility.getAvailableDonorsList(getDonorsAvailableRequest);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> requests_map = sqlManager.runSelectQuery(sql);
        getDonorsAvailableResponse.setDonorsList(requests_map);
        return getDonorsAvailableResponse;
    }

    public GetAllDonorsCountResponse getAllDonorsCount(GetAllDonorsCountRequest getAllDonorsCountRequest) throws Exception {
        GetAllDonorsCountResponse getAllDonorsCountResponse = new GetAllDonorsCountResponse();
        String sql = sqlUtility.getAllDonorsCount();
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> donor_count_map = sqlManager.runSelectQuery(sql);
        getAllDonorsCountResponse.setCount(donor_count_map.get(0).get(Common.COUNT).toString());
        return getAllDonorsCountResponse;
    }

    public GetBloodGroupsResponse getAllBloodGroupsList(GetBloodGroupsRequest getBloodGroupsRequest) throws Exception {
        GetBloodGroupsResponse getBloodGroupsResponse = new GetBloodGroupsResponse();
        String sql = sqlUtility.getBloodGroupsListSql(getBloodGroupsRequest);
        Map<String, List<String>> groups_map = sqlManager.runSelectQueryReturnMapOfList(sql);
        getBloodGroupsResponse.setBloodGroupsList(groups_map);
        return getBloodGroupsResponse;
    }

}
