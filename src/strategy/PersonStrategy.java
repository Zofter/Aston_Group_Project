package strategy;

import model.Person;

public interface PersonStrategy {
    void getPerson(Person.Builder b);
}
