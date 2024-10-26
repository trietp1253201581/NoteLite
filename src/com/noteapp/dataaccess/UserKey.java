package com.noteapp.dataaccess;

/**
 *
 * @author admin
 */
public class UserKey {
    private String username;

    public UserKey(String username) {
        this.username = username;
    }

    public UserKey() {
        username = "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
