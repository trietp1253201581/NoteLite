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
    public static ServerService invoke(String serviceName) throws IllegalArgumentException {
        switch (serviceName) {
            case "CheckLogin" -> {
                return new CheckLoginService();
            }
            case "CreateUser" -> {
                return new CreateUserService();
            }
            case "UpdateUser" -> {
                return new UpdateUserService();
            }
            case "CreateNote" -> {
                return new CreateNoteService();
            }
            case "DeleteNote" -> {
                return new DeleteNoteService();
            }
            case "OpenNote" -> {
                return new OpenNoteService();
            }
            case "OpenLastNote" -> {
                return new OpenLastNoteService();
            }
            case "GetAllNotes" -> {
                return new GetAllNotesService();
            }
            case "SaveNote" -> {
                return new SaveNoteService();
            }
            case "SendNote" -> {
                return new SendNoteService();
            }
            case "GetAllReceivedNotes" -> {
                return new GetAllReceivedNotesService();
            }
            default -> //Thông báo service không tồn tại
                throw new IllegalArgumentException("This service is unsupported");
        }
    }   
}
