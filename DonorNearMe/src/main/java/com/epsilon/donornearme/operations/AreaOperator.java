package com.epsilon.donornearme.operations;

import com.epsilon.donornearme.Common;
import com.epsilon.donornearme.dao.AreaQueries;
import com.epsilon.donornearme.utilities.SqlRendererUtility;
import com.epsilon.donornearme.models.request.*;
import com.epsilon.donornearme.models.response.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class AreaOperator {
    protected static final Logger logger = LogManager.getLogger(AreaOperator.class);
    AreaQueries areaQueries = new AreaQueries();
    SqlRendererUtility sqlRenderer = new SqlRendererUtility();

    public GetCountriesListResponse getCountriesList(GetCountriesListRequest getCountriesListRequest) throws Exception {
        GetCountriesListResponse getCountriesListResponse = new GetCountriesListResponse();
        String sql = areaQueries.getCountriesList(getCountriesListRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        Map<String, List<String>> pincode_map = sqlRenderer.runSelectQueryReturnMapOfList(sql);
        getCountriesListResponse.setCountriesList(pincode_map);
        return getCountriesListResponse;
    }

    public GetStatesListResponse getStatesList(GetStatesListRequest getStatesListRequest) throws Exception {
        GetStatesListResponse getStatesListResponse = new GetStatesListResponse();
        String sql = areaQueries.getStatesListSql(getStatesListRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        Map<String, List<String>> pincode_map = sqlRenderer.runSelectQueryReturnMapOfList(sql);
        getStatesListResponse.setStatesList(pincode_map);
        return getStatesListResponse;
    }

    public GetDistrictsListResponse getDistrictsList(GetDistrictsListRequest getDistrictsListRequest) throws Exception {
        GetDistrictsListResponse getDistrictsListResponse = new GetDistrictsListResponse();
        String sql = areaQueries.getDistrictsListSql(getDistrictsListRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        Map<String, List<String>> pincode_map = sqlRenderer.runSelectQueryReturnMapOfList(sql);
        getDistrictsListResponse.setDistrictsList(pincode_map);
        return getDistrictsListResponse;
    }

    public GetTownsListResponse getTownsList(GetTownsListRequest getTownsListRequest) throws Exception {
        GetTownsListResponse getTownsListResponse = new GetTownsListResponse();
        String sql = areaQueries.getTownsList(getTownsListRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        Map<String, List<String>> pincode_map = sqlRenderer.runSelectQueryReturnMapOfList(sql);
        getTownsListResponse.setTownsList(pincode_map);
        return getTownsListResponse;
    }

    public GetCitiesListResponse getCitiesList(GetCitiesListRequest getCitiesListRequest) throws Exception {
        GetCitiesListResponse getCitiesListResponse = new GetCitiesListResponse();
        String sql = areaQueries.getCitiesListSql(getCitiesListRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        Map<String, List<String>> pincode_map = sqlRenderer.runSelectQueryReturnMapOfList(sql);
        getCitiesListResponse.setCitiesList(pincode_map);
        return getCitiesListResponse;
    }


    public GetPincodeResponse getAreaPincode(GetPincodeRequest getPincodeRequest) {
        GetPincodeResponse getPincodeResponse = new GetPincodeResponse();
        String sql = areaQueries.getPincodeOfArea(getPincodeRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        List<Map<String, Object>> pincode_map = sqlRenderer.runSelectQuery(sql);
        String pincode = (String) pincode_map.get(0).get("pincode");
        getPincodeResponse.setPincode(pincode);
        getPincodeResponse.setStatus("Success");
        return getPincodeResponse;
    }
}

