package com.noteliteserver.network;

import com.noteliteserver.service.ServerRequestProcessor;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Một server có thể hoạt động đa luồng, phục vụ nhiều client cùng lúc
 * @author Nhóm 23
 * @since 30/03/2024
 * @version 1.0
 */
public class MultiThreadServer implements Server {    
    private final ServerSocket serverSocket;
    private final int port;
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
        serverSocket = new ServerSocket(this.port);        
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
            System.err.println(ex);
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
            //Tạo một socket để connect
            Socket connectSocket;
            try {
                //Đồng ý truy cập
                connectSocket = serverSocket.accept();
                //Xử lý các luồng truy cập
                WorkerThread handler = new WorkerThread(connectSocket);
                //Set bộ xử lý và chạy luồng
                handler.setServerRequestProcessor(ServerRequestProcessor.getInstance());
                executorService.execute(handler);           
            } catch (IOException ex) {
                System.out.println(ex);
            }         
        }       
    }    
}