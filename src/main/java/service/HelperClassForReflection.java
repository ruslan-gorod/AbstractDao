package service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class HelperClassForReflection {

    public static String getFieldNames(Class clazz) {
        StringBuilder fieldNames = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            fieldNames.append(field.toString()).append(", ");
        }
        fieldNames.deleteCharAt(fieldNames.length() - 2);
        return fieldNames.toString();
    }

    public static String[] getListFieldNames(Class clazz) {
        List<String> listFieldNames = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            listFieldNames.add(field.toString());
        }
        return (String[]) listFieldNames.toArray();
    }


    public static <T> String getFieldValues(T obj) {
        StringBuilder fieldValues = new StringBuilder();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                fieldValues.append(field.get(obj).toString()).append(", ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        fieldValues.deleteCharAt(fieldValues.length() - 2);
        return fieldValues.toString();
    }

    public static <T> String[] getListFieldValues(T obj){
        List<String> listFieldValue = new ArrayList<>();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                listFieldValue.add(field.get(obj).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return (String[]) listFieldValue.toArray();
    }

    public static <T> String getFieldNamesAndValues(T obj) {
        StringBuilder fieldNamesAndValues = new StringBuilder();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                fieldNamesAndValues.append(field.getName()).append("=").append(field.get(obj).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return fieldNamesAndValues.toString();
    }

    public static <T> String getIdValue(T obj) throws IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        String idValues = "";
        for (Field field : fields) {
            if (field.getName().toUpperCase().equals("ID")){
                return field.get(obj).toString();
            }
        }
        return idValues;
    }
}
