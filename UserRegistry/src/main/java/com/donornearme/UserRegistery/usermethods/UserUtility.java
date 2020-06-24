package com.donornearme.UserRegistery.usermethods;

import com.donornearme.UserRegistery.Common;
import com.donornearme.UserRegistery.model.request.*;
import com.donornearme.UserRegistery.model.response.*;
import com.donornearme.UserRegistery.usercontroller.BaseController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.modak.utils.JSONUtils;
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

//    hello

    public boolean userExists(String mailid) throws Exception {
        String sql = sqlUtility.userExistsSql(mailid);
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
        List<Map<String, Object>> credMap = sqlManager.renderSelectQuery(sql);
        String password_from_table = (String) credMap.get(0).get("password");
        logger.info(Common.ORIGINAL_PASSWORD_IS + password_from_table);
        logger.info(Common.PASSWORD_BY_USER + authenticationRequest.getPassword());
        return password_from_table.equals(authenticationRequest.getPassword());

    }

    public boolean finishedOtpValidation(String mailid) throws Exception {
        String sql = sqlUtility.finishedOTPValidationSql(mailid);
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
        HashMap<String, Object> status_map = new HashMap<>();
        AddUserResponse addUserResponse = new AddUserResponse();
        UserUtility userUtility = new UserUtility();
        if (userExists(addUserRequest.getMailid())) {
            status_map.put(Common.ERROR, Common.USER_ALREADY_EXISTS);
            addUserResponse.setStatus(Common.USER_ALREADY_EXISTS);
        } else if (!userUtility.finishedOtpValidation(addUserRequest.getMailid())) {
            addUserResponse.setStatus(Common.VERIFY_OTP_FIRST);
            status_map.put(Common.ERROR, Common.VERIFY_OTP_FIRST);
        } else {
            logger.info(Common.ADDING_USER);
            String insert_user = sqlUtility.addUserToDbSql(addUserRequest);
            sqlManager.renderInsertQuery(insert_user);

            String sql = "select * from users.blood_groups_count where pincode = '" + addUserRequest.getPincode() + "' and blood_group = '" + addUserRequest.getBloodgroup() + "';";
            List<Map<String, Object>> donorcount_map = sqlManager.renderSelectQuery(sql);
            if (donorcount_map.size() > 0) {
                sql = "update users.blood_groups_count set count = (select count from users.blood_groups_count where blood_group = '" + addUserRequest.getBloodgroup() + "')+1 where blood_group = '" + addUserRequest.getBloodgroup() + "' and pincode = '" + addUserRequest.getPincode() + "';";
                sqlManager.renderInsertQuery(sql);
            } else {
                sql = "insert into users.blood_groups_count (blood_group,count,pincode) values ('" + addUserRequest.getBloodgroup() + "',1,'" + addUserRequest.getPincode() + "');";
                sqlManager.renderInsertQuery(sql);
            }
            addUserResponse.setStatus(Common.ADDED_USER_SUCCESFULLY);
            status_map.put(Common.STATUS, Common.ADDED_USER_SUCCESFULLY);
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

        HashMap<String, Object> status_map = new HashMap<>();
        String sql = sqlUtility.validateOtpSql(validateOTPRequest);
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
            sqlManager.renderInsertQuery(sql);
            sql = "update users.details set verification_status = 'VALIDATED' where mailid = '" + validateOTPRequest.getMailid() + "';";
            sqlManager.renderInsertQuery(sql);
            status_map.put(Common.STATUS, Common.OTP_VERIFIED_SUCCESFULLY);
            validateOTPResponse.setStatus(Common.OTP_VERIFIED_SUCCESFULLY);
        } else {
            status_map.put(Common.ERROR, Common.WRONG_OTP_ENTERED_MSG);
            validateOTPResponse.setStatus(Common.WRONG_OTP_ENTERED_MSG);
        }
        return validateOTPResponse;
    }

    public String deleteUser(String mailid) throws Exception {
        logger.info(Common.DELETING_USER);
        HashMap<String, Object> status_map = new HashMap<>();
        HashMap<String, Object> delete_map = new HashMap<>();
        delete_map.put("mailid", mailid);
        String sql = sqlUtility.deleteUserSql(mailid);
        sqlManager.renderInsertQuery(sql);
        status_map.put("stats", Common.DELETED_USER_SUCCESS_MSG);
        return mapper.writeValueAsString(status_map);
    }

    public String getDataRequested(String country, String state, String district, String city, String town, String bloodgroup) throws Exception {
        logger.info("Fetching Requested Details..");
        HashMap<String, Object> request_data_map = new HashMap<>();
        request_data_map.put("country", country);
        request_data_map.put("state", state);
        request_data_map.put("district", district);
        request_data_map.put("city", city);
        request_data_map.put("town", town);
        request_data_map.put("bloodgroup", bloodgroup);
        try {
            List<Map<String, Object>> requests_map = STRenderer.renderSelectTemplate
                    (dbConnection.getConnection(), "getRequestedDetails", Common.STRING_TEMPLATES_PATH + File.separator + Common.BASIC_TEMPLATE, request_data_map);

            return mapper.writeValueAsString(requests_map);

        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new Exception("Failed to get Details");
    }

    public UpdateUserResponse updataUser(String mailid, UpdateUserRequest updateUserRequest) throws Exception {
        UpdateUserResponse updateUserResponse = new UpdateUserResponse();
        updateUserResponse.setMailid(updateUserRequest.getMailid());
        HashMap<String, Object> status_map = new HashMap<>();
        String sql = sqlUtility.updateUserSql(updateUserRequest);
        sqlManager.renderInsertQuery(sql);
        status_map.put(Common.STATUS, Common.UPDATED_SUCCESFULLY);
        updateUserResponse.setStatus(Common.UPDATED_SUCCESFULLY);
        return updateUserResponse;
    }

    private Boolean validatePincode(HashMap<String, Object> user_map) throws Exception {
        List<Map<String, Object>> pincode_map = STRenderer.renderSelectTemplate(dbConnection.getConnection(), "validate_pincode", Common.STRING_TEMPLATES_PATH + File.separator + Common.BASIC_TEMPLATE, user_map);
        return pincode_map.size() > 0;
    }

    public GetCountriesListResponse getCountriesList(GetCountriesListRequest getCountriesListRequest) throws Exception {
        GetCountriesListResponse getCountriesListResponse = new GetCountriesListResponse();
        String sql = sqlUtility.getCountriesList(getCountriesListRequest);
        Map<String, List<String>> pincode_map = sqlManager.renderSelectQueryReturnMapOfList(sql);
        getCountriesListResponse.setCountriesList(pincode_map);
        return getCountriesListResponse;
    }

    public GetStatesListResponse getStatesList(GetStatesListRequest getStatesListRequest) throws Exception {
        GetStatesListResponse getStatesListResponse = new GetStatesListResponse();
        String sql = sqlUtility.getStatesListSql(getStatesListRequest);
        Map<String, List<String>> pincode_map = sqlManager.renderSelectQueryReturnMapOfList(sql);
        getStatesListResponse.setStatesList(pincode_map);
        return getStatesListResponse;
    }

    public GetDistrictsListResponse getDistrictsList(GetDistrictsListRequest getDistrictsListRequest) throws Exception {
        GetDistrictsListResponse getDistrictsListResponse = new GetDistrictsListResponse();
        String sql = sqlUtility.getDistrictsListSql(getDistrictsListRequest);
        Map<String, List<String>> pincode_map = sqlManager.renderSelectQueryReturnMapOfList(sql);
        getDistrictsListResponse.setDistrictsList(pincode_map);
        return getDistrictsListResponse;
    }

    public GetTownsListResponse getTownsList(GetTownsListRequest getTownsListRequest) throws Exception {
        GetTownsListResponse getTownsListResponse = new GetTownsListResponse();
        String sql = sqlUtility.getTownsList(getTownsListRequest);
        Map<String, List<String>> pincode_map = sqlManager.renderSelectQueryReturnMapOfList(sql);
        getTownsListResponse.setTownsList(pincode_map);
        return getTownsListResponse;
    }

    public GetCitiesListResponse getCitiesList(GetCitiesListRequest getCitiesListRequest) throws Exception {
        GetCitiesListResponse getCitiesListResponse = new GetCitiesListResponse();
        String sql = sqlUtility.getCitiesListSql(getCitiesListRequest);
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
            authenticationResponse.setStatus("Email does not exist");
            return authenticationResponse;
        } else {
            if (correctPasswordEntered(authenticationRequest)) {
                String sql = "delete from users.login_sessions where mailid = '" + authenticationRequest.getMailid() + "' or CURRENT_TIMESTAMP - start_ts > INTERVAL '30' MINUTE;";
                sqlManager.renderInsertQuery(sql);

                sql = "insert into users.login_sessions (mailid,start_ts,end_ts) values ('" + authenticationRequest.getMailid() + "',CURRENT_TIMESTAMP,'9999-12-31 00:00:00');";
                sqlManager.renderInsertQuery(sql);

                sql = "select * from users.details d where mailid = '" + authenticationRequest.getMailid() + "'";

                List<Map<String, Object>> tempList = sqlManager.renderSelectQuery(sql);

                authenticationResponse.setUsername((String) tempList.get(0).get("username"));

                authenticationResponse.setStatus(Common.VALIDATION_SUCCESFUL);
                status_map.put(Common.STATUS, Common.VALIDATION_SUCCESFUL);
            } else {
                authenticationResponse.setStatus(Common.INCORRECT_PASSWORD);
                status_map.put(Common.ERROR, Common.INCORRECT_PASSWORD);
            }
            return authenticationResponse;
        }
    }

    public boolean sessionExists(String mailid) throws Exception {
        HashMap<String, Object> user_map = new HashMap<>();
        user_map.put("mailid", mailid);
        List<Map<String, Object>> session_count_map = STRenderer.renderSelectTemplate(dbConnection.getConnection(), "session_exists", Common.STRING_TEMPLATES_PATH + File.separator + Common.BASIC_TEMPLATE, user_map);

        if (session_count_map.size() > 0) {
            logger.info("Session Exists : " + true);
            return true;
        } else {
            logger.info("Session Exists : " + false);
            return false;
        }
    }


    public String getUserDetails(String mailid) throws Exception {
        HashMap<String, Object> detail_map = new HashMap<>();
        detail_map.put("mailid", mailid);
        List<Map<String, Object>> user_detail_map = STRenderer.renderSelectTemplate
                (dbConnection.getConnection(), "getUserDetails", Common.STRING_TEMPLATES_PATH + File.separator + Common.BASIC_TEMPLATE, detail_map);
        return mapper.writeValueAsString(user_detail_map.get(0));
    }

    public AddUserReviewResponse addUserReview(AddUserReviewRequest addUserReviewRequest) throws Exception {
        AddUserReviewResponse addUserReviewResponse = new AddUserReviewResponse();
        addUserReviewResponse.setMailid(addUserReviewRequest.getMailid());
        HashMap<String, Object> status_map = new HashMap<>();
//        HashMap<String, Object> review_map = JSONUtils.jsonToMap(json);
        String sql = "insert into users.reviews values ('" + addUserReviewRequest.getMailid() + "','" + addUserReviewRequest.getStars() + "','" + addUserReviewRequest.getComment() + "')";
        sqlManager.renderInsertQuery(sql);

        int stars_given = addUserReviewRequest.getStars();
        if (stars_given >= 3) {
            addUserReviewResponse.setStatus(Common.ABOVE_THREE);
            status_map.put(Common.STATUS, Common.ABOVE_THREE);
        } else {
            addUserReviewResponse.setStatus(Common.BELOW_THREE);
            status_map.put(Common.STATUS, Common.BELOW_THREE);
        }
        return addUserReviewResponse;
    }

    public String getAllDonorsCount() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        String sql = sqlUtility.getAllDonorsCount();
        List<Map<String, Object>> donor_count_map = sqlManager.renderSelectQuery(sql);
        map.put(Common.STATUS, donor_count_map.get(0).get(Common.COUNT));
        return mapper.writeValueAsString(map);
    }

    public String getAllBloodGroupsList() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        Map<String, List<String>> groups_map = STRenderer.renderSelectTemplateConverttoList(dbConnection.getConnection(), "getBloodGroups", Common.STRING_TEMPLATES_PATH + File.separator + Common.BASIC_TEMPLATE, map);
        return mapper.writeValueAsString(groups_map);
    }

    public String sendMessageToBot(String json) throws JSONException, JsonProcessingException {
        HashMap<String, Object> bot_input_msg_map = JSONUtils.jsonToMap(json);
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


    public String forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        String mailid = forgotPasswordRequest.getMailid();

        return null;
    }
}
