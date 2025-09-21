package factory;

import collection.CustomCollection;
import model.Person;
import strategy.PersonStrategy;
import strategy.RandomStrategy;

public final class PersonPull {
    public static Person createPerson(PersonStrategy personStrategy) {
        var b = new Person.Builder();
        personStrategy.getPerson(b);
        return b.build();
    }
}