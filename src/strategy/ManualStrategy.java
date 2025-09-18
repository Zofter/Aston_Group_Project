package strategy;

import model.Person;

import java.util.Scanner;

public class ManualStrategy implements PersonStrategy {
    @Override
    public void getPerson(Person.Builder b) {
        Scanner in = new Scanner(System.in);
        System.out.println("Укажите имя создаваемого человека");
        String name = in.next();

        int age = 0;
        while (true) {
            System.out.println("Укажите возраст создаваемого человека");
            String input = in.next();
            try {
                age = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Возраст должен быть целым числом, попробуйте ещё раз.");
            }
        }

        double weight = 0;
        while (true) {
            System.out.println("Укажите вес создаваемого человека");
            String input = in.next();
            try {
                weight = Double.parseDouble(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Вес должен быть числом (например, 72.5), попробуйте ещё раз.");
            }
        }
        b.name(name).age(age).weight(weight);
    }
}