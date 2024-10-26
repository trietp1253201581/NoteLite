package com.noteapp.service;

import com.noteapp.dataaccess.BasicDataAccess;
import com.noteapp.dataaccess.DataAccessException;
import com.noteapp.dataaccess.NullKey;
import com.noteapp.dataaccess.UserDataAccess;
import com.noteapp.dataaccess.UserKey;
import com.noteapp.model.datatransfer.User;

/**
 * Kiểm tra thông tin đăng nhập
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class CheckLoginService implements ServerService<User> {  
    private String username;
    private String password;
    protected BasicDataAccess<User, UserKey, NullKey> userDataAccess;
    
    public CheckLoginService() {
        username = "";
        password = "";
    }

    public CheckLoginService(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Override
    public User execute() throws DataAccessException {
        userDataAccess = UserDataAccess.getInstance();
        User user = userDataAccess.get(new UserKey(username));
        if(password.equals(user.getPassword())) {
            return user;
        } else {
            throw new DataAccessException("Password is false!");
        }
    }    
}