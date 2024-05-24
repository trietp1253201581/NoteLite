package com.noteliteserver.dataaccess;

/**
 *
 * @author admin
 */
public class NotExistDataException extends DataAccessException {  
    public NotExistDataException() {
        super("Data is not exist");
    }
    
    public NotExistDataException(String message) {
        super(message);
    }
}
