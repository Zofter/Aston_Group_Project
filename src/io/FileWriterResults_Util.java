package io;

import com.google.gson.Gson;
import model.Person;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;

public class FileWriterResults_Util {
    public static void writeResults(Path file, Collection<Person> persons, Person searchPerson, String message) throws IOException {
        Gson gson = new Gson();
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write("Коллекция:");
            writer.newLine();
            // 1. Записываем коллекцию как JSON-line
            for (Person person : persons) {
                gson.toJson(person, writer);
                writer.newLine();
            }
            // 2. Записываем строку
            writer.write("_____________");
            writer.newLine();
            writer.write("Искомый элемент: ");
            gson.toJson(searchPerson, writer);
            writer.write(" -> ");
            writer.write(message);
            writer.newLine();
            // 3. Записываем разделитель
            writer.write("-----------------------------------------------------------");
            writer.newLine();
        }
    }
}
