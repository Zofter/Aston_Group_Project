package main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import comparator.*;
import io.FileWriterJSON_Util;
import model.Person;
import service.BinarySearch;
import service.CollectionEvenSorter;
import service.CollectionSorter;
import strategy.*;
import service.CollectionFiller;
import collection.CustomCollection;

import static service.CountOccurrMultiThreaded.countOccurrencesMultiThreaded;

public class Main {
    private static CustomCollection<Person> personCollection = new CustomCollection<>();
    private static String sortField = "";

    public static void main(String[] args) {
        boolean workInMainCycle = true;

        Scanner in = new Scanner(System.in);

        while (workInMainCycle) {
            System.out.println("=== Меню ===");
            System.out.println("1. Создать и отсортировать коллекцию");
            System.out.println("2. Бинарный поиск");
            System.out.println("3. Подсчет вхождений (многопоточный)");
            System.out.println("4. Выход");
            System.out.print("Выберите номер опции: ");

            int choice = in.nextInt();
            in.nextLine();

            switch (choice) {
                case 1 -> createAndSortCollection(in);
                case 2 -> {
                    if (personCollection.isEmpty()) {
                        System.out.println("Сначала создайте и отсортируйте коллекцию!\n");
                        break;
                    }
                    binarySearch(in);
                }
                case 3 -> {
                    if (personCollection.isEmpty()) {
                        System.out.println("Сначала создайте и отсортируйте коллекцию!\n");
                        break;
                    }
                    countOccurrences(in);
                }
                case 4 -> workInMainCycle = false;
                default -> System.out.println("Неверный выбор");
            }
        }
        System.out.println("Выход из программы");
    }

    private static void createAndSortCollection(Scanner in) {
        System.out.println("Укажите длину коллекции: ");
        int collectionSize = in.nextInt();
        in.nextLine();

        System.out.println("Выберите способ заполнения (random/self/file): ");
        String typeFillingCollection = in.nextLine();

        personCollection = switch (typeFillingCollection) {
            case "random" -> CollectionFiller.fill(new RandomStrategy(), collectionSize);
            case "self" -> CollectionFiller.fill(new ManualStrategy(), collectionSize);
            case "file" -> CollectionFiller.fill(new FileStrategy(), collectionSize);

            // case "random" -> CollectionFiller.fillRndPrsnCollFromStream(collectionSize);
            // case "file" -> CollectionFiller.fillFilePrsnCollFromStream(collectionSize);

            default -> {
                System.out.println("Неверный выбор, используется random");
                yield CollectionFiller.fill(new RandomStrategy(), collectionSize);
            }
        };

        System.out.println("Сгенерированная коллекция: ");
        personCollection.forEach(System.out::print);

        System.out.println("Выберите поле для сортировки (name/age/weight): ");
        sortField = in.nextLine();

        switch (sortField) {
            case "name" -> CollectionSorter.sort(personCollection, new PersonNameComparator());
            case "age" -> {
                System.out.println("Применить сортировку только для четных значений возраста? (y/n): ");
                if (in.nextLine().equalsIgnoreCase("y")) {
                    CollectionEvenSorter.sort(personCollection, new PersonAgeComparator(), Person::getAge);
                } else {
                    CollectionSorter.sort(personCollection, new PersonAgeComparator());
                }
            }
            case "weight" -> CollectionSorter.sort(personCollection, new PersonWeightComparator());
            default -> {
                System.out.println("Команда не распознана, выбрана сортировка по возрасту");
                CollectionSorter.sort(personCollection, new PersonAgeComparator());
            }
        }

        System.out.println("Вывод отсортированной коллекции:");
        personCollection.forEach(System.out::print);

        System.out.println("Записать результат в файл? (y/n): ");
        try {
            if (in.nextLine().equalsIgnoreCase("y")) {
                FileWriterJSON_Util.writePersons(Paths.get("Output.txt"), personCollection);
                System.out.println("Результат записан в output.txt");
            }
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл: " + e.getMessage());
        }
    }

    private static void binarySearch(Scanner in) {
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

        Comparator<Person> byField = switch(sortField) {
            case "name" -> new PersonNameComparator();
            case "age" -> new PersonAgeComparator();
            case "weight" -> new PersonWeightComparator();
            default -> throw new IllegalStateException("Неизвестное значение имени параметра Person: " + sortField);
        };

        OptionalInt idx = BinarySearch.search(personCollection, searchPerson, byField);
        if(idx.isPresent()) {
            System.out.println("Порядковый индекс искомого элемента: " + idx.getAsInt());
        } else {
            System.out.println("Искомый элемент не найден");
        }
    }

    private static void countOccurrences(Scanner in) {
        System.out.println("Введите данные объекта для подсчета количества вхождений его в коллекцию: ");
        System.out.print("Имя: ");
        String name = in.nextLine();
        System.out.print("Возраст: ");
        int age = in.nextInt();
        System.out.print("Вес: ");
        double weight = in.nextDouble();
        in.nextLine();

        Person targetPerson = new Person.Builder()
                .name(name)
                .age(age)
                .weight(weight)
                .build();

        int occurCount = countOccurrencesMultiThreaded(personCollection, targetPerson, 3);
        System.out.println("Количество вхождений искомого элемента: " + occurCount + "\n");
    }
}
