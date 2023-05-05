package managers;

import console.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.net.ServerSocket;

public class Server {
    private int port;
    private int soTimeout;
    private Print console = new EmptyConsole();
    private ServerSocket serverSocket;
    private RequestHandler requestHandler;
    private FileManager fileManager;

    static final Logger serverLogger = LoggerFactory.getLogger(Server.class);

    public Server(int port, int soTimeout, RequestHandler requestHandler, FileManager fileManager) {
        this.port = port;
        this.soTimeout = soTimeout;
        this.requestHandler = requestHandler;
        this.fileManager = fileManager;
    }


}
