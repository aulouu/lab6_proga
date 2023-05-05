package commands;

import exceptions.*;
import managers.*;
import models.*;
import work.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Команда, которая удаляет из коллекции все элементы, ID которых меньше, чем заданный
 */

public class RemoveLowerCommand extends Command implements EditCollection {
    private CollectionManager collectionManager;

    public RemoveLowerCommand(CollectionManager collectionManager) {
        super("remove_lower", " id : удалить из коллекции все элементы, ID которых меньше, чем заданный");
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
        if (request.getArgs().isBlank()) throw new IllegalArgument();
        try {
            int id = Integer.parseInt(request.getArgs().trim());
            Collection<Worker> remove = collectionManager.getCollection().stream()
                    .filter(Objects::nonNull)
                    .filter(worker -> worker.getId() < id)
                    .toList();
            collectionManager.removeElements(remove);
            if (remove.isEmpty()) {
                return new Response(ResponseStatus.ERROR, "Нет элементов, у которых ID меньше, чем заданный.");
            } else return new Response(ResponseStatus.OK, "Элементы, ID которых меньше, чем заданный, удалены.");
        } catch (NumberFormatException exception) {
            return new Response(ResponseStatus.ERROR, "ID должно быть числом типа integer.");
        }
    }
}
