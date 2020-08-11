package com.epsilon.donornearme.dao;

import com.epsilon.donornearme.models.request.GetBloodGroupsRequest;
import com.epsilon.donornearme.models.request.GetDonorsAvailableRequest;
import com.epsilon.donornearme.models.request.UserDonorLogRequest;

public class DonorQueries {


    public String getAllDonorsCount() {
        return "select count(mailid) from users.details;";
    }

    public String getAvailableDonorsList(GetDonorsAvailableRequest getDonorsAvailableRequest) {
        return "select * from users.details where bloodgroup = '"+getDonorsAvailableRequest.getBloodgroup()+"' and country = '"+getDonorsAvailableRequest.getCountry()+"' and town = '"+getDonorsAvailableRequest.getTown()+"' and district = '"+getDonorsAvailableRequest.getDistrict()+"' and city = '"+getDonorsAvailableRequest.getCity()+"' and state = '"+getDonorsAvailableRequest.getState()+"'";
    }

    public String getBloodGroupsListSql(GetBloodGroupsRequest getBloodGroupsRequest) {
        return "select blood_group from users.blood_groups order by blood_group;";
    }

    public String insertIntoBloodRequestsTable(UserDonorLogRequest userDonorLogRequest) {
        return "insert into users.blood_requests (donor_id ,receipent_id ,requested_ts ,donated,message,blood_group ) values ('"+userDonorLogRequest.getDonorId()+"','"+userDonorLogRequest.getReceipentId()+"',now(),false,'"+userDonorLogRequest.getMessage()+"',(select bloodgroup from users.details where mailid in ('"+userDonorLogRequest.getDonorId()+"')));";
    }
}
