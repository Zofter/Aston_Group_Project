package tests;

import comparator.PersonNameComparator;
import model.Person;

public class PersonNameComparatorTest {

    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        System.out.println("Запуск тестов PersonNameComparator...");
        System.out.println("===================================");

        try {
            testCompareLexicographically();
            testCompareEqualNames();
            testCompareSameObject();
            testCompareWithDifferentAges();
            testAlphabeticalOrder();
            testCaseSensitivity();
            testEmptyAndSpecialNames();

            System.out.println(GREEN + "✅ Все тесты пройдены успешно!" + RESET);
        } catch (Exception e) {
            System.err.println(RED + "❌ Тест провален: " + e.getMessage() + RESET);
            e.printStackTrace();
        }
    }

    // Тест 1: Лексикографическое сравнение имен
    private static void testCompareLexicographically() {
        try {
            PersonNameComparator comparator = new PersonNameComparator();
            Person person1 = new Person.Builder().name("Alice").age(25).weight(55.5).build();
            Person person2 = new Person.Builder().name("Bob").age(30).weight(70.0).build();

            int result = comparator.compare(person1, person2);
            assert result < 0 : "Ожидалось отрицательное значение, так как 'Alice' < 'Bob', получено: " + result;

            System.out.println(GREEN + "✅ Тест лексикографического сравнения пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест лексикографического сравнения провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 2: Сравнение одинаковых имен
    private static void testCompareEqualNames() {
        try {
            PersonNameComparator comparator = new PersonNameComparator();
            Person person1 = new Person.Builder().name("Charlie").age(25).weight(55.5).build();
            Person person2 = new Person.Builder().name("Charlie").age(35).weight(70.0).build();

            int result = comparator.compare(person1, person2);
            assert result == 0 : "Ожидалось 0, так как имена одинаковые, получено: " + result;

            System.out.println(GREEN + "✅ Тест сравнения одинаковых имен пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест сравнения одинаковых имен провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 3: Сравнение объекта с самим собой
    private static void testCompareSameObject() {
        try {
            PersonNameComparator comparator = new PersonNameComparator();
            Person person = new Person.Builder().name("David").age(30).weight(65.0).build();

            int result = comparator.compare(person, person);
            assert result == 0 : "Ожидалось 0 при сравнении объекта с самим собой, получено: " + result;

            System.out.println(GREEN + "✅ Тест сравнения с самим собой пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест сравнения с самим собой провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 4: Сравнение с разными возрастами, но одинаковыми именами
    private static void testCompareWithDifferentAges() {
        try {
            PersonNameComparator comparator = new PersonNameComparator();
            Person person1 = new Person.Builder().name("Eve").age(20).weight(50.0).build();
            Person person2 = new Person.Builder().name("Eve").age(40).weight(60.0).build();

            int result = comparator.compare(person1, person2);
            assert result == 0 : "Ожидалось 0, так как имена одинаковые, несмотря на разный возраст, получено: " + result;

            System.out.println(GREEN + "✅ Тест сравнения с разными возрастами пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест сравнения с разными возрастами провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 5: Алфавитный порядок
    private static void testAlphabeticalOrder() {
        try {
            PersonNameComparator comparator = new PersonNameComparator();
            Person person1 = new Person.Builder().name("Zoe").age(25).weight(55.0).build();
            Person person2 = new Person.Builder().name("Adam").age(30).weight(70.0).build();
            Person person3 = new Person.Builder().name("Mike").age(35).weight(75.0).build();

            assert comparator.compare(person1, person2) > 0 : "Ожидалось положительное значение, так как 'Zoe' > 'Adam'";

            assert comparator.compare(person2, person3) < 0 : "Ожидалось отрицательное значение, так как 'Adam' < 'Mike'";

            assert comparator.compare(person1, person3) > 0 : "Ожидалось положительное значение, так как 'Zoe' > 'Mike'";

            System.out.println(GREEN + "✅ Тест алфавитного порядка пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест алфавитного порядка провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 6: Чувствительность к регистру
    private static void testCaseSensitivity() {
        try {
            PersonNameComparator comparator = new PersonNameComparator();
            Person person1 = new Person.Builder().name("alice").age(25).weight(55.0).build();
            Person person2 = new Person.Builder().name("Bob").age(30).weight(70.0).build();
            Person person3 = new Person.Builder().name("Alice").age(35).weight(60.0).build();

            assert comparator.compare(person1, person2) > 0 : "Ожидалось положительное значение для 'alice' vs 'Bob'";

            assert comparator.compare(person1, person3) > 0 : "Ожидалось положительное значение для 'alice' vs 'Alice'";

            assert comparator.compare(person3, person2) < 0 : "Ожидалось отрицательное значение для 'Alice' vs 'Bob'";

            System.out.println(GREEN + "✅ Тест чувствительности к регистру пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест чувствительности к регистру провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 7: Пустые и специальные имена
    private static void testEmptyAndSpecialNames() {
        try {
            PersonNameComparator comparator = new PersonNameComparator();
            Person person1 = new Person.Builder().name(" ").age(25).weight(55.0).build();
            Person person2 = new Person.Builder().name("").age(30).weight(60.0).build();
            Person person3 = new Person.Builder().name("Anna").age(35).weight(58.0).build();

            assert comparator.compare(person1, person2) > 0 : "Пробел должен быть больше пустой строки";

            assert comparator.compare(person2, person3) < 0 : "Пустая строка должна быть меньше 'Anna'";

            System.out.println(GREEN + "✅ Тест пустых и специальных имен пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест пустых и специальных имен провален: " + e.getMessage() + RESET);
        }
    }
}