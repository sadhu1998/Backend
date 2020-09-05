package com.epsilon.donornearme.dao;

import com.epsilon.donornearme.models.request.*;

public class UserQueries {
    public String userExistsSql(String mailid) {
        return "select * from users.otp_validation where mailid = " + "'" + mailid + "'" + " and status = 'VALIDATED';";
    }

    public String finishedOTPValidationSql(String mailid) {
        return "select * from users.otp_validation ov where mailid = '" + mailid + "' and status = 'VALIDATED';";
    }

    public String addUserToDbDetailsSql(AddUserRequest addUserRequest) {
        return "insert into users.details(username , phonenumber , bloodgroup , town , district , city , state , country , mailid ,  pincode) values" + "('" + addUserRequest.getUsername() + "','" + addUserRequest.getPhonenumber() + "','" + addUserRequest.getBloodgroup() + "','" + addUserRequest.getTown() + "','" + addUserRequest.getDistrict() + "','" + addUserRequest.getCity() + "','" + addUserRequest.getState() + "','" + addUserRequest.getCountry() + "','" + addUserRequest.getMailid() + "','" + addUserRequest.getPincode() + "');";
    }

    public String addUserToDbCredssSql(AddUserRequest addUserRequest) {
        return  "insert into users.creds(username,password,mailid,fcmtoken) values" + "('" + addUserRequest.getUsername() + "','" + addUserRequest.getPassword() + "','" + addUserRequest.getMailid() + "','"+ addUserRequest.getFcmtoken() +"');";
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
        return "select otp from users.otp_validation where mailid = '" + validateOTPRequest.getMailid() + "' order by crt_ts desc limit 1;";
    }

    public String deleteUserSql(String mailid) {
        return "delete from users.otp_validation where mailid = '" + mailid + " " +
                "delete from users.details where mailid = '" + mailid + "'\n" +
                "delete from users.creds where mailid = '" + mailid + "'\n" +
                "delete from users.jwt_token_details where mailid = '" + mailid + "'";
    }

    public String updateUserSql(UpdateUserRequest updateUserRequest) {
        return "update users.details set username = '" + updateUserRequest.getUsername() + "',phonenumber = '" + updateUserRequest.getPhonenumber() + "',bloodgroup = '" + updateUserRequest.getBloodgroup() + "',town = '" + updateUserRequest.getTown() + "',district = '" + updateUserRequest.getDistrict() + "',city= '" + updateUserRequest.getCity() + "',state= '" + updateUserRequest.getState() + "',country = '" + updateUserRequest.getCountry() + "',mailid = '" + updateUserRequest.getMailid() + "',pincode = '" + updateUserRequest.getPincode() + "' where mailid in ('"+updateUserRequest.getMailid()+"');" +
                "update users.creds set mailid = '"+updateUserRequest.getMailid()+"', username = '"+updateUserRequest.getUsername()+"' , password = '"+updateUserRequest.getPassword()+"' where mailid in ('"+updateUserRequest.getMailid()+"');";
    }

    public String getUserDetails(GetUserDetailsRequest getUserDetailsRequest) {
        return "select d.username,password,phonenumber,bloodgroup,town,district,city,state,country,d.mailid,pincode,verification_status,mail_notification,sms_notification from users.details d left join users.alerts a on d.mailid = a.mailid left join users.creds c on c.mailid = d.mailid where d.mailid =  '"+getUserDetailsRequest.getMailid()+"'";
    }

    public String addReviewSql(AddUserReviewRequest addUserReviewRequest) {
        return "insert into users.reviews values ('" + addUserReviewRequest.getMailid() + "','" + addUserReviewRequest.getStars() + "','" + addUserReviewRequest.getComment() + "')";
    }

    public String updateForgotPasswordSql(UpdateForgotPasswordRequest updateForgotPasswordRequest) {
        return "update users.creds set password = '"+updateForgotPasswordRequest.getPassword()+"' where mailid = '"+updateForgotPasswordRequest.getMailid()+"'";
    }

    public String getOtpStatus(CheckOtpStatusRequest checkOtpStatusRequest) {
        return "select status from users.otp_validation where mailid = '" + checkOtpStatusRequest.getMailid() + "' order by crt_ts desc limit 1;";
    }

    public String validateOtpSqlMail(ValidateUserViaLinkRequest validateUserViaLinkRequest) {
        return "select otp from users.otp_validation where mailid = '" + validateUserViaLinkRequest.getMailid() + "' order by crt_ts desc limit 1;";
    }

    public String getReviewStatusSql(ReviewSubmittedStatusRequest reviewSubmittedStatusRequest) {
        return "select case when (select count(mailid) from users.reviews where mailid= '"+reviewSubmittedStatusRequest.getMailid()+"')>0 then 'true' else 'false' end as status";
    }

    public String getBloodRequestDetails(GetBloodNeedRequest getBloodNeedRequest) {
        return "select donor_id,receipent_id,message,d.phonenumber,d.username,br.blood_group from users.blood_requests br left join users.details d on d.mailid = br.receipent_id where br.donor_id in ('"+getBloodNeedRequest.getMailid()+"')";
    }

    public String getOtpOfUser(GetOtpOfUserRequest getOtpOfUserRequest) {
        return "select otp from users.otp_validation ov where mailid = '"+getOtpOfUserRequest.getMailid()+"' order by crt_ts desc limit 1";
    }

    public String updateFcmQuery(UpdateFcmTokenRequest updateFcmTokenRequest) {
        return "update users.creds set fcmtoken = '"+updateFcmTokenRequest.getFcmToken()+"' where mailid in ('"+updateFcmTokenRequest.getMailid()+"');";
    }

    public String getFcmFromDb(GetFcmTokenRequest getFcmTokenRequest) {
        return "select fcmtoken from users.creds where mailid = '"+getFcmTokenRequest.getMailid()+"'";
    }

    public String getGlobalBloodRequestedDetails(GetGlobalBloodRequest getGlobalBloodRequest) {
        return "select distinct (receipent_id),message,d.phonenumber,d.username,br.blood_group from users.blood_requests br left join users.details d on d.mailid = br.receipent_id  ";
    }
}
