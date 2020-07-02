package com.donornearme.UserRegistery.controllers;

import com.donornearme.UserRegistery.models.request.*;
import com.donornearme.UserRegistery.models.response.*;
import com.donornearme.UserRegistery.operations.MailOperation;
import com.donornearme.UserRegistery.operations.UserOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "users", description = "API's for controlling user")
public class UserController extends BaseController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    private final UserOperation userOperation = new UserOperation();
    private final MailOperation mailOperation = new MailOperation();

    @RequestMapping(method = RequestMethod.POST, value = "/addUser")
    public AddUserResponse addUser(@RequestBody AddUserRequest addUserRequest) throws Exception {
        return userOperation.addUserToDb(addUserRequest);
    }

    @ApiOperation(value = "Send OTP")
    @RequestMapping(method = RequestMethod.POST, value = "/sendotp")
    public SendOTPToMailResponse sendOtpToMail(@RequestBody SendOTPToMailRequest sendOTPToMailRequest) throws Exception {
        return mailOperation.sendOTP(sendOTPToMailRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validateotp")
    public ValidateOTPResponse validateOtp(@RequestBody ValidateOTPRequest validateOTPRequest) throws Exception {
        return userOperation.validateOtp(validateOTPRequest);
    }

    @PutMapping("/updateUser")
    public UpdateUserResponse updateUser(String mailid, @RequestBody UpdateUserRequest updateUserRequest) throws Exception {
        return userOperation.updataUser(mailid, updateUserRequest);
    }

    @DeleteMapping("/deleteUser")
    public DeleteUserResponse deleteUser(DeleteUserRequest deleteUserRequest) throws Exception {
        return userOperation.deleteUser(deleteUserRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addreview")
    public AddUserReviewResponse addReviews(@RequestBody AddUserReviewRequest addUserReviewRequest) throws Exception {
        return userOperation.addUserReview(addUserReviewRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/forgotpassword")
    public ForgotPasswordResponse forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) throws Exception {
        return userOperation.forgotPassword(forgotPasswordRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/updatepassword")
    public UpdateForgotPasswordResponse updateForgottenPassword(@RequestBody UpdateForgotPasswordRequest updateForgotPasswordRequest) {
        return userOperation.updateforgotPassword(updateForgotPasswordRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/contactus")
    public ContactUsResponse contactUs(@RequestBody ContactUsRequest contactUsRequest) throws Exception {
        return userOperation.contactUsViaMail(contactUsRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validatemailvialink")
    public ValidateUserViaLinkResponse validateMailViaLink(ValidateUserViaLinkRequest validateUserViaLinkRequest) throws Exception {
        return userOperation.validateOtpViaEmail(validateUserViaLinkRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/checkotpstatus")
    public CheckOtpStatusResponse validateMailViaLink(CheckOtpStatusRequest checkOtpStatusRequest) throws Exception {
        return userOperation.checkOtpStatus(checkOtpStatusRequest);
    }

    @RequestMapping(method = RequestMethod.GET, value = "getdetails/user")
    public GetUserDetailsResponse getUserDetails(GetUserDetailsRequest getUserDetailsRequest) throws Exception {
        return userOperation.getUserDetails(getUserDetailsRequest);
    }


}
