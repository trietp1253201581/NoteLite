package client.service;

/**
 * Exception cho các clientserver service
 * 
 * @author Lê Minh Triết
 * @since 17/04/2024
 * @version 1.0
 */
public class ClientServerServiceErrorException extends Exception {
    private final ClientServerServiceErrorType errorType;
    
    public ClientServerServiceErrorException(ClientServerServiceErrorType errorType) {
        super("ClientServerServiceError");
        this.errorType = errorType;
    }
    
    public ClientServerServiceErrorType getErrorType() {
        return errorType;
    }
}
