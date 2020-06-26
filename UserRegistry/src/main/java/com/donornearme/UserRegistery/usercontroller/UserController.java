package com.donornearme.UserRegistery.usercontroller;

import com.donornearme.UserRegistery.model.request.*;
import com.donornearme.UserRegistery.model.response.*;
import com.donornearme.UserRegistery.usermethods.MailUtility;
import com.donornearme.UserRegistery.usermethods.UserUtility;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "users", description = "Endpoint for user management")
public class UserController extends BaseController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    private UserUtility userUtility = new UserUtility();
    private MailUtility mailUtility = new MailUtility();

    @RequestMapping(method = RequestMethod.POST, value = "/addUser")
    public AddUserResponse addUser(@RequestBody AddUserRequest addUserRequest) throws Exception {
        return userUtility.addUserToDb(addUserRequest);
    }

    @ApiOperation(value = "Send OTP")
    @RequestMapping(method = RequestMethod.POST, value = "/sendotp")
    public SendOTPToMailResponse sendOtpToMail(@RequestBody SendOTPToMailRequest sendOTPToMailRequest) throws Exception {
        return mailUtility.sendOTP(sendOTPToMailRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validateotp")
    public ValidateOTPResponse validateOtp(@RequestBody ValidateOTPRequest validateOTPRequest) throws Exception {
        return userUtility.validateOtp(validateOTPRequest);
    }

//    @RequestMapping(method = RequestMethod.POST, value = "/forgotpassword")
//    public ForgotPasswordResponse forgetPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) throws Exception {
//        return mailUtility.sendOTPForgotPassword(forgotPasswordRequest);
//    }

    @PutMapping("/updateUser")
    public UpdateUserResponse updateUser(@RequestParam(required = true) String mailid, @RequestBody UpdateUserRequest updateUserRequest) throws Exception {
        return userUtility.updataUser(mailid, updateUserRequest);
    }

    @DeleteMapping("/deleteUser")
    public DeleteUserResponse deleteUser(DeleteUserRequest deleteUserRequest) throws Exception {
        return userUtility.deleteUser(deleteUserRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addreview")
    public AddUserReviewResponse addReviews(@RequestBody AddUserReviewRequest addUserReviewRequest) throws Exception {
        return userUtility.addUserReview(addUserReviewRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/forgotpassword")
    public ForgotPasswordResponse forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest) throws Exception {
        return userUtility.forgotPassword(forgotPasswordRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/updatepassword")
    public UpdateForgotPasswordResponse updateForgottenPassword(@RequestBody UpdateForgotPasswordRequest updateForgotPasswordRequest) throws Exception {
        return userUtility.updateforgotPassword(updateForgotPasswordRequest);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/contactus")
    public ContactUsResponse contactUs(@RequestBody ContactUsRequest contactUsRequest) throws Exception {
        return userUtility.contactUsViaMail(contactUsRequest);
    }
}
