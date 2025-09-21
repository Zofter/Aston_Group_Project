package tests;

import model.Person;
import strategy.ManualStrategy;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class ManualStrategyTest {

    public static void main(String[] args) {
        System.out.println("=== ТЕСТИРОВАНИЕ ManualStrategy ===\n");

        // Тест 1: Успешное создание объекта с корректными данными
        testSuccessfulCreation();

        // Тест 2: Обработка некорректного возраста
        testInvalidAgeHandling();

        // Тест 3: Обработка некорректного веса
        testInvalidWeightHandling();

        System.out.println("\n=== ТЕСТИРОВАНИЕ ЗАВЕРШЕНО ===");
    }

    private static void testSuccessfulCreation() {
        System.out.println("Тест 1: Успешное создание объекта");

        String input = "Иван\n25\n75.5\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);

        try {
            ManualStrategy strategy = new ManualStrategy();
            Person.Builder builder = new Person.Builder();
            strategy.getPerson(builder);
            Person person = builder.build();

            if (person.getName().equals("Иван") &&
                    person.getAge() == 25 &&
                    person.getWeight() == 75.5) {
                System.out.println("✅ Успешное создание объекта с корректными данными");
            } else {
                System.out.println("❌ Ошибка при создании объекта с корректными данными");
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка при тестировании успешного создания: " + e.getMessage());
        }

        System.setIn(System.in);
    }

    private static void testInvalidAgeHandling() {
        System.out.println("\nТест 2: Обработка некорректного возраста");

        String input = "Мария\nне_число\n30\n65.0\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);

        try {
            ManualStrategy strategy = new ManualStrategy();
            Person.Builder builder = new Person.Builder();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outputStream));

            strategy.getPerson(builder);
            Person person = builder.build();

            System.setOut(originalOut);

            if (person.getName().equals("Мария") &&
                    person.getAge() == 30 &&
                    person.getWeight() == 65.0) {
                System.out.println("✅ Корректная обработка некорректного возраста");
            } else {
                System.out.println("❌ Ошибка при обработке некорректного возраста");
            }
        } catch (Exception e) {
            System.setIn(System.in);
            System.setOut(System.out);
            System.out.println("❌ Ошибка при тестировании обработки возраста: " + e.getMessage());
        }

        System.setIn(System.in);
    }

    private static void testInvalidWeightHandling() {
        System.out.println("\nТест 3: Обработка некорректного веса");

        String input = "Алексей\n40\nне_вес\n70.5\n";
        InputStream inputStream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        System.setIn(inputStream);

        try {
            ManualStrategy strategy = new ManualStrategy();
            Person.Builder builder = new Person.Builder();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream originalOut = System.out;
            System.setOut(new PrintStream(outputStream));

            strategy.getPerson(builder);
            Person person = builder.build();

            System.setOut(originalOut);

            if (person.getName().equals("Алексей") &&
                    person.getAge() == 40 &&
                    person.getWeight() == 70.5) {
                System.out.println("✅ Корректная обработка некорректного веса");
            } else {
                System.out.println("❌ Ошибка при обработке некорректного веса");
            }
        } catch (Exception e) {
            System.setIn(System.in);
            System.setOut(System.out);
            System.out.println("❌ Ошибка при тестировании обработки веса: " + e.getMessage());
        }

        System.setIn(System.in);
    }
}