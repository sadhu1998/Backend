package com.epsilon.donornearme.operations;

import com.epsilon.donornearme.Common;
import com.epsilon.donornearme.dao.CommonQueries;
import com.epsilon.donornearme.utilities.SqlRendererUtility;
import com.epsilon.donornearme.models.request.AuthenticationRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class CommonOperator {
    protected static final Logger logger = LogManager.getLogger(CommonOperator.class);
    SqlRendererUtility sqlRenderer = new SqlRendererUtility();

    CommonQueries commonQueries = new CommonQueries();

    public boolean userExists(String mailid) throws Exception {
        String sql = commonQueries.userExistsSql(mailid);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> count_map = sqlRenderer.runSelectQuery(sql);
        if (count_map.size() > 0) {
            logger.info(Common.USER_EXISTS + true);
            return true;
        } else {
            logger.info(Common.USER_EXISTS + false);
            return false;
        }
    }



    public boolean correctPasswordEntered(AuthenticationRequest authenticationRequest) throws Exception {
        String sql = commonQueries.correctPasswordEnteredSql(authenticationRequest);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> credMap = sqlRenderer.runSelectQuery(sql);
        String password_from_table = (String) credMap.get(0).get("password");
        logger.info(Common.ORIGINAL_PASSWORD_IS + password_from_table);
        logger.info(Common.PASSWORD_BY_USER + authenticationRequest.getPassword());
        return password_from_table.equals(authenticationRequest.getPassword());
    }

    public boolean sessionExists(String mailid) throws Exception {
        String sql = commonQueries.sessionExistsSql(mailid);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> session_count_map = sqlRenderer.runSelectQuery(sql);
        if (session_count_map.size() > 0) {
            logger.info(Common.SESSION_EXISTS + true);
            return true;
        } else {
            logger.info(Common.SESSION_EXISTS + false);
            return false;
        }
    }
}
