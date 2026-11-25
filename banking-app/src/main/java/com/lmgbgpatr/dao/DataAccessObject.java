package com.lmgbgpatr.dao;

import java.util.List;

public interface DataAccessObject<T> {

    /**
    * Retrieves an object of type {@code T} with the corresponding ID.
    * @param id the object's ID
    * @return the matching object or null if no match
    */
    T getByID(int id);

    /**
    * Retrieves all objects of type {@code T}.
    * @return a list of objects
    */
    List<T> getAll();

    /**
    * Inserts an object of type {@code T} into its corresponding table.
    * @param t the object to insert
    */
    void insert(T t);

    /**
    * Deletes the object of type {@code T} with the matching ID.
    * @param id the ID of the object to delete
    */
    void deleteByID(int id);
}
