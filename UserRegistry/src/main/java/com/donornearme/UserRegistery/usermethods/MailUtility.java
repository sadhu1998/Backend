package com.donornearme.UserRegistery.usermethods;

import com.donornearme.UserRegistery.Common;
import com.donornearme.UserRegistery.extensionmanager.SQLManager;
import com.donornearme.UserRegistery.model.request.ContactUsRequest;
import com.donornearme.UserRegistery.model.request.SendMailToDonorRequest;
import com.donornearme.UserRegistery.model.request.SendOTPToMailRequest;
import com.donornearme.UserRegistery.model.response.ContactUsResponse;
import com.donornearme.UserRegistery.model.response.SendMailToDonorResponse;
import com.donornearme.UserRegistery.model.response.SendOTPToMailResponse;
import com.donornearme.UserRegistery.usercontroller.BaseController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MailUtility extends BaseController {
    protected static final Logger logger = LogManager.getLogger(MailUtility.class);
    SQLManager sqlManager = new SQLManager();
    String user = "sadhvik.nps@gmail.com";
    String password = "9951974800";

    public SendOTPToMailResponse sendOTP(SendOTPToMailRequest sendOTPToMailRequest) throws Exception {
        SendOTPToMailResponse sendOTPToMailResponse = new SendOTPToMailResponse();
        sendOTPToMailResponse.setMailid(sendOTPToMailRequest.getMailid());
        HashMap<String, Object> status_map = new HashMap<>();
        logger.info("Sending OTP to registered Mail");
//        if (userExists(sendOTPToMailRequest.getMailid())) {
//            status_map.put(Common.ERROR, "Provided mailid " + sendOTPToMailRequest.getMailid() + "already exists. Please login");
//            sendOTPToMailResponse.setError("Provided mailid " + sendOTPToMailRequest.getMailid() + "already exists. Please login");
//            return sendOTPToMailResponse;
//        }

        String mailid = sendOTPToMailRequest.getMailid();

        String otp = generateOTP();
        Map<String, String> body_map = new HashMap<>();
        body_map.put(Common.FROM_MAIL, user);
        body_map.put(Common.FROM_MAIL_PASSWORD, password);
        body_map.put(Common.TO_MAIL, mailid);
        body_map.put(Common.SUBJECT, Common.OTP_VALIDATION_SUBJECT);
        body_map.put(Common.OTP_SEND_MESSAGE, otp + " is the OTP for verifying mail_id for donornearme." +
                "Thanks for becoming a member. Thankyou! Hope we help you! ");
        body_map.put("alias", "sadhu1998@gmail.com");

        HttpResponse<String> response = Unirest.post(Common.EMAIL_ENDPOINT)
                .header(Common.CONTENT_TYPE, Common.CONTEST_JSON)
                .header(Common.CACHE_CONTROL, Common.NO_CACHE)
                .body(mapper.writeValueAsString(body_map))
                .asString();

        if (response.getStatus() == 200) {
            String sql = "insert into users.otp_validation (mailid , otp, status, crt_ts) " +
                    "values ('" + sendOTPToMailResponse.getMailid() + "','" + otp + "','initiated',now());";
            sqlManager.renderInsertQuery(sql);
            logger.info("OTP sent successfully...");
            status_map.put(Common.STATUS, "Mail has been sent to " + mailid + " succesfully.");
            sendOTPToMailResponse.setStatus("Mail has been sent to " + mailid + " succesfully.");
        } else {
            logger.error("Failed to send OTP");
            sendOTPToMailResponse.setStatus("Mail has been sent to " + mailid + " succesfully.");
        }

        return sendOTPToMailResponse;
    }

    private String generateOTP() {
        Random rnd = new Random();
        return String.format("%06d", rnd.nextInt(999999));
    }

    public boolean userExists(String mailid) throws Exception {
        String sql = "select * from users.details where mailid = " + "'" + mailid + "'" + " and verification_status = 'VALIDATED';";
        List<Map<String, Object>> count_map = sqlManager.renderSelectQuery(sql);
        if (count_map.size() > 0) {
            logger.info("User Exists : " + true);
            return true;
        } else {
            logger.info("User Exists : " + false);
            return false;
        }
    }


    public SendMailToDonorResponse sendMailToDonor(SendMailToDonorRequest sendMailToDonorRequest) throws JSONException, JsonProcessingException, UnirestException {
        SendMailToDonorResponse sendMailToDonorResponse = new SendMailToDonorResponse();
        sendMailToDonorResponse.setMailid(sendMailToDonorRequest.getMailid());
        HashMap<String, Object> status_map = new HashMap<>();
        HashMap<String, Object> mail_to_donor_map = new HashMap<>();

        mail_to_donor_map.put(Common.FROM_MAIL, user);
        mail_to_donor_map.put(Common.FROM_MAIL_PASSWORD, password);
        mail_to_donor_map.put(Common.SUBJECT, "Donor Near Me - Require Blood");
        mail_to_donor_map.put("alias", "sadhu1998@gmail.com");
        mail_to_donor_map.put(Common.TO_MAIL, sendMailToDonorRequest.getMailid());

        if (mail_to_donor_map.get(Common.OTP_SEND_MESSAGE).equals("") || !mail_to_donor_map.containsKey(Common.OTP_SEND_MESSAGE)) {
            mail_to_donor_map.put(Common.OTP_SEND_MESSAGE, "We are in require of Blood. Please contact the number or mailid!");
        }

        HttpResponse<String> response = Unirest.post(Common.EMAIL_ENDPOINT)
                .header(Common.CONTENT_TYPE, Common.CONTEST_JSON)
                .header(Common.CACHE_CONTROL, Common.NO_CACHE)
                .body(mapper.writeValueAsString(mail_to_donor_map))
                .asString();

        if (response.getStatus() == 200) {
            logger.info("Mail sent successfully from " + user + " to " + mail_to_donor_map.get(Common.TO_MAIL));
            sendMailToDonorResponse.setStatus("Mail has been sent succesfully.");
            status_map.put(Common.STATUS, "Mail has been sent succesfully.");
        } else {
            sendMailToDonorResponse.setStatus("Failed to send Mail");
            logger.error("Failed to send Mail");
            status_map.put(Common.ERROR, "Failed to send Mail");
        }
        return sendMailToDonorResponse;
    }

    public ContactUsResponse contactUsMail(ContactUsRequest contactUsRequest) throws JsonProcessingException, UnirestException {
        ContactUsResponse contactUsResponse = new ContactUsResponse();
        HashMap<String, Object> mail_to_donor_map = new HashMap<>();
        contactUsResponse.setMailid(contactUsRequest.getMailid());

        mail_to_donor_map.put(Common.FROM_MAIL, user);
        mail_to_donor_map.put(Common.FROM_MAIL_PASSWORD, password);
        mail_to_donor_map.put(Common.SUBJECT, "User want to contact");
        mail_to_donor_map.put("alias", "sadhu1998@gmail.com");
        mail_to_donor_map.put(Common.TO_MAIL, user);

        mail_to_donor_map.put(Common.OTP_SEND_MESSAGE, contactUsSubject(contactUsRequest) );

        HttpResponse<String> response = Unirest.post(Common.EMAIL_ENDPOINT)
                .header(Common.CONTENT_TYPE, Common.CONTEST_JSON)
                .header(Common.CACHE_CONTROL, Common.NO_CACHE)
                .body(mapper.writeValueAsString(mail_to_donor_map))
                .asString();

        if (response.getStatus() == 200) {
            contactUsResponse.setStatus("We will reach you shortly");
            return contactUsResponse;
        } else {
            contactUsResponse.setError("We are currently busy.");
            logger.error("Failed to send Mail");
            return contactUsResponse;
        }
    }

    public String contactUsSubject(ContactUsRequest contactUsRequest){
        return "Hi, I am "+contactUsRequest.getUsername()+"We want to contact You. This is my email :" +contactUsRequest.getMailid()+" This is my Phone Number :"+contactUsRequest.getPhonenumber();
    }
}
