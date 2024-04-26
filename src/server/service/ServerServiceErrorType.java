package server.service;

/**
 * Các lỗi có thể gặp với các service
 * @author Lê Minh Triết
 * @since 10/04/2024
 * @version 1.0
 */
public enum ServerServiceErrorType {
    ALREADY_EXISTS,
    NOT_EXISTS,
    CAN_NOT_EXECUTE, 
    FALSE_INFORMATION,
    UNSUPPORTED_SERVICE;
}
