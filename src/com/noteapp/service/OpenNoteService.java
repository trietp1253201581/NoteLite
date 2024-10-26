package com.noteapp.service;

import com.noteapp.dataaccess.BasicDataAccess;
import com.noteapp.dataaccess.DataAccessException;
import com.noteapp.dataaccess.NoteDataAccess;
import com.noteapp.dataaccess.NoteKey;
import com.noteapp.dataaccess.UserKey;
import com.noteapp.model.datatransfer.Note;

/**
 * Mở note với header và author cho trước
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class OpenNoteService implements ServerService<Note> {    
    private int noteId;
    protected BasicDataAccess<Note, NoteKey, UserKey> noteDataAccess;
    
    public OpenNoteService() {
        noteId = -1;
    }

    public OpenNoteService(int noteId) {
        this.noteId = noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }
 
    @Override
    public Note execute() throws DataAccessException {        
        noteDataAccess = NoteDataAccess.getInstance();
        return noteDataAccess.get(new NoteKey(noteId));
    }    
}