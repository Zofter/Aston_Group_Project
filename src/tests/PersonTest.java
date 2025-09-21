package tests;
import model.Person;

public class PersonTest {

    public static void main(String[] args) {
        System.out.println("=== ТЕСТИРОВАНИЕ КЛАССА PERSON ===\n");

        // Тест 1: Успешное создание объекта
        try {
            Person person1 = new Person.Builder()
                    .name("Иван")
                    .age(30)
                    .weight(75.5)
                    .build();
            System.out.println("✅ Объект Person успешно создан");
        } catch (Exception e) {
            System.out.println("❌ Ошибка при создании объекта Person: " + e.getMessage());
        }

        // Тест 2: Попытка создать без имени
        try {
            Person person2 = new Person.Builder()
                    .age(25)
                    .weight(60.0)
                    .build();
            System.out.println("❌ Ошибка: объект создан без имени");
        } catch (IllegalStateException e) {
            System.out.println("✅ Исключение при создании без имени: " + e.getMessage());
        }

        // Тест 3: Пустое имя
        try {
            new Person.Builder().name("");
            System.out.println("❌ Ошибка: пустое имя принято");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ Исключение при пустом имени: " + e.getMessage());
        }

        // Тест 4: Неверный возраст (отрицательный)
        try {
            new Person.Builder().age(-5);
            System.out.println("❌ Ошибка: отрицательный возраст принят");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ Исключение при отрицательном возрасте: " + e.getMessage());
        }

        // Тест 5: Неверный возраст (слишком большой)
        try {
            new Person.Builder().age(200);
            System.out.println("❌ Ошибка: слишком большой возраст принят");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ Исключение при слишком большом возрасте: " + e.getMessage());
        }

        // Тест 6: Неверный вес (отрицательный)
        try {
            new Person.Builder().weight(-10);
            System.out.println("❌ Ошибка: отрицательный вес принят");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ Исключение при отрицательном весе: " + e.getMessage());
        }

        // Тест 7: Неверный вес (слишком большой)
        try {
            new Person.Builder().weight(600);
            System.out.println("❌ Ошибка: слишком большой вес принят");
        } catch (IllegalArgumentException e) {
            System.out.println("✅ Исключение при слишком большом весе: " + e.getMessage());
        }

        // Тест 8: equals и hashCode
        try {
            Person personA = new Person.Builder().name("Анна").age(25).weight(55.0).build();
            Person personB = new Person.Builder().name("Анна").age(25).weight(55.0).build();
            Person personC = new Person.Builder().name("Олег").age(30).weight(80.0).build();

            boolean equals1 = personA.equals(personB);
            boolean equals2 = !personA.equals(personC);
            boolean equals3 = !personA.equals(null);
            boolean equals4 = !personA.equals("строка");

            if (equals1 && equals2 && equals3 && equals4) {
                System.out.println("✅ Метод equals работает корректно");
            } else {
                System.out.println("❌ Ошибка в методе equals");
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка при тестировании equals: " + e.getMessage());
        }

        // Тест 9: toString
        try {
            Person person = new Person.Builder().name("Мария").age(28).weight(62.3).build();
            String result = person.toString();
            boolean containsName = result.contains("Мария");
            boolean containsAge = result.contains("28");
            boolean containsWeight = result.contains("62.3");

            if (containsName && containsAge && containsWeight) {
                System.out.println("✅ Метод toString работает корректно");
            } else {
                System.out.println("❌ Ошибка в методе toString");
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка при тестировании toString: " + e.getMessage());
        }

        // Тест 10: Граничные значения (минимальные)
        try {
            Person person = new Person.Builder()
                    .name("Тест")
                    .age(0)
                    .weight(0.0)
                    .build();
            System.out.println("✅ Граничные минимальные значения приняты");
        } catch (Exception e) {
            System.out.println("❌ Ошибка при граничных минимальных значениях: " + e.getMessage());
        }

        // Тест 11: Граничные значения (максимальные)
        try {
            Person person = new Person.Builder()
                    .name("Тест")
                    .age(150)
                    .weight(500.0)
                    .build();
            System.out.println("✅ Граничные максимальные значения приняты");
        } catch (Exception e) {
            System.out.println("❌ Ошибка при граничных максимальных значениях: " + e.getMessage());
        }

        System.out.println("\n=== ТЕСТИРОВАНИЕ ЗАВЕРШЕНО ===");
    }
}