package tests;

import java.util.*;
import model.Person;
import collection.CustomCollection;

public class CustomCollectionTest {

    public static void main(String[] args) {
        testSortWithIntegers();
        testSortWithStrings();
        testSortWithPersons();
        testSortEmptyList();
        testSortSingleElement();
        testSortAlreadySorted();
        testSortReverseOrder();
        testSortWithFixedSizeCapacity();
    }

    // Тест: сортировка списка целых чисел
    static void testSortWithIntegers() {
        System.out.println("=== Тест 1: Сортировка целых чисел ===");
        CustomCollection<Integer> list = new CustomCollection<>();
        int[] data = {64, 34, 25, 12, 22, 11, 90};
        for (int num : data) {
            list.add(num);
        }
        System.out.println("До сортировки: " + listToString(list));

        service.CollectionSorter.sort(list, Integer::compareTo);

        boolean isSorted = isListSorted(list, Integer::compareTo);
        System.out.println("После сортировки: " + listToString(list));
        System.out.println(isSorted ? "✅ УСПЕХ" : "❌ ПРОВАЛ");
        System.out.println();
    }

    // Тест: сортировка списка строк
    static void testSortWithStrings() {
        System.out.println("=== Тест 2: Сортировка строк ===");
        CustomCollection<String> list = new CustomCollection<>();
        String[] data = {"яблоко", "банан", "апельсин", "груша", "абрикос"};
        for (String str : data) {
            list.add(str);
        }
        System.out.println("До сортировки: " + listToString(list));

        service.CollectionSorter.sort(list, String::compareTo);

        boolean isSorted = isListSorted(list, String::compareTo);
        System.out.println("После сортировки: " + listToString(list));
        System.out.println(isSorted ? "✅ УСПЕХ" : "❌ ПРОВАЛ");
        System.out.println();
    }

    // Тест: сортировка списка объектов Person по возрасту
    static void testSortWithPersons() {
        System.out.println("=== Тест 3: Сортировка объектов Person ===");
        CustomCollection<Person> list = new CustomCollection<>();
        Person[] data = {
                new Person.Builder().name("Иван").age(30).weight(75.5).build(),
                new Person.Builder().name("Анна").age(25).weight(60.0).build(),
                new Person.Builder().name("Борис").age(35).weight(80.2).build(),
                new Person.Builder().name("Галина").age(20).weight(55.8).build()
        };
        for (Person person : data) {
            list.add(person);
        }

        System.out.println("До сортировки:");
        printList(list);

        service.CollectionSorter.sort(list, Comparator.comparingInt(Person::getAge));

        boolean isSorted = isListSorted(list, Comparator.comparingInt(Person::getAge));
        System.out.println("После сортировки по возрасту:");
        printList(list);
        System.out.println(isSorted ? "✅ УСПЕХ" : "❌ ПРОВАЛ");
        System.out.println();
    }

    // Тест: сортировка пустого списка
    static void testSortEmptyList() {
        System.out.println("=== Тест 4: Сортировка пустого списка ===");
        CustomCollection<Integer> list = new CustomCollection<>();

        try {
            service.CollectionSorter.sort(list, Integer::compareTo);
            System.out.println("Размер после сортировки: " + list.size());
            System.out.println("✅ УСПЕХ - нет исключений");
        } catch (Exception e) {
            System.out.println("❌ ПРОВАЛ - возникло исключение: " + e.getMessage());
        }
        System.out.println();
    }

    // Тест: сортировка списка с одним элементом
    static void testSortSingleElement() {
        System.out.println("=== Тест 5: Сортировка списка с одним элементом ===");
        CustomCollection<Integer> list = new CustomCollection<>();
        list.add(42);
        System.out.println("До сортировки: " + listToString(list));

        service.CollectionSorter.sort(list, Integer::compareTo);

        boolean success = list.size() == 1 && list.get(0) == 42;
        System.out.println("После сортировки: " + listToString(list));
        System.out.println(success ? "✅ УСПЕХ" : "❌ ПРОВАЛ");
        System.out.println();
    }

    // Тест: сортировка уже отсортированного списка
    static void testSortAlreadySorted() {
        System.out.println("=== Тест 6: Сортировка уже отсортированного списка ===");
        CustomCollection<Integer> list = new CustomCollection<>();
        int[] data = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        for (int num : data) {
            list.add(num);
        }
        System.out.println("До сортировки: " + listToString(list));

        service.CollectionSorter.sort(list, Integer::compareTo);

        boolean isSorted = isListSorted(list, Integer::compareTo);
        System.out.println("После сортировки: " + listToString(list));
        System.out.println(isSorted ? "✅ УСПЕХ" : "❌ ПРОВАЛ");
        System.out.println();
    }

    // Тест: сортировка списка в обратном порядке
    static void testSortReverseOrder() {
        System.out.println("=== Тест 7: Сортировка списка в обратном порядке ===");
        CustomCollection<Integer> list = new CustomCollection<>();
        int[] data = {10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
        for (int num : data) {
            list.add(num);
        }
        System.out.println("До сортировки: " + listToString(list));

        service.CollectionSorter.sort(list, Integer::compareTo);

        boolean isSorted = isListSorted(list, Integer::compareTo);
        System.out.println("После сортировки: " + listToString(list));
        System.out.println(isSorted ? "✅ УСПЕХ" : "❌ ПРОВАЛ");
        System.out.println();
    }

    // Тест: сортировка с фиксированной емкостью
    static void testSortWithFixedSizeCapacity() {
        System.out.println("=== Тест 8: Сортировка с фиксированной емкостью ===");
        CustomCollection<Integer> list = new CustomCollection<>(5);
        int[] data = {5, 2, 8, 1, 9};
        for (int num : data) {
            list.add(num);
        }
        System.out.println("До сортировки: " + listToString(list));

        service.CollectionSorter.sort(list, Integer::compareTo);

        boolean isSorted = isListSorted(list, Integer::compareTo);
        System.out.println("После сортировки: " + listToString(list));
        System.out.println("Размер: " + list.size() + ", Емкость увеличилась: " + (list.toArray().length >= 5));
        System.out.println(isSorted ? "✅ УСПЕХ" : "❌ ПРОВАЛ");
        System.out.println();
    }

    // Вспомогательный метод для проверки, отсортирован ли список
    private static <T> boolean isListSorted(CustomCollection<T> list, Comparator<T> comparator) {
        for (int i = 0; i < list.size() - 1; i++) {
            if (comparator.compare(list.get(i), list.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    // Вспомогательный метод для преобразования списка в строку
    private static <T> String listToString(CustomCollection<T> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(list.get(i));
        }
        sb.append("]");
        return sb.toString();
    }

    // Вспомогательный метод для печати списка
    private static <T> void printList(CustomCollection<T> list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i));
        }
    }
}