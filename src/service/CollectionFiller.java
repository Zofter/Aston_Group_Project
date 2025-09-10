package service;

import collection.CustomCollection;
import factory.PersonPull;
import model.Person;
import strategy.PersonStrategy;

import java.util.*;

public class CollectionFiller {
    public static List<Person> fill(PersonStrategy personStrategy, int size) {
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

