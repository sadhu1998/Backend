package com.epsilon.donornearme.utilities;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlRendererUtility {
    DBConnectionManager dbConnectionManager = new DBConnectionManager();

    public List<Map<String, Object>> runSelectQuery(String sql) {
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

    public Map<String, List<String>> runSelectQueryReturnMapOfList(String sql) {
        try {
            Connection connection = dbConnectionManager.getConnection();
            ResultSet rs = connection.createStatement().executeQuery(sql);
            connection.close();
            return resultSetToList(rs);
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

    public void runInsertQuery(String sql) {
        try {
            Connection connection = dbConnectionManager.getConnection();
            connection.createStatement().executeUpdate(sql);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<String, List<String>> resultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        Map<String, List<String>> rowlist = new HashMap<>();

        while (rs.next()) {
            for (int i = 1; i <= columns; i++) {
                List<String> templist = new ArrayList<>();
                if (!rowlist.containsKey(md.getColumnName(i))) {
                    templist.add(rs.getObject(i).toString());
                    rowlist.put(md.getColumnName(i), templist);
                } else {
                    templist = rowlist.get(md.getColumnName(i));
                    templist.add(rs.getObject(i).toString());
                    rowlist.put(md.getColumnName(i), templist);
                }
            }
        }
        return rowlist;
    }
}
