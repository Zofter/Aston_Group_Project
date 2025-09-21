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
import java.util.Collection;

public class FileWriterJSON_Util {
    // JSON Lines формат
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