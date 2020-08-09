package com.epsilon.donornearme.controllers;

import com.epsilon.donornearme.operations.AreaOperator;
import com.epsilon.donornearme.models.request.*;
import com.epsilon.donornearme.models.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "location", description = "API's related to Locations")
public class AreasController extends BaseController {
    private static final Logger logger = LogManager.getLogger(AreasController.class);
    private final AreaOperator areaOperator = new AreaOperator();


    @ApiOperation(value = "Get Countries List")
    @RequestMapping(method = RequestMethod.GET, value = "/areas/list/countries")
    public GetCountriesListResponse getListOfCountries(GetCountriesListRequest getCountriesListRequest) throws Exception {
        return areaOperator.getCountriesList(getCountriesListRequest);
    }

    @ApiOperation(value = "Get States List")
    @RequestMapping(method = RequestMethod.GET, value = "/areas/list/states")
    public GetStatesListResponse getListOfStates(GetStatesListRequest getStatesListRequest) throws Exception {
        return areaOperator.getStatesList(getStatesListRequest);
    }

    @ApiOperation(value = "Get Districts List")
    @RequestMapping(method = RequestMethod.GET, value = "/areas/list/districts")
    public GetDistrictsListResponse getListOfDistricts(GetDistrictsListRequest getDistrictsListRequest) throws Exception {
        return areaOperator.getDistrictsList(getDistrictsListRequest);
    }

    @ApiOperation(value = "Get Cities List")
    @RequestMapping(method = RequestMethod.GET, value = "/areas/list/cities")
    public GetCitiesListResponse getListOfCities(GetCitiesListRequest getCitiesListRequest) throws Exception {
        return areaOperator.getCitiesList(getCitiesListRequest);
    }

    @ApiOperation(value = "Get Towns List")
    @RequestMapping(method = RequestMethod.GET, value = "/areas/list/towns")
    public GetTownsListResponse getListOfTowns(GetTownsListRequest getTownsListRequest) throws Exception {
        return areaOperator.getTownsList(getTownsListRequest);
    }

    @ApiOperation(value = "Get States List")
    @RequestMapping(method = RequestMethod.GET, value = "/areas/pincode")
    public GetPincodeResponse getPincodeOfArea(GetPincodeRequest getPincodeRequest) throws Exception {
        return areaOperator.getAreaPincode(getPincodeRequest);
    }
}
