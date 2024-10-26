package com.noteapp.service;

import com.noteapp.dataaccess.BasicDataAccess;
import com.noteapp.dataaccess.DataAccessException;
import com.noteapp.dataaccess.ShareNoteDataAccess;
import com.noteapp.dataaccess.ShareNoteKey;
import com.noteapp.model.datatransfer.ShareNote;
import java.util.List;

/**
 * Lấy tất cả các ShareNote được share tới user
 * @author Nhóm 23
 * @since 07/04/2024
 * @version 1.0
 */
public class GetAllReceivedNotesService implements CollectionServerService<ShareNote> {
    private String receiver;
    protected BasicDataAccess<ShareNote, ShareNoteKey, ShareNoteKey> shareNoteDataAccess;
    
    public GetAllReceivedNotesService() {
        receiver = "";
    }
    
    public GetAllReceivedNotesService(String receiver) {
        this.receiver = receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
    
    @Override
    public List<ShareNote> execute() throws DataAccessException {
        shareNoteDataAccess = ShareNoteDataAccess.getInstance();
        return shareNoteDataAccess.getAll(new ShareNoteKey(-1, receiver));
    }
}