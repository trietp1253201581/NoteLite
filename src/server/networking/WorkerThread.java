package server.networking;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import server.service.ServerRequestProcessor;

/**
 * Cung cấp các phương thức để xử lý luồng truy cập
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class WorkerThread extends Thread {    
    private final Socket socket;
    private ServerRequestProcessor serverRequestProcessor;
    
    /**
     * Khởi tạo một đối tượng để xử lý luồng tạo bởi một socket
     * @param socket
     */
    public WorkerThread(Socket socket) {
        this.socket = socket;
    }
    
    public void setServerRequestProcessor(ServerRequestProcessor serverRequestProcessor) {
        this.serverRequestProcessor = serverRequestProcessor;
    }
    
    /**
     * Xử lý luồng, viết đè phương thức run của {@link Thread}
     */
    @Override
    public void run() {
        //Thông báo đang xử lý socket nào
        System.out.println("Processing: " + socket);
        
        try {
            //Tạo các stream để gửi và nhận
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            
            while(true) {
                //Nhận dữ liệu từ client
                String str = dataInputStream.readUTF();
                //Nếu đó là thông báo kết thúc thì dừng xử lý
                if(str.equals("end")){
                    break;
                }
                //Thông báo thông điệp
                System.out.println(socket + " say " + str);
                //Chuyển thành RequestCommand và xử lý
                String rs = serverRequestProcessor.process(str);  
                //Trả về kết quả
                dataOutputStream.writeUTF(rs);
                dataOutputStream.flush();
            }
            //Đóng các stream
            dataInputStream.close();
            dataOutputStream.close();
            //Thông báo hoàn thành việc xử lý socket này
            System.out.println("Complete: " + socket); 
            socket.close();
        } catch (IOException ex) {
            System.err.println(ex);
        }                    
    }  
}