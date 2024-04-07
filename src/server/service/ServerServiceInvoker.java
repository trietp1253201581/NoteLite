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
            case "CreateUser":
                return new CreateUser();
            case "UpdateUser":
                return new UpdateUser();
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
            case "SendNote":
                return new SendNote();
            case "GetAllReceivedNotes":
                return new GetAllReceivedNotes();
            default:
                //Thông báo service không tồn tại
                throw new IllegalArgumentException("This service is unsupported");
        }
    }   
}
