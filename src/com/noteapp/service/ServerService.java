package com.noteapp.service;

import com.noteapp.dataaccess.DataAccessException;

/**
 * Định nghĩa các phương thức xử lý service bên server
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public interface ServerService<T> {   

    /**
     * Thực thi service
     * @return Kết quả của việc thực thi
     */
    T execute() throws DataAccessException;   
}