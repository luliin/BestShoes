package model;

import java.util.List;
import java.util.Optional;

/**
 * Created by Julia Wigenstedt
 * Date: 2021-02-21
 * Time: 21:18
 * Project: ShoesJavaFx
 * Copyright: MIT
 */
public interface IDao<T> {

    Optional<T> get(long id);

    List<T> getAll();

    void save(T t);

    void update(T t, String[] parameters);

    void delete(T t);
}
