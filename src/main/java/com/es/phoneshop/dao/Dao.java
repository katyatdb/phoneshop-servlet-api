package com.es.phoneshop.dao;

import java.util.List;

public interface Dao<T> {
    List<T> getAll();

    <E> T get(E id);

    void save(T item);

    <E> void delete(E id);
}
