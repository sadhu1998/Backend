package com.donornearme.UserRegistery.usercontroller;

import com.donornearme.UserRegistery.usermethods.UserUtility;
import io.swagger.annotations.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Api(value = "location", description = "Get Details of Address")
public class AreaController extends BaseController {
    private static final Logger logger = LogManager.getLogger(AreaController.class);
    private UserUtility userUtility = new UserUtility();


    @ApiOperation(value = "Get Lists")
    @RequestMapping(method = RequestMethod.GET, value = "/getcountrieslist")
    public String getListOfCountries() throws Exception {
        return userUtility.getCountriesList();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getstateslist")
    public String getListOfStates(@RequestBody String json) throws Exception {
        return userUtility.getStatesList(json);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getcitieslist")
    public String getGetListOfCities(@RequestBody String json) throws Exception {
        return userUtility.getCitiesList(json);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getdistrictslist")
    public String getGetListOfDistricts(@RequestBody String json) throws Exception {
        return userUtility.getDistrictsList(json);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/gettownslist")
    public String getGetListOfTowns(@RequestBody String json) throws Exception {
        return userUtility.getTownsList(json);
    }
}
