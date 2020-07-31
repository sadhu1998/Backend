package com.epsilon.donornearme;

public class Common {

//    Email
    public static final String EMAIL_ENDPOINT = "http://www.latherapeutics.co/send_mail";
    public static final String CONTEST_JSON = "application/json";
    public static final String CONTENT_TYPE = "content-type";
    public static final String CACHE_CONTROL= "cache-control";
    public static final String NO_CACHE = "no-cache";
    public static final String FROM_MAIL ="from_mail";
    public static final String FROM_MAIL_PASSWORD = "password";
    public static final String TO_MAIL ="to";
    public static final String SUBJECT = "subject";

//  Postgres Creds
    public static final String DB_URL = "jdbc:postgresql://35.238.212.200:5432/donornearme";
    public static final String DB_USERNAME = "admin";
    public static final String DB_PASSWORD = "admin";

//    Swagger
    public static final String DONORNEARME_SPRING_BOOT_API = "DonorNearMe Spring Boot API";
    public static final String SWAGGER_VERSION = "3.0";
    public static final String COPYRIGHT = "(C) Copyright Donor Near Me";
    public static final String DESCRIPTION = "List of all endpoints used in API";
    public static final String PATH_MAPPING = "/";

    //    Templates Common
    public static final String ERROR = "error";
    public static final Character ST_DELIMITER = '$';
    public static final String DATA = "data";
    public static final String STATUS = "status";
    public static final String COUNT = "count";
    public static final String LIST_OF_MAP = "listofmap";
    public static final String LIST = "list";

//    Kafka Common
    public static final String TOPIC_LIST="topics";
    public static final String TOPIC_CONTAINS_PATTERN="topic_contains_pattern";
    public static final String KAFKA_BOOTSTRAP_SERVERS="bootstrap_servers";
    public static final String SESSION_TIMEOUT_MS_CONFIG="60000";
    public static final String ENABLE_AUTO_COMMIT_CONFIG="false";
    public static final Integer MAX_POLL_RECORDS_CONFIG=1;
    public static final Integer MAX_POLL_INTERVAL_MS_CONFIG=172800000;
    public static final Integer REQUEST_TIMEOUT_MS_CONFIG=172830000;
    public static final String GROUP_ID="group_id";
    public static final String CONSUMER_CLIENT_ID="client_id";
    public static final String ACKS_CONFIG="1";

//    OTP
    public static final String OTP_GEN = "%06d";
    public static final String ORIGINAL_OTP_IS = "Original OTP is : ";
    public static final String OTP_ENTERED_BY_USER = "OTP Entered By User is : ";

//    Logging
    public static final String USER_EXISTS = "User Exists : ";
    public static final String ADDED_USER_SUCCESFULLY = "Added User Succesfully. Please sign in to continue";
    public static final String ORIGINAL_PASSWORD_IS = "Original password is ";
    public static final String PASSWORD_BY_USER = "Original password is ";
    public static final String USER_ALREADY_EXISTS = "User already exists";
    public static final String VERIFY_OTP_FIRST = "Please Verfiy OTP First";
    public static final String ADDING_USER = "Adding User..";
    public static final String OTP_STATUS = "OTP Validated : ";
    public static final String DELETING_USER = "Deleting User..";
    public static final String UPDATED_SUCCESFULLY = "Updated Succesfully";
    public static final String VALIDATION_SUCCESFUL = "Validation Succesful";
    public static final String INCORRECT_PASSWORD = "Incorrect Password";
    public static final String OTP_VERIFIED_SUCCESFULLY = "OTP verified succesfully";
    public static final String WRONG_OTP_ENTERED_MSG = "Wrong OTP entered.. Please retry";
    public static final String DELETED_USER_SUCCESS_MSG = "Deleted User succesfully";
    public static final String FETCHING_REQUESTED_DETAILS = "Fetching Requested Details..";
    public static final String OTP_VALIDATION_SUBJECT = "Donor Near Me - OTP Verification";
    public static final String OTP_SEND_MESSAGE = "message";
    public static final String EXECUTING_SQL ="Executing SQL : ";
    public static final String SESSION_EXISTS = "Session Exists : ";

//    Reviews
    public static final String ABOVE_THREE = "Thankyou. Always happy to help you!!";
    public static final String BELOW_THREE = "Sorry, we could not help you. Please do provide suggestions to improve!!";
}
