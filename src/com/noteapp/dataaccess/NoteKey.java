package com.noteapp.dataaccess;

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
