package com.noteliteserver.dataaccess;

/**
 *
 * @author admin
 */
public class FailedExecuteException extends DataAccessException {
    public FailedExecuteException() {
        super("Can't execute!");
    }
    
    public FailedExecuteException(String message) {
        super(message);
    }
}
