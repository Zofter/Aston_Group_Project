package service;

import collection.CustomCollection;
import factory.PersonPull;
import model.Person;
import strategy.FileStrategy;
import strategy.PersonStrategy;
import strategy.RandomStrategy;

import java.io.IOException;
import java.util.*;

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
        } finally { // Закрываем поток json-ридера только в стратегии с AutoCloseable, для остальных стратегий - не трогать.
            if (personStrategy instanceof AutoCloseable ac) {
                try {
                    ac.close();
                } catch (Exception ignore) {
                }
            }
        }
        return tempCollection;
    }

    // Дополнительное задание #3: Заполнение посредством стримов.
    public static CustomCollection<Person> fillRndPrsnCollFromStream(int size) {
        return RandomStrategy.getRandomPersonCollection(size);
    }

    public static CustomCollection<Person> fillFilePrsnCollFromStream(int size) {
        try {
            CustomCollection<Person> tempCollection = FileStrategy.getFilePersonCollection(size);
            if (tempCollection.size() < size) {
                System.out.println("Объектов в файле - меньше чем размер желаемой для генерации коллекции");
            }
            return tempCollection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}