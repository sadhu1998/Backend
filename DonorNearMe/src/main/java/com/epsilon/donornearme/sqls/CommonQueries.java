package com.epsilon.donornearme.sqls;

import com.epsilon.donornearme.models.request.AuthenticationRequest;

public class CommonQueries {
    public String userExistsSql(String mailid) {
        return "select * from users.details where mailid = " + "'" + mailid + "'" + " and verification_status = 'VALIDATED';";
    }


    public String sessionExistsSql(String mailid) {
        return "select * from users.login_sessions ls where CURRENT_TIMESTAMP - start_ts < INTERVAL '30' MINUTE and mailid = '" + mailid + "';";
    }

    public String correctPasswordEnteredSql(AuthenticationRequest authenticationRequest) {
        return "select password from users.creds where mailid = '" + authenticationRequest.getMailid() + "';";
    }
}
