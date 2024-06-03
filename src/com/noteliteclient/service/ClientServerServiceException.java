package com.noteliteclient.service;

/**
 * Exception cho các clientserver service
 * 
 * @author Nhóm 23
 * @since 17/04/2024
 * @version 1.0
 */
public class ClientServerServiceException extends Exception {
    public ClientServerServiceException(String message) {
        super(message);
    }
}