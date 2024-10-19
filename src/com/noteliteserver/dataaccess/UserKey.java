/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.noteliteserver.dataaccess;

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
