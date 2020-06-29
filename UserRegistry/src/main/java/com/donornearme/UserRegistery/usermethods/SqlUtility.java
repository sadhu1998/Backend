package com.donornearme.UserRegistery.usermethods;

import com.donornearme.UserRegistery.model.request.*;

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
        return "select otp from users.otp_validation where mailid = '" + validateOTPRequest.getMailid() + "' order by crt_ts limit 1;";
    }

    public String updateUserSql(UpdateUserRequest updateUserRequest) {
        return "update users.details set username = " + updateUserRequest.getUsername() + ",phonenumber = " + updateUserRequest.getPhonenumber() + ",bloodgroup = " + updateUserRequest.getBloodgroup() + ",town = " + updateUserRequest.getTown() + ",district = " + updateUserRequest.getDistrict() + ",city= " + updateUserRequest.getCity() + ",state= " + updateUserRequest.getState() + ",country = " + updateUserRequest.getCountry() + ",mailid = " + updateUserRequest.getMailid() + ",pincode = " + updateUserRequest.getPincode() + "";
    }

    public String getAllDonorsCount() {
        return "select count(mailid) from users.details;";
    }

    public String deleteUserSql(String mailid) {
        return "delete from users.otp_validation where mailid = '" + mailid + " " +
                "delete from users.details where mailid = '" + mailid + "'\n" +
                "delete from users.creds where mailid = '" + mailid + "'\n" +
                "delete from users.jwt_token_details where mailid = '" + mailid + "'";
    }

    public String getStatesListSql(GetStatesListRequest getStatesListRequest) {
        return "select  distinct state as states from users.locations where country = '" + getStatesListRequest.getCountry() + "' order by state;";
    }

    public String getDistrictsListSql(GetDistrictsListRequest getDistrictsListRequest) {
        return "select distinct(district) as districts from users.locations where state = '" + getDistrictsListRequest.getState() + "' order by district;";
    }

    public String getCitiesListSql(GetCitiesListRequest getCitiesListRequest) {
        return "select distinct city as city from users.locations where district = '" + getCitiesListRequest.getDistrict() + "' order by city;";
    }

    public String getTownsList(GetTownsListRequest getTownsListRequest) {
        return "select distinct(town) from users.locations where city = '" + getTownsListRequest.getCity() + "' order by town;";
    }

    public String getCountriesList(GetCountriesListRequest getCountriesListRequest) {
        return "select distinct(country) from users.locations order by country";
    }

    public String getAvailableDonorsList(GetDonorsAvailableRequest getDonorsAvailableRequest) {
        return "select * from users.details where bloodgroup = '" + getDonorsAvailableRequest.getBloodgroup() + "' and country = '" + getDonorsAvailableRequest.getCountry() + "' and town = '" + getDonorsAvailableRequest.getTown() + "' and district = '" + getDonorsAvailableRequest.getDistrict() + "' and city = '" + getDonorsAvailableRequest.getCity() + "' and state = '" + getDonorsAvailableRequest.getState() + "'";
    }

    public String getUserDetails(GetUserDetailsRequest getUserDetailsRequest) {
        return "select * from users.details d where mailid = '" + getUserDetailsRequest.getMailid() + "'";
    }

    public String getBloodGroupsListSql(GetBloodGroupsRequest getBloodGroupsRequest) {
        return "select blood_group from users.blood_groups order by blood_group;";
    }

    public String addReviewSql(AddUserReviewRequest addUserReviewRequest) {
        return "insert into users.reviews values ('" + addUserReviewRequest.getMailid() + "','" + addUserReviewRequest.getStars() + "','" + addUserReviewRequest.getComment() + "')";
    }

    public String getBloodDonorCountWithPincode(AddUserRequest addUserRequest) {
        return "select * from users.blood_groups_count where pincode = '" + addUserRequest.getPincode() + "' and blood_group = '" + addUserRequest.getBloodgroup() + "';";
    }

    public String modifyBloodGroupCountTable(AddUserRequest addUserRequest, boolean exists) {
        if (exists) {
            return "update users.blood_groups_count set count = (select count from users.blood_groups_count where blood_group = '" + addUserRequest.getBloodgroup() + "')+1 where blood_group = '" + addUserRequest.getBloodgroup() + "' and pincode = '\" + addUserRequest.getPincode() + \"';\"";
        } else {
            return "insert into users.blood_groups_count (blood_group,count,pincode) values ('" + addUserRequest.getBloodgroup() + "',1,'" + addUserRequest.getPincode() + "');";
        }
    }

    public String updateForgotPasswordSql(UpdateForgotPasswordRequest updateForgotPasswordRequest) {
        return "update users.creds set password = '"+updateForgotPasswordRequest.getPassword()+"' where mailid = '"+updateForgotPasswordRequest.getMailid()+"'";
    }

    public String sessionExistsSql(String mailid) {
        return "select * from users.login_sessions ls where CURRENT_TIMESTAMP - start_ts < INTERVAL '30' MINUTE and mailid = '"+mailid+"';";
    }

    public String validateOtpSqlMail(ValidateUserViaLinkRequest validateUserViaLinkRequest) {
        return "select otp from users.otp_validation where mailid = '" + validateUserViaLinkRequest.getMailid() + "' order by crt_ts limit 1;";
    }

    public String getOtpStatus(CheckOtpStatusRequest checkOtpStatusRequest) {
        return "select status from users.otp_validation where mailid = '" + checkOtpStatusRequest.getMailid() + "' order by crt_ts limit 1;";
    }
}
