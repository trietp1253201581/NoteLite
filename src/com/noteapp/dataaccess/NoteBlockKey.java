package com.noteapp.dataaccess;

/**
 *
 * @author admin
 */
public class NoteBlockKey {
    private int noteId;
    private int blockId;
    private String header;
    private String editor;

    public NoteBlockKey() {
        noteId = -1;
        blockId = -1;
    }

    public NoteBlockKey(int noteId, int blockId, String header, String editor) {
        this.noteId = noteId;
        this.blockId = blockId;
        this.header = header;
        this.editor = editor;
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

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }
}
