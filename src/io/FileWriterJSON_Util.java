package io;

import collection.CustomCollection;
import com.google.gson.Gson;
import model.Person;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileWriterJSON_Util {
    /**
     * Записывает коллекцию объектов Person в файл в формате JSON Lines
     * Каждый объект записывается на отдельной строке в JSON-формате
     * Файл создается или дополняется, если уже существует
     *
     * @param file путь к файлу для записи
     * @param persons коллекция объектов Person для записи
     * @throws IOException если произошла ошибка ввода-вывода при записи файла
     */
    public static void writePersons(Path file, CustomCollection<Person> persons) throws IOException {
        Gson gson = new Gson();
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            for (Person person : persons) {
                gson.toJson(person, writer);
                writer.newLine();
            }
        }
    }
}