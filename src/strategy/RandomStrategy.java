package strategy;

import collection.CustomCollection;
import model.Person;

import java.util.Random;
import java.util.stream.Stream;

public class RandomStrategy implements PersonStrategy {
    private static final Random rnd = new Random();

    @Override
    public void getPerson(model.Person.Builder b) {
        b.name("User" + rnd.nextInt(100))
                .age(18 + rnd.nextInt(50))
                .weight(40 + rnd.nextInt(60));
    }

    public static CustomCollection<Person> getRandomPersonCollection(int size) {
        return Stream.generate(() -> {
                    Person.Builder builder = new Person.Builder();
                    builder.name("User" + rnd.nextInt(100))
                            .age(18 + rnd.nextInt(50))
                            .weight(40 + rnd.nextInt(60));
                    return builder.build();
                })
                .limit(size)
                .collect(
                        () -> new CustomCollection<>(size),
                        CustomCollection::add,
                        CustomCollection::addAll
                );
    }
}