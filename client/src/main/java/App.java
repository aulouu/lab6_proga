import console.EmptyConsole;
import console.Print;
import exceptions.IllegalArgument;
import managers.*;

import java.util.Scanner;

public class App {
    private static Print console = new EmptyConsole();
    private static String host;
    private static int port;

    private static boolean initializeConnectionAddress(String[] hostAndPortArgs) {
        try {
            if (hostAndPortArgs.length != 2)
                throw new IllegalArgument("Хост и порт необходимо передать в формате <host> <port>");
            host = hostAndPortArgs[0];
            port = Integer.parseInt(hostAndPortArgs[1]);
            if (port < 0) throw new IllegalArgument("Порт не может быть отрицательным.");
            return true;
        } catch (IllegalArgument exception) {
            console.printError(exception.getMessage());
        }
        return false;
    }

    public static void main(String[] args) {
        if (!initializeConnectionAddress(args)) return;
        //console = new Console();
        Client client = new Client(host, port, 10000, 5, console);
        new RuntimeManager(console, new Scanner(System.in), client).runTime();
    }
}
