package com.epsilon.donornearme.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtility {
    public JSONUtility() {
    }

    public static boolean isJSON(String json) {
        boolean retval = true;

        try {
            new JSONObject(json);
        } catch (JSONException var3) {
            retval = false;
        }

        return retval;
    }

    public static HashMap<String, Object> jsonToMap(String json) throws JSONException {
        HashMap<String, Object> retMap = new HashMap();
        if (json != null) {
            JSONObject jsonObject = new JSONObject(json);
            retMap = toMap(jsonObject);
        }

        return retMap;
    }

    public static HashMap<String, Object> jsonToMap(JSONObject json) throws JSONException {
        HashMap<String, Object> retMap = new HashMap();
        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }

        return retMap;
    }

    public static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
        HashMap map = new HashMap();
        Iterator keysItr = object.keys();

        while(keysItr.hasNext()) {
            String key = (String)keysItr.next();
            Object value = object.get(key);
            if (value instanceof JSONArray) {
                value = toList((JSONArray)value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject)value);
            }

            if (value.equals(JSONObject.NULL)) {
                map.put(key, (Object)null);
            } else {
                map.put(key, value);
            }
        }

        return map;
    }

    public static ArrayList<Object> toList(JSONArray array) throws JSONException {
        ArrayList<Object> list = new ArrayList();

        for(int i = 0; i < array.length(); ++i) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray)value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject)value);
            }

            list.add(value);
        }

        return list;
    }

    public static String object2JsonString(Object object) {
        return object2JsonString(object, 0, 0);
    }

    public static String object2JsonString(Object object, int indentSize, int indentLevel) {
        if (object instanceof String) {
            return "\"" + quoteQuote(object) + "\"";
        } else if (object instanceof Map) {
            return map2JsonString((Map)object, indentSize, indentLevel);
        } else if (object instanceof List) {
            return list2JsonString((List)object, indentSize, indentLevel);
        } else {
            return object == null ? "null" : object.toString();
        }
    }

    private static String quoteQuote(Object object) {
        return object.toString().replaceAll("\"", "\\\\\"");
    }

    public static String map2JsonString(Map<String, Object> map) {
        return map2JsonString(map, 0);
    }

    public static String map2JsonString(Map<String, Object> map, int indentSize) {
        return map2JsonString(map, indentSize, 0);
    }

    public static String map2JsonString(Map<String, Object> map, int indentSize, int indentLevel) {
        StringBuilder string = new StringBuilder();
        if (map.size() == 0) {
            string.append("{}");
        } else {
            String key;
            for(Iterator var4 = map.keySet().iterator(); var4.hasNext(); string.append(object2JsonString(key)).append(" : ").append(object2JsonString(map.get(key), indentSize, indentLevel))) {
                key = (String)var4.next();
                if (string.length() == 0) {
                    string.append(indentSize == 0 ? "{ " : "{\n");
                    if (indentSize > 0) {
                        ++indentLevel;
                        indent(string, indentSize, indentLevel);
                    }
                } else {
                    string.append(indentSize == 0 ? ", " : ",\n");
                    indent(string, indentSize, indentLevel);
                }
            }

            string.append(indentSize == 0 ? " }" : "\n");
            if (indentSize > 0) {
                --indentLevel;
                indent(string, indentSize, indentLevel);
                string.append("}");
            }
        }

        return string.toString();
    }

    private static void indent(StringBuilder string, int indentSize, int indentLevel) {
        for(int j = 0; j < indentLevel; ++j) {
            for(int i = 0; i < indentSize; ++i) {
                string.append(" ");
            }
        }

    }

    public static String list2JsonString(List<Object> list) {
        return list2JsonString(list, 0);
    }

    public static String list2JsonString(List<Object> list, int indentSize) {
        return list2JsonString(list, indentSize, 0);
    }

    public static String list2JsonString(List<Object> list, int indentSize, int indentLevel) {
        StringBuilder string = new StringBuilder();
        if (list.size() == 0) {
            string.append("[]");
        } else {
            Object value;
            for(Iterator var4 = list.iterator(); var4.hasNext(); string.append(object2JsonString(value, indentSize, indentLevel))) {
                value = var4.next();
                if (string.length() == 0) {
                    string.append(indentSize == 0 ? "[ " : "[\n");
                    if (indentSize > 0) {
                        ++indentLevel;
                        indent(string, indentSize, indentLevel);
                    }
                } else {
                    string.append(indentSize == 0 ? ", " : ",\n");
                    indent(string, indentSize, indentLevel);
                }
            }

            string.append(indentSize == 0 ? " ]" : "\n");
            if (indentSize > 0) {
                --indentLevel;
                indent(string, indentSize, indentLevel);
                string.append("]");
            }
        }

        return string.toString();
    }
}

