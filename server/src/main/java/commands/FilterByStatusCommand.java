package commands;

import exceptions.*;
import managers.*;
import models.*;
import work.*;

import java.util.Objects;

/**
 * Команда, которая выводит элементы, значение поля status которых равно заданному
 */

public class FilterByStatusCommand extends Command {
    private CollectionManager collectionManager;

    public FilterByStatusCommand(CollectionManager collectionManager) {
        super("filter_by_status", " status : вывести элементы, значение поля status которых равно заданному");
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
            var status = Status.valueOf(request.getArgs().trim());
            return new Response(ResponseStatus.OK, "Элемент с заданным статусом: " +
                    collectionManager.getCollection().stream()
                            .filter(Objects::nonNull)
                            .filter(worker -> worker.getStatus().equals(status))
                            .map(Objects::toString));
            //collectionManager.filterByStatus();
        } catch (IllegalArgumentException exception) {
            return new Response(ResponseStatus.ERROR, "Такой статус недоступен.");
        }
    }
}
