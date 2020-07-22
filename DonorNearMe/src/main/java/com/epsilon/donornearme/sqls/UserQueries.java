package com.epsilon.donornearme.sqls;

import com.epsilon.donornearme.models.request.*;

public class UserQueries {
    public String userExistsSql(String mailid) {
        return "select * from users.otp_validation where mailid = " + "'" + mailid + "'" + " and verification_status = 'VALIDATED';";
    }

    public String finishedOTPValidationSql(String mailid) {
        return "select * from users.otp_validation ov where mailid = '" + mailid + "' and status = 'VALIDATED';";
    }

    public String addUserToDbDetailsSql(AddUserRequest addUserRequest) {
        return "insert into users.details(username , phonenumber , bloodgroup , town , district , city , state , country , mailid ,  pincode) values" + "('" + addUserRequest.getUsername() + "','" + addUserRequest.getPhonenumber() + "','" + addUserRequest.getBloodgroup() + "','" + addUserRequest.getTown() + "','" + addUserRequest.getDistrict() + "','" + addUserRequest.getCity() + "','" + addUserRequest.getState() + "','" + addUserRequest.getState() + "','" + addUserRequest.getMailid() + "','" + addUserRequest.getPincode() + ");";
    }

    public String addUserToDbCredssSql(AddUserRequest addUserRequest) {
        return  "insert into users.creds(username,password,mailid) values" + "('" + addUserRequest.getUsername() + "','" + addUserRequest.getPassword() + "','" + addUserRequest.getMailid() + "');";
    }

    public String addUserToDbAlertsSql(AddUserRequest addUserRequest) {
        return "insert into users.alerts(mailid,mail_notification,sms_notification) values" + "('" + addUserRequest.getMailid() + "','" + addUserRequest.getMail_notification() + "','" + addUserRequest.getSms_notification() + "');";
    }

    public String getBloodDonorCountWithPincode(AddUserRequest addUserRequest) {
        return "select * from users.blood_groups_count where pincode = '" + addUserRequest.getPincode() + "' and blood_group = '" + addUserRequest.getBloodgroup() + "';";
    }

    public String modifyBloodGroupCountTable(AddUserRequest addUserRequest, boolean exists) {
        if (exists) {
            return "update users.blood_groups_count set count = (select count from users.blood_groups_count where blood_group = '" + addUserRequest.getBloodgroup() + "' and pincode = '"+addUserRequest.getPincode()+"')+1 where blood_group = '" + addUserRequest.getBloodgroup() + "' and pincode = '" + addUserRequest.getPincode() + "';";
        } else {
            return "insert into users.blood_groups_count (blood_group,count,pincode) values ('" + addUserRequest.getBloodgroup() + "',1,'" + addUserRequest.getPincode() + "');";
        }
    }

    public String validateOtpSql(ValidateOTPRequest validateOTPRequest) {
        return "select otp from users.otp_validation where mailid = '" + validateOTPRequest.getMailid() + "' order by crt_ts limit 1;";
    }

    public String deleteUserSql(String mailid) {
        return "delete from users.otp_validation where mailid = '" + mailid + " " +
                "delete from users.details where mailid = '" + mailid + "'\n" +
                "delete from users.creds where mailid = '" + mailid + "'\n" +
                "delete from users.jwt_token_details where mailid = '" + mailid + "'";
    }

    public String updateUserSql(UpdateUserRequest updateUserRequest) {
        return "update users.details set username = " + updateUserRequest.getUsername() + ",phonenumber = " + updateUserRequest.getPhonenumber() + ",bloodgroup = " + updateUserRequest.getBloodgroup() + ",town = " + updateUserRequest.getTown() + ",district = " + updateUserRequest.getDistrict() + ",city= " + updateUserRequest.getCity() + ",state= " + updateUserRequest.getState() + ",country = " + updateUserRequest.getCountry() + ",mailid = " + updateUserRequest.getMailid() + ",pincode = " + updateUserRequest.getPincode() + "";
    }

    public String getUserDetails(GetUserDetailsRequest getUserDetailsRequest) {
        return "select * from users.details d where mailid = '" + getUserDetailsRequest.getMailid() + "'";
    }

    public String addReviewSql(AddUserReviewRequest addUserReviewRequest) {
        return "insert into users.reviews values ('" + addUserReviewRequest.getMailid() + "','" + addUserReviewRequest.getStars() + "','" + addUserReviewRequest.getComment() + "')";
    }


    public String updateForgotPasswordSql(UpdateForgotPasswordRequest updateForgotPasswordRequest) {
        return "update users.creds set password = '"+updateForgotPasswordRequest.getPassword()+"' where mailid = '"+updateForgotPasswordRequest.getMailid()+"'";
    }

    public String getOtpStatus(CheckOtpStatusRequest checkOtpStatusRequest) {
        return "select status from users.otp_validation where mailid = '" + checkOtpStatusRequest.getMailid() + "' order by crt_ts limit 1;";
    }

    public String validateOtpSqlMail(ValidateUserViaLinkRequest validateUserViaLinkRequest) {
        return "select otp from users.otp_validation where mailid = '" + validateUserViaLinkRequest.getMailid() + "' order by crt_ts limit 1;";
    }

    public String getReviewStatusSql(ReviewSubmittedStatusRequest reviewSubmittedStatusRequest) {
        return "select case when (select count(mailid) from users.reviews where mailid= '"+reviewSubmittedStatusRequest.getMailid()+"')>0 then 'true' else 'false' end as status";
    }
}
