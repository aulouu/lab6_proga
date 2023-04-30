package console;

import exceptions.*;

/**
 * Интерфейс, реализующий Command Pattern
 */

public interface CommandExecute {
    void execute(String args) throws IllegalArgument, CommandRuntime, MustExit;
}
