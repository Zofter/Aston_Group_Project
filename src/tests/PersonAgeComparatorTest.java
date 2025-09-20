package tests;

import comparator.PersonAgeComparator;
import model.Person;

public class PersonAgeComparatorTest {

    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        System.out.println("Запуск тестов PersonAgeComparator...");
        System.out.println("===================================");

        try {
            testCompareLessThan();
            testCompareGreaterThan();
            testCompareEqual();
            testCompareSameObject();
            testCompareWithNullFields();
            testConsistentWithEquals();

            System.out.println(GREEN + "✅ Все тесты пройдены успешно!" + RESET);
        } catch (Exception e) {
            System.err.println(RED + "❌ Тест провален: " + e.getMessage() + RESET);
            e.printStackTrace();
        }
    }

    // Тест 1: Первый человек младше второго
    private static void testCompareLessThan() {
        try {
            PersonAgeComparator comparator = new PersonAgeComparator();
            Person person1 = new Person.Builder().name("Alice").age(25).weight(55.5).build();
            Person person2 = new Person.Builder().name("Bob").age(30).weight(70.0).build();

            int result = comparator.compare(person1, person2);
            assert result < 0 : "Ожидалось отрицательное значение, так как 25 < 30, получено: " + result;

            System.out.println(GREEN + "✅ Тест сравнения 'меньше' пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест сравнения 'меньше' провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 2: Первый человек старше второго
    private static void testCompareGreaterThan() {
        try {
            PersonAgeComparator comparator = new PersonAgeComparator();
            Person person1 = new Person.Builder().name("Charlie").age(45).weight(55.5).build();
            Person person2 = new Person.Builder().name("David").age(35).weight(55.5).build();

            int result = comparator.compare(person1, person2);
            assert result > 0 : "Ожидалось положительное значение, так как 45 > 35, получено: " + result;

            System.out.println(GREEN + "✅ Тест сравнения 'больше' пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест сравнения 'больше' провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 3: Люди одного возраста
    private static void testCompareEqual() {
        try {
            PersonAgeComparator comparator = new PersonAgeComparator();
            Person person1 = new Person.Builder().name("Eve").age(28).weight(55.5).build();
            Person person2 = new Person.Builder().name("Frank").age(28).weight(70.0).build();

            int result = comparator.compare(person1, person2);
            assert result == 0 : "Ожидалось 0, так как оба имеют возраст 28, получено: " + result;

            System.out.println(GREEN + "✅ Тест сравнения 'равно' пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест сравнения 'равно' провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 4: Сравнение объекта с самим собой
    private static void testCompareSameObject() {
        try {
            PersonAgeComparator comparator = new PersonAgeComparator();
            Person person = new Person.Builder().name("Grace").age(33).weight(55.5).build();

            int result = comparator.compare(person, person);
            assert result == 0 : "Ожидалось 0 при сравнении объекта с самим собой, получено: " + result;

            System.out.println(GREEN + "✅ Тест сравнения с самим собой пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест сравнения с самим собой провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 5: Сравнение с разными весами, но одинаковым возрастом
    private static void testCompareWithNullFields() {
        try {
            PersonAgeComparator comparator = new PersonAgeComparator();
            Person person1 = new Person.Builder().name("Henry").age(40).weight(60.0).build();
            Person person2 = new Person.Builder().name("Ivy").age(40).weight(50.0).build();

            int result = comparator.compare(person1, person2);
            assert result == 0 : "Ожидалось 0, так как возраст одинаковый (40), получено: " + result;

            System.out.println(GREEN + "✅ Тест сравнения с разными весами пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест сравнения с разными весами провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 6: Проверка согласованности с equals (транзитивность)
    private static void testConsistentWithEquals() {
        try {
            PersonAgeComparator comparator = new PersonAgeComparator();
            Person person1 = new Person.Builder().name("Jack").age(22).weight(55.5).build();
            Person person2 = new Person.Builder().name("Kate").age(22).weight(55.5).build();
            Person person3 = new Person.Builder().name("Liam").age(22).weight(55.5).build();

            int result1 = comparator.compare(person1, person2);
            int result2 = comparator.compare(person2, person3);
            int result3 = comparator.compare(person1, person3);

            assert result1 == 0 && result2 == 0 && result3 == 0 :
                    "Все результаты должны быть 0, так как все имеют одинаковый возраст";

            Person person4 = new Person.Builder().name("Mia").age(25).weight(55.5).build();
            Person person5 = new Person.Builder().name("Noah").age(30).weight(55.5).build();

            int result4 = comparator.compare(person4, person5);
            int result5 = comparator.compare(person5, person4);

            assert result4 < 0 && result5 > 0 :
                    "Если 25 < 30, то результат должен быть отрицательным, а обратное сравнение - положительным";

            System.out.println(GREEN + "✅ Тест согласованности с equals пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест согласованности с equals провален: " + e.getMessage() + RESET);
        }
    }
}