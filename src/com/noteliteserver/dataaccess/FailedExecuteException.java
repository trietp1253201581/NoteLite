package com.noteliteserver.dataaccess;

/**
 * Ngoại lệ không thể thực thi câu lệnh SQL
 * @author Nhóm 23
 * @since 23/05/2024
 * @version 1.0
 */
public class FailedExecuteException extends DataAccessException {
    public FailedExecuteException() {
        super("Can't execute!");
    }
    
    public FailedExecuteException(String message) {
        super(message);
    }
}
