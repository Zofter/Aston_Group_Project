package strategy;

import java.util.Random;

public class RandomStrategy implements PersonStrategy {
    private final Random rnd = new Random();

    @Override
    public void getPerson(model.Person.Builder b) {
        b.name("User" + rnd.nextInt(100))
                .age(18 + rnd.nextInt(50))
                .weight(40 + rnd.nextInt(60));
    }
}