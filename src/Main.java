import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class Main {
    public static void main(String[] args) {
        boolean workInMainCycle = true;
        int collectionSize = 0;
        List<Person> personCollection = List.of();
        Scanner in = new Scanner(System.in);

        while (workInMainCycle) {
            System.out.println("Укажите длину новой кастомной коллекции");
            collectionSize = Integer.parseInt(in.next());

            System.out.println("Для выбора заполнения коллекции случайными параметрами, введите \"random\"");
            System.out.println("Для выбора заполнения коллекции собственными значениями, введите \"self\"");
            System.out.println("Для выбора заполнения коллекции значениями из файла, введите \"file\"");
            System.out.println("Для выхода из программы введите команду \"exit\"");
            String typeFillingCollection = in.next();

            if (typeFillingCollection.equals("random"))
                personCollection = fillingCollection(new RandomStrategy(), collectionSize);
            if (typeFillingCollection.equals("self"))
                personCollection = fillingCollection(new ManualStrategy(), collectionSize);
            if (typeFillingCollection.equals("file"))
                personCollection = fillingCollection(new FileStrategy(), collectionSize);

            personCollection.forEach(System.out::print);

            System.out.println();

            if (typeFillingCollection.equals("exit")) {
                in.close();
                workInMainCycle = false;
            }
        }
        System.out.println("Выход из главного цикла");
    }

    static List<Person> fillingCollection(PersonStrategy personStrategy, int size) {
        List<Person> tempCollection = new CustomCollection<>(size);
        try {
            for (int i = 0; i < size; i++) {
                try {
                    tempCollection.add(PersonPull.createPerson(personStrategy));
                } catch (NoSuchElementException eof) {
                    break;
                }
            }
        } finally {
            if (personStrategy instanceof AutoCloseable ac) {
                try { ac.close(); } catch (Exception ignore) {}
            }
        }
        return tempCollection;
    }
}

interface PersonStrategy {
    void getPerson(Person.Builder b);
}

final class PersonPull {
    public static Person createPerson(PersonStrategy personStrategy) {
        var b = new Person.Builder();
        personStrategy.getPerson(b);
        return b.build();
    }
}

class FileStrategy implements PersonStrategy, AutoCloseable {
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
                // ожидается формат: [ { ... }, { ... }, ... ]
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
                    default -> jr.skipValue(); // игнорируем лишние поля
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

class RandomStrategy implements PersonStrategy {
    private final Random rnd = new Random();

    @Override
    public void getPerson(Person.Builder b) {
        b.name("User" + rnd.nextInt(100))
                .age(18 + rnd.nextInt(50))
                .weight(40 + rnd.nextInt(60));
    }
}

class ManualStrategy implements PersonStrategy {
    @Override
    public void getPerson(Person.Builder b) {
        Scanner in = new Scanner(System.in);
        System.out.println("Укажите имя создаваемого человека");
        String name = in.next();
        System.out.println("Укажите возраст создаваемого человека");
        int age = Integer.parseInt(in.next());
        System.out.println("Укажите вес создаваемого человека");
        double weight = Double.parseDouble(in.next());
        b.name(name).age(age).weight(weight);
    }
}