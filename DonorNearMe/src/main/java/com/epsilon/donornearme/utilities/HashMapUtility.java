package com.epsilon.donornearme.utilities;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class HashMapUtility {
    public static HashMap<String, Object> getMap(HashMap<String, Object> sourceMap, String key) {
        if (sourceMap == null || key == null) {
            return null;
        }
        String[] parts = key.split("\\.");
        int i = 0;
        while (i < parts.length - 1) {
            sourceMap = getMap(sourceMap, parts[i]);
            i++;
        }
        if (sourceMap.get(parts[i]) != null) {
            if (sourceMap.get(parts[i]) instanceof HashMap) {
                return (HashMap<String, Object>) sourceMap.get(parts[i]);
            }
        }
        return null;
    }

    public static ArrayList getArrayList(HashMap<String, Object> sourceMap, String key) {
        if (sourceMap == null || key == null) {
            return null;
        }
        String[] parts = key.split("\\.");
        int i = 0;
        while (i < parts.length - 1) {
            sourceMap = getMap(sourceMap, parts[i]);
            i++;
        }
        if (sourceMap.get(parts[i]) != null) {
            if (sourceMap.get(parts[i]) instanceof ArrayList) {
                return (ArrayList) sourceMap.get(parts[i]);
            }
        }
        return null;
    }

    public static String getString(HashMap<String, Object> sourceMap, String key) {
        if (sourceMap == null || key == null) {
            return null;
        }
        String[] parts = key.split("\\.");
        int i = 0;
        while (i < parts.length - 1) {
            sourceMap = getMap(sourceMap, parts[i]);
            i++;
        }
        if (sourceMap.get(parts[i]) != null) {
            if (sourceMap.get(parts[i]) instanceof String) {
                return (String) sourceMap.get(parts[i]);
            }
        }
        return null;
    }

    public static Integer getInteger(HashMap<String, Object> sourceMap, String key) {
        if (sourceMap == null || key == null) {
            return null;
        }
        String[] parts = key.split("\\.");
        int i = 0;
        while (i < parts.length - 1) {
            sourceMap = getMap(sourceMap, parts[i]);
            i++;
        }
        if (sourceMap.get(parts[i]) != null) {
            if (sourceMap.get(parts[i]) instanceof Integer) {
                return Integer.valueOf(sourceMap.get(parts[i]).toString());
            }
        }
        return null;
    }

    public static Long getLong(HashMap<String, Object> sourceMap, String key) {
        if (sourceMap == null || key == null) {
            return null;
        }
        String[] parts = key.split("\\.");
        int i = 0;
        while (i < parts.length - 1) {
            sourceMap = getMap(sourceMap, parts[i]);
            i++;
        }
        if (sourceMap.get(parts[i]) != null) {
            if (sourceMap.get(parts[i]) instanceof Long || sourceMap.get(parts[i]) instanceof Integer) {
                return Long.valueOf(sourceMap.get(parts[i]).toString());
            }
        }
        return null;
    }

    public static Boolean getBoolean(HashMap<String, Object> sourceMap, String key) {
        if (sourceMap == null || key == null) {
            return null;
        }
        String[] parts = key.split("\\.");
        int i = 0;
        while (i < parts.length - 1) {
            sourceMap = getMap(sourceMap, parts[i]);
            i++;
        }
        if (sourceMap.get(parts[i]) != null) {
            if (sourceMap.get(parts[i]) instanceof Boolean) {
                return (Boolean) sourceMap.get(parts[i]);
            }
        }
        return null;
    }

    public static Object get(HashMap<String, Object> sourceMap, String key) {
        if (sourceMap == null || key == null) {
            return null;
        }
        String[] parts = key.split("\\.");
        int i = 0;
        while (i < parts.length - 1) {
            sourceMap = getMap(sourceMap, parts[i]);
            i++;
        }
        if (sourceMap.get(parts[i]) != null) {
            return sourceMap.get(parts[i]);
        }
        return null;
    }

    public static void put(HashMap<String, Object> sourceMap, String key, Object value) throws Exception {
        if (sourceMap == null) {
            throw new NullPointerException("EXP_SOURCE_MAP_ARGUMENT_NULL");
        }
        if (key == null) {
            throw new NullPointerException("EXP_KEY_ARGUMENT_NULL");
        }
        String[] parts = key.split("\\.");
        int i = 0;
        while (i < parts.length - 1) {
            sourceMap = getMap(sourceMap, parts[i]);
            i++;
        }
        if (sourceMap != null) {
            sourceMap.put(parts[i], value);
        } else {
            throw new Exception("EXP_PARENT_MAPS_NOT_FOUND");
        }
    }

    public static String getNonEmptyString(HashMap<String, Object> sourceMap, String key) throws Exception {
        String val = getString(sourceMap, key);
        if (StringUtils.isBlank(val)) {
            throw new Exception("EXP_NULL_OR_EMPTY_VALUE" + key);
        }
        return val;
    }
}
