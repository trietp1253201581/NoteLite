package com.noteapp.service;

import com.noteapp.dataaccess.BasicDataAccess;
import com.noteapp.dataaccess.DataAccessException;
import com.noteapp.dataaccess.NoteDataAccess;
import com.noteapp.dataaccess.NoteKey;
import com.noteapp.dataaccess.UserKey;
import com.noteapp.model.datatransfer.Note;

import java.util.List;

/**
 * Lấy tất cả các note của một user
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class GetAllNotesService implements CollectionServerService<Note> {   
    private String author;
    protected BasicDataAccess<Note, NoteKey, UserKey> noteDataAccess;
    
    public GetAllNotesService() {
        author = "";
    }
    
    public GetAllNotesService(String author) {
        this.author = author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
  
    @Override
    public List<Note> execute() throws DataAccessException {       
        noteDataAccess = NoteDataAccess.getInstance();
        return noteDataAccess.getAll(new UserKey(author));
    }   
}