package commands;

import exceptions.*;
import managers.*;
import models.*;
import work.*;

import java.util.Objects;

/**
 * Команда, которая добавляет новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции
 */

public class AddIfMinCommand extends Command implements EditCollection {
    private CollectionManager collectionManager;

    public AddIfMinCommand(CollectionManager collectionManager) {
        super("add_if_min", " {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекци");
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
        if (element.compareTo(collectionManager.getCollection().stream()
                .filter(Objects::nonNull)
                .min(Worker::compareTo)
                .orElse(null)) >= 1) {
            collectionManager.addElement(element);
            return new Response(ResponseStatus.OK, "Элемент успешно добавлен");
        } else {
            return new Response(ResponseStatus.ERROR, "Элемент не соответствует условиям команды.");
        }
    }
}

