package commands;

import exceptions.*;
import managers.*;
import work.*;

/**
 * Команда, которая удаляет элемент из коллекции, ШВ которого равен заданному
 */

public class RemoveByIDCommand extends Command implements EditCollection {
    private CollectionManager collectionManager;

    public RemoveByIDCommand(CollectionManager collectionManager) {
        super("remove_by_id", " id : удалить элемент из коллекции, ID которого равен заданному");
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
        class NoId extends RuntimeException {
        }
        try {
            int id = Integer.parseInt(request.getArgs().trim());
            if (!collectionManager.checkId(id)) throw new NoId();
            collectionManager.removeElement(collectionManager.getById(id));
            return new Response(ResponseStatus.OK, "Элемент с таким ID удален.");
        } catch (NoId exception) {
            return new Response(ResponseStatus.ERROR, "В коллекции нет элемента с таким ID.");
        } catch (NumberFormatException exception) {
            return new Response(ResponseStatus.ERROR, "ID должно быть числом типа integer.");
        }
    }
}
