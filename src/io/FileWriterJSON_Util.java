package io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Person;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

public class FileWriterJSON_Util {

    public static void writePersons(Path file, Collection<Person> persons) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (Writer writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            gson.toJson(persons, writer);
        }
    }
}