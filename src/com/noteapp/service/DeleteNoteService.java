package com.noteapp.service;

import com.noteapp.dataaccess.BasicDataAccess;
import com.noteapp.dataaccess.DataAccessException;
import com.noteapp.dataaccess.NoteDataAccess;
import com.noteapp.dataaccess.NoteKey;
import com.noteapp.dataaccess.ShareNoteDataAccess;
import com.noteapp.dataaccess.ShareNoteKey;
import com.noteapp.dataaccess.UserKey;
import com.noteapp.model.datatransfer.Note;
import com.noteapp.model.datatransfer.ShareNote;
import java.util.List;

/**
 * Xóa một Note đã có
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class DeleteNoteService implements ServerService<Note> {    
    private int noteId;
    protected BasicDataAccess<Note, NoteKey, UserKey> noteDataAccess;
    protected BasicDataAccess<ShareNote, ShareNoteKey, ShareNoteKey> shareNoteDataAccess;
    
    public DeleteNoteService() {
        noteId = -1;
    }

    public DeleteNoteService(int noteId) {
        this.noteId = noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    @Override
    public Note execute() throws DataAccessException {    
        noteDataAccess = NoteDataAccess.getInstance();
        shareNoteDataAccess = ShareNoteDataAccess.getInstance();
        Note deleteNote = noteDataAccess.get(new NoteKey(noteId));
        if(deleteNote.getLastModified() == 1) {
            List<Note> remainNotes = noteDataAccess.getAll(new UserKey(deleteNote.getAuthor()));
            if(remainNotes.size() >= 2) {
                Note newLastNote = remainNotes.get(remainNotes.size()-2);
                newLastNote.setLastModified(1);
                noteDataAccess.update(newLastNote);
            }
        }
        shareNoteDataAccess.deleteAll(new ShareNoteKey(noteId, ""));
        noteDataAccess.delete(new NoteKey(noteId));
        return deleteNote;
    } 
}