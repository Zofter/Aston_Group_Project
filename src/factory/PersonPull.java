package factory;

import model.Person;
import strategy.PersonStrategy;

public final class PersonPull {
    public static Person createPerson(PersonStrategy personStrategy) {
        var b = new Person.Builder();
        personStrategy.getPerson(b);
        return b.build();
    }
}