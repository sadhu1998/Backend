package com.epsilon.donornearme.controllers;

import com.donornearme.dnm.models.request.*;
import com.donornearme.dnm.models.response.*;
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
    @RequestMapping(method = RequestMethod.GET, value = "/getlist/countries")
    public GetCountriesListResponse getListOfCountries(GetCountriesListRequest getCountriesListRequest) throws Exception {
        return areaOperator.getCountriesList(getCountriesListRequest);
    }

    @ApiOperation(value = "Get States List")
    @RequestMapping(method = RequestMethod.GET, value = "/getlist/states")
    public GetStatesListResponse getListOfStates(GetStatesListRequest getStatesListRequest) throws Exception {
        return areaOperator.getStatesList(getStatesListRequest);
    }

    @ApiOperation(value = "Get Districts List")
    @RequestMapping(method = RequestMethod.GET, value = "/getlist/districts")
    public GetDistrictsListResponse getListOfDistricts(GetDistrictsListRequest getDistrictsListRequest) throws Exception {
        return areaOperator.getDistrictsList(getDistrictsListRequest);
    }

    @ApiOperation(value = "Get Cities List")
    @RequestMapping(method = RequestMethod.GET, value = "/getlist/cities")
    public GetCitiesListResponse getListOfCities(GetCitiesListRequest getCitiesListRequest) throws Exception {
        return areaOperator.getCitiesList(getCitiesListRequest);
    }

    @ApiOperation(value = "Get Towns List")
    @RequestMapping(method = RequestMethod.GET, value = "/getlist/towns")
    public GetTownsListResponse getListOfTowns(GetTownsListRequest getTownsListRequest) throws Exception {
        return areaOperator.getTownsList(getTownsListRequest);
    }
}
