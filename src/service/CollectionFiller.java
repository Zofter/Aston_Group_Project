package service;

import collection.CustomCollection;
import factory.PersonPull;
import model.Person;
import strategy.FileStrategy;
import strategy.PersonStrategy;
import strategy.RandomStrategy;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;

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
    public static CustomCollection<Person> fillRndPrsnCollFromStream(int size) {
        return RandomStrategy.getRandomPersonCollection(size);
    }

    public static CustomCollection<Person> fillFilePrsnCollFromStream(int size) {
        try {
            return FileStrategy.getFilePersonCollection(size);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}