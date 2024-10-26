package com.noteliteclient.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class cung cấp các phương tức dể chuyển Note sang PDF
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class PdfService {   

    /**
     * Export Note sang PDF
     * @param outputFile tên file sau khi export
     * @param content nội dung cần export
     * @throws java.io.FileNotFoundException
     * @throws com.itextpdf.text.DocumentException
     */
    public static void export(String outputFile, String content) throws FileNotFoundException, DocumentException {     
        //Tạo 1 document mới
        Document document = new Document();
        //Tạo một PDF Writer để export
        PdfWriter.getInstance(document, new FileOutputStream(outputFile));
        //Mở, viết, đóng document
        document.open();      
        document.add(new Paragraph(content));           
        document.close();    
    }
    
    /**
     * Đọc 1 file PDF và lấy nội dung ở trang cụ thể
     * @param inputFile đường dẫn file cần đọc
     * @param page trang cần đọc
     * @return nội dung đọc từ file
     * @throws java.io.IOException
     */
    public static String read(String inputFile, int page) throws IOException {       
        //Tạo 1 PdfReader để đọc file PDF
        PdfReader pdfReader = new PdfReader(inputFile);          
        String content = PdfTextExtractor.getTextFromPage(pdfReader, page);
        return content;     
    }    
    
    public static int getNumberOfPage(String inputFile) throws IOException {
        PdfReader pdfReader = new PdfReader(inputFile);
        return pdfReader.getNumberOfPages();
    }
}