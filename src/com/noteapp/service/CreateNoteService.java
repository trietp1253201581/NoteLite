package com.noteapp.service;

import com.noteapp.dataaccess.BasicDataAccess;
import com.noteapp.dataaccess.DataAccessException;
import com.noteapp.dataaccess.FailedExecuteException;
import com.noteapp.dataaccess.NotExistDataException;
import com.noteapp.dataaccess.NoteDataAccess;
import com.noteapp.dataaccess.NoteKey;
import com.noteapp.dataaccess.NullKey;
import com.noteapp.dataaccess.UserDataAccess;
import com.noteapp.dataaccess.UserKey;
import com.noteapp.model.datatransfer.Note;
import com.noteapp.model.datatransfer.User;
import java.util.List;

/**
 * Tạo một Note mới
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class CreateNoteService implements ServerService<Note> {   
    private Note note;
    protected BasicDataAccess<Note, NoteKey, UserKey> noteDataAccess;
    protected BasicDataAccess<User, UserKey, NullKey> userDataAccess;

    public CreateNoteService() {
        note = new Note();
    }

    public CreateNoteService(Note note) {
        this.note = note;
    }

    public void setNote(Note note) {
        this.note = note;
    }
    
    @Override
    public Note execute() throws DataAccessException {
        noteDataAccess = NoteDataAccess.getInstance();  
        userDataAccess = UserDataAccess.getInstance();
        try {
            noteDataAccess.get(new NoteKey(note.getId()));
            throw new DataAccessException("This note already exists");
        } catch (FailedExecuteException ex1) {
            throw ex1;
        } catch (NotExistDataException ex2) {
            userDataAccess.get(new UserKey(note.getAuthor()));
            List<Note> existNotes = noteDataAccess.getAll();
            Note lastNote = existNotes.getLast();
            note.setId(lastNote.getId() + 1);
            noteDataAccess.add(note);
            return note;
        }
    }    
}