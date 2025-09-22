package main;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import comparator.*;
import io.FileWriterJSON_Util;
import io.FileWriterResults_Util;
import model.Person;
import service.BinarySearch;
import service.CollectionEvenSorter;
import service.CollectionSorter;
import strategy.*;
import service.CollectionFiller;
import collection.CustomCollection;
import static service.CountOccurrMultiThreaded.countOccurrencesMultiThreaded;

public class Main {
    public static void main(String[] args) {
        new MenuController().run();
    }
}

//============================================================
// Управление меню
//============================================================
class MenuController {
    private CustomCollection<Person> personCollection = new CustomCollection<>();
    private String sortField = "";
    private final Scanner scanner = new Scanner(System.in);

    public void run() {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = InputUtils.readInt(scanner, "Выберите номер опции: ");

            switch (choice) {
                case 1 -> createAndSortCollection();
                case 2 -> performBinarySearch();
                case 3 -> performCountOccurrences();
                case 4 -> running = false;
                default -> System.out.println("Неверный выбор");
            }
        }
        System.out.println("Выход из программы");
    }

    void printMenu() {
        System.out.println("""
                === Меню ===
                1. Создать/заполнить и отсортировать коллекцию
                2. Бинарный поиск
                3. Подсчет вхождений (многопоточный)
                4. Выход
                """);
    }

    //========================
    // Пункт 1: Заполнение + сортировка коллекции
    //========================
    private void createAndSortCollection() {
        int collectionSize = InputUtils.readInt(scanner, "Укажите длину коллекции: ");
        String fillingType = InputUtils.readLine(scanner, "Выберите способ заполнения (random/self/file): ");

        // Заполнение коллекции через выбор стратегии сортировки - и возможности использования Stream API.
        personCollection = switch (fillingType) {
            case "self" -> CollectionFiller.fill(new ManualStrategy(), collectionSize);
            case "random" -> {
                String ans = InputUtils.readLine(scanner, "Использовать Stream API? (y/n): ");
                if (ans.equalsIgnoreCase("y")) {
                    yield CollectionFiller.fillRndPrsnCollFromStream(collectionSize);
                } else {
                    yield CollectionFiller.fill(new RandomStrategy(), collectionSize);
                }
            }
            case "file" -> {
                String ans = InputUtils.readLine(scanner, "Использовать Stream API? (y/n): ");
                if (ans.equalsIgnoreCase("y")) {
                    yield CollectionFiller.fillFilePrsnCollFromStream(collectionSize);
                } else {
                    yield CollectionFiller.fill(new FileStrategy(), collectionSize);
                }
            }
            default -> {
                System.out.println("Неверный выбор, используется random без Stream API");
                yield CollectionFiller.fill(new RandomStrategy(), collectionSize);
            }
        };

        System.out.println("Сгенерированная коллекция: ");
        personCollection.forEach(System.out::print);

        // Сортировка коллекции
        sortField = InputUtils.readLine(scanner, "Выберите поле для сортировки (name/age/weight): ");
        sortCollection();

        System.out.println("Вывод отсортированной коллекции:");
        personCollection.forEach(System.out::print);

        // Сохранение в файл в формате Json-Line
        FileUtils.askAndWriteJSON(scanner, personCollection);
    }

    private void sortCollection() {
        switch (sortField) {
            case "name" -> CollectionSorter.sort(personCollection, new PersonNameComparator());
            case "age" -> {
                String answer = InputUtils.readLine(scanner, "Применить сортировку только для четных возрастов? (y/n): ");
                if (answer.equalsIgnoreCase("y")) {
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
    }

    //========================
    // Пункт 2: Бинарный поиск
    //========================
    private void performBinarySearch() {
        if (personCollection.isEmpty()) {
            System.out.println("Сначала создайте и отсортируйте коллекцию!\n");
            return;
        }

        System.out.println("Укажите искомый объект Person для поиска в коллекции:");
        Person searchPerson = InputUtils.readPerson(scanner);

        // Получение компаратора для бинарного поиска через установленное поле для сортировки sortField
        Comparator<Person> byField = switch (sortField) {
            case "name" -> new PersonNameComparator();
            case "age" -> new PersonAgeComparator();
            case "weight" -> new PersonWeightComparator();
            default -> new PersonAgeComparator();
        };

        // Осуществление бинарного поиска
        OptionalInt idx = BinarySearch.search(personCollection, searchPerson, byField);

        String message = idx.isPresent()
                ? "Порядковый индекс искомого элемента: " + idx.getAsInt()
                : "Искомый элемент не найден";
        System.out.println(message);

        // Сохранение в файл в формате Json-Line
        FileUtils.askAndWriteResults(scanner, personCollection, searchPerson, message);
    }

    //========================
    // Пункт 3: Подсчёт вхождений
    //========================
    private void performCountOccurrences() {
        if (personCollection.isEmpty()) {
            System.out.println("Сначала создайте и отсортируйте коллекцию!\n");
            return;
        }

        System.out.println("Укажите искомый объект Person для подсчета числа вхождений:");
        Person searchingPerson = InputUtils.readPerson(scanner);

        // Функция подсчета вхождений объекта Person в коллекции
        int occurCount = countOccurrencesMultiThreaded(personCollection, searchingPerson, 3);

        String message = "Количество вхождений искомого элемента: " + occurCount;
        System.out.println(message);

        // Сохранение в файл в формате Json-Line
        FileUtils.askAndWriteResults(scanner, personCollection, searchingPerson, message);
    }

    //============================================================
    // Методы для ввода с консоли
    //============================================================
    class InputUtils {
        static int readInt(Scanner sc, String msg) {
            int value;
            System.out.print(msg);
            while (true) {
                if (!sc.hasNextInt()) {
                    System.out.print("Ошибка: введите целое число. Повторите: ");
                    sc.next();
                } else {
                    value = sc.nextInt();
                    if (value <= 0) {
                        System.out.print("Ошибка: число должно быть больше нуля . Повторите: ");
                    } else {
                        break;
                    }
                }
            }
            sc.nextLine();
            return value;
        }

        static String readLine(Scanner sc, String msg) {
            System.out.print(msg);
            return sc.nextLine();
        }

        static Person readPerson(Scanner sc) {
            String name = readLine(sc, "Имя: ");
            int age = readInt(sc, "Возраст: ");
            double weight = readDouble(sc, "Вес: ");
            return new Person.Builder().name(name).age(age).weight(weight).build();
        }

        static double readDouble(Scanner sc, String msg) {
            System.out.print(msg);
            while (!sc.hasNextDouble()) {
                System.out.print("Ошибка ввода. Повторите: ");
                sc.next();
            }
            double res = sc.nextDouble();
            sc.nextLine();
            return res;
        }
    }

    //============================================================
    // Методы для записи в файл
    //============================================================
    class FileUtils {
        static void askAndWriteJSON(Scanner sc, CustomCollection<Person> collection) {
            String ans = InputUtils.readLine(sc, "Записать результат в файл? (y/n): ");
            if (ans.equalsIgnoreCase("y")) {
                try {
                    FileWriterJSON_Util.writePersons(Paths.get("JSON.txt"), collection);
                    System.out.println("Результат записан в JSON.txt");
                } catch (IOException e) {
                    System.out.println("Ошибка записи в файл: " + e.getMessage());
                }
            }
        }

        static void askAndWriteResults(Scanner sc, CustomCollection<Person> collection, Person target, String message) {
            String ans = InputUtils.readLine(sc, "Записать результат в файл? (y/n): ");
            if (ans.equalsIgnoreCase("y")) {
                try {
                    FileWriterResults_Util.writeResults(Paths.get("Results.txt"), collection, target, message);
                    System.out.println("Результат записан в Results.txt");
                } catch (IOException e) {
                    System.out.println("Ошибка записи в файл: " + e.getMessage());
                }
            }
        }
    }

}