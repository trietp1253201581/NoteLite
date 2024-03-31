package server.networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Một server có thể hoạt động đa luồng, phục vụ nhiều client cùng lúc
 * @author Lê Minh Triết
 * @since 30/03/2024
 * @version 1.0
 */
public class MultiThreadServer implements Server {   
    private ServerSocket serverSocket;
    private int port;
    private ServerStatus status;
    private static final int NUM_OF_THREADS = 20; 
    
    /**
     * Khởi tạo một Server phục vụ đa luồng
     * @param port cổng phục vụ
     * @throws IOException
     */
    public MultiThreadServer(int port) throws IOException {       
        this.port = port;
        this.status = ServerStatus.SUCCESS;
        serverSocket = new ServerSocket(port);        
    }
    
    /**
     * Đóng Server
     */
    @Override
    public void closeServer() {        
        //Chuyển status của Server về CLOSE
        this.status = ServerStatus.CLOSE;
        //Đóng Server
        try {
            serverSocket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }       
    }
    
    /**
     * Xem status của Server
     * @return Một ServerStatus miêu tả trạng thái của server
     */
    @Override
    public ServerStatus getStatus() {
        return status;
    }
    
    /**
     * Chạy Server
     */
    @Override
    public void runServer() {    
        //Tạo NUM_OF_THREADS luồng
        ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);
        //Thực hiện khi trạng thái server không phải CLOSE
        while(status != ServerStatus.CLOSE) {
            //Tạo một socker để connect
            Socket connectSocket;
            try {
                //Đồng ý truy cập
                connectSocket = serverSocket.accept();
                //Xử lý các luồng truy cập
                WorkerThread handler = new WorkerThread(connectSocket);
                executorService.execute(handler);           
            } catch (IOException ex) {
                ex.printStackTrace();
            }         
        }       
    }    
}
