/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.noteliteserver.dataaccess;

/**
 *
 * @author admin
 */
public class NoteKey {
    private int id;

    public NoteKey() {
        id = -1;
    }

    public NoteKey(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
