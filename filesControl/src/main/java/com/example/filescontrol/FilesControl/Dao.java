package com.example.filescontrol.FilesControl;

import java.util.List;

public interface Dao<Object> {

    /**
     * @return list of objects
     */
    List<Object> getAll();

    /**
     * @param t add new object
     */
    void save(Object t);

    /**
     * @param t updateble object
     * @param params params for update
     */
    void update(Object t, String[] params);

    /**
     * @param t deletable object
     */
    void delete(Object t);
}
