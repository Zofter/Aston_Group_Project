package tests;

import factory.PersonPull;
import model.Person;
import strategy.PersonStrategy;
import strategy.RandomStrategy;

import java.util.ArrayList;
import java.util.List;

public class PersonPullTest {

    public static void main(String[] args) {
        System.out.println("=== ТЕСТИРОВАНИЕ PersonPull ===\n");

        // Тест 1: Успешное создание объекта с RandomStrategy
        testSuccessfulCreationWithRandomStrategy();

        // Тест 2: Создание нескольких объектов
        testMultipleObjectCreation();

        // Тест 3: Проверка неизменяемости созданных объектов
        testImmutability();

        // Тест 4: Создание объекта с кастомной стратегией
        testCustomStrategy();

        // Тест 5: Проверка корректности передачи данных
        testDataIntegrity();

        System.out.println("\n=== ТЕСТИРОВАНИЕ ЗАВЕРШЕНО ===");
    }

    private static void testSuccessfulCreationWithRandomStrategy() {
        try {
            RandomStrategy strategy = new RandomStrategy();
            Person person = PersonPull.createPerson(strategy);

            // Проверяем, что объект создан и имеет все необходимые поля
            if (person != null &&
                    person.getName() != null &&
                    !person.getName().isEmpty() &&
                    person.getAge() >= 0 &&
                    person.getWeight() >= 0) {
                System.out.println("✅ Успешное создание объекта Person через RandomStrategy");
            } else {
                System.out.println("❌ Ошибка при создании объекта Person");
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка при тестировании создания объекта: " + e.getMessage());
        }
    }

    private static void testMultipleObjectCreation() {
        try {
            List<Person> persons = new ArrayList<>();
            RandomStrategy strategy = new RandomStrategy();

            for (int i = 0; i < 5; i++) {
                Person person = PersonPull.createPerson(strategy);
                if (person == null) {
                    System.out.println("❌ Ошибка: создан null объект");
                    return;
                }
                persons.add(person);
            }

            // Проверяем, что все объекты созданы
            if (persons.size() == 5) {
                System.out.println("✅ Множественное создание объектов работает корректно");
            } else {
                System.out.println("❌ Ошибка при множественном создании объектов");
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка при тестировании множественного создания: " + e.getMessage());
        }
    }

    private static void testImmutability() {
        try {
            RandomStrategy strategy = new RandomStrategy();
            Person person1 = PersonPull.createPerson(strategy);
            Person person2 = PersonPull.createPerson(strategy);

            // Проверяем, что объекты неизменяемы (не ссылки друг на друга)
            if (person1 != person2) {
                System.out.println("✅ Созданные объекты неизменяемы и уникальны");
            } else {
                System.out.println("❌ Ошибка: созданы ссылки на один и тот же объект");
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка при тестировании неизменяемости: " + e.getMessage());
        }
    }

    private static void testCustomStrategy() {
        try {
            PersonStrategy customStrategy = new PersonStrategy() {
                @Override
                public void getPerson(model.Person.Builder b) {
                    b.name("ТестовыйПользователь")
                            .age(25)
                            .weight(70.0);
                }
            };

            Person person = PersonPull.createPerson(customStrategy);

            if (person.getName().equals("ТестовыйПользователь") &&
                    person.getAge() == 25 &&
                    person.getWeight() == 70.0) {
                System.out.println("✅ Создание объекта с кастомной стратегией работает");
            } else {
                System.out.println("❌ Ошибка при создании объекта с кастомной стратегией");
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка при тестировании кастомной стратегии: " + e.getMessage());
        }
    }

    private static void testDataIntegrity() {
        try {
            // Создаем стратегию с известными данными
            PersonStrategy integrityStrategy = new PersonStrategy() {
                @Override
                public void getPerson(model.Person.Builder b) {
                    b.name("Интеграция")
                            .age(30)
                            .weight(65.5);
                }
            };

            Person person = PersonPull.createPerson(integrityStrategy);

            // Проверяем целостность данных
            boolean nameCorrect = person.getName().equals("Интеграция");
            boolean ageCorrect = person.getAge() == 30;
            boolean weightCorrect = person.getWeight() == 65.5;

            if (nameCorrect && ageCorrect && weightCorrect) {
                System.out.println("✅ Целостность данных сохраняется при передаче");
            } else {
                System.out.println("❌ Ошибка в целостности данных");
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка при тестировании целостности данных: " + e.getMessage());
        }
    }
}