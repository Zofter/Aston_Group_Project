package io;
import model.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManualInput {

    private static final Scanner scanner = new Scanner(System.in);

    public static List<Person> inputPersons(int count) {
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            System.out.println("Введите данные для Person #" + (i + 1));
            System.out.print("Имя: ");
            String name = scanner.nextLine();
            System.out.print("Возраст: ");
            int age = Integer.parseInt(scanner.nextLine());
            System.out.print("Вес: ");
            double weight = Double.parseDouble(scanner.nextLine());

            persons.add(new Person.Builder()
                    .name(name)
                    .age(age)
                    .weight(weight)
                    .build());
        }
        return persons;
    }

}

