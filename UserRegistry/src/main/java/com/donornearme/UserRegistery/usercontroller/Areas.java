package com.donornearme.UserRegistery.usercontroller;

import com.donornearme.UserRegistery.model.request.*;
import com.donornearme.UserRegistery.model.response.*;
import com.donornearme.UserRegistery.usermethods.UserUtility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "location", description = "API's related to Locations")
public class Areas extends BaseController {
    private static final Logger logger = LogManager.getLogger(Areas.class);
    private final UserUtility userUtility = new UserUtility();


    @ApiOperation(value = "Get Countries List")
    @RequestMapping(method = RequestMethod.GET, value = "/getlist/countries")
    public GetCountriesListResponse getListOfCountries(GetCountriesListRequest getCountriesListRequest) throws Exception {
        return userUtility.getCountriesList(getCountriesListRequest);
    }

    @ApiOperation(value = "Get States List")
    @RequestMapping(method = RequestMethod.GET, value = "/getlist/states")
    public GetStatesListResponse getListOfStates(GetStatesListRequest getStatesListRequest) throws Exception {
        return userUtility.getStatesList(getStatesListRequest);
    }

    @ApiOperation(value = "Get Districts List")
    @RequestMapping(method = RequestMethod.GET, value = "/getlist/districts")
    public GetDistrictsListResponse getListOfDistricts(GetDistrictsListRequest getDistrictsListRequest) throws Exception {
        return userUtility.getDistrictsList(getDistrictsListRequest);
    }

    @ApiOperation(value = "Get Cities List")
    @RequestMapping(method = RequestMethod.GET, value = "/getlist/cities")
    public GetCitiesListResponse getListOfCities(GetCitiesListRequest getCitiesListRequest) throws Exception {
        return userUtility.getCitiesList(getCitiesListRequest);
    }

    @ApiOperation(value = "Get Towns List")
    @RequestMapping(method = RequestMethod.GET, value = "/getlist/towns")
    public GetTownsListResponse getListOfTowns(GetTownsListRequest getTownsListRequest) throws Exception {
        return userUtility.getTownsList(getTownsListRequest);
    }
}
