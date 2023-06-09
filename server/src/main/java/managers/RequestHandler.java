package managers;

import commands.Command;
import commands.HelpCommand;
import exceptions.*;
import models.Worker;
import work.*;

public class RequestHandler {
    private CommandManager commandManager;

    public RequestHandler(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public Response handle(Request request) {
        try {
            return commandManager.execute(request);
        } catch (IllegalArgument exception) {
            return new Response(ResponseStatus.WRONG_ARGUMENTS, "Введены неправильные аргументы команды.");
        } catch (CommandRuntime exception) {
            return new Response(ResponseStatus.ERROR, "Ошибка при исполнении программы");
        } catch (NoCommand exception) {
            return new Response(ResponseStatus.ERROR, "Такой команды не существует.");
        } catch (MustExit exception) {
            return new Response(ResponseStatus.EXIT);
        }
    }
}
