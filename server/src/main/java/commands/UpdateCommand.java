package commands;

import exceptions.*;
import managers.*;
import models.*;
import work.*;

import java.util.Objects;

/**
 * Команда, которая обновляет значение элемента коллекции, ID которого равен заданному
 */

public class UpdateCommand extends Command implements EditCollection {
    private CollectionManager collectionManager;

    public UpdateCommand(CollectionManager collectionManager) {
        super("update", " id {element} : обновить значение элемента коллекции, ID которого равен заданному");
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
            if (Objects.isNull(request.getObject())) {
                return new Response(ResponseStatus.ASK_OBJECT, "Для команды " + this.getName() + " требуется объект.");
            }
            Worker element = request.getObject();
            collectionManager.editById(id, element);
            return new Response(ResponseStatus.OK, "Элемент обновлен.");
        } catch (NoId exception) {
            return new Response(ResponseStatus.ERROR, "В коллекции нет элемента с таким ID.");
        } catch (NumberFormatException exception) {
            return new Response(ResponseStatus.ERROR, "ID должно быть числом типа integer.");
        }
    }
}
