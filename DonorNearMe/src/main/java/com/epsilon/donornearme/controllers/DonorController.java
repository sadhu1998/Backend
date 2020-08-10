package com.epsilon.donornearme.controllers;

import com.epsilon.donornearme.models.request.*;
import com.epsilon.donornearme.models.response.*;
import com.epsilon.donornearme.operations.DonorOperator;
import com.epsilon.donornearme.operations.MailOperation;
import com.epsilon.donornearme.operations.UserOperator;
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
    private final UserOperator userUtility = new UserOperator();
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

    @RequestMapping(method = RequestMethod.GET, value = "/list/bloodgroups")
    public GetBloodGroupsResponse getBloodGroupsList(GetBloodGroupsRequest getBloodGroupsRequest) throws Exception {
        return donorOperation.getAllBloodGroupsList(getBloodGroupsRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/donorrequest")
    public UserDonorLogResponse insertRequestLog(@RequestBody UserDonorLogRequest userDonorLogRequest){
        return donorOperation.insertIntoRequestsLog(userDonorLogRequest);
    }


}
