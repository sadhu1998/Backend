package com.donornearme.UserRegistery.operations;

import com.donornearme.UserRegistery.Common;
import com.donornearme.UserRegistery.controllers.BaseController;
import com.donornearme.UserRegistery.extensionsmanager.SQLManager;
import com.donornearme.UserRegistery.models.request.ContactUsRequest;
import com.donornearme.UserRegistery.models.request.SendMailToDonorRequest;
import com.donornearme.UserRegistery.models.request.SendOTPToMailRequest;
import com.donornearme.UserRegistery.models.response.ContactUsResponse;
import com.donornearme.UserRegistery.models.response.SendMailToDonorResponse;
import com.donornearme.UserRegistery.models.response.SendOTPToMailResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MailOperation extends BaseController {
    protected static final Logger logger = LogManager.getLogger(MailOperation.class);
    SQLManager sqlManager = new SQLManager();
    ObjectMapper mapper = new ObjectMapper();
    String user = "donornearme@gmail.com";
    String password = "A*1sadhvik";

    public SendOTPToMailResponse sendOTP(SendOTPToMailRequest sendOTPToMailRequest) throws Exception {
        SendOTPToMailResponse sendOTPToMailResponse = new SendOTPToMailResponse();
        sendOTPToMailResponse.setMailid(sendOTPToMailRequest.getMailid());
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
                "Thanks for becoming a member. Thankyou! Hope we help you!" + "" +
                "You can also click on the link below to validate your email " + generateValidateMailLink(sendOTPToMailRequest, otp));
        body_map.put("alias", "sadhu1998@gmail.com");

        HttpResponse<String> response = Unirest.post(Common.EMAIL_ENDPOINT)
                .header(Common.CONTENT_TYPE, Common.CONTEST_JSON)
                .header(Common.CACHE_CONTROL, Common.NO_CACHE)
                .body(mapper.writeValueAsString(body_map))
                .asString();

        if (response.getStatus() == 200) {
            String sql = "insert into users.otp_validation (mailid , otp, status, crt_ts) " +
                    "values ('" + sendOTPToMailResponse.getMailid() + "','" + otp + "','initiated',now());";
            sqlManager.runInsertQuery(sql);
            logger.info("OTP sent successfully...");
        } else {
            logger.error("Failed to send OTP");
        }
        sendOTPToMailResponse.setStatus("Mail has been sent to " + mailid + " succesfully.");

        return sendOTPToMailResponse;
    }

    private String generateOTP() {
        Random rnd = new Random();
        return String.format("%06d", rnd.nextInt(999999));
    }

    public SendMailToDonorResponse sendMailToDonor(SendMailToDonorRequest sendMailToDonorRequest) throws JSONException, JsonProcessingException, UnirestException {
        SendMailToDonorResponse sendMailToDonorResponse = new SendMailToDonorResponse();
        sendMailToDonorResponse.setMailid(sendMailToDonorRequest.getMailid());
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
        } else {
            sendMailToDonorResponse.setStatus("Failed to send Mail");
            logger.error("Failed to send Mail");
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

        mail_to_donor_map.put(Common.OTP_SEND_MESSAGE, contactUsSubject(contactUsRequest));

        HttpResponse<String> response = Unirest.post(Common.EMAIL_ENDPOINT)
                .header(Common.CONTENT_TYPE, Common.CONTEST_JSON)
                .header(Common.CACHE_CONTROL, Common.NO_CACHE)
                .body(mapper.writeValueAsString(mail_to_donor_map))
                .asString();

        if (response.getStatus() == 200) {
            contactUsResponse.setStatus("We will reach you shortly");
        } else {
            contactUsResponse.setError("We are currently busy.");
            logger.error("Failed to send Mail");
        }
        return contactUsResponse;
    }

    public String contactUsSubject(ContactUsRequest contactUsRequest) {
        return "Hi, I am " + contactUsRequest.getUsername() + "We want to contact You. This is my email :" + contactUsRequest.getMailid() + " This is my Phone Number :" + contactUsRequest.getPhonenumber();
    }

    public String generateValidateMailLink(SendOTPToMailRequest sendOTPToMailRequest, String otp) {
        String mailid = "mailid=" + sendOTPToMailRequest.getMailid();
        String otptext = "otp=" + otp;
        String baseurl = "http://localhost:8080/validatemailvialink?";
        logger.info("Verification Link : " + baseurl + mailid + "&" + otptext);
        return baseurl + mailid + "&" + otptext;
    }
}
