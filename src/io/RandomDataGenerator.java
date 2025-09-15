package io;
import model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomDataGenerator {

    private static final String[] NAMES = {"Alex", "Maria", "John", "Kate", "Ivan", "Anna"};
    private static final String[] TITLES = {"Laptop", "Phone", "Tablet", "Monitor", "Mouse", "Keyboard"};
    private static final Random RANDOM = new Random();

    public static List<Person> generateRandomPersons(int count) {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            persons.add(new Person.Builder()
                    .name(NAMES[RANDOM.nextInt(NAMES.length)])
                    .age(18 + RANDOM.nextInt(50))
                    .weight(50 + RANDOM.nextDouble() * 50)
                    .build());
        }
        return persons;
    }
}

