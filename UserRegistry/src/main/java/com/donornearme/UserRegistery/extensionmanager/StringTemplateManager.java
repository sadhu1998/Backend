package com.donornearme.UserRegistery.extensionmanager;

import com.donornearme.UserRegistery.Common;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.stringtemplate.v4.STGroupFile;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringTemplateManager {
    protected static final Logger logger = LogManager.getLogger(StringTemplateManager.class);

    public void renderInsertTemplate(Connection connection, String instanceName, String path, HashMap<String, Object> dataMap) {
        try {
            String sql = new STGroupFile
                    (path, Common.ST_DELIMITER, Common.ST_DELIMITER).getInstanceOf(instanceName).add(Common.DATA, dataMap).render();
            logger.info(sql);
            connection.createStatement().executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String renderSelectTemplateAndConvertToTable(Connection connection, String instanceName, String path, HashMap<String, Object> dataMap) {
        String sql = new STGroupFile
                (path, Common.ST_DELIMITER, Common.ST_DELIMITER).getInstanceOf(instanceName).add(Common.DATA, dataMap).render();
        logger.info(sql);
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            List<Map<String, Object>> resultListMap = resultSetToListOfMap(resultSet);
            return convertJRStoTable(resultListMap);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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

    private Map<String, List<String>> resultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        Map<String, List<String>> rowlist = new HashMap<>();
        ArrayList<String> eachrow = new ArrayList<>();

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

    private String convertJRStoTable(List<Map<String, Object>> listOfMaps) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table id= \"getDetialsTable\" style=\"width:100%\">");
        sb.append("<thead id =\"detailsTableHeading\">");
        sb.append("<tr>");
        for (String heading : listOfMaps.get(0).keySet()) {
            sb.append("<td>").append(heading).append("</td>");
        }
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody");
        for (Map<String, Object> aListOfMap : listOfMaps) {
            sb.append("<tr>");
            HashMap<String, Object> map = (HashMap<String, Object>) aListOfMap;
            for (String key : map.keySet()) {
                sb.append("<td>").append(map.get(key)).append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</tbody");
        sb.append("</table>");

        return sb.toString();
    }

    public List<Map<String, Object>> renderSelectTemplate(Connection connection, String instanceName, String path, HashMap<String, Object> dataMap) throws SQLException {
        String sql = new STGroupFile
                (path, Common.ST_DELIMITER, Common.ST_DELIMITER).getInstanceOf(instanceName).add(Common.DATA, dataMap).render();
        logger.info(sql);
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        return resultSetToListOfMap(resultSet);
    }

    public Map<String, List<String>> renderSelectTemplateConverttoList(Connection connection, String instanceName, String path, HashMap<String, Object> dataMap) throws SQLException {
        String sql = new STGroupFile
                (path, Common.ST_DELIMITER, Common.ST_DELIMITER).getInstanceOf(instanceName).add(Common.DATA, dataMap).render();
        logger.info(sql);
        ResultSet resultSet = connection.createStatement().executeQuery(sql);
        return resultSetToList(resultSet);
    }
}
