package com.noteapp.service;

import com.noteapp.dataaccess.BasicDataAccess;
import com.noteapp.dataaccess.DataAccessException;
import com.noteapp.dataaccess.FailedExecuteException;
import com.noteapp.dataaccess.NotExistDataException;
import com.noteapp.dataaccess.NullKey;
import com.noteapp.dataaccess.UserDataAccess;
import com.noteapp.dataaccess.UserKey;
import com.noteapp.model.datatransfer.User;

/**
 * Tạo một User mới
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class CreateUserService implements ServerService<User> {    
    private User user;
    protected BasicDataAccess<User, UserKey, NullKey> userDataAccess;
    
    public CreateUserService() {
        user = new User();
    }

    public CreateUserService(User user) {
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public User execute() throws DataAccessException {       
        //Tạo một đối tượng access dữ liệu
        userDataAccess = UserDataAccess.getInstance();
        try {
            userDataAccess.get(new UserKey(user.getUsername()));
            throw new DataAccessException("This user is already exist!");
        } catch (FailedExecuteException ex1) {
            throw ex1;
        } catch (NotExistDataException ex2) {
            return userDataAccess.add(user);
        }
    }    
}