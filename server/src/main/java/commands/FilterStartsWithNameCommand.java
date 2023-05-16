package commands;

import exceptions.*;
import managers.*;
import work.*;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Команда, которая выводит элементы, значение поля name которых начинается с заданной подстроки
 */

public class FilterStartsWithNameCommand extends Command {
    private CollectionManager collectionManager;

    public FilterStartsWithNameCommand(CollectionManager collectionManager) {
        super("filter_starts_with_name", " {name} : вывести элементы, значение поля name которых начинается с заданной подстроки");
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
        //var str = String.valueOf(request.getArgs().trim());
        return new Response(ResponseStatus.OK, "Соответствующие элементы: " +
                collectionManager.getCollection().stream()
                        .filter(Objects::nonNull)
                        .filter(worker -> worker.getName().startsWith(request.getArgs()))
                        .map(Objects::toString).collect(Collectors.joining(", ")));
        //collectionManager.filterStartsWithName();
    }
}
