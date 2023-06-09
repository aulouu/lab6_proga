package managers;

import console.*;
import exceptions.*;
import forms.WorkerForm;
import models.Worker;
import work.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Класс для работы с вводом пользователя
 */

public class RuntimeManager {
    private final Print console;
    private final Scanner scanner;
    private final Client client;
    private static List<Path> Stack = new LinkedList<>();

    public RuntimeManager(Print console, Scanner scanner, Client client) {
        this.console = console;
        this.scanner = scanner;
        this.client = client;
    }

    public void printResponse(Response response) {
        switch (response.getResponseStatus()) {
            case OK -> {
                if ((Objects.isNull(response.getCollection()))) {
                    console.println(response.getResponse());
                } else {
                    console.println(response.getResponse() + "\n" + response.getCollection().toString());
                }
            }
            case ERROR -> console.printError(response.getResponse());
            case WRONG_ARGUMENTS -> console.printError("Неверное использование команды.");
            default -> {
            }
        }
    }

    /**
     * Работа с пользователем и выполнение команд
     */
    public void runTime() {
        //Scanner scanner = ScannerManager.getScanner();
        while (true) {
            try {
                if (!scanner.hasNext()) throw new MustExit();
                String[] userCommand = (scanner.nextLine().trim() + " ").split(" ", 2);
                Response response = client.sendAndAskResponse(new Request(userCommand[0].trim(), userCommand[1].trim()));
                this.printResponse(response);
                switch (response.getResponseStatus()) {
                    case ASK_OBJECT -> {
                        ScannerManager.setScanner(scanner);
                        Worker worker = new WorkerForm(console).build();
                        if (!worker.validate()) throw new InvalidForm();
                        Response newResponse = client.sendAndAskResponse(
                                new Request(
                                        userCommand[0].trim(),
                                        userCommand[1].trim(),
                                        worker));
                        if (newResponse.getResponseStatus() != ResponseStatus.OK) {
                            console.printError(newResponse.getResponse());
                        } else {
                            this.printResponse(newResponse);
                        }
                    }
                    case EXIT -> throw new MustExit();
                    case EXECUTE_SCRIPT -> {
                        ScannerManager.setFileMode();
                        this.script(response.getResponse());
                        ScannerManager.setUserMode();
                    }
                    default -> {
                    }
                }
            } catch (NoSuchElementException exception) {
                console.printError("Пользовательский ввод не обнаружен.");
            } catch (InvalidForm exception) {
                console.printError("Поля не валидны. Объект не создан.");
            } catch (MustExit exception) {
                console.printError("Выход из программы. Bye!");
                return;
            } catch (IOException exception) {
                console.printError("Неизвестная ошибка.");
            }
        }
    }

    public void script(String fileName) throws MustExit {
        Stack.add(Paths.get(fileName));
        try (Scanner scrScanner = new Scanner(new File(fileName))) {
            Scanner tmpScanner = ScannerManager.getScanner();
            ScannerManager.setScanner(scrScanner);
            ScannerManager.setFileMode();
            do {
                String[] userCmd = (scrScanner.nextLine().trim() + " ").split(" ", 2);
                while (scrScanner.hasNextLine() && userCmd[0].isEmpty()) {
                    userCmd = (scrScanner.nextLine().trim() + " ").split(" ", 2);
                }
                if (userCmd[0].equals("execute_script")) {
                    if (Stack.contains(Paths.get(userCmd[1].trim())))
                        throw new RecursionScript();
                }
                console.println("$ " + String.join(" ", userCmd));
                Response response = client.sendAndAskResponse(new Request(userCmd[0].trim(), userCmd[1].trim()));
                this.printResponse(response);
                switch (response.getResponseStatus()) {
                    case ASK_OBJECT -> {
                        Worker worker = new WorkerForm(console).build();
                        if (!worker.validate()) throw new InvalidForm();
                        Response newResponse = client.sendAndAskResponse(
                                new Request(
                                        userCmd[0].trim(),
                                        userCmd[1].trim(),
                                        worker));
                        if (newResponse.getResponseStatus() != ResponseStatus.OK) {
                            console.printError(newResponse.getResponse());
                        } else {
                            this.printResponse(newResponse);
                        }
                    }
                    case EXIT -> throw new MustExit();
                    case EXECUTE_SCRIPT -> {
                        this.script(response.getResponse());
                    }
                    default -> {
                    }
                }
            } while (scrScanner.hasNextLine());
            ScannerManager.setScanner(tmpScanner);
            ScannerManager.setUserMode();
        } catch (FileNotFoundException exception) {
            console.printError("Файл не найден.");
        } catch (NoSuchElementException exception) {
            console.printError("Файл пустой.");
        } catch (RecursionScript exception) {
            Stack.clear();
            console.printError("Скрипт не может вызваться рекурсивно.");
        } catch (InvalidForm exception) {
            console.printError("Поля в файле не валидны. Объект не создан.");
        } catch (IOException exception) {
            console.printError("Неизвестная ошибка.");
        }
    }
}
