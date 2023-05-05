package commands;

import exceptions.*;
import work.*;

/**
 * Интерфейс, реализующий Command Pattern
 */

public interface CommandExecute {
    Response execute(Request request) throws IllegalArgument, CommandRuntime, MustExit;
}
