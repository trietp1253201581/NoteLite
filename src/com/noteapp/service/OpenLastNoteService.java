package com.noteapp.service;

import com.noteapp.dataaccess.BasicDataAccess;
import com.noteapp.dataaccess.DataAccessException;
import com.noteapp.dataaccess.NoteDataAccess;
import com.noteapp.dataaccess.NoteKey;
import com.noteapp.dataaccess.UserKey;
import com.noteapp.model.datatransfer.Note;
import java.util.List;

/**
 * Mở note được chỉnh sửa gần nhất bởi user
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class OpenLastNoteService implements ServerService<Note> {   
    private String author;
    protected BasicDataAccess<Note, NoteKey, UserKey> noteDataAccess;
    
    public OpenLastNoteService() {
        author = "";
    }

    public OpenLastNoteService(String author) {
        this.author = author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public Note execute() throws DataAccessException { 
        noteDataAccess = NoteDataAccess.getInstance();
        List<Note> notes = noteDataAccess.getAll(new UserKey(author));
        return notes.getLast();
    }    
}