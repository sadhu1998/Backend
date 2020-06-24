package com.donornearme.UserRegistery.usercontroller;

import com.donornearme.UserRegistery.model.request.*;
import com.donornearme.UserRegistery.model.response.*;
import com.donornearme.UserRegistery.usermethods.MailUtility;
import com.donornearme.UserRegistery.usermethods.UserUtility;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "Donor fetcher", description = "Fetch donor details")
public class DonorController extends BaseController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserUtility userUtility = new UserUtility();
    MailUtility mailUtility = new MailUtility();

    @RequestMapping(method = RequestMethod.POST, value = "/sendmailtodonor")
    public SendMailToDonorResponse sendMailToDonor(@RequestBody SendMailToDonorRequest sendMailToDonorRequest) throws Exception {
        return mailUtility.sendMailToDonor(sendMailToDonorRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getdonors/available")
    public GetDonorsAvailableResponse getDonorsAvailabe(GetDonorsAvailableRequest getDonorsAvailableRequest) throws Exception {
        return userUtility.getDataRequested(getDonorsAvailableRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "getdetails/user")
    public GetUserDetailsResponse getUserDetails(GetUserDetailsRequest getUserDetailsRequest) throws Exception {
        return userUtility.getUserDetails(getUserDetailsRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getdonorcount")
    public String getTotalDonorCount() throws Exception {
        return userUtility.getAllDonorsCount();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getlist/bloodgroups")
    public GetBloodGroupsResponse getBloodGroupsList(GetBloodGroupsRequest getBloodGroupsRequest) throws Exception {
        return userUtility.getAllBloodGroupsList(getBloodGroupsRequest);
    }

}
