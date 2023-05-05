package commands;

import exceptions.*;
import managers.*;
import models.Worker;
import work.*;

import java.util.Objects;

/**
 * Команда, которая добавляет новый элемент в коллекцию
 */

public class AddElementCommand extends Command implements EditCollection {
    private CollectionManager collectionManager;

    public AddElementCommand(CollectionManager collectionManager) {
        super("add", " {element} : добавить новый элемент в коллекцию");
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
        } else {
            request.getObject().setId(Worker.incNextId());
            collectionManager.addElement(request.getObject());
            return new Response(ResponseStatus.OK, "Элемент успешно добавлен");
        }
    }
}
