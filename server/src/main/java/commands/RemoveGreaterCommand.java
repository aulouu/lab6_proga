package commands;

import exceptions.*;
import managers.*;
import models.*;
import work.*;

import java.util.Objects;
import java.util.Collection;

/**
 * Команда, которая удаляет из коллекции все элементы, превышающие заданный
 */

public class RemoveGreaterCommand extends Command implements EditCollection {
    private CollectionManager collectionManager;

    public RemoveGreaterCommand(CollectionManager collectionManager) {
        super("remove_greater", " {element} : удалить из коллекции элементы, превышающие заданный");
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
        if (Objects.isNull(request.getObject())) {
            return new Response(ResponseStatus.ASK_OBJECT, "Для команды " + this.getName() + " требуется объект.");
        }
        Worker element = request.getObject();
        Collection<Worker> remove = collectionManager.getCollection().stream()
                .filter(Objects::nonNull)
                .filter(worker -> worker.compareTo(element) >= 1)
                .toList();
        collectionManager.removeElements(remove);
        if (remove.isEmpty()) {
            return new Response(ResponseStatus.ERROR, "Нет элементов, превышающие заданный.");
        } else return new Response(ResponseStatus.OK, "Элементы, превышающие заданный, удалены.");
    }
}
