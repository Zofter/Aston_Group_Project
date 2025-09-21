package strategy;

import collection.CustomCollection;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import model.Person;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collector;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileStrategy implements PersonStrategy, AutoCloseable {
    private final Reader mainReader;
    private final JsonReader mainJr;
    private final AtomicBoolean arrayStarted = new AtomicBoolean(false);
    private boolean finished = false;

    public FileStrategy() {
        try {
            this.mainReader = Files.newBufferedReader(Paths.get("Source_JSON_Collection_File.txt"), StandardCharsets.UTF_8);
            this.mainJr = new JsonReader(mainReader);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void getPerson(Person.Builder b) {
        if (finished) throw new NoSuchElementException("В файле больше нет объектов");
        try {
            ensureArrayStarted(mainJr, arrayStarted);
            if (!mainJr.hasNext()) {
                mainJr.endArray();
                finished = true;
                throw new NoSuchElementException("В файле больше нет объектов");
            }
            parsePerson(mainJr, b);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static CustomCollection<Person> getFilePersonCollection(int size) {
        return stream().limit(size)
                .collect(
                        Collector.of(
                                () -> new CustomCollection<>(size),
                                CustomCollection::add,
                                (left, right) -> {
                                    left.addAll(right);
                                    return left;
                                }
                        )
                );
    }

    public static Stream<Person> stream() {
        try {
            Reader r = Files.newBufferedReader(Paths.get("Source_JSON_Collection_File.txt"), StandardCharsets.UTF_8);
            JsonReader jr = new JsonReader(r);
            PersonIterator it = new PersonIterator(jr);

            return StreamSupport.stream(
                    Spliterators.spliteratorUnknownSize(it, 0),
                    false
            ).onClose(() -> {
                try {
                    jr.close();
                    r.close();
                } catch (IOException ignore) {
                }
            });

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static class PersonIterator implements Iterator<Person> {
        private final JsonReader jr;
        private final AtomicBoolean arrayStarted = new AtomicBoolean(false);
        private boolean finished = false;

        PersonIterator(JsonReader jr) {
            this.jr = jr;
        }

        @Override
        public boolean hasNext() {
            if (finished) return false;
            try {
                ensureArrayStarted(jr, arrayStarted);
                if (!jr.hasNext()) {
                    jr.endArray();
                    finished = true;
                    return false;
                }
                return true;
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }

        @Override
        public Person next() {
            if (!hasNext()) throw new NoSuchElementException("Больше людей нет");
            try {
                Person.Builder b = new Person.Builder();
                parsePerson(jr, b); // общий метод
                return b.build();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    private static void parsePerson(JsonReader jr, Person.Builder b) throws IOException {
        jr.beginObject();
        while (jr.hasNext()) {
            String name = jr.nextName();
            switch (name) {
                case "name" -> {
                    if (jr.peek() == JsonToken.NULL) jr.nextNull();
                    else b.name(jr.nextString());
                }
                case "age" -> {
                    if (jr.peek() == JsonToken.NULL) jr.nextNull();
                    else b.age(jr.nextInt());
                }
                case "weight" -> {
                    if (jr.peek() == JsonToken.NULL) jr.nextNull();
                    else b.weight(jr.nextDouble());
                }
                default -> jr.skipValue();
            }
        }
        jr.endObject();
    }

    private static void ensureArrayStarted(JsonReader jr, AtomicBoolean arrayStarted) throws IOException {
        if (!arrayStarted.get()) {
            if (jr.peek() == JsonToken.BEGIN_ARRAY) {
                jr.beginArray();
                arrayStarted.set(true);
            } else {
                throw new IllegalStateException("Ожидался JSON-массив в корне файла");
            }
        }
    }

    @Override
    public void close() throws Exception {
        mainJr.close();
        mainReader.close();
    }
}