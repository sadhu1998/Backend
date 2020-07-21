package com.epsilon.donornearme.operations;

import com.epsilon.donornearme.Common;
import com.epsilon.donornearme.models.request.AuthenticationRequest;
import com.epsilon.donornearme.models.response.AuthenticationResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AdditionalOperator extends CommonOperator {
    protected static final Logger logger = LogManager.getLogger(AdditionalOperator.class);

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws Exception {
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setMailid(authenticationRequest.getMailid());
        if (!userExists(authenticationRequest.getMailid())) {
            authenticationResponse.setError("Email does not exist");
            return authenticationResponse;
        } else {
            if (correctPasswordEntered(authenticationRequest)) {
                String sql = "delete from users.login_sessions where mailid = '" + authenticationRequest.getMailid() + "' or CURRENT_TIMESTAMP - start_ts > INTERVAL '30' MINUTE;";
                logger.info(Common.EXECUTING_SQL + sql);
                sqlRenderer.runInsertQuery(sql);

                sql = "insert into users.login_sessions (mailid,start_ts,end_ts) values ('" + authenticationRequest.getMailid() + "',CURRENT_TIMESTAMP,'9999-12-31 00:00:00');";
                logger.info(Common.EXECUTING_SQL + sql);
                sqlRenderer.runInsertQuery(sql);

                sql = "select * from users.details d where mailid = '" + authenticationRequest.getMailid() + "'";
                logger.info(Common.EXECUTING_SQL + sql);

                List<Map<String, Object>> tempList = sqlRenderer.runSelectQuery(sql);

                authenticationResponse.setUsername((String) tempList.get(0).get("username"));

                authenticationResponse.setStatus(Common.VALIDATION_SUCCESFUL);
            } else {
                authenticationResponse.setError(Common.INCORRECT_PASSWORD);
            }
            return authenticationResponse;
        }
    }

}

