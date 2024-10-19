/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.noteliteserver.dataaccess;

/**
 *
 * @author admin
 */
public class ShareNoteKey {
    private int noteId;
    private String receiver;
    
    public ShareNoteKey() {
        noteId = -1;
        receiver = "";
    }
    
    public ShareNoteKey(int noteId, String receiver) {
        this.noteId = noteId;
        this.receiver = receiver;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
