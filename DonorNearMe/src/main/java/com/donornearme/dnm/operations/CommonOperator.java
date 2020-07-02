package com.donornearme.dnm.operations;

import com.donornearme.dnm.Common;
import com.donornearme.dnm.extensionsmanager.SQLManager;
import com.donornearme.dnm.models.request.AuthenticationRequest;
import com.donornearme.dnm.sqls.SqlUtility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class CommonOperator {
    protected static final Logger logger = LogManager.getLogger(CommonOperator.class);
    SQLManager sqlManager = new SQLManager();

    SqlUtility sqlUtility = new SqlUtility();

    public boolean userExists(String mailid) throws Exception {
        String sql = sqlUtility.userExistsSql(mailid);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> count_map = sqlManager.runSelectQuery(sql);
        if (count_map.size() > 0) {
            logger.info(Common.USER_EXISTS + true);
            return true;
        } else {
            logger.info(Common.USER_EXISTS + false);
            return false;
        }
    }

    public boolean correctPasswordEntered(AuthenticationRequest authenticationRequest) throws Exception {
        String sql = sqlUtility.correctPasswordEnteredSql(authenticationRequest);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> credMap = sqlManager.runSelectQuery(sql);
        String password_from_table = (String) credMap.get(0).get("password");
        logger.info(Common.ORIGINAL_PASSWORD_IS + password_from_table);
        logger.info(Common.PASSWORD_BY_USER + authenticationRequest.getPassword());
        return password_from_table.equals(authenticationRequest.getPassword());
    }

    public boolean sessionExists(String mailid) throws Exception {
        String sql = sqlUtility.sessionExistsSql(mailid);
        logger.info(Common.EXECUTING_SQL + sql);
        List<Map<String, Object>> session_count_map = sqlManager.runSelectQuery(sql);
        if (session_count_map.size() > 0) {
            logger.info(Common.SESSION_EXISTS + true);
            return true;
        } else {
            logger.info(Common.SESSION_EXISTS + false);
            return false;
        }
    }
}
