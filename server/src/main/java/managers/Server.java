package managers;

import console.*;
import exceptions.*;
import work.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {
    private int port;
    private int soTimeout;
    private Print console = new EmptyConsole();
    private ServerSocket serverSocket;
    private RequestHandler requestHandler;
    private FileManager fileManager;

    static final Logger serverLogger = LoggerFactory.getLogger(Server.class);

    public Server(int port, int soTimeout, RequestHandler requestHandler) {
        this.port = port;
        this.soTimeout = soTimeout;
        this.requestHandler = requestHandler;
    }

    /**
     * Открывает серверный сокет
     */
    private void open() throws OpeningServer {
        try {
            serverLogger.info("Запуск сервера...");
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(soTimeout);
            serverLogger.info("Сервер запущен успешно.");
        } catch (IllegalArgumentException exception) {
            console.printError("Порт " + port + "недоступен.");
            serverLogger.error("Порт " + port + "недоступен.");
            throw new OpeningServer();
        } catch (IOException exception) {
            console.printError("Ошибка при использовании порта " + port);
            serverLogger.error("Ошибка при использовании порта " + port);
            throw new OpeningServer();
        }
    }

    /**
     * Подключение к клиенту
     */
    private Socket connectToClient() throws ConnectionError, SocketTimeoutException {
        try {
            Socket clientSocket = serverSocket.accept();
            console.println("Соединение с клиентом установлено успешно.");
            serverLogger.info("Соединение с клиентом установлено успешно.");
            return clientSocket;
        } catch (SocketTimeoutException exception) {
            console.printError("Превышено время ожидания подключения.");
            serverLogger.error("Превышено время ожидания подключения.");
            throw new SocketTimeoutException();
        } catch (IOException exception) {
            console.printError("Произошла ошибка при соединении с клиентом.");
            serverLogger.error("Произошла ошибка при соединении с клиентом.");
            throw new ConnectionError();
        }
    }

    /**
     * Получение запроса от клиента
     */
    private boolean processClientRequest(Socket clientSocket) {
        Request userRequest = null;
        Response responseToUser = null;
        try (ObjectInputStream clientReader = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.getOutputStream())) {
            do {
                userRequest = (Request) clientReader.readObject();
                responseToUser = requestHandler.handle(userRequest);
                serverLogger.info("Запрос " + userRequest.getCommandName() + " обработан успешно.");
                clientWriter.writeObject(responseToUser);
                clientWriter.flush();
            } while (responseToUser.getResponseStatus() != ResponseStatus.EXIT);
            return false;
        } catch (ClassNotFoundException exception) {
            console.printError("Произошла ошибка при чтении данных.");
            serverLogger.error("Произошла ошибка при чтении данных.");
        } catch (InvalidClassException | NotSerializableException exception) {
            console.printError("Произошла ошибка при отправке данных.");
            serverLogger.error("Произошла ошибка при отправке данных.");
        } catch (IOException exception) {
            if (userRequest == null) {
                console.printError("Разрыв соединения с клиентом.");
                serverLogger.error("Разрыв соединения с клиентом.");
            } else {
                console.printError("Клиент отключен от сервера успешно.");
                serverLogger.info("Клиент отключен от сервера успешно.");
            }
        }
        return true;
    }

    /**
     * Начало работы сервера
     */
    public void run() {
        try {
            open();
            boolean processingStatus = true;
            while (processingStatus) {
                try (Socket clientSocket = connectToClient()) {
                    processingStatus = processClientRequest(clientSocket);
                } catch (ConnectionError | SocketTimeoutException exception) {
                    break;
                } catch (IOException exception) {
                    console.printError("Произошла ошибка при попытке завершить соединение с клиентом.");
                    serverLogger.error("Произошла ошибка при попытке завершить соединение с клиентом.");
                }
            }
            stop();
        } catch (OpeningServer exception) {
            console.printError("Сервер не может быть запущен.");
            serverLogger.error("Сервер не может быть запущен.");
        }
    }

    /**
     * Заканчивает работу сервера
     */
    private void stop() {
        try {
            serverLogger.info("Завершение работы сервера...");
            if (serverSocket == null) throw new ClosingSocket();
            serverSocket.close();
            console.println("Работа сервера успешно завершена.");
            serverLogger.info("Работа сервера успешно завершена.");
        } catch (ClosingSocket exception) {
            console.printError("Нельзя завершить работу незапущенного сервера.");
            serverLogger.error("Нельзя завершить работу незапущенного сервера.");
        } catch (IOException exception) {
            console.printError("Произошла ошибка при завершении работы сервера.");
            serverLogger.error("Произошла ошибка при завершении работы сервера.");
        }
    }
}
