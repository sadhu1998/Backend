package com.epsilon.donornearme.sqls;

import com.epsilon.donornearme.models.request.GetBloodGroupsRequest;
import com.epsilon.donornearme.models.request.GetDonorsAvailableRequest;

public class DonorQueries {


    public String getAllDonorsCount() {
        return "select count(mailid) from users.details;";
    }

    public String getAvailableDonorsList(GetDonorsAvailableRequest getDonorsAvailableRequest) {
        return "select * from users.details where bloodgroup = '" + getDonorsAvailableRequest.getBloodgroup() + "' and country = '" + getDonorsAvailableRequest.getCountry() + "' and town = '" + getDonorsAvailableRequest.getTown() + "' and district = '" + getDonorsAvailableRequest.getDistrict() + "' and city = '" + getDonorsAvailableRequest.getCity() + "' and state = '" + getDonorsAvailableRequest.getState() + "'";
    }

    public String getBloodGroupsListSql(GetBloodGroupsRequest getBloodGroupsRequest) {
        return "select blood_group from users.blood_groups order by blood_group;";
    }
}
