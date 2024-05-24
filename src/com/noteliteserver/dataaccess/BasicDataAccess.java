package com.noteliteserver.dataaccess;

/**
 * Định nghĩa các phương thức thao tác cơ bản với CSDL
 * @author Nhóm 23
 * @param <T> Kiểu datatransfer cho data từ CSDL
 * @since 30/03/2024
 * @version 1.0
 */
public interface BasicDataAccess<T> {
    /**
     * Lấy object theo id
     * @param id id của object cần lấy
     * @return object lấy được
     * @throws com.noteliteserver.dataaccess.DataAccessException nếu không thực thi được
     * hoặc không tồn tại object có id này
     */
    T get(int id) throws DataAccessException;
    
    /**
     * Thêm object element vào CSDL
     * @param element object cần thêm vào CSDL
     * @throws com.noteliteserver.dataaccess.DataAccessException nếu không thực thi được
     */
    void add(T element) throws DataAccessException;
    
    /**
     * Chỉnh sửa một object element trong CSDL
     * @param element object cần chỉnh sửa
     * @throws com.noteliteserver.dataaccess.DataAccessException nếu không thực thi được
     */
    void update(T element) throws DataAccessException;
    
    /**
     * Xóa một object element ra khỏi CSDL
     * @param id id của object cần xóa
     * @throws com.noteliteserver.dataaccess.DataAccessException nếu không thực thi được
     */
    void delete(int id) throws DataAccessException;
}