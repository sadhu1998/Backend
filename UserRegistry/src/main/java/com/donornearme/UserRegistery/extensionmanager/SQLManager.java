package com.donornearme.UserRegistery.extensionmanager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLManager {
    DBConnectionManager dbConnectionManager = new DBConnectionManager();
    public List<Map<String, Object>> renderSelectQuery(String sql) {
        try {
            Connection connection = dbConnectionManager.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            connection.close();
            return resultSetToListOfMap(rs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Map<String, Object>> resultSetToListOfMap(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        while (rs.next()) {
            Map<String, Object> row = new HashMap<String, Object>(columns);
            for (int i = 1; i <= columns; ++i) {
                row.put(md.getColumnName(i), rs.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }

    public void renderInsertQuery(String sql) {
        try {
            Connection connection = dbConnectionManager.getConnection();
            connection.createStatement().executeUpdate(sql);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
