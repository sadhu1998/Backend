package com.epsilon.donornearme.operations;

import com.epsilon.donornearme.Common;
import com.epsilon.donornearme.controllers.BaseController;
import com.epsilon.donornearme.dao.UserQueries;
import com.epsilon.donornearme.models.request.*;
import com.epsilon.donornearme.models.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class UserOperator extends BaseController {
    protected static final Logger logger = LogManager.getLogger(UserOperator.class);
    UserQueries userQueries = new UserQueries();

    public boolean finishedOtpValidation(String mailid) throws Exception {
        String sql = userQueries.finishedOTPValidationSql(mailid);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> count_map = sqlRenderer.runSelectQuery(sql);
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
        UserOperator userUtility = new UserOperator();
         if (!userUtility.finishedOtpValidation(addUserRequest.getMailid())) {
            addUserResponse.setError(Common.VERIFY_OTP_FIRST);
        } else {
            logger.info(Common.ADDING_USER);
            String sql = userQueries.addUserToDbDetailsSql(addUserRequest);
            logger.info(Common.EXECUTING_SQL + sql);
            sqlRenderer.runInsertQuery(sql);

            sql = userQueries.addUserToDbCredssSql(addUserRequest);
            logger.info(Common.EXECUTING_SQL + sql);
            sqlRenderer.runInsertQuery(sql);

            sql = userQueries.addUserToDbAlertsSql(addUserRequest);
            logger.info(Common.EXECUTING_SQL + sql);
            sqlRenderer.runInsertQuery(sql);

            sql = userQueries.getBloodDonorCountWithPincode(addUserRequest);
            logger.info(Common.EXECUTING_SQL + sql);
            List<Map<String, Object>> donorcount_map = sqlRenderer.runSelectQuery(sql);

            sql = userQueries.modifyBloodGroupCountTable(addUserRequest, donorcount_map.size() > 0);
            logger.info(Common.EXECUTING_SQL + sql);
            sqlRenderer.runInsertQuery(sql);
            addUserResponse.setStatus(Common.ADDED_USER_SUCCESFULLY);
        }
        return addUserResponse;
    }

    private String generateOTP() {
        Random rnd = new Random();

        //        return String.format(Common.OTP_GEN, rnd.nextInt(999999));
        return "000000";
    }

    public ValidateOTPResponse validateOtp(ValidateOTPRequest validateOTPRequest) throws Exception {
        ValidateOTPResponse validateOTPResponse = new ValidateOTPResponse();
        validateOTPResponse.setMailid(validateOTPRequest.getMailid());

        String sql = userQueries.validateOtpSql(validateOTPRequest);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> otp_map = sqlRenderer.runSelectQuery(sql);
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
            sqlRenderer.runInsertQuery(sql);
            sql = "update users.details set verification_status = 'VALIDATED' where mailid = '" + validateOTPRequest.getMailid() + "';";
            logger.info(Common.EXECUTING_SQL + sql);
            sqlRenderer.runInsertQuery(sql);
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
        String sql = userQueries.deleteUserSql(deleteUserRequest.getMailid());
        logger.info(Common.EXECUTING_SQL + sql);
        sqlRenderer.runInsertQuery(sql);
        deleteUserResponse.setStatus(Common.DELETED_USER_SUCCESS_MSG);
        return deleteUserResponse;
    }


    public UpdateUserResponse updataUser(String mailid, UpdateUserRequest updateUserRequest) throws Exception {
        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        updateUserResponse.setMailid(updateUserRequest.getMailid());
        if(mailid.equals(updateUserRequest.getMailid())) {

            String sql = userQueries.updateUserSql(updateUserRequest);
            logger.info(Common.EXECUTING_SQL + sql);
            sqlRenderer.runInsertQuery(sql);
            updateUserResponse.setStatus(Common.UPDATED_SUCCESFULLY);
            return updateUserResponse;
        }
        else{
            updateUserResponse.setError("UnAuthorized");
            return updateUserResponse;
        }
    }

//    private Boolean validatePincode(HashMap<String, Object> user_map) throws Exception {
//        List<Map<String, Object>> pincode_map = STruner.runSelectTemplate(dbConnection.getConnection(), "validate_pincode", Common.STRING_TEMPLATES_PATH + File.separator + Common.BASIC_TEMPLATE, user_map);
//        return pincode_map.size() > 0;
//    }


    public GetUserDetailsResponse getUserDetails(GetUserDetailsRequest getUserDetailsRequest) throws Exception {
        GetUserDetailsResponse getUserDetailsResponse = new GetUserDetailsResponse();
        String sql = userQueries.getUserDetails(getUserDetailsRequest);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> userDetails = sqlRenderer.runSelectQuery(sql);
        getUserDetailsResponse.setUserDetails(userDetails);
        return getUserDetailsResponse;
    }

    public AddUserReviewResponse addUserReview(AddUserReviewRequest addUserReviewRequest) throws Exception {
        AddUserReviewResponse addUserReviewResponse = new AddUserReviewResponse();
        addUserReviewResponse.setMailid(addUserReviewRequest.getMailid());
        String sql = userQueries.addReviewSql(addUserReviewRequest);
        logger.info(Common.EXECUTING_SQL + sql);
        sqlRenderer.runInsertQuery(sql);
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
        String sql = userQueries.updateForgotPasswordSql(updateForgotPasswordRequest);
        logger.info(Common.EXECUTING_SQL + sql);
        sqlRenderer.runInsertQuery(sql);
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
        String sql = userQueries.validateOtpSqlMail(validateUserViaLinkRequest);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> otp_map = sqlRenderer.runSelectQuery(sql);
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
            sqlRenderer.runInsertQuery(sql);
            sql = "update users.details set verification_status = 'VALIDATED' where mailid = '" + validateUserViaLinkRequest.getMailid() + "';";
            logger.info(Common.EXECUTING_SQL + sql);
            sqlRenderer.runInsertQuery(sql);
            validateUserViaLinkResponse.setStatus(Common.OTP_VERIFIED_SUCCESFULLY);
        } else {
            validateUserViaLinkResponse.setError(Common.WRONG_OTP_ENTERED_MSG);
        }
        return validateUserViaLinkResponse;

    }

    public CheckOtpStatusResponse checkOtpStatus(CheckOtpStatusRequest checkOtpStatusRequest) {
        CheckOtpStatusResponse checkOtpStatusResponse = new CheckOtpStatusResponse();
        checkOtpStatusResponse.setMailid(checkOtpStatusRequest.getMailid());
        String sql = userQueries.getOtpStatus(checkOtpStatusRequest);
        List<Map<String, Object>> checkmap = sqlRenderer.runSelectQuery(sql);
        checkOtpStatusResponse.setStatus((String) checkmap.get(0).get("status"));
        return checkOtpStatusResponse;
    }

    public UserQueryResponse userQuery(UserQueryRequest userQueryRequest) throws JsonProcessingException, UnirestException {
        MailOperation mailOperation = new MailOperation();
        return mailOperation.userQueryMail(userQueryRequest);
    }

    public ReviewSubmittedStatusResponse getReviewStatus(ReviewSubmittedStatusRequest reviewSubmittedStatusRequest) {
        ReviewSubmittedStatusResponse reviewSubmittedStatusResponse = new ReviewSubmittedStatusResponse();
        reviewSubmittedStatusResponse.setMailid(reviewSubmittedStatusRequest.getMailid());
        String sql = userQueries.getReviewStatusSql(reviewSubmittedStatusRequest);
        List<Map<String,Object>> statusMap = sqlRenderer.runSelectQuery(sql);
        reviewSubmittedStatusResponse.setStatus((String) statusMap.get(0).get("status"));
        return reviewSubmittedStatusResponse;
    }

    public GetBloodNeedResponse getRequestList(GetBloodNeedRequest getBloodNeedRequest) {
        GetBloodNeedResponse getBloodNeedResponse = new GetBloodNeedResponse();
        getBloodNeedResponse.setMailid(getBloodNeedRequest.getMailid());
        String sql = userQueries.getBloodRequestDetails(getBloodNeedRequest);
        List<Map<String,Object>> requestsMap = sqlRenderer.runSelectQuery(sql);
        getBloodNeedResponse.setRequestsList(requestsMap);
        getBloodNeedResponse.setStatus("Success");
        return getBloodNeedResponse;
    }

    public GetOtpOfUserResponse getUserOtp(GetOtpOfUserRequest getOtpOfUserRequest) {
        GetOtpOfUserResponse getOtpOfUserResponse = new GetOtpOfUserResponse();
        getOtpOfUserResponse.setMailid(getOtpOfUserRequest.getMailid());
        String sql = userQueries.getOtpOfUser(getOtpOfUserRequest);
        List<Map<String, Object>> userOtpMap = sqlRenderer.runSelectQuery(sql);
        String otp = userOtpMap.get(0).get("otp").toString();
        getOtpOfUserResponse.setOtp(otp);
        getOtpOfUserResponse.setStatus("Got Otp");
        return getOtpOfUserResponse;
    }

    public UpdateFcmTokenResponse updateFcmInDB(UpdateFcmTokenRequest updateFcmTokenRequest) {
        UpdateFcmTokenResponse updateFcmTokenResponse = new UpdateFcmTokenResponse();
        updateFcmTokenResponse.setMailid(updateFcmTokenRequest.getMailid());
        String sql = userQueries.updateFcmQuery(updateFcmTokenRequest);
        logger.info(sql);
        sqlRenderer.runInsertQuery(sql);
        updateFcmTokenResponse.setStatus("Success");
        return updateFcmTokenResponse;
    }

    public GetFcmTokenResponse getUserFcmToken(GetFcmTokenRequest getFcmTokenRequest) {
        GetFcmTokenResponse getFcmTokenResponse = new GetFcmTokenResponse();
        getFcmTokenResponse.setMailid(getFcmTokenRequest.getMailid());
        String sql = userQueries.getFcmFromDb(getFcmTokenRequest);
        List<Map<String, Object>> fcmMap = sqlRenderer.runSelectQuery(sql);
        if (fcmMap.size()==0){
            getFcmTokenResponse.setError("Error Occured");
            return getFcmTokenResponse;
        }
        String fcmToken = fcmMap.get(0).get("fcmtoken").toString();
        if (fcmToken.equals("")){
            getFcmTokenResponse.setError("Error Occured");
        }else{
            getFcmTokenResponse.setFcmToken(fcmToken);
            getFcmTokenResponse.setStatus("Success");
        }
        return getFcmTokenResponse;
    }

    public GetGlobalBloodResponse getGlobalRequestList(GetGlobalBloodRequest getGlobalBloodRequest) {
        GetGlobalBloodResponse getGlobalBloodResponse = new GetGlobalBloodResponse();
        String sql = userQueries.getGlobalBloodRequestedDetails(getGlobalBloodRequest);
        List<Map<String,Object>> requestsMap = sqlRenderer.runSelectQuery(sql);
        getGlobalBloodResponse.setRequestsList(requestsMap);
        return getGlobalBloodResponse;
    }
}
