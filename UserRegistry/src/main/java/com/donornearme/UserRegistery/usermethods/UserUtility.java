package com.donornearme.UserRegistery.usermethods;

import com.donornearme.UserRegistery.Common;
import com.donornearme.UserRegistery.model.request.*;
import com.donornearme.UserRegistery.model.response.*;
import com.donornearme.UserRegistery.usercontroller.BaseController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import java.io.File;
import java.util.*;

public class UserUtility extends BaseController {
    protected static final Logger logger = LogManager.getLogger(UserUtility.class);
    KafkaUtility kafkaUtility = new KafkaUtility();
    SqlUtility sqlUtility = new SqlUtility();

    public boolean userExists(String mailid) throws Exception {
        String sql = sqlUtility.userExistsSql(mailid);
        logger.info(Common.EXECUTING_SQL +sql);
        List<Map<String, Object>> count_map = sqlManager.renderSelectQuery(sql);
        if (count_map.size() > 0) {
            logger.info(Common.USER_EXISTS + true);
            return true;
        } else {
            logger.info(Common.USER_EXISTS + false);
            return false;
        }
    }

    public boolean correctPasswordEntered(AuthenticationRequest authenticationRequest) throws Exception {
        String sql = sqlUtility.correctPasswordEnteredSql(authenticationRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        List<Map<String, Object>> credMap = sqlManager.renderSelectQuery(sql);
        String password_from_table = (String) credMap.get(0).get("password");
        logger.info(Common.ORIGINAL_PASSWORD_IS + password_from_table);
        logger.info(Common.PASSWORD_BY_USER + authenticationRequest.getPassword());
        return password_from_table.equals(authenticationRequest.getPassword());

    }

    public boolean finishedOtpValidation(String mailid) throws Exception {
        String sql = sqlUtility.finishedOTPValidationSql(mailid);
        logger.info(Common.EXECUTING_SQL +sql);
        List<Map<String, Object>> count_map = sqlManager.renderSelectQuery(sql);
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
        UserUtility userUtility = new UserUtility();
        if (userExists(addUserRequest.getMailid())) {
            addUserResponse.setError(Common.USER_ALREADY_EXISTS);
        } else if (!userUtility.finishedOtpValidation(addUserRequest.getMailid())) {
            addUserResponse.setError(Common.VERIFY_OTP_FIRST);
        } else {
            logger.info(Common.ADDING_USER);
            String sql = sqlUtility.addUserToDbSql(addUserRequest);
            logger.info(Common.EXECUTING_SQL +sql);
            sqlManager.renderInsertQuery(sql);

            sql = sqlUtility.getBloodDonorCountWithPincode(addUserRequest);
            List<Map<String, Object>> donorcount_map = sqlManager.renderSelectQuery(sql);
            sql = sqlUtility.modifyBloodGroupCountTable(addUserRequest, donorcount_map.size() > 0);
            sqlManager.renderInsertQuery(sql);
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
        logger.info(Common.EXECUTING_SQL +sql);
        List<Map<String, Object>> otp_map = sqlManager.renderSelectQuery(sql);
        if (otp_map.size() == 0) {
            validateOTPResponse.setStatus("Please Wait!");
            return validateOTPResponse;
        }
        String otp_from_table = (String) otp_map.get(0).get("otp");
        logger.info(Common.ORIGINAL_OTP_IS + otp_from_table);
        logger.info(Common.OTP_ENTERED_BY_USER + validateOTPRequest.getOtp());
        if (validateOTPRequest.getOtp().equals(otp_from_table)) {
            sql = "update users.otp_validation set status = 'VALIDATED' where mailid = '" + validateOTPRequest.getMailid() + "';";
            logger.info(Common.EXECUTING_SQL +sql);
            sqlManager.renderInsertQuery(sql);
            sql = "update users.details set verification_status = 'VALIDATED' where mailid = '" + validateOTPRequest.getMailid() + "';";
            logger.info(Common.EXECUTING_SQL +sql);
            sqlManager.renderInsertQuery(sql);
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
        logger.info(Common.EXECUTING_SQL +sql);
        sqlManager.renderInsertQuery(sql);
        deleteUserResponse.setStatus(Common.DELETED_USER_SUCCESS_MSG);
        return deleteUserResponse;
    }

    public GetDonorsAvailableResponse getDataRequested(GetDonorsAvailableRequest getDonorsAvailableRequest) throws Exception {
        GetDonorsAvailableResponse getDonorsAvailableResponse = new GetDonorsAvailableResponse();
        logger.info(Common.FETCHING_REQUESTED_DETAILS);
        String sql = sqlUtility.getAvailableDonorsList(getDonorsAvailableRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        List<Map<String, Object>> requests_map = sqlManager.renderSelectQuery(sql);
        getDonorsAvailableResponse.setDonorsList(requests_map);
        return getDonorsAvailableResponse;
    }

    public UpdateUserResponse updataUser(String mailid, UpdateUserRequest updateUserRequest) throws Exception {
        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        updateUserResponse.setMailid(updateUserRequest.getMailid());
        String sql = sqlUtility.updateUserSql(updateUserRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        sqlManager.renderInsertQuery(sql);
        updateUserResponse.setStatus(Common.UPDATED_SUCCESFULLY);
        return updateUserResponse;
    }

//    private Boolean validatePincode(HashMap<String, Object> user_map) throws Exception {
//        List<Map<String, Object>> pincode_map = STRenderer.renderSelectTemplate(dbConnection.getConnection(), "validate_pincode", Common.STRING_TEMPLATES_PATH + File.separator + Common.BASIC_TEMPLATE, user_map);
//        return pincode_map.size() > 0;
//    }

    public GetCountriesListResponse getCountriesList(GetCountriesListRequest getCountriesListRequest) throws Exception {
        GetCountriesListResponse getCountriesListResponse = new GetCountriesListResponse();
        String sql = sqlUtility.getCountriesList(getCountriesListRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        Map<String, List<String>> pincode_map = sqlManager.renderSelectQueryReturnMapOfList(sql);
        getCountriesListResponse.setCountriesList(pincode_map);
        return getCountriesListResponse;
    }

    public GetStatesListResponse getStatesList(GetStatesListRequest getStatesListRequest) throws Exception {
        GetStatesListResponse getStatesListResponse = new GetStatesListResponse();
        String sql = sqlUtility.getStatesListSql(getStatesListRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        Map<String, List<String>> pincode_map = sqlManager.renderSelectQueryReturnMapOfList(sql);
        getStatesListResponse.setStatesList(pincode_map);
        return getStatesListResponse;
    }

    public GetDistrictsListResponse getDistrictsList(GetDistrictsListRequest getDistrictsListRequest) throws Exception {
        GetDistrictsListResponse getDistrictsListResponse = new GetDistrictsListResponse();
        String sql = sqlUtility.getDistrictsListSql(getDistrictsListRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        Map<String, List<String>> pincode_map = sqlManager.renderSelectQueryReturnMapOfList(sql);
        getDistrictsListResponse.setDistrictsList(pincode_map);
        return getDistrictsListResponse;
    }

    public GetTownsListResponse getTownsList(GetTownsListRequest getTownsListRequest) throws Exception {
        GetTownsListResponse getTownsListResponse = new GetTownsListResponse();
        String sql = sqlUtility.getTownsList(getTownsListRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        Map<String, List<String>> pincode_map = sqlManager.renderSelectQueryReturnMapOfList(sql);
        getTownsListResponse.setTownsList(pincode_map);
        return getTownsListResponse;
    }

    public GetCitiesListResponse getCitiesList(GetCitiesListRequest getCitiesListRequest) throws Exception {
        GetCitiesListResponse getCitiesListResponse = new GetCitiesListResponse();
        String sql = sqlUtility.getCitiesListSql(getCitiesListRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        Map<String, List<String>> pincode_map = sqlManager.renderSelectQueryReturnMapOfList(sql);
        getCitiesListResponse.setCitiesList(pincode_map);
        return getCitiesListResponse;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws Exception {
        HashMap<String, Object> status_map = new HashMap<>();
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setMailid(authenticationRequest.getMailid());
        if (!userExists(authenticationRequest.getMailid())) {
            status_map.put(Common.ERROR, "Email does not exist");
            authenticationResponse.setError("Email does not exist");
            return authenticationResponse;
        } else {
            if (correctPasswordEntered(authenticationRequest)) {
                String sql = "delete from users.login_sessions where mailid = '" + authenticationRequest.getMailid() + "' or CURRENT_TIMESTAMP - start_ts > INTERVAL '30' MINUTE;";
                logger.info(Common.EXECUTING_SQL +sql);
                sqlManager.renderInsertQuery(sql);

                sql = "insert into users.login_sessions (mailid,start_ts,end_ts) values ('" + authenticationRequest.getMailid() + "',CURRENT_TIMESTAMP,'9999-12-31 00:00:00');";
                logger.info(Common.EXECUTING_SQL +sql);
                sqlManager.renderInsertQuery(sql);

                sql = "select * from users.details d where mailid = '" + authenticationRequest.getMailid() + "'";
                logger.info(Common.EXECUTING_SQL +sql);
                
                List<Map<String, Object>> tempList = sqlManager.renderSelectQuery(sql);

                authenticationResponse.setUsername((String) tempList.get(0).get("username"));

                authenticationResponse.setStatus(Common.VALIDATION_SUCCESFUL);
                status_map.put(Common.STATUS, Common.VALIDATION_SUCCESFUL);
            } else {
                authenticationResponse.setError(Common.INCORRECT_PASSWORD);
                status_map.put(Common.ERROR, Common.INCORRECT_PASSWORD);
            }
            return authenticationResponse;
        }
    }


    public GetUserDetailsResponse getUserDetails(GetUserDetailsRequest getUserDetailsRequest) throws Exception {
        GetUserDetailsResponse getUserDetailsResponse = new GetUserDetailsResponse();
        String sql = sqlUtility.getUserDetails(getUserDetailsRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        List<Map<String, Object>> userDetails = sqlManager.renderSelectQuery(sql);
        getUserDetailsResponse.setUserDetails(userDetails);
        return getUserDetailsResponse;
    }

    public AddUserReviewResponse addUserReview(AddUserReviewRequest addUserReviewRequest) throws Exception {
        AddUserReviewResponse addUserReviewResponse = new AddUserReviewResponse();
        addUserReviewResponse.setMailid(addUserReviewRequest.getMailid());
        String sql = sqlUtility.addReviewSql(addUserReviewRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        sqlManager.renderInsertQuery(sql);
        int stars_given = addUserReviewRequest.getStars();
        if (stars_given >= 3) {
            addUserReviewResponse.setStatus(Common.ABOVE_THREE);
        } else {
            addUserReviewResponse.setStatus(Common.BELOW_THREE);
        }
        return addUserReviewResponse;
    }

    public boolean sessionExists(String mailid) throws Exception {
        String sql = sqlUtility.sessionExistsSql(mailid);
        logger.info(Common.EXECUTING_SQL +sql);
        List<Map<String, Object>> session_count_map = sqlManager.renderSelectQuery(sql);
        if (session_count_map.size() > 0) {
            logger.info(Common.SESSION_EXISTS + true);
            return true;
        } else {
            logger.info(Common.SESSION_EXISTS + false);
            return false;
        }
    }


    public GetAllDonorsCountResponse getAllDonorsCount(GetAllDonorsCountRequest getAllDonorsCountRequest) throws Exception {
        GetAllDonorsCountResponse getAllDonorsCountResponse = new GetAllDonorsCountResponse();
        String sql = sqlUtility.getAllDonorsCount();
        logger.info(Common.EXECUTING_SQL +sql);
        List<Map<String, Object>> donor_count_map = sqlManager.renderSelectQuery(sql);
        getAllDonorsCountResponse.setCount(donor_count_map.get(0).get(Common.COUNT).toString());
        return getAllDonorsCountResponse;
    }

    public GetBloodGroupsResponse getAllBloodGroupsList(GetBloodGroupsRequest getBloodGroupsRequest) throws Exception {
        GetBloodGroupsResponse getBloodGroupsResponse = new GetBloodGroupsResponse();
        String sql = sqlUtility.getBloodGroupsListSql(getBloodGroupsRequest);
        Map<String, List<String>> groups_map = sqlManager.renderSelectQueryReturnMapOfList(sql);
        getBloodGroupsResponse.setBloodGroupsList(groups_map);
        return getBloodGroupsResponse;
    }

    public String sendMessageToBot(String json) throws JSONException, JsonProcessingException {
        HashMap<String, Object> bot_input_msg_map = JSONUtility.jsonToMap(json);
        HashMap<String, Object> kafka_config = new HashMap<>();
        HashMap<String, Object> status_map = new HashMap<>();

        kafka_config.put(Common.KAFKA_BOOTSTRAP_SERVERS, "localhost:9092");
        try {
            KafkaProducer kafkaProducer = kafkaUtility.createKafkaProducer(kafka_config);

            kafkaProducer.send(new ProducerRecord("test", json));
            kafkaProducer.close();

            status_map.put(Common.STATUS, "Status sent to Bot");
            return getResponseOfBot();

        } catch (Exception e) {
            status_map.put(Common.STATUS, "Sending Message Failed");
            e.printStackTrace();
        }
        return "Bot under progress";
    }

    public String getResponseOfBot() throws Exception {
        HashMap<String, Object> kafka_config_map_consumer = new HashMap<String, Object>();
        ArrayList<String> topics = new ArrayList<String>();
        topics.add("response");
        kafka_config_map_consumer.put(Common.KAFKA_BOOTSTRAP_SERVERS, "localhost:9092");
        kafka_config_map_consumer.put(Common.GROUP_ID, "donor_bot_response");
        kafka_config_map_consumer.put(Common.TOPIC_LIST, topics);
        kafka_config_map_consumer.put(Common.TOPIC_CONTAINS_PATTERN, false);
        KafkaConsumer kafkaConsumer = getKafkaConsumer();
        String response;
        while (true) {
            logger.info("Waiting for Response");
            ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                response = record.value();
                logger.info("Chat Bot Response :" + response);
                kafkaConsumer.commitSync();
                kafkaConsumer.close();
                return response;
            }
        }
    }

    public KafkaConsumer getKafkaConsumer() throws Exception {
        HashMap<String, Object> kafka_config_map_consumer = new HashMap<String, Object>();
        ArrayList<String> topics = new ArrayList<String>();
        topics.add("response");
        kafka_config_map_consumer.put(Common.KAFKA_BOOTSTRAP_SERVERS, "localhost:9092");
        kafka_config_map_consumer.put(Common.GROUP_ID, "donor_bot_response");
        kafka_config_map_consumer.put(Common.TOPIC_LIST, topics);
        kafka_config_map_consumer.put(Common.TOPIC_CONTAINS_PATTERN, false);

        return kafkaUtility.createKafkaConsumer(kafka_config_map_consumer);
    }


    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest forgotPasswordRequest) throws Exception {
        ForgotPasswordResponse forgotPasswordResponse = new ForgotPasswordResponse();
        forgotPasswordResponse.setMailid(forgotPasswordRequest.getMailid());
        SendOTPToMailRequest sendOTPToMailRequest = new SendOTPToMailRequest();
        sendOTPToMailRequest.setMailid(forgotPasswordRequest.getMailid());
        MailUtility mailUtility = new MailUtility();
        SendOTPToMailResponse sendOTPToMailResponse = mailUtility.sendOTP(sendOTPToMailRequest);

        if (sendOTPToMailResponse.getStatus().equals("")) {
            forgotPasswordResponse.setError(sendOTPToMailResponse.getError());
        }else{
            forgotPasswordResponse.setStatus(sendOTPToMailResponse.getStatus());
        }
        return forgotPasswordResponse;
    }

    public UpdateForgotPasswordResponse updateforgotPassword(UpdateForgotPasswordRequest updateForgotPasswordRequest) {
        UpdateForgotPasswordResponse updateForgotPasswordResponse = new UpdateForgotPasswordResponse();
        updateForgotPasswordResponse.setMailid(updateForgotPasswordRequest.getMailid());
        String sql = sqlUtility.updateForgotPasswordSql(updateForgotPasswordRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        sqlManager.renderInsertQuery(sql);
        updateForgotPasswordResponse.setStatus(Common.UPDATED_SUCCESFULLY);
        return updateForgotPasswordResponse;
    }

    public ContactUsResponse contactUsViaMail(ContactUsRequest contactUsRequest) throws JsonProcessingException, UnirestException {
        MailUtility mailUtility = new MailUtility();
        return mailUtility.contactUsMail(contactUsRequest);
    }

    public ValidateUserViaLinkResponse validateOtpViaEmail(ValidateUserViaLinkRequest validateUserViaLinkRequest) {
        ValidateUserViaLinkResponse validateUserViaLinkResponse = new ValidateUserViaLinkResponse();
        validateUserViaLinkResponse.setMailid(validateUserViaLinkRequest.getMailid());
        String sql = sqlUtility.validateOtpSqlMail(validateUserViaLinkRequest);
        logger.info(Common.EXECUTING_SQL +sql);
        List<Map<String, Object>> otp_map = sqlManager.renderSelectQuery(sql);
        if (otp_map.size() == 0) {
            validateUserViaLinkResponse.setStatus("Please Wait!");
            return validateUserViaLinkResponse;
        }
        String otp_from_table = (String) otp_map.get(0).get("otp");
        logger.info(Common.ORIGINAL_OTP_IS + otp_from_table);
        logger.info(Common.OTP_ENTERED_BY_USER + validateUserViaLinkRequest.getOtp());
        if (validateUserViaLinkRequest.getOtp().equals(otp_from_table)) {
            sql = "update users.otp_validation set status = 'VALIDATED' where mailid = '" + validateUserViaLinkRequest.getMailid() + "';";
            logger.info(Common.EXECUTING_SQL +sql);
            sqlManager.renderInsertQuery(sql);
            sql = "update users.details set verification_status = 'VALIDATED' where mailid = '" + validateUserViaLinkRequest.getMailid() + "';";
            logger.info(Common.EXECUTING_SQL +sql);
            sqlManager.renderInsertQuery(sql);
            validateUserViaLinkResponse.setStatus(Common.OTP_VERIFIED_SUCCESFULLY);
        } else {
            validateUserViaLinkResponse.setError(Common.WRONG_OTP_ENTERED_MSG);
        }
        return validateUserViaLinkResponse;

    }

    public CheckOtpStatusResponse checkOtpStatus(CheckOtpStatusRequest checkOtpStatusRequest) {
        CheckOtpStatusResponse checkOtpStatusResponse = new CheckOtpStatusResponse();
        checkOtpStatusResponse.setMailid(checkOtpStatusRequest.getMailid());
        
        return checkOtpStatusResponse;
    }
}
