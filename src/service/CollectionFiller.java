package service;

import collection.CustomCollection;
import factory.PersonPull;
import model.Person;
import strategy.FileStrategy;
import strategy.PersonStrategy;
import strategy.RandomStrategy;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CollectionFiller {
    public static CustomCollection<Person> fill(PersonStrategy personStrategy, int size) {
        CustomCollection<Person> tempCollection = new CustomCollection<>(size);
        try {
            for (int i = 0; i < size; i++) {
                try {
                    tempCollection.add(PersonPull.createPerson(personStrategy));
                } catch (NoSuchElementException eof) {
                    System.out.println(eof.getMessage());
                    break;
                }
            }
        } finally { // Закрываем поток json-ридера только в стратегии с AutoCloseable, остальные стратегии - не трогать.
            if (personStrategy instanceof AutoCloseable ac) {
                try { ac.close(); } catch (Exception ignore) {}
            }
        }
        return tempCollection;
    }
    public static CustomCollection<Person> fillRandomPersonCollection(int size) {
        return RandomStrategy.getRandomPersonCollection(size);
    }

    public static CustomCollection<Person> fillFromFilePersonCollection(int size) {
        try (FileStrategy fs = new FileStrategy()) {
            return FileStrategy.stream()
                    .limit(size) // если нужно ограничить кол-во
                    .collect(
                            Collector.of(
                                    () -> new CustomCollection<>(size),   // supplier (как создать пустую коллекцию)
                                    CustomCollection::add,                // accumulator (как добавить элемент)
                                    (left, right) -> {                    // combiner (если стрим будет параллельный)
                                        left.addAll(right);
                                        return left;
                                    }
                            )
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}