package tests;

import model.Person;
import service.CollectionEvenSorter;
import java.util.*;

public class CollectionEvenSorterTest {

    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        System.out.println("Запуск тестов CollectionEvenSorter...");
        System.out.println("=====================================");

        try {
            testEmptyList();
            testSingleElement();
            testNoEvenElements();
            testAllEvenElements();
            testMixedElements();
            testAlreadySorted();
            testReverseSorted();
            testStringElements();
            testLargeList();
            testCustomObjects();
            testNegativeNumbers();
            testDuplicates();
            testSingleEvenElement();

            System.out.println(GREEN + "✅ Все тесты пройдены успешно!" + RESET);
        } catch (Exception e) {
            System.err.println(RED + "❌ Тест провален: " + e.getMessage() + RESET);
            e.printStackTrace();
        }
    }

    // Тест 1: Пустой список
    private static void testEmptyList() {
        try {
            List<Integer> list = new ArrayList<>();
            CollectionEvenSorter.sort(list, Integer::compareTo, x -> x);
            assert list.isEmpty() : "Пустой список должен остаться пустым";
            System.out.println(GREEN + "✅ Тест пустого списка пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест пустого списка провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 2: Список с одним элементом
    private static void testSingleElement() {
        try {
            List<Integer> list = new ArrayList<>(Arrays.asList(5));
            CollectionEvenSorter.sort(list, Integer::compareTo, x -> x);
            assert list.get(0) == 5 : "Список с одним элементом должен остаться без изменений";
            System.out.println(GREEN + "✅ Тест списка с одним элементом пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест списка с одним элементом провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 3: Нет четных элементов
    private static void testNoEvenElements() {
        try {
            List<Integer> list = new ArrayList<>(Arrays.asList(1, 3, 5, 7, 9));
            List<Integer> original = new ArrayList<>(list);
            CollectionEvenSorter.sort(list, Integer::compareTo, x -> x);
            assert list.equals(original) : "Список без четных элементов должен остаться без изменений";
            System.out.println(GREEN + "✅ Тест отсутствия четных элементов пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест отсутствия четных элементов провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 4: Все элементы четные
    private static void testAllEvenElements() {
        try {
            List<Integer> list = new ArrayList<>(Arrays.asList(8, 2, 6, 4));
            CollectionEvenSorter.sort(list, Integer::compareTo, x -> x);
            List<Integer> expected = Arrays.asList(2, 4, 6, 8);
            assert list.equals(expected) : "Все четные элементы должны быть отсортированы";
            System.out.println(GREEN + "✅ Тест всех четных элементов пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест всех четных элементов провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 5: Смешанные элементы (четные и нечетные)
    private static void testMixedElements() {
        try {
            List<Integer> list = new ArrayList<>(Arrays.asList(1, 8, 3, 2, 5, 6, 7, 4));
            CollectionEvenSorter.sort(list, Integer::compareTo, x -> x);
            List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);
            assert list.equals(expected) : "Четные элементы должны быть отсортированы, нечетные на местах";
            System.out.println(GREEN + "✅ Тест смешанных элементов пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест смешанных элементов провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 6: Уже отсортированный список
    private static void testAlreadySorted() {
        try {
            List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
            CollectionEvenSorter.sort(list, Integer::compareTo, x -> x);
            List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 6);
            assert list.equals(expected) : "Уже отсортированный список должен остаться без изменений";
            System.out.println(GREEN + "✅ Тест уже отсортированного списка пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест уже отсортированного списка провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 7: Обратно отсортированный список
    private static void testReverseSorted() {
        try {
            List<Integer> list = new ArrayList<>(Arrays.asList(5, 8, 3, 6, 1, 4, 7, 2));
            CollectionEvenSorter.sort(list, Integer::compareTo, x -> x);
            List<Integer> expected = Arrays.asList(5, 2, 3, 4, 1, 6, 7, 8);
            assert list.equals(expected) : "Четные элементы должны быть отсортированы по возрастанию";
            System.out.println(GREEN + "✅ Тест обратно отсортированного списка пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест обратно отсортированного списка провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 8: Строковые элементы
    private static void testStringElements() {
        try {
            List<String> list = new ArrayList<>(Arrays.asList("a", "d", "b", "f", "c", "e"));
            CollectionEvenSorter.sort(list, String::compareTo, s -> s.length());
            List<String> expected = Arrays.asList("a", "b", "c", "d", "e", "f");
            assert list.equals(expected) : "Строки должны быть отсортированы";
            System.out.println(GREEN + "✅ Тест строковых элементов пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест строковых элементов провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 9: Большой список
    private static void testLargeList() {
        try {
            List<Integer> list = new ArrayList<>();
            for (int i = 100; i >= 1; i--) {
                list.add(i);
            }
            CollectionEvenSorter.sort(list, Integer::compareTo, x -> x);

            List<Integer> evenElements = new ArrayList<>();
            List<Integer> oddPositions = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i) % 2 == 0) {
                    evenElements.add(list.get(i));
                } else {
                    oddPositions.add(list.get(i));
                }
            }

            for (int i = 1; i < evenElements.size(); i++) {
                assert evenElements.get(i-1) <= evenElements.get(i) : "Четные элементы должны быть отсортированы";
            }

            System.out.println(GREEN + "✅ Тест большого списка пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест большого списка провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 10: Пользовательские объекты
    private static void testCustomObjects() {
        try {
            List<Person> list = new ArrayList<>(Arrays.asList(
                    new Person.Builder().name("Alice").age(25).weight(55.5).build(),
                    new Person.Builder().name("Bob").age(30).weight(70.0).build(),
                    new Person.Builder().name("Charlie").age(35).weight(55.5).build(),
                    new Person.Builder().name("David").age(40).weight(55.5).build()
            ));

            CollectionEvenSorter.sort(list,
                    Comparator.comparing(Person::getName),
                    Person::getAge);

            assert list.get(1).getAge() == 30 : "Bob должен быть на второй позиции (возраст 30 - четный)";
            assert list.get(3).getAge() == 40 : "David должен быть на четвертой позиции (возраст 40 - четный)";

            assert list.get(0).getName().equals("Alice") : "Alice должна быть на первой позиции";
            assert list.get(2).getName().equals("Charlie") : "Charlie должен быть на третьей позиции";

            System.out.println(GREEN + "✅ Тест пользовательских объектов пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест пользовательских объектов провален: " + e.getMessage() + RESET);
            e.printStackTrace();
        }
    }

    // Тест 11: Отрицательные числа
    private static void testNegativeNumbers() {
        try {
            List<Integer> list = new ArrayList<>(Arrays.asList(-3, -4, -1, -2, 0, 1, 2));
            CollectionEvenSorter.sort(list, Integer::compareTo, x -> x);
            List<Integer> expected = Arrays.asList(-3, -4, -1, -2, 0, 1, 2);
            assert list.equals(expected) : "Отрицательные четные числа должны быть отсортированы";
            System.out.println(GREEN + "✅ Тест отрицательных чисел пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест отрицательных чисел провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 12: Дубликаты
    private static void testDuplicates() {
        try {
            List<Integer> list = new ArrayList<>(Arrays.asList(2, 4, 2, 6, 4, 8, 2));
            CollectionEvenSorter.sort(list, Integer::compareTo, x -> x);
            List<Integer> expected = Arrays.asList(2, 2, 2, 4, 4, 6, 8);
            assert list.equals(expected) : "Дубликаты четных чисел должны быть отсортированы";
            System.out.println(GREEN + "✅ Тест дубликатов пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест дубликатов провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 13: Один четный элемент
    private static void testSingleEvenElement() {
        try {
            List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3));
            CollectionEvenSorter.sort(list, Integer::compareTo, x -> x);
            List<Integer> expected = Arrays.asList(1, 2, 3);
            assert list.equals(expected) : "Список с одним четным элементом должен остаться без изменений";
            System.out.println(GREEN + "✅ Тест одного четного элемента пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест одного четного элемента провален: " + e.getMessage() + RESET);
        }
    }
}
