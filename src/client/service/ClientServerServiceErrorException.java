package client.service;

/**
 * Exception cho các clientserver service
 * 
 * @author Lê Minh Triết
 * @since 17/04/2024
 * @version 1.0
 */
public class ClientServerServiceErrorException extends Exception {
    private final ClientServerService.ErrorType errorType;
    
    public ClientServerServiceErrorException(ClientServerService.ErrorType errorType) {
        super("ClientServerServiceError");
        this.errorType = errorType;
    }
    
    public ClientServerService.ErrorType getErrorType() {
        return errorType;
    }
}