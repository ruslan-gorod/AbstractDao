package dao;

import exceptions.GetByIdException;
import exceptions.ParseResultSetException;
import exceptions.SaveToDbException;
import exceptions.UpdateException;
import exceptions.WrongIdException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import service.HelperClassForReflection;
import service.QuerySql;
import org.apache.log4j.Logger;

public abstract class AbstractDao<T, ID> implements GenericDao<T, ID> {

    private static final Logger logger = Logger.getLogger(AbstractDao.class);
    protected final Connection connection;
    private Class clazz;

    protected AbstractDao(Connection connection, Class clazz) {
        this.connection = connection;
        this.clazz = clazz;
    }

    @Override
    public T save(T t) throws SaveToDbException {
        String insertQuery = QuerySql.createInsetQuery(t);
        String getQuery = QuerySql.createGetAllQuery(t);
        try (Statement statement = connection.createStatement()) {
            statement.execute(insertQuery);
            ResultSet resultSet = statement.executeQuery(getQuery);
            if (resultSet.next()) {
                return fromResultSetToClass(resultSet);
            }
        } catch (Exception e) {
            logger.error("Error save " + t.getClass().getName() + "\r\nError sql query (insert) " + insertQuery, e);
        }
        throw new SaveToDbException();
    }

    @Override
    public T get(ID id) throws GetByIdException {
        String getQuery = QuerySql.createGetByIdQuery(id, clazz);
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(getQuery);
            if (resultSet.next()) {
                return fromResultSetToClass(resultSet);
            }
        } catch (SQLException | ParseResultSetException e) {
            logger.error(("Error sql query (get by id) = " + getQuery), e);
        }
        throw new GetByIdException();
    }

    @Override
    public T update(T t) throws WrongIdException, IllegalAccessException, UpdateException {
        String updateQuery = QuerySql.createUpdateQuery(clazz);
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(updateQuery);
            if (resultSet.next()) {
                return fromResultSetToClass(resultSet);
            }
        } catch (SQLException | ParseResultSetException e) {
            logger.error(("Error sql query (update) = " + updateQuery), e);
        }
        throw new UpdateException();
    }

    @Override
    public void delete(ID t) {
        String deleteQuery = QuerySql.createDeleteByIdQuery(t,clazz);
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(deleteQuery);
        } catch (SQLException e) {
            logger.error("Error sql query (delete) = " + deleteQuery, e);
        }
    }

    @Override
    public List<T> getAll() throws ParseResultSetException {
        String getAllQuery = QuerySql.createGetAllQuery(clazz);

        try (Statement statement = connection.createStatement()) {
            List<T> list = new ArrayList<>();
            ResultSet resultSet = statement.executeQuery(getAllQuery);
            while (resultSet.next()) {
                list.add(fromResultSetToClass(resultSet));
            }
            return list;
        } catch (SQLException e) {
            logger.error("Error sql query (getAll) " + getAllQuery, e);
        }
        return Collections.emptyList();    }

    private T fromResultSetToClass(ResultSet resultSet) throws ParseResultSetException {
        String[] fieldsName = HelperClassForReflection.getListFieldNames(clazz);
        Field[] fields = clazz.getDeclaredFields();
        try {
            T newClass = (T) clazz.newInstance();
            for (int i = 0; i < fieldsName.length; i++) {
                fields[i].setAccessible(true);
                fields[i].set(newClass, resultSet.getObject(fieldsName[i]));
            }
            return newClass;
        } catch (InstantiationException e) {
            logger.error("Wrong type " + clazz.getName(), e);
        } catch (IllegalAccessException e) {
            logger.error("Wrong fields in " + clazz.getName(), e);
        } catch (SQLException e) {
            logger.error("Error parse result of SQL query " + clazz.getName(), e);
        }
        throw new ParseResultSetException();
    }
}