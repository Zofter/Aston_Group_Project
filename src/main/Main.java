package main;

import java.util.*;
import comparator.*;
import model.Person;
import service.CollectionSorter;
import service.BinarySearch;
import strategy.*;
import service.CollectionFiller;
import io.FileWriterUtil;
import collection.CustomCollection;

public class Main {
    public static void main(String[] args) {
        boolean workInMainCycle = true;
        Scanner in = new Scanner(System.in);

        while (workInMainCycle) {
            System.out.println("=== Меню ===");
            System.out.println("1. Создать и отсортировать коллекцию");
            System.out.println("2. Бинарный поиск");
            System.out.println("3. Подсчет вхождений (многопоточный)");
            System.out.println("4. Выход");
            System.out.print("Выберите опцию: ");

            int choice = in.nextInt();
            in.nextLine(); // consume newline

            switch (choice) {
                case 1 -> createAndSortCollection(in);
                case 2 -> binarySearch(in);
                case 3 -> countOccurrences(in);
                case 4 -> workInMainCycle = false;
                default -> System.out.println("Неверный выбор");
            }
        }

        CollectionSorter.shutdown();
        System.out.println("Выход из программы");
    }

    private static void createAndSortCollection(Scanner in) {
        System.out.println("Укажите длину коллекции: ");
        int collectionSize = in.nextInt();
        in.nextLine();

        System.out.println("Выберите способ заполнения (random/self/file): ");
        String typeFillingCollection = in.nextLine();

        List<Person> personCollection = switch (typeFillingCollection) {
            case "random" -> CollectionFiller.fill(new RandomStrategy(), collectionSize);
            case "self" -> CollectionFiller.fill(new ManualStrategy(), collectionSize);
            case "file" -> CollectionFiller.fill(new FileStrategy(), collectionSize);
            default -> {
                System.out.println("Неверный выбор, используется random");
                yield CollectionFiller.fill(new RandomStrategy(), collectionSize);
            }
        };

        CustomCollection<Person> customCollection = new CustomCollection<>();
        customCollection.addAll(personCollection);

        System.out.println("Выберите поле для сортировки (name/age/weight): ");
        String sortField = in.nextLine();

        Comparator<Person> comparator = switch (sortField) {
            case "name" -> new PersonNameComparator();
            case "age" -> new PersonAgeComparator();
            case "weight" -> new PersonWeightComparator();
            default -> new PersonNameComparator();
        };

        CollectionSorter.sort(customCollection, comparator);

        System.out.println("Применить сортировку только для четных значений возраста? (y/n): ");
        if (in.nextLine().equalsIgnoreCase("y")) {
            customCollection.sortWithEvenOdd(Person::getAge, comparator);
        }

        System.out.println("Отсортированная коллекция:");
        customCollection.forEach(System.out::println);

        try {
            System.out.println("Записать результат в файл? (y/n): ");
            if (in.nextLine().equalsIgnoreCase("y")) {
                FileWriterUtil.writePersonsToCSV("output.csv", customCollection, true);
                System.out.println("Результат записан в output.csv");
            }
        } catch (Exception e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
        }
    }

    private static void binarySearch(Scanner in) {
        // Реализация бинарного поиска
        System.out.println("Введите данные для поиска:");
        System.out.print("Имя: ");
        String name = in.nextLine();
        System.out.print("Возраст: ");
        int age = in.nextInt();
        System.out.print("Вес: ");
        double weight = in.nextDouble();
        in.nextLine();

        Person searchPerson = new Person.Builder()
                .name(name)
                .age(age)
                .weight(weight)
                .build();

        System.out.println("Сначала создайте и отсортируйте коллекцию");
    }

    private static void countOccurrences(Scanner in) {
        System.out.println("Введите имя для подсчета: ");
        String name = in.nextLine();

        Person target = new Person.Builder()
                .name(name)
                .age(0) 
                .weight(0) 
                .build();

        System.out.println("Сначала создайте коллекцию");
    }
}
