package managers;

import commands.Command;
import commands.EditCollection;
import exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import work.Request;
import work.Response;

import java.util.Collection;
import java.util.HashMap;

/**
 * Командный менеджер - класс для управления командами
 */

public class CommandManager {
    private final HashMap<String, Command> commands = new HashMap<>();
    private final CollectionManager collectionManager;
    private final FileManager fileManager;
    static final Logger commandManagerLogger = LoggerFactory.getLogger(CommandManager.class);

    public CommandManager(FileManager fileManager, CollectionManager collectionManager) {
        this.fileManager = fileManager;
        this.collectionManager = collectionManager;
    }

    /**
     * Добавляет команду
     *
     * @param command комманда
     */
    public void addCommand(Command command) {
        this.commands.put(command.getName(), command);
        commandManagerLogger.info("Добавлена команда " + command);
    }

    /**
     * @return коллекция команд
     */
    public Collection<Command> getCommands() {
        return commands.values();
    }

    /**
     * @param request аргументы
     * @throws IllegalArgument неверные аргументы команды
     * @throws NoCommand       такой команды нет
     * @throws CommandRuntime  ошибка при исполнении команды
     * @throws MustExit        обязательный выход из программы
     */
    public Response execute(Request request) throws IllegalArgument, NoCommand, CommandRuntime, MustExit {
        Command command = commands.get(request.getCommandName());
        if (command == null) throw new NoCommand();
        Response response = command.execute(request);
        if (command instanceof EditCollection) {
            fileManager.saveCollection(collectionManager.getCollection());
        }
        return response;
    }
}
