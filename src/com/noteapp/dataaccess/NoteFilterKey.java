package com.noteapp.dataaccess;

/**
 *
 * @author admin
 */
public class NoteFilterKey {
    private int noteId;
    private String filter;

    public NoteFilterKey() {
        noteId = -1;
        filter = "";
    }

    public NoteFilterKey(int noteId, String filter) {
        this.noteId = noteId;
        this.filter = filter;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}
