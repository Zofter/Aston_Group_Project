package tests;

import service.CountOccurrMultiThreaded;
import java.util.*;

public class CountOccurrMultiThreadedTest {

    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        System.out.println("Запуск тестов...");

        try {
            testEmptyList();
            testNullTarget();
            testSingleElementList();
            testNoOccurrences();
            testAllOccurrences();
            testMixedOccurrences();
            testLargeList();
            testDifferentThreadCounts();
            testNegativeThreadCount();
            testZeroThreadCount();
            testThreadCountExceedingListSize();
            testWithNullElements();
            testStringElements();

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
            int result = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, 5, 4);
            assert result == 0 : "Ожидалось 0 для пустого списка, получено: " + result;
            System.out.println(GREEN + "✅ Тест пустого списка пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест пустого списка провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 2: Целевой элемент null
    private static void testNullTarget() {
        try {
            List<Integer> list = Arrays.asList(1, null, 3, null, 5);
            int result = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, null, 2);
            assert result == 2 : "Ожидалось 2 вхождения null, получено: " + result;
            System.out.println(GREEN + "✅ Тест null целевого элемента пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест null целевого элемента провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 3: Список с одним элементом
    private static void testSingleElementList() {
        try {
            List<Integer> list = Arrays.asList(42);
            int result1 = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, 42, 1);
            int result2 = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, 99, 1);
            assert result1 == 1 : "Ожидалось 1 вхождение, получено: " + result1;
            assert result2 == 0 : "Ожидалось 0 вхождений, получено: " + result2;
            System.out.println(GREEN + "✅ Тест списка с одним элементом пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест списка с одним элементом провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 4: Нет вхождений
    private static void testNoOccurrences() {
        try {
            List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
            int result = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, 99, 3);
            assert result == 0 : "Ожидалось 0 вхождений, получено: " + result;
            System.out.println(GREEN + "✅ Тест отсутствия вхождений пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест отсутствия вхождений провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 5: Все элементы совпадают
    private static void testAllOccurrences() {
        try {
            List<Integer> list = Arrays.asList(7, 7, 7, 7, 7);
            int result = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, 7, 2);
            assert result == 5 : "Ожидалось 5 вхождений, получено: " + result;
            System.out.println(GREEN + "✅ Тест всех совпадений пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест всех совпадений провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 6: Смешанные вхождения
    private static void testMixedOccurrences() {
        try {
            List<Integer> list = Arrays.asList(1, 2, 3, 2, 4, 2, 5);
            int result = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, 2, 3);
            assert result == 3 : "Ожидалось 3 вхождения, получено: " + result;
            System.out.println(GREEN + "✅ Тест смешанных вхождений пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест смешанных вхождений провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 7: Большой список
    private static void testLargeList() {
        try {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                list.add(i % 100); // 0-99 повторяются
            }
            int result = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, 50, 8);
            assert result == 100 : "Ожидалось 100 вхождений, получено: " + result;
            System.out.println(GREEN + "✅ Тест большого списка пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест большого списка провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 8: Разное количество потоков
    private static void testDifferentThreadCounts() {
        try {
            List<Integer> list = Arrays.asList(1, 1, 2, 1, 3, 1, 4, 1);
            int result2 = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, 1, 2);
            int result4 = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, 1, 4);
            int result8 = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, 1, 8);

            assert result2 == 5 : "Ожидалось 5 вхождений с 2 потоками, получено: " + result2;
            assert result4 == 5 : "Ожидалось 5 вхождений с 4 потоками, получено: " + result4;
            assert result8 == 5 : "Ожидалось 5 вхождений с 8 потоками, получено: " + result8;
            System.out.println(GREEN + "✅ Тест разного количества потоков пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест разного количества потоков провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 9: Отрицательное количество потоков
    private static void testNegativeThreadCount() {
        try {
            List<Integer> list = Arrays.asList(1, 2, 1, 3, 1);
            int result = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, 1, -1);
            assert result == 3 : "Ожидалось 3 вхождения с отрицательным threadCount, получено: " + result;
            System.out.println(GREEN + "✅ Тест отрицательного количества потоков пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест отрицательного количества потоков провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 10: Нулевое количество потоков
    private static void testZeroThreadCount() {
        try {
            List<Integer> list = Arrays.asList(1, 2, 1, 3, 1);
            int result = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, 1, 0);
            assert result == 3 : "Ожидалось 3 вхождения с нулевым threadCount, получено: " + result;
            System.out.println(GREEN + "✅ Тест нулевого количества потоков пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест нулевого количества потоков провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 11: Количество потоков больше размера списка
    private static void testThreadCountExceedingListSize() {
        try {
            List<Integer> list = Arrays.asList(1, 2, 3);
            int result = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, 2, 10);
            assert result == 1 : "Ожидалось 1 вхождение, получено: " + result;
            System.out.println(GREEN + "✅ Тест превышения количества потоков пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест превышения количества потоков провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 12: Список с null элементами
    private static void testWithNullElements() {
        try {
            List<Integer> list = Arrays.asList(1, null, 3, null, 5, null);
            int result1 = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, null, 2);
            int result2 = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, 1, 2);
            assert result1 == 3 : "Ожидалось 3 вхождения null, получено: " + result1;
            assert result2 == 1 : "Ожидалось 1 вхождение 1, получено: " + result2;
            System.out.println(GREEN + "✅ Тест с null элементами пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест с null элементами провален: " + e.getMessage() + RESET);
        }
    }

    // Тест 13: Строковые элементы
    private static void testStringElements() {
        try {
            List<String> list = Arrays.asList("hello", "world", "hello", "java", "hello");
            int result = CountOccurrMultiThreaded.countOccurrencesMultiThreaded(list, "hello", 3);
            assert result == 3 : "Ожидалось 3 вхождения 'hello', получено: " + result;
            System.out.println(GREEN + "✅ Тест строковых элементов пройден" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "❌ Тест строковых элементов провален: " + e.getMessage() + RESET);
        }
    }
}