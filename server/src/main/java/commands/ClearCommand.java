package commands;

import exceptions.*;
import managers.*;
import work.*;

/**
 * Команда, которая очищает коллекцию
 */

public class ClearCommand extends Command implements EditCollection {
    private CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        super("clear", " : очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * Исполнение команды
     *
     * @param request аргументы
     * @throws IllegalArgument неверные аргументы команды
     */
    @Override
    public Response execute(Request request) throws IllegalArgument {
        if (!request.getArgs().isBlank()) throw new IllegalArgument();
        collectionManager.clear();
        return new Response(ResponseStatus.OK, "Коллекция очищена.");
    }
}
