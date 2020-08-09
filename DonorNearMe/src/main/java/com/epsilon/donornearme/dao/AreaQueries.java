package com.epsilon.donornearme.dao;

import com.epsilon.donornearme.models.request.*;

public class AreaQueries {

    public String getStatesListSql(GetStatesListRequest getStatesListRequest) {
        return "select distinct(state) as states from users.locations where country = '" + getStatesListRequest.getCountry() + "' order by state;";
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

    public String getPincodeOfArea(GetPincodeRequest getPincodeRequest) {
        return "select pincode from users.locations l where country = '" + getPincodeRequest.getCountry() + "' and state = '" + getPincodeRequest.getState() + "' and district = '" + getPincodeRequest.getDistrict() + "' and city = '" + getPincodeRequest.getCity() + "' and town = '" + getPincodeRequest.getTown() + "'";
    }
}
