package dao;

import exceptions.GetByIdException;
import exceptions.ParseResultSetException;
import exceptions.SaveToDbException;
import exceptions.UpdateException;
import exceptions.WrongIdException;
import java.util.List;

public interface GenericDao<T, ID> {

    T save(T t) throws SaveToDbException;

    T get(ID id) throws GetByIdException;

    T update(T t) throws WrongIdException, IllegalAccessException, UpdateException;

    void delete(ID id);

    List<T> getAll() throws ParseResultSetException;
}
