package managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import console.*;
import models.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.NoSuchElementException;

/**
 * Файл менеджер - класс для работы с файлом
 */

public class FileManager {
    private final Print console;
    private final String myenv;
    File file;
    static final Logger fileManagerLogger = LoggerFactory.getLogger(FileManager.class);

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public FileManager(Print console, String file_path) {
        this.console = console;
        this.myenv = file_path;
        try {
            this.file = new File(System.getenv(myenv));
        } catch (NullPointerException exception) {
            fileManagerLogger.error("Системная переменная LABA с загрузочным файлом не найдена! Добавьте системную переменную LABA и попробуйте вновь.");
            System.exit(0);
        } catch (Exception exception) {
            fileManagerLogger.error("Что-то пошло не так. Перезапустите программу.");
        }
    }

    /**
     * Обращение к переменным среды и чтение файла
     *
     * @return коллекция
     */
    public Collection<Worker> readCollection() {
        if (System.getenv(myenv) != null) {
            if (file.exists() && !file.canRead()) {
                fileManagerLogger.error("Недостаточно прав для чтения данных из файла. Добавьте права на чтение и запустите программу вновь.");
                System.exit(0);
            }
            try (var fileReader = new InputStreamReader(new FileInputStream(System.getenv(myenv)))) {
                var collectionType = new TypeToken<ArrayDeque<Worker>>() {
                }.getType();
                var reader = new BufferedReader(fileReader);
                var jsonStr = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (!line.equals("")) {
                        jsonStr.append(line);
                    }
                }

                if (jsonStr.length() == 0) {
                    jsonStr = new StringBuilder("[]");
                }

                ArrayDeque<Worker> collection = gson.fromJson(jsonStr.toString(), collectionType);
                fileManagerLogger.info("Коллекция загружена из файла.");
                return collection;

            } catch (FileNotFoundException exception) {
                fileManagerLogger.error("Файл не найден.");
                return new ArrayDeque<>();
            } catch (NoSuchElementException exception) {
                fileManagerLogger.error("Файл пустой.");
                System.exit(0);
            } catch (JsonParseException exception) {
                fileManagerLogger.error("В файле не найдена необходимая коллекция.");
                System.exit(0);
            } catch (IOException | IllegalStateException exception) {
                fileManagerLogger.error("Непредвиденная ошибка!");
                System.exit(0);
            }
        } else {
            fileManagerLogger.info("Системная переменная LABA с загрузочным файлом не найдена!");
            System.exit(0);
        }
        return new ArrayDeque<>();
    }
    // $ file_path=aboba java -jar sus.jar (aboba путь до файла)
    /*  set A=b
        echo %A% ---> b (выводит b)  */

    /**
     * Сохранение коллекции из менеджера в файл
     */
    public boolean saveCollection(Collection<Worker> collection) {
        if (System.getenv(myenv) != null) {
            if (!file.exists()) {
                try {
                    Files.createDirectories(Paths.get(System.getenv(myenv)).getParent());
                    file.createNewFile();
                } catch (IOException exception) {
                    fileManagerLogger.error("Невозможно создать файл.");
                    return false;
                }

            }
            if (file.exists() && !file.canRead()) {
                fileManagerLogger.error("Недостаточно прав для чтения данных из файла. Добавьте права на чтение и запустите программу вновь.");
            }
            try {
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(System.getenv(myenv)));
                bufferedOutputStream.write(gson.toJson(collection).getBytes(StandardCharsets.UTF_8));
                bufferedOutputStream.close();
                return true;
            } catch (FileNotFoundException exception) {
                fileManagerLogger.error("Файл не существует.");
            } catch (IOException exception) {
                fileManagerLogger.error("Произошла непредвиденная ошибка. Коллекция не сохранена.");
            }
        } else {
            fileManagerLogger.error("Системная переменная LABA с загрузочным файлом не найдена!");
        }
        return false;
    }
}
