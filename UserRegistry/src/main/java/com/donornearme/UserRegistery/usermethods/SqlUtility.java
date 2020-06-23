package com.donornearme.UserRegistery.usermethods;

import com.donornearme.UserRegistery.model.request.AddUserRequest;
import com.donornearme.UserRegistery.model.request.AuthenticationRequest;
import com.donornearme.UserRegistery.model.request.UpdateUserRequest;
import com.donornearme.UserRegistery.model.request.ValidateOTPRequest;

public class SqlUtility {
    public String userExistsSql(String mailid) {
        return "select * from users.details where mailid = " + "'" + mailid + "'" + " and verification_status = 'VALIDATED';";
    }

    public String correctPasswordEnteredSql(AuthenticationRequest authenticationRequest) {
        return "select password from users.creds where mailid = '" + authenticationRequest.getMailid() + "';";
    }

    public String finishedOTPValidationSql(String mailid) {
        return "select * from users.otp_validation ov where mailid = '" + mailid + "' and status = 'VALIDATED';";
    }

    public String addUserToDbSql(AddUserRequest addUserRequest) {
        return "insert into users.details(username , phonenumber , bloodgroup , town , district , city , state , country , mailid , verification_status, pincode) values" + "('" + addUserRequest.getUsername() + "','" + addUserRequest.getPhonenumber() + "','" + addUserRequest.getBloodgroup() + "','" + addUserRequest.getTown() + "','" + addUserRequest.getDistrict() + "','" + addUserRequest.getCity() + "','" + addUserRequest.getState() + "','" + addUserRequest.getState() + "','" + addUserRequest.getMailid() + "','FALSE', " + addUserRequest.getPincode() + ");" + "" +
                "insert into users.creds(username,password,mailid) values" + "('" + addUserRequest.getUsername() + "','" + addUserRequest.getPassword() + "','" + addUserRequest.getPassword() + "');" + "" +
                "insert into users.alerts(mailid,mail_notification,sms_notification) values" + "('" + addUserRequest.getMailid() + "','" + addUserRequest.getMail_notification() + "','" + addUserRequest.getSms_notification() + "');";
    }

    public String validateOtpSql(ValidateOTPRequest validateOTPRequest) {
        return "select otp from users.otp_validation where mailid = '" + validateOTPRequest.getMailid() + "';";

    }

    public String updateUserSql(UpdateUserRequest updateUserRequest){
        return "update users.details set username = "+updateUserRequest.getUsername()+",phonenumber = "+updateUserRequest.getPhonenumber()+",bloodgroup = "+updateUserRequest.getBloodgroup()+",town = "+updateUserRequest.getTown()+",district = "+updateUserRequest.getDistrict()+",city= "+updateUserRequest.getCity()+",state= "+updateUserRequest.getState()+",country = "+updateUserRequest.getCountry()+",mailid = "+updateUserRequest.getMailid()+",pincode = "+updateUserRequest.getPincode()+"";
    }

    public String getAllDonorsCount(){
        return "select count(mailid) from users.details;";
    }

    public String deleteUserSql(String mailid){
        return "delete from users.otp_validation where mailid = '"+mailid+" " +
                "delete from users.details where mailid = '"+mailid+"'\n" +
                "delete from users.creds where mailid = '"+mailid+"'\n" +
                "delete from users.jwt_token_details where mailid = '"+mailid+"'";
    }
}
