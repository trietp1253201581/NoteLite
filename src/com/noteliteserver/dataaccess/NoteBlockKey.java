/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.noteliteserver.dataaccess;

/**
 *
 * @author admin
 */
public class NoteBlockKey {
    private int noteId;
    private int blockId;

    public NoteBlockKey() {
        noteId = -1;
        blockId = -1;
    }

    public NoteBlockKey(int noteId, int blockId) {
        this.noteId = noteId;
        this.blockId = blockId;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }
}
