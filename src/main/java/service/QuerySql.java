package service;

import exceptions.WrongIdException;

public class QuerySql {

    public static <T> String createInsetQuery(T obj) {

        return "INSERT INTO " +
                obj.getClass().getName().toUpperCase() +
                " (" +
                HelperClassForReflection.getFieldNames(obj.getClass()) +
                ") VALUES (" +
                HelperClassForReflection.getFieldValues(obj) +
                ");";
    }

    public static <T> String createUpdateQuery(T obj) throws IllegalAccessException, WrongIdException {
        String idValue = HelperClassForReflection.getIdValue(obj);
        if (idValue.equals("")) {
            throw new WrongIdException();
        }
        return "UPDATE " +
                obj.getClass().getName().toUpperCase() +
                " SET " +
                HelperClassForReflection.getFieldNamesAndValues(obj) +
                " WHERE ID=" +
                idValue +
                ";";
    }

    public static <ID, T> String createDeleteByIdQuery(ID id, T obj){
        return "DELETE FROM " +
                obj.getClass().getName().toUpperCase() +
                " WHERE ID=" +
                id +
                ";";
    }

    public static <ID, T> String createGetByIdQuery(ID id, T obj){
        return "SELECT " +
                HelperClassForReflection.getFieldNames(obj.getClass()) +
                " FROM " +
                obj.getClass().getName().toUpperCase() +
                " WHERE ID=" +
                id +
                ";";
    }

    public static <T> String createGetAllQuery(T obj){
        return "SELECT " +
                HelperClassForReflection.getFieldNames(obj.getClass()) +
                " FROM " +
                obj.getClass().getName().toUpperCase();
    }
}
