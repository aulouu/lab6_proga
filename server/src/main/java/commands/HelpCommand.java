package commands;

import exceptions.*;
import managers.*;
import work.*;

/**
 * Команда, которая выводит информацию о всех остальных командах
 */

public class HelpCommand extends Command {
    private CommandManager commandManager;

    public HelpCommand(CommandManager commandManager) {
        super("help", " : вывести информацию о всех остальных командах");
        this.commandManager = commandManager;
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
        return new Response(ResponseStatus.OK,
                String.join("\n",
                        commandManager.getCommands()
                                .stream().map(Command::toString).toList()));
    }
}
