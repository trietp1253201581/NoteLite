package com.noteapp.service;

import com.noteapp.dataaccess.BasicDataAccess;
import com.noteapp.dataaccess.DataAccessException;
import com.noteapp.dataaccess.NullKey;
import com.noteapp.dataaccess.UserDataAccess;
import com.noteapp.dataaccess.UserKey;
import com.noteapp.model.datatransfer.User;


/**
 * Cập nhật User
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class UpdateUserService implements ServerService<User> {    
    private User user;
    protected BasicDataAccess<User, UserKey, NullKey> userDataAccess;
    
    public UpdateUserService() {
        user = new User();
    }

    public UpdateUserService(User user) {
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User execute() throws DataAccessException {
        userDataAccess = UserDataAccess.getInstance();
        userDataAccess.update(user);
        return user;
    }    
}