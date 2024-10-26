package com.noteapp.dataaccess;

/**
 * Ngoại lệ cho việc access dữ liệu
 * @author Nhóm 23
 * @since 23/05/2024
 * @version 1.0
 */
public class DataAccessException extends Exception {
    public DataAccessException(String message) {
        super(message);
    }
}