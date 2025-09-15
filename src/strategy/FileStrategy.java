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
                if (jr.peek() == JsonToken.BEGIN_ARRAY) {   // peek() - для определения типа следующего токена без его извлечения (без продвижения позиции чтения)
                    jr.beginArray();                        // проверка текущего токена на начало массива и перенос в случае успеха парсера вперед
                    arrayStarted = true;
                } else {
                    throw new IllegalStateException("Ожидался JSON-массив в корне файла");
                }
            }
            // hasNext() - не потребляя токен - проверяет наличие элементов - определяет, есть ли еще элементы
            // для чтения в массиве или в объекте
            if (!jr.hasNext()) {
                jr.endArray(); // Проверка того, что текущий токен является концом массива (]) и завершение чтения массива с выходом.
                finished = true;
                throw new NoSuchElementException("В файле больше нет объектов");
            }

            // beginObject() Проверяет текущий токен - убеждается, что следующий токен в JSON-потоке является началом объекта ({)
            // Перемещает парсер вперед - после успешной проверки переходит к чтению пар ключ-значение объекта
            jr.beginObject();
            while (jr.hasNext()) {
                // nextName() - Читает ключ объекта - извлекает имя свойства из пары ключ-значение
                // Перемещает парсер - после чтения ключа парсер готов к чтению значения
                // Валидирует структуру - проверяет, что текущий контекст является объектом
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
