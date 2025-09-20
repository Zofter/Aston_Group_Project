package tests;

import comparator.PersonWeightComparator;
import model.Person;

public class PersonWeightComparatorTest {

    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        System.out.println("Запуск тестов PersonWeightComparator...");
        System.out.println("====================================");

        try {
            testCompareLessThan();
            testCompareGreaterThan();
            testCompareEqual();
            testCompareSameObject();
            testCompareWithDifferentAges();
            testConsistentOrdering();
            testBoundaryWeights();

            System.out.println(GREEN + "✅ Все тесты пройдены успешно!" + RESET);
        } catch (Exception e) {
            System.err.println(RED + "❌ Тест провален: " + e.getMessage() + RESET);
            e.printStackTrace();
        }
    }

    // Тест 1: Первый человек легче второго
    private static void testCompareLessThan() {
        try {
            PersonWeightComparator comparator = new PersonWeightComparator();
            Person person1 = new Person.Builder().name("Alice").age(25).weight(55.5).build();
            Person person2 = new Person.Builder().name("Bob").age(30).weight(70.0).build();

            int result = comparator.compare(person1, person2);
            assert result < 0 : "Ожидалось отрицательное значение, так как 55.5 < 70.0, получено: " + result;

            System.out.println(GREEN + "✅ Тест сравнения 'меньше' пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест сравнения 'меньше' провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 2: Первый человек тяжелее второго
    private static void testCompareGreaterThan() {
        try {
            PersonWeightComparator comparator = new PersonWeightComparator();
            Person person1 = new Person.Builder().name("Charlie").age(35).weight(85.5).build();
            Person person2 = new Person.Builder().name("David").age(40).weight(65.0).build();

            int result = comparator.compare(person1, person2);
            assert result > 0 : "Ожидалось положительное значение, так как 85.5 > 65.0, получено: " + result;

            System.out.println(GREEN + "✅ Тест сравнения 'больше' пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест сравнения 'больше' провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 3: Люди с одинаковым весом
    private static void testCompareEqual() {
        try {
            PersonWeightComparator comparator = new PersonWeightComparator();
            Person person1 = new Person.Builder().name("Eve").age(28).weight(60.0).build();
            Person person2 = new Person.Builder().name("Frank").age(32).weight(60.0).build();

            int result = comparator.compare(person1, person2);
            assert result == 0 : "Ожидалось 0, так как оба имеют вес 60.0, получено: " + result;

            System.out.println(GREEN + "✅ Тест сравнения 'равно' пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест сравнения 'равно' провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 4: Сравнение объекта с самим собой
    private static void testCompareSameObject() {
        try {
            PersonWeightComparator comparator = new PersonWeightComparator();
            Person person = new Person.Builder().name("Grace").age(33).weight(58.5).build();

            int result = comparator.compare(person, person);
            assert result == 0 : "Ожидалось 0 при сравнении объекта с самим собой, получено: " + result;

            System.out.println(GREEN + "✅ Тест сравнения с самим собой пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест сравнения с самим собой провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 5: Сравнение с разными возрастами, но одинаковым весом
    private static void testCompareWithDifferentAges() {
        try {
            PersonWeightComparator comparator = new PersonWeightComparator();
            Person person1 = new Person.Builder().name("Henry").age(25).weight(75.0).build();
            Person person2 = new Person.Builder().name("Ivy").age(45).weight(75.0).build();

            int result = comparator.compare(person1, person2);
            assert result == 0 : "Ожидалось 0, так как вес одинаковый (75.0), несмотря на разный возраст, получено: " + result;

            System.out.println(GREEN + "✅ Тест сравнения с разными возрастами пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест сравнения с разными возрастами провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 6: Проверка согласованного порядка (транзитивность)
    private static void testConsistentOrdering() {
        try {
            PersonWeightComparator comparator = new PersonWeightComparator();
            Person person1 = new Person.Builder().name("Jack").age(22).weight(50.0).build();
            Person person2 = new Person.Builder().name("Kate").age(27).weight(65.5).build();
            Person person3 = new Person.Builder().name("Liam").age(32).weight(80.0).build();

            int result1 = comparator.compare(person1, person2);
            int result2 = comparator.compare(person2, person3);
            int result3 = comparator.compare(person1, person3);

            assert result1 < 0 && result2 < 0 && result3 < 0 :
                    "Все результаты должны быть отрицательными для транзитивного порядка";

            int reverse1 = comparator.compare(person2, person1);
            assert reverse1 > 0 : "Обратное сравнение должно давать положительный результат";

            System.out.println(GREEN + "✅ Тест согласованного порядка пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест согласованного порядка провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 7: Граничные значения веса
    private static void testBoundaryWeights() {
        try {
            PersonWeightComparator comparator = new PersonWeightComparator();
            Person person1 = new Person.Builder().name("Mia").age(20).weight(0.0).build();
            Person person2 = new Person.Builder().name("Noah").age(25).weight(0.1).build();
            Person person3 = new Person.Builder().name("Olivia").age(30).weight(499.9).build();
            Person person4 = new Person.Builder().name("Peter").age(35).weight(500.0).build();

            assert comparator.compare(person1, person2) < 0 : "0.0 должен быть меньше 0.1";

            assert comparator.compare(person3, person4) < 0 : "499.9 должен быть меньше 500.0";

            assert comparator.compare(person2, person3) < 0 : "0.1 должен быть меньше 499.9";

            System.out.println(GREEN + "✅ Тест граничных значений веса пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест граничных значений веса провален: " + e.getMessage() + RESET);
        }
    }
}