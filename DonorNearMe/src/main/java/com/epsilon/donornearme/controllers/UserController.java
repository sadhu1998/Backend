package com.epsilon.donornearme.controllers;

import com.epsilon.donornearme.operations.MailOperation;
import com.epsilon.donornearme.operations.UserOperator;
import com.epsilon.donornearme.models.request.*;
import com.epsilon.donornearme.models.response.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "users", description = "API's for controlling user")
public class UserController extends BaseController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserOperator userOperator = new UserOperator();
    private final MailOperation mailOperation = new MailOperation();

    @RequestMapping(method = RequestMethod.POST, value = "/addUser")
    public AddUserResponse addUser(@RequestBody AddUserRequest addUserRequest) throws Exception {
        return userOperator.addUserToDb(addUserRequest);
    }

    @ApiOperation(value = "Send OTP")
    @RequestMapping(method = RequestMethod.POST, value = "/sendotp")
    public SendOTPToMailResponse sendOtpToMail(@RequestBody SendOTPToMailRequest sendOTPToMailRequest) throws Exception {
        return mailOperation.sendOTP(sendOTPToMailRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validateotp")
    public ValidateOTPResponse validateOtp(@RequestBody ValidateOTPRequest validateOTPRequest) throws Exception {
        return userOperator.validateOtp(validateOTPRequest);
    }

    @PutMapping("/updateUser")
    public UpdateUserResponse updateUser(String mailid, @RequestBody UpdateUserRequest updateUserRequest) throws Exception {
        return userOperator.updataUser(mailid, updateUserRequest);
    }

    @DeleteMapping("/deleteUser")
    public DeleteUserResponse deleteUser(DeleteUserRequest deleteUserRequest) throws Exception {
        return userOperator.deleteUser(deleteUserRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addreview")
    public AddUserReviewResponse addReviews(@RequestBody AddUserReviewRequest addUserReviewRequest) throws Exception {
        return userOperator.addUserReview(addUserReviewRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/forgotpassword")
    public ForgotPasswordResponse forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) throws Exception {
        return userOperator.forgotPassword(forgotPasswordRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/updatepassword")
    public UpdateForgotPasswordResponse updateForgottenPassword(@RequestBody UpdateForgotPasswordRequest updateForgotPasswordRequest) {
        return userOperator.updateforgotPassword(updateForgotPasswordRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/contactus")
    public ContactUsResponse contactUs(@RequestBody ContactUsRequest contactUsRequest) throws Exception {
        return userOperator.contactUsViaMail(contactUsRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validatemailvialink")
    public ValidateUserViaLinkResponse validateMailViaLink(ValidateUserViaLinkRequest validateUserViaLinkRequest) throws Exception {
        return userOperator.validateOtpViaEmail(validateUserViaLinkRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/checkotpstatus")
    public CheckOtpStatusResponse validateMailViaLink(CheckOtpStatusRequest checkOtpStatusRequest) throws Exception {
        return userOperator.checkOtpStatus(checkOtpStatusRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/test")
    public String testingApi() {
        return "Welcome";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/test")
    public TestResponse userQuery(@RequestBody TestRequest testRequest) throws Exception {
        TestResponse testResponse = new TestResponse();
        testResponse.setUser(testRequest.getUser());
        return testResponse;
    }

    @RequestMapping(method = RequestMethod.GET, value = "getdetails/user")
    public GetUserDetailsResponse getUserDetails(GetUserDetailsRequest getUserDetailsRequest) throws Exception {
        return userOperator.getUserDetails(getUserDetailsRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/userquery")
    public UserQueryResponse userQuery(@RequestBody UserQueryRequest userQueryRequest) throws Exception {
        return userOperator.userQuery(userQueryRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/reviewsubmitstatus")
    public ReviewSubmittedStatusResponse getReviewStatus(ReviewSubmittedStatusRequest reviewSubmittedStatusRequest) throws Exception {
        return userOperator.getReviewStatus(reviewSubmittedStatusRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getbloodrequests")
    public GetBloodNeedResponse getBloodRequests(GetBloodNeedRequest getBloodNeedRequest) throws Exception {
        return userOperator.getRequestList(getBloodNeedRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getbloodrequests/global")
    public GetGlobalBloodResponse getGlobalBloodRequests(GetGlobalBloodRequest getGlobalBloodRequest) throws Exception {
        return userOperator.getGlobalRequestList(getGlobalBloodRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getuserotp")
    public GetOtpOfUserResponse getOtpOfUser(GetOtpOfUserRequest getOtpOfUserRequest) throws Exception {
        return userOperator.getUserOtp(getOtpOfUserRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/updatefcm/login")
    public UpdateFcmTokenResponse updateFcmToken(@RequestBody UpdateFcmTokenRequest updateFcmTokenRequest) throws Exception {
        return userOperator.updateFcmInDB(updateFcmTokenRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/user/fcmtoken")
    public GetFcmTokenResponse getOtpOfUser(GetFcmTokenRequest getFcmTokenRequest) throws Exception {
        return userOperator.getUserFcmToken(getFcmTokenRequest);
    }





}
