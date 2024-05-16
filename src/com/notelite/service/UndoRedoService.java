package com.notelite.service;

import java.util.Stack;

/**
 * Cung cấp các phương thức để undo, redo văn bản
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class UndoRedoService {
    //Sử dụng stack để lưu trữ các văn bản
    private final Stack<String> undoStack;
    private final Stack<String> redoStack;
    
    public UndoRedoService() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }
    
    /**
     * Lưu text hiện tại
     * @param text văn bản được lưu
     */
    public void saveText(String text) {
        undoStack.push(text);
    }
    
    /**
     * Lấy text được lưu gần nhất
     * @return text được lưu gần nhất
     */
    public String getLastText() {       
        if(undoStack.size() > 1) {
            return undoStack.peek();
        } else {
            return "Empty!!!";
        }       
    }
    
    /**
     * Thực hiện thao tác undo văn bản
     * @return trả về văn bản sau khi undo nếu undo được, hoặc trả về {@code Can't undo} nếu không undo được
     */
    public String undo() {
        if(undoStack.size() > 1) {
            //Lấy văn bản mới nhất ra khỏi undoStack và cho vào redoStack
            String text = undoStack.pop();    
            redoStack.push(text);
            //Trả về văn bản mới nhất trong undoStack
            return undoStack.peek();
        } else {
            return "Can't undo";
        }
    }
    
    /**
     * Thực hiện thao tác redo văn bản
     * @return trả về văn bản sau khi redo nếu redo được, hoặc trả về {@code Can't redo} nếu không redo được
     */
    public String redo() {
        if(!redoStack.isEmpty()) {
            //Lấy văn bản gần nhất được undo (ở đầu redoStack) và cho vào undoStack
            String text = redoStack.pop();       
            undoStack.push(text);
            //Trả về văn bản vừa được redo
            return text;
        } else {
            return "Can't redo";
        }
    }   
}