package strategy;

import collection.CustomCollection;
import model.Person;

public interface PersonStrategy {
    void getPerson(Person.Builder b);
}
