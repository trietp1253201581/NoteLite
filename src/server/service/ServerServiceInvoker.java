package server.service;

/**
 * Gọi các service phù hợp phụ thuộc vào request
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class ServerServiceInvoker {   

    /**
     * Gọi service dựa trên đầu vào
     * @param serviceName String đại diện cho tên của service được gọi
     * @return thể hiện của service được yêu cầu
     */
    public static ServerService invoke(String serviceName) {
        switch (serviceName) {
            case "CheckLogin":
                return new CheckLogin();
            case "CreateAccount":
                return new CreateAccount();
            case "UpdateAccount":
                return new UpdateAccount();
            case "CreateNote":
                return new CreateNote();
            case "DeleteNote":
                return new DeleteNote();
            case "OpenNote":
                return new OpenNote();
            case "OpenLastNote":
                return new OpenLastNote();
            case "GetAllNotes":
                return new GetAllNotes();
            case "SaveNote":
                return new SaveNote();
            default:
                //Thông báo service không tồn tại
                throw new IllegalArgumentException("This service is unsupported");
        }
    }   
}
