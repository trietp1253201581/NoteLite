package com.noteapp.service;

import com.noteapp.dataaccess.BasicDataAccess;
import com.noteapp.dataaccess.DataAccessException;
import com.noteapp.dataaccess.FailedExecuteException;
import com.noteapp.dataaccess.NotExistDataException;
import com.noteapp.dataaccess.NoteDataAccess;
import com.noteapp.dataaccess.NoteKey;
import com.noteapp.dataaccess.NullKey;
import com.noteapp.dataaccess.ShareNoteDataAccess;
import com.noteapp.dataaccess.ShareNoteKey;
import com.noteapp.dataaccess.UserDataAccess;
import com.noteapp.dataaccess.UserKey;
import com.noteapp.model.datatransfer.Note;
import com.noteapp.model.datatransfer.ShareNote;
import com.noteapp.model.datatransfer.User;


/**
 * Send một Note tới user khác
 * @author Nhóm 23
 * @since 07/04/2024
 * @version 1.0
 */
public class SendNoteService implements ServerService<ShareNote> {
    private ShareNote shareNote;
    protected BasicDataAccess<User, UserKey, NullKey> userDataAccess;
    protected BasicDataAccess<Note, NoteKey, UserKey> noteDataAccess;
    protected BasicDataAccess<ShareNote, ShareNoteKey, ShareNoteKey> shareNoteDataAccess;
    
    public SendNoteService() {
        shareNote = new ShareNote();
    }

    public SendNoteService(ShareNote shareNote) {
        this.shareNote = shareNote;
    }

    public void setShareNote(ShareNote shareNote) {
        this.shareNote = shareNote;
    }
        @Override
    public ShareNote execute() throws DataAccessException {
        userDataAccess = UserDataAccess.getInstance();
        noteDataAccess = NoteDataAccess.getInstance();
        shareNoteDataAccess = ShareNoteDataAccess.getInstance();
        try {
            shareNoteDataAccess.get(new ShareNoteKey(shareNote.getId(), shareNote.getReceiver()));
            shareNoteDataAccess.update(shareNote);
            return shareNote;
        } catch (FailedExecuteException ex1) {
            throw ex1;
        } catch (NotExistDataException ex2) {
            userDataAccess.get(new UserKey(shareNote.getReceiver()));
            noteDataAccess.get(new NoteKey(shareNote.getId()));
            return shareNoteDataAccess.add(shareNote);
        }
    }
}