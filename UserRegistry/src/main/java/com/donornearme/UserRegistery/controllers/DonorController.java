package com.donornearme.UserRegistery.controllers;

import com.donornearme.UserRegistery.models.request.GetAllDonorsCountRequest;
import com.donornearme.UserRegistery.models.request.GetBloodGroupsRequest;
import com.donornearme.UserRegistery.models.request.GetDonorsAvailableRequest;
import com.donornearme.UserRegistery.models.request.SendMailToDonorRequest;
import com.donornearme.UserRegistery.models.response.GetAllDonorsCountResponse;
import com.donornearme.UserRegistery.models.response.GetBloodGroupsResponse;
import com.donornearme.UserRegistery.models.response.GetDonorsAvailableResponse;
import com.donornearme.UserRegistery.models.response.SendMailToDonorResponse;
import com.donornearme.UserRegistery.operations.DonorOperator;
import com.donornearme.UserRegistery.operations.MailOperation;
import com.donornearme.UserRegistery.operations.UserOperation;
import io.swagger.annotations.Api;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "Donor fetcher", description = "API's for donors")
public class DonorController extends BaseController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserOperation userUtility = new UserOperation();
    DonorOperator donorOperation = new DonorOperator();
    MailOperation mailOperation = new MailOperation();

    @RequestMapping(method = RequestMethod.POST, value = "/sendmailtodonor")
    public SendMailToDonorResponse sendMailToDonor(@RequestBody SendMailToDonorRequest sendMailToDonorRequest) throws Exception {
        return mailOperation.sendMailToDonor(sendMailToDonorRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getdonors/available")
    public GetDonorsAvailableResponse getDonorsAvailabe(GetDonorsAvailableRequest getDonorsAvailableRequest) throws Exception {
        return donorOperation.getDataRequested(getDonorsAvailableRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getdonorcount")
    public GetAllDonorsCountResponse getTotalDonorCount(GetAllDonorsCountRequest getAllDonorsCountRequest) throws Exception {
        return donorOperation.getAllDonorsCount(getAllDonorsCountRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getlist/bloodgroups")
    public GetBloodGroupsResponse getBloodGroupsList(GetBloodGroupsRequest getBloodGroupsRequest) throws Exception {
        return donorOperation.getAllBloodGroupsList(getBloodGroupsRequest);
    }


}
