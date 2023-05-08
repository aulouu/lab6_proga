import commands.*;
import console.EmptyConsole;
import console.Print;
import managers.*;

import java.util.List;

public class App {
    public static int port = 6090;
    public static final int connection_timeout = 60 * 1000;
    private static final Print console = new EmptyConsole();

    public static void main(String[] args) {
        //console = new Console();
        if (args.length != 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException ignored) {
            }
        }
        final String myenv = "LABA";
        FileManager fileManager = new FileManager(console, myenv);
        CollectionManager collectionManager = new CollectionManager(fileManager);
        collectionManager.validateAll();
        CommandManager commandManager = new CommandManager(fileManager, collectionManager);

        commandManager.addCommand(List.of(
                new AddElementCommand(collectionManager),
                new AddIfMinCommand(collectionManager),
                new ClearCommand(collectionManager),
                new ExecuteScriptCommand(),
                new ExitCommand(),
                new FilterByStatusCommand(collectionManager),
                new FilterStartsWithNameCommand(collectionManager),
                new HeadCommand(collectionManager),
                new HelpCommand(commandManager),
                new InfoCommand(collectionManager),
                new RemoveByIDCommand(collectionManager),
                new RemoveGreaterCommand(collectionManager),
                new RemoveLowerCommand(collectionManager),
                new ShowCommand(collectionManager),
                new UpdateCommand(collectionManager)
        ));

        RequestHandler requestHandler = new RequestHandler(commandManager);
        Server server = new Server(port, connection_timeout, requestHandler);
        server.run();
    }
}
