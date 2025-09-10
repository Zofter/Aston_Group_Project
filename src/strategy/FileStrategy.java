package strategy;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import model.Person;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

public class FileStrategy implements PersonStrategy, AutoCloseable {
    private final JsonReader jr;
    private boolean arrayStarted = false;
    private boolean finished = false;

    public FileStrategy() {
        try {
            Reader r = Files.newBufferedReader(Paths.get("Source_JSON_Collection_File.txt"), StandardCharsets.UTF_8);
            this.jr = new JsonReader(r);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void getPerson(Person.Builder b) {
        if (finished) throw new NoSuchElementException("В файле больше нет объектов");
        try {

            if (!arrayStarted) {
                // Ожидается формат массива типа: [ { ... }, { ... }, ... ]
                if (jr.peek() == JsonToken.BEGIN_ARRAY) {
                    jr.beginArray();
                    arrayStarted = true;
                } else {
                    throw new IllegalStateException("Ожидался JSON-массив в корне файла");
                }
            }

            if (!jr.hasNext()) {
                jr.endArray();
                finished = true;
                throw new NoSuchElementException("В файле больше нет объектов");
            }

            jr.beginObject();
            while (jr.hasNext()) {
                String name = jr.nextName();
                switch (name) {
                    case "name" -> {
                        if (jr.peek() == JsonToken.NULL) { jr.nextNull(); }
                        else b.name(jr.nextString());
                    }
                    case "age" -> {
                        if (jr.peek() == JsonToken.NULL) { jr.nextNull(); }
                        else b.age(jr.nextInt());
                    }
                    case "weight" -> {
                        if (jr.peek() == JsonToken.NULL) { jr.nextNull(); }
                        else b.weight(jr.nextDouble());
                    }
                    default -> jr.skipValue(); // Игнорирование лишних полей, если они есть.
                }
            }
            jr.endObject();

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void close() throws Exception {
        jr.close();
    }
}
