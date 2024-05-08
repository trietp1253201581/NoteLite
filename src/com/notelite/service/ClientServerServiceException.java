package com.notelite.service;

/**
 * Exception cho các clientserver service
 * 
 * @author Lê Minh Triết
 * @since 17/04/2024
 * @version 1.0
 */
public class ClientServerServiceException extends Exception {
    private final ClientServerService.ErrorType errorType;
    
    public ClientServerServiceException(ClientServerService.ErrorType errorType) {
        super("ClientServerServiceError");
        this.errorType = errorType;
    }
    
    public ClientServerService.ErrorType getErrorType() {
        return errorType;
    }
}