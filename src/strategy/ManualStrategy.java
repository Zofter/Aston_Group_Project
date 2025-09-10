package strategy;

import model.Person;

import java.util.Scanner;

public class ManualStrategy implements PersonStrategy {
    @Override
    public void getPerson(Person.Builder b) {
        Scanner in = new Scanner(System.in);
        System.out.println("Укажите имя создаваемого человека");
        String name = in.next();
        System.out.println("Укажите возраст создаваемого человека");
        int age = Integer.parseInt(in.next());
        System.out.println("Укажите вес создаваемого человека");
        double weight = Double.parseDouble(in.next());
        b.name(name).age(age).weight(weight);
    }
}