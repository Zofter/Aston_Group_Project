package strategy;

import collection.CustomCollection;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import model.Person;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

public class FileStrategy implements PersonStrategy, AutoCloseable {

    private final BufferedReader reader;
    private final Gson gson = new Gson();
    private boolean finished = false;
    private static final Path filePath = Paths.get("JSON.txt");

    public FileStrategy() {
        try {
            this.reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось открыть файл для чтения:", e);
        }
    }

    public FileStrategy(String filePath) {
        try {
            this.reader = Files.newBufferedReader(Path.of(filePath), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("Не удалось открыть файл для чтения:", e);
        }
    }

    @Override
    public void getPerson(Person.Builder b) {
        if (finished) {
            throw new NoSuchElementException("В файле больше нет объектов");
        }
        try {
            String line = reader.readLine();

            if (line == null) {
                finished = true;
                throw new NoSuchElementException("В файле больше нет объектов");
            }

            if (line.trim().isEmpty()) {
                // Пропускаем пустые строки и пробуем следующую
                getPerson(b);
                return;
            }

            // Парсим объект Person из строки
            Person p = gson.fromJson(line, Person.class);

            if (p == null) {
                // Если строка кривая – пропустим и пробуем дальше
                getPerson(b);
                return;
            }

            // Заполняем билдер
            b.name(p.getName());
            b.age(p.getAge());
            b.weight(p.getWeight());

        } catch (JsonSyntaxException e) {
            System.err.println("Ошибка синтаксиса JSON, строка пропущена.");
            getPerson(b);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static CustomCollection<Person> getFilePersonCollection(int size) throws IOException {
        // Если файла нет, возвращаем пустую коллекцию
        if (!Files.exists(filePath)) {
            return new CustomCollection<>();
        }

        Gson gson = new Gson();

        // Files.lines(...) открывает файл и возвращает поток строк (Stream<String>), где каждая строка — одна строчка файла.
        // и его нужно закрывать, поэтому используем try-with-resources
        try (Stream<String> lines = Files.lines(filePath, StandardCharsets.UTF_8)) {
            return lines
                    // Шаг 1: Преобразовать каждую строку в объект Person, обработав ошибки
                    .map(line -> {
                        if (line.trim().isEmpty()) {
                            return null; // Пустые строки пропускаем
                        }
                        try {
                            return gson.fromJson(line, Person.class);
                        } catch (JsonSyntaxException e) {
                            // Сообщаем об ошибке и возвращаем null, чтобы потом отфильтровать
                            System.err.println("Пропуск некорректной строки JSON: " + line);
                            return null;
                        }
                    })
                    // Шаг 2: Убрать из потока все null, которые могли появиться на шаге 1
                    .filter(Objects::nonNull)
                    // Шаг 3: обрезать коллекцию по размеру
                    .limit(size)
                    // Шаг 4: Собрать оставшиеся объекты Person в CustomCollection
                    .collect(
                            CustomCollection::new,      // 1. Supplier - создаёт новую пустую коллекцию для результатов
                            CustomCollection::add,      // 2. Accumulator - как добавить каждый элемент в коллекцию
                            CustomCollection::addAll    // 3. Combiner - объединить две частичные коллекции (для параллельных стримов)
                    );
        }
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}