package com.calanger.tools.util;

import org.apache.commons.lang.StringUtils;

import com.raddle.jdbc.meta.table.ColumnInfo;

public class PropertyNameUtils {
    public static String getPropertyName(String columnName) {
        char[] chars = columnName.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '_' && i < chars.length - 1) {
                sb.append(Character.toUpperCase(chars[i + 1]));
                i++;//
            } else if (c != '_') {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }

    public static String getMethodNameSuffixFromColumnName(String columnName) {
        return getMethodNameSuffix(getPropertyName(columnName));
    }

    public static String getMethodNameSuffix(String propName) {
        if (Character.isUpperCase(propName.charAt(0))) {
            if (propName.length() == 1 || Character.isLowerCase(propName.charAt(1))) {
                throw new IllegalArgumentException("非法属性名[" + propName + "]");
            }
            return propName;
        } else {
            if (propName.length() > 1 && Character.isUpperCase(propName.charAt(1))) {
                return propName;
            } else {
                return StringUtils.capitalize(propName);
            }
        }
    }

    public static String getClassName(String tableName) {
        return StringUtils.capitalize(getPropertyName(tableName));
    }

    public static String getClassName(String tableName, String prefix, String suffix) {
        return StringUtils.defaultString(prefix) + StringUtils.capitalize(getPropertyName(tableName))
                + StringUtils.defaultString(suffix);
    }

    public static String getJavaType(ColumnInfo colunmInfo) {
        if ("CHAR".equals(colunmInfo.getColumnTypeName()) || "VARCHAR".equals(colunmInfo.getColumnTypeName())
                || "VARCHAR2".equals(colunmInfo.getColumnTypeName())) {
            return "String";
        } else if ("DECIMAL".equals(colunmInfo.getColumnTypeName()) || "NUMBER".equals(colunmInfo.getColumnTypeName())
                || "INT".equals(colunmInfo.getColumnTypeName())) {
            if (colunmInfo.getScale() > 0) {
                return "java.math.BigDecimal";
            } else if (colunmInfo.getLength() < 10) {
                return "Integer";
            } else {
                return "Long";
            }
        } else if ("DATE".equals(colunmInfo.getColumnTypeName())) {
            return "java.util.Date";
        } else {
            return "Unknown[" + colunmInfo.getColumnTypeName() + "]";
        }
    }
}
