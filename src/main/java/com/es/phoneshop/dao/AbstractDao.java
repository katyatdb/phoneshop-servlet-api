package com.es.phoneshop.dao;

import com.es.phoneshop.model.DaoItem;

import java.util.List;
import java.util.NoSuchElementException;

public abstract class AbstractDao<T extends DaoItem> implements Dao<T> {
    private List<T> items;

    public AbstractDao() {
    }

    public void init(List<T> items) {
        this.items = items;
    }

    @Override
    public List<T> getAll() {
        return items;
    }

    @Override
    public <E> T get(E id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public void save(T item) {
        if (item == null || item.getId() == null) {
            throw new IllegalArgumentException();
        }

        boolean noneMatchEqualId = items.stream()
                .noneMatch(item1 -> item1.getId().equals(item.getId()));

        if (noneMatchEqualId) {
            items.add(item);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public <E> void delete(E id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }

        boolean isDeleted = items.removeIf(product -> product.getId().equals(id));
        if (!isDeleted) {
            throw new NoSuchElementException();
        }
    }
}
