package commands;

import exceptions.*;
import work.*;

/**
 * Команда, которая завершает программу без сохранения в файл
 */

public class ExitCommand extends Command {

    public ExitCommand() {
        super("exit", " : завершить программу без сохранения в файл");
    }

    /**
     * Исполнение команды
     *
     * @param request аргументы
     */
    @Override
    public Response execute(Request request) {
        return new Response(ResponseStatus.EXIT);
    }
}
