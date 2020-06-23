package com.donornearme.UserRegistery.usercontroller;

import com.donornearme.UserRegistery.model.request.AddUserReviewRequest;
import com.donornearme.UserRegistery.model.request.SendMailToDonorRequest;
import com.donornearme.UserRegistery.model.request.SendOTPToMailRequest;
import com.donornearme.UserRegistery.model.response.AddUserResponse;
import com.donornearme.UserRegistery.model.response.AddUserReviewResponse;
import com.donornearme.UserRegistery.model.response.SendMailToDonorResponse;
import com.donornearme.UserRegistery.model.response.SendOTPToMailResponse;
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

    @RequestMapping(method = RequestMethod.POST, value = "/addreview")
    public AddUserReviewResponse addReviews(@RequestBody AddUserReviewRequest addUserReviewRequest) throws Exception {
        return userUtility.addUserReview(addUserReviewRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/sendmailtodonor")
    public SendMailToDonorResponse sendMailToDonor(@RequestBody SendMailToDonorRequest sendMailToDonorRequest) throws Exception {
        return mailUtility.sendMailToDonor(sendMailToDonorRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "getdetails/{country}/{state}/{district}/{city}/{town}/{bloodgroup}")
    public String getDonors(@PathVariable String country, @PathVariable String state, @PathVariable String district, @PathVariable String city, @PathVariable String town, @PathVariable String bloodgroup) throws Exception {
        return userUtility.getDataRequested(country, state, district, city, town, bloodgroup);
    }

    @RequestMapping(method = RequestMethod.GET, value = "getuserdetails/{mailid}")
    public String getUserDetails(@PathVariable String mailid) throws Exception {
        return userUtility.getUserDetails(mailid);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getdonorcount")
    public String getTotalDonorCount() throws Exception {
        return userUtility.getAllDonorsCount();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getbloodgroupslist")
    public String getBloodGroupsList() throws Exception {
        return userUtility.getAllBloodGroupsList();
    }

}
