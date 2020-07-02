package com.donornearme.UserRegistery.operations;

import com.donornearme.UserRegistery.Common;
import com.donornearme.UserRegistery.controllers.BaseController;
import com.donornearme.UserRegistery.models.request.*;
import com.donornearme.UserRegistery.models.response.*;
import com.donornearme.UserRegistery.sqls.SqlUtility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class UserOperation extends BaseController {
    protected static final Logger logger = LogManager.getLogger(UserOperation.class);
    SqlUtility sqlUtility = new SqlUtility();

    public boolean userExists(String mailid) throws Exception {
        String sql = sqlUtility.userExistsSql(mailid);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> count_map = sqlManager.runSelectQuery(sql);
        if (count_map.size() > 0) {
            logger.info(Common.USER_EXISTS + true);
            return true;
        } else {
            logger.info(Common.USER_EXISTS + false);
            return false;
        }
    }

    public boolean finishedOtpValidation(String mailid) throws Exception {
        String sql = sqlUtility.finishedOTPValidationSql(mailid);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> count_map = sqlManager.runSelectQuery(sql);
        if (count_map.size() > 0) {
            logger.info(Common.OTP_STATUS + true);
            return true;
        } else {
            logger.info(Common.OTP_STATUS + false);
            return false;
        }
    }

    public AddUserResponse addUserToDb(AddUserRequest addUserRequest) throws Exception {
        AddUserResponse addUserResponse = new AddUserResponse();
        UserOperation userUtility = new UserOperation();
        if (userExists(addUserRequest.getMailid())) {
            addUserResponse.setError(Common.USER_ALREADY_EXISTS);
        } else if (!userUtility.finishedOtpValidation(addUserRequest.getMailid())) {
            addUserResponse.setError(Common.VERIFY_OTP_FIRST);
        } else {
            logger.info(Common.ADDING_USER);
            String sql = sqlUtility.addUserToDbSql(addUserRequest);
            logger.info(Common.EXECUTING_SQL + sql);
            sqlManager.runInsertQuery(sql);

            sql = sqlUtility.getBloodDonorCountWithPincode(addUserRequest);
            List<Map<String, Object>> donorcount_map = sqlManager.runSelectQuery(sql);
            sql = sqlUtility.modifyBloodGroupCountTable(addUserRequest, donorcount_map.size() > 0);
            sqlManager.runInsertQuery(sql);
            addUserResponse.setStatus(Common.ADDED_USER_SUCCESFULLY);
        }
        return addUserResponse;
    }

    private String generateOTP() {
        Random rnd = new Random();
        return String.format(Common.OTP_GEN, rnd.nextInt(999999));
    }

    public ValidateOTPResponse validateOtp(ValidateOTPRequest validateOTPRequest) throws Exception {
        ValidateOTPResponse validateOTPResponse = new ValidateOTPResponse();
        validateOTPResponse.setMailid(validateOTPRequest.getMailid());

        String sql = sqlUtility.validateOtpSql(validateOTPRequest);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> otp_map = sqlManager.runSelectQuery(sql);
        if (otp_map.size() == 0) {
            validateOTPResponse.setStatus("Please Wait!");
            return validateOTPResponse;
        }
        String otp_from_table = (String) otp_map.get(0).get("otp");
        logger.info(Common.ORIGINAL_OTP_IS + otp_from_table);
        logger.info(Common.OTP_ENTERED_BY_USER + validateOTPRequest.getOtp());
        if (validateOTPRequest.getOtp().equals(otp_from_table)) {
            sql = "update users.otp_validation set status = 'VALIDATED' where mailid = '" + validateOTPRequest.getMailid() + "';";
            logger.info(Common.EXECUTING_SQL + sql);
            sqlManager.runInsertQuery(sql);
            sql = "update users.details set verification_status = 'VALIDATED' where mailid = '" + validateOTPRequest.getMailid() + "';";
            logger.info(Common.EXECUTING_SQL + sql);
            sqlManager.runInsertQuery(sql);
            validateOTPResponse.setStatus(Common.OTP_VERIFIED_SUCCESFULLY);
        } else {
            validateOTPResponse.setError(Common.WRONG_OTP_ENTERED_MSG);
        }
        return validateOTPResponse;
    }

    public DeleteUserResponse deleteUser(DeleteUserRequest deleteUserRequest) throws Exception {
        DeleteUserResponse deleteUserResponse = new DeleteUserResponse();
        deleteUserResponse.setMailid(deleteUserRequest.getMailid());
        logger.info(Common.DELETING_USER);
        String sql = sqlUtility.deleteUserSql(deleteUserRequest.getMailid());
        logger.info(Common.EXECUTING_SQL + sql);
        sqlManager.runInsertQuery(sql);
        deleteUserResponse.setStatus(Common.DELETED_USER_SUCCESS_MSG);
        return deleteUserResponse;
    }


    public UpdateUserResponse updataUser(String mailid, UpdateUserRequest updateUserRequest) throws Exception {
        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        updateUserResponse.setMailid(updateUserRequest.getMailid());
        String sql = sqlUtility.updateUserSql(updateUserRequest);
        logger.info(Common.EXECUTING_SQL + sql);
        sqlManager.runInsertQuery(sql);
        updateUserResponse.setStatus(Common.UPDATED_SUCCESFULLY);
        return updateUserResponse;
    }

//    private Boolean validatePincode(HashMap<String, Object> user_map) throws Exception {
//        List<Map<String, Object>> pincode_map = STruner.runSelectTemplate(dbConnection.getConnection(), "validate_pincode", Common.STRING_TEMPLATES_PATH + File.separator + Common.BASIC_TEMPLATE, user_map);
//        return pincode_map.size() > 0;
//    }


    public GetUserDetailsResponse getUserDetails(GetUserDetailsRequest getUserDetailsRequest) throws Exception {
        GetUserDetailsResponse getUserDetailsResponse = new GetUserDetailsResponse();
        String sql = sqlUtility.getUserDetails(getUserDetailsRequest);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> userDetails = sqlManager.runSelectQuery(sql);
        getUserDetailsResponse.setUserDetails(userDetails);
        return getUserDetailsResponse;
    }

    public AddUserReviewResponse addUserReview(AddUserReviewRequest addUserReviewRequest) throws Exception {
        AddUserReviewResponse addUserReviewResponse = new AddUserReviewResponse();
        addUserReviewResponse.setMailid(addUserReviewRequest.getMailid());
        String sql = sqlUtility.addReviewSql(addUserReviewRequest);
        logger.info(Common.EXECUTING_SQL + sql);
        sqlManager.runInsertQuery(sql);
        int stars_given = addUserReviewRequest.getStars();
        if (stars_given >= 3) {
            addUserReviewResponse.setStatus(Common.ABOVE_THREE);
        } else {
            addUserReviewResponse.setStatus(Common.BELOW_THREE);
        }
        return addUserReviewResponse;
    }




    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws Exception {
        ForgotPasswordResponse forgotPasswordResponse = new ForgotPasswordResponse();
        forgotPasswordResponse.setMailid(forgotPasswordRequest.getMailid());
        SendOTPToMailRequest sendOTPToMailRequest = new SendOTPToMailRequest();
        sendOTPToMailRequest.setMailid(forgotPasswordRequest.getMailid());
        MailOperation mailOperation = new MailOperation();
        SendOTPToMailResponse sendOTPToMailResponse = mailOperation.sendOTP(sendOTPToMailRequest);

        if (sendOTPToMailResponse.getStatus().equals("")) {
            forgotPasswordResponse.setError(sendOTPToMailResponse.getError());
        } else {
            forgotPasswordResponse.setStatus(sendOTPToMailResponse.getStatus());
        }
        return forgotPasswordResponse;
    }

    public UpdateForgotPasswordResponse updateforgotPassword(UpdateForgotPasswordRequest updateForgotPasswordRequest) {
        UpdateForgotPasswordResponse updateForgotPasswordResponse = new UpdateForgotPasswordResponse();
        updateForgotPasswordResponse.setMailid(updateForgotPasswordRequest.getMailid());
        String sql = sqlUtility.updateForgotPasswordSql(updateForgotPasswordRequest);
        logger.info(Common.EXECUTING_SQL + sql);
        sqlManager.runInsertQuery(sql);
        updateForgotPasswordResponse.setStatus(Common.UPDATED_SUCCESFULLY);
        return updateForgotPasswordResponse;
    }

    public ContactUsResponse contactUsViaMail(ContactUsRequest contactUsRequest) throws JsonProcessingException, UnirestException {
        MailOperation mailOperation = new MailOperation();
        return mailOperation.contactUsMail(contactUsRequest);
    }

    public ValidateUserViaLinkResponse validateOtpViaEmail(ValidateUserViaLinkRequest validateUserViaLinkRequest) {
        ValidateUserViaLinkResponse validateUserViaLinkResponse = new ValidateUserViaLinkResponse();
        validateUserViaLinkResponse.setMailid(validateUserViaLinkRequest.getMailid());
        String sql = sqlUtility.validateOtpSqlMail(validateUserViaLinkRequest);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> otp_map = sqlManager.runSelectQuery(sql);
        if (otp_map.size() == 0) {
            validateUserViaLinkResponse.setStatus("Please Wait!");
            return validateUserViaLinkResponse;
        }
        String otp_from_table = (String) otp_map.get(0).get("otp");
        logger.info(Common.ORIGINAL_OTP_IS + otp_from_table);
        logger.info(Common.OTP_ENTERED_BY_USER + validateUserViaLinkRequest.getOtp());
        if (validateUserViaLinkRequest.getOtp().equals(otp_from_table)) {
            sql = "update users.otp_validation set status = 'VALIDATED' where mailid = '" + validateUserViaLinkRequest.getMailid() + "';";
            logger.info(Common.EXECUTING_SQL + sql);
            sqlManager.runInsertQuery(sql);
            sql = "update users.details set verification_status = 'VALIDATED' where mailid = '" + validateUserViaLinkRequest.getMailid() + "';";
            logger.info(Common.EXECUTING_SQL + sql);
            sqlManager.runInsertQuery(sql);
            validateUserViaLinkResponse.setStatus(Common.OTP_VERIFIED_SUCCESFULLY);
        } else {
            validateUserViaLinkResponse.setError(Common.WRONG_OTP_ENTERED_MSG);
        }
        return validateUserViaLinkResponse;

    }

    public CheckOtpStatusResponse checkOtpStatus(CheckOtpStatusRequest checkOtpStatusRequest) {
        CheckOtpStatusResponse checkOtpStatusResponse = new CheckOtpStatusResponse();
        checkOtpStatusResponse.setMailid(checkOtpStatusRequest.getMailid());
        String sql = sqlUtility.getOtpStatus(checkOtpStatusRequest);
        List<Map<String, Object>> checkmap = sqlManager.runSelectQuery(sql);
        checkOtpStatusResponse.setStatus((String) checkmap.get(0).get("status"));
        return checkOtpStatusResponse;
    }
}
