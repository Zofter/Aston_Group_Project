package tests;

import model.Person;
import strategy.FileStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

public class FileStrategyTest {

    public static void main(String[] args) {
        testFileStrategy();
    }

    public static void testFileStrategy() {
        Path testFile = Paths.get("JSON.txt");

        try {
            createTestFile(testFile);

            // Тестируем FileStrategy
            testReadingPersons();

            Files.deleteIfExists(testFile);

        } catch (IOException e) {
            System.out.println("❌ Ошибка при работе с файлом: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Неожиданная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testReadingPersons() {
        try (FileStrategy strategy = new FileStrategy()) {
            Person.Builder builder1 = new Person.Builder();
            Person.Builder builder2 = new Person.Builder();
            Person.Builder builder3 = new Person.Builder();

            strategy.getPerson(builder1);
            Person person1 = builder1.build();

            strategy.getPerson(builder2);
            Person person2 = builder2.build();

            strategy.getPerson(builder3);
            Person person3 = builder3.build();

            if ("Иван".equals(person1.getName()) && person1.getAge() == 25 && Math.abs(person1.getWeight() - 70.5) < 0.001) {
                System.out.println("✅ Первый человек прочитан корректно: " + person1);
            } else {
                System.out.println("❌ Ошибка в данных первого человека: " + person1);
            }

            if ("Мария".equals(person2.getName()) && person2.getAge() == 30 && Math.abs(person2.getWeight() - 65.0) < 0.001) {
                System.out.println("✅ Второй человек прочитан корректно: " + person2);
            } else {
                System.out.println("❌ Ошибка в данных второго человека: " + person2);
            }

            if ("Петр".equals(person3.getName()) && person3.getAge() == 35 && Math.abs(person3.getWeight() - 80.2) < 0.001) {
                System.out.println("✅ Третий человек прочитан корректно: " + person3);
            } else {
                System.out.println("❌ Ошибка в данных третьего человека: " + person3);
            }

            // Проверяем, что больше объектов нет
            try {
                Person.Builder builder4 = new Person.Builder();
                strategy.getPerson(builder4);
                System.out.println("❌ Ожидалось исключение NoSuchElementException");
            } catch (NoSuchElementException e) {
                System.out.println("✅ Корректно обработано окончание файла");
            }

        } catch (Exception e) {
            System.out.println("❌ Ошибка при тестировании FileStrategy: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTestFile(Path filePath) throws IOException {
        String jsonContent = "[\n" +
                "  {\n" +
                "    \"name\": \"Иван\",\n" +
                "    \"age\": 25,\n" +
                "    \"weight\": 70.5\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Мария\",\n" +
                "    \"age\": 30,\n" +
                "    \"weight\": 65.0\n" +
                "  },\n" +
                "  {\n" +
                "    \"name\": \"Петр\",\n" +
                "    \"age\": 35,\n" +
                "    \"weight\": 80.2\n" +
                "  }\n" +
                "]";

        Files.writeString(filePath, jsonContent);
        System.out.println("✅ Тестовый файл создан: " + filePath);
    }
}
