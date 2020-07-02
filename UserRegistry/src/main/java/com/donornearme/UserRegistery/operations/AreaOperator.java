package com.donornearme.UserRegistery.operations;

import com.donornearme.UserRegistery.Common;
import com.donornearme.UserRegistery.extensionsmanager.SQLManager;
import com.donornearme.UserRegistery.models.request.*;
import com.donornearme.UserRegistery.models.response.*;
import com.donornearme.UserRegistery.sqls.SqlUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class AreaOperator {
    protected static final Logger logger = LogManager.getLogger(AreaOperator.class);
    SQLManager sqlManager = new SQLManager();

    SqlUtility sqlUtility = new SqlUtility();
    public GetCountriesListResponse getCountriesList(GetCountriesListRequest getCountriesListRequest) throws Exception {
        GetCountriesListResponse getCountriesListResponse = new GetCountriesListResponse();
        String sql = sqlUtility.getCountriesList(getCountriesListRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        Map<String, List<String>> pincode_map = sqlManager.runSelectQueryReturnMapOfList(sql);
        getCountriesListResponse.setCountriesList(pincode_map);
        return getCountriesListResponse;
    }

    public GetStatesListResponse getStatesList(GetStatesListRequest getStatesListRequest) throws Exception {
        GetStatesListResponse getStatesListResponse = new GetStatesListResponse();
        String sql = sqlUtility.getStatesListSql(getStatesListRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        Map<String, List<String>> pincode_map = sqlManager.runSelectQueryReturnMapOfList(sql);
        getStatesListResponse.setStatesList(pincode_map);
        return getStatesListResponse;
    }

    public GetDistrictsListResponse getDistrictsList(GetDistrictsListRequest getDistrictsListRequest) throws Exception {
        GetDistrictsListResponse getDistrictsListResponse = new GetDistrictsListResponse();
        String sql = sqlUtility.getDistrictsListSql(getDistrictsListRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        Map<String, List<String>> pincode_map = sqlManager.runSelectQueryReturnMapOfList(sql);
        getDistrictsListResponse.setDistrictsList(pincode_map);
        return getDistrictsListResponse;
    }

    public GetTownsListResponse getTownsList(GetTownsListRequest getTownsListRequest) throws Exception {
        GetTownsListResponse getTownsListResponse = new GetTownsListResponse();
        String sql = sqlUtility.getTownsList(getTownsListRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        Map<String, List<String>> pincode_map = sqlManager.runSelectQueryReturnMapOfList(sql);
        getTownsListResponse.setTownsList(pincode_map);
        return getTownsListResponse;
    }

    public GetCitiesListResponse getCitiesList(GetCitiesListRequest getCitiesListRequest) throws Exception {
        GetCitiesListResponse getCitiesListResponse = new GetCitiesListResponse();
        String sql = sqlUtility.getCitiesListSql(getCitiesListRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        Map<String, List<String>> pincode_map = sqlManager.runSelectQueryReturnMapOfList(sql);
        getCitiesListResponse.setCitiesList(pincode_map);
        return getCitiesListResponse;
    }


}

