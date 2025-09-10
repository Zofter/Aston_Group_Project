package main;

import java.util.*;
import model.Person;
import strategy.*;
import service.CollectionFiller;

public class Main {
    public static void main(String[] args) {
        boolean workInMainCycle = true;
        Scanner in = new Scanner(System.in);

        while (workInMainCycle) {
            System.out.println("Укажите длину новой кастомной коллекции");
            int collectionSize = Integer.parseInt(in.next());

            System.out.println("Для выбора заполнения коллекции случайными параметрами, введите \"random\"");
            System.out.println("Для выбора заполнения коллекции собственными значениями, введите \"self\"");
            System.out.println("Для выбора заполнения коллекции значениями из файла, введите \"file\"");
            System.out.println("Для выхода из программы введите команду \"exit\"");
            String typeFillingCollection = in.next();

            if (typeFillingCollection.equals("exit")) {
                in.close();
                workInMainCycle = false;
            }

            List<Person> personCollection = switch (typeFillingCollection) {
                case "random" -> CollectionFiller.fill(new RandomStrategy(), collectionSize);
                case "self" -> CollectionFiller.fill(new ManualStrategy(), collectionSize);
                case "file" -> CollectionFiller.fill(new FileStrategy(), collectionSize);
                default -> List.of();
            };

            personCollection.forEach(System.out::print);
            System.out.println();
        }
        System.out.println("Выход из главного цикла");
    }
}