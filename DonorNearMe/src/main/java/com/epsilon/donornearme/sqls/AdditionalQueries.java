package com.epsilon.donornearme.sqls;

public class AdditionalQueries {
    public String userExistsSql(String mailid) {
        return "select * from users.details where mailid = " + "'" + mailid + "'" + " and verification_status = 'VALIDATED';";
    }
}
