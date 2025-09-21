package tests;

import io.FileWriterJSON_Util;
import model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;

public class FileWriterJSON_UtilTest {

    public static void main(String[] args) {
        testWritePersons();
    }

    public static void testWritePersons() {
        try {
            Path tempFile = Files.createTempFile("persons_test", ".json");

            Collection<Person> persons = createTestPersons();

            // FileWriterJSON_Util.writePersons(tempFile, persons);

            String content = Files.readString(tempFile);

            // Проверяем, что файл не пустой
            if (content != null && !content.isEmpty()) {
                System.out.println("✅ Файл успешно создан и содержит данные");
            } else {
                System.out.println("❌ Файл пустой");
            }

            // Проверяем формат JSON
            if (content.contains("{") && content.contains("}") && content.contains("\"name\"")) {
                System.out.println("✅ Содержимое имеет правильный формат JSON");
            } else {
                System.out.println("❌ Неправильный формат JSON");
            }

            // Проверяем наличие всех полей
            if (content.contains("\"name\"") && content.contains("\"age\"") && content.contains("\"weight\"")) {
                System.out.println("✅ Все поля объекта Person присутствуют в JSON");
            } else {
                System.out.println("❌ Не все поля объекта Person присутствуют в JSON");
            }

            Files.deleteIfExists(tempFile);

        } catch (IOException e) {
            System.out.println("❌ Ошибка при работе с файлом: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Неожиданная ошибка: " + e.getMessage());
        }
    }

    private static Collection<Person> createTestPersons() {
        Person person1 = new Person.Builder()
                .name("Иван")
                .age(25)
                .weight(70.5)
                .build();

        Person person2 = new Person.Builder()
                .name("Мария")
                .age(30)
                .weight(65.0)
                .build();

        Person person3 = new Person.Builder()
                .name("Петр")
                .age(35)
                .weight(80.2)
                .build();

        return Arrays.asList(person1, person2, person3);
    }
}