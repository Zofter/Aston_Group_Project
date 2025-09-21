package strategy;

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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FileStrategy implements PersonStrategy, AutoCloseable {
    private final Reader mainReader;
    private final JsonReader mainJr;
    private boolean arrayStarted = false;
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
            if (!arrayStarted) {
                // Ожидается формат массива типа: [ { ... }, { ... }, ... ]
                // peek() - для определения типа следующего токена без его извлечения (без продвижения позиции чтения)
                if (mainJr.peek() == JsonToken.BEGIN_ARRAY) {
                    mainJr.beginArray();  // проверка текущего токена на начало массива и перенос в случае успеха парсера вперед
                    arrayStarted = true;
                } else {
                    throw new IllegalStateException("Ожидался JSON-массив в корне файла");
                }
            }
            // hasNext() - не потребляя токен - проверяет наличие элементов - определяет, есть ли еще элементы
            // для чтения в массиве или в объекте
            if (!mainJr.hasNext()) {
                // Проверка того, что текущий токен является концом массива (]) и завершение чтения массива с выходом.
                mainJr.endArray();
                finished = true;
                throw new NoSuchElementException("В файле больше нет объектов");
            }

            // beginObject() Проверяет текущий токен - убеждается, что следующий токен в JSON-потоке является началом объекта ({)
            // Перемещает парсер вперед - после успешной проверки переходит к чтению пар ключ-значение объекта
            mainJr.beginObject();
            while (mainJr.hasNext()) {
                // nextName() - Читает ключ объекта - извлекает имя свойства из пары ключ-значение
                // Перемещает парсер - после чтения ключа парсер готов к чтению значения
                // Валидирует структуру - проверяет, что текущий контекст является объектом
                String name = mainJr.nextName();
                switch (name) {
                    case "name" -> {
                        if (mainJr.peek() == JsonToken.NULL) mainJr.nextNull();
                        else b.name(mainJr.nextString());
                    }
                    case "age" -> {
                        if (mainJr.peek() == JsonToken.NULL) mainJr.nextNull();
                        else b.age(mainJr.nextInt());
                    }
                    case "weight" -> {
                        if (mainJr.peek() == JsonToken.NULL) mainJr.nextNull();
                        else b.weight(mainJr.nextDouble());
                    }
                    default -> mainJr.skipValue();  // Игнорирование лишних полей, если они есть.
                }
            }
            mainJr.endObject();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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

    @Override
    public void close() throws Exception {
        mainJr.close();
        mainReader.close();
    }

    static class PersonIterator implements Iterator<Person> {
        private final JsonReader jr;
        private boolean arrayStarted = false;
        private boolean finished = false;

        PersonIterator(JsonReader jr) {
            this.jr = jr;
        }

        @Override
        public boolean hasNext() {
            if (finished) return false;
            try {
                if (!arrayStarted) {
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
                return b.build();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }
}