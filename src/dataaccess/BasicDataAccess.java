package dataaccess;

/**
 * Định nghĩa các phương thức thao tác cơ bản với CSDL
 * @author Lê Minh Triết
 * @param <T> Kiểu datatransfer cho data từ CSDL
 * @since 30/03/2024
 * @version 1.0
 */
public interface BasicDataAccess<T> {
    /**
     * Lấy object theo id
     * @param id id của object cần lấy
     * @return (1) object lấy được nếu id tồn tại, (2) giá trị default nếu id không tồn tại
     */
    T get(int id);
    
    /**
     * Thêm object element vào CSDL
     * @param element object cần thêm vào CSDL
     * @return (1) một số tự nhiên biểu thị row count khi thực thi lệnh SQL này,
     * (2) -1 nếu không thực hiện được
     */
    int add(T element);
    
    /**
     * Chỉnh sửa một object element trong CSDL
     * @param element object cần chỉnh sửa
     * @return (1) một số tự nhiên biểu thị row count khi thực thi lệnh SQL này,
     * (2) -1 nếu không thực hiện được
     */
    int update(T element);
    
    /**
     * Xóa một object element ra khỏi CSDL
     * @param id id của object cần xóa
     * @return (1) một số tự nhiên biểu thị row count khi thực thi lệnh SQL này,
     * (2) -1 nếu không thực hiện được
     */
    int delete(int id);
}