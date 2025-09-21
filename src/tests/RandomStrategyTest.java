package tests;

import model.Person;
import strategy.RandomStrategy;

public class RandomStrategyTest {

    public static void main(String[] args) {
        System.out.println("=== ТЕСТИРОВАНИЕ RandomStrategy ===\n");

        // Тест 1: Успешное создание объектов
        testSuccessfulCreation();

        // Тест 2: Проверка диапазонов значений
        testValueRanges();

        // Тест 3: Уникальность сгенерированных данных
        testUniqueness();

        // Тест 4: Множественное создание объектов
        testMultipleCreations();

        System.out.println("\n=== ТЕСТИРОВАНИЕ ЗАВЕРШЕНО ===");
    }

    private static void testSuccessfulCreation() {
        try {
            RandomStrategy strategy = new RandomStrategy();
            Person.Builder builder = new Person.Builder();
            strategy.getPerson(builder);
            Person person = builder.build();

            // Проверяем, что объект создан и имеет все необходимые поля
            if (person.getName() != null &&
                    !person.getName().isEmpty() &&
                    person.getAge() >= 0 &&
                    person.getWeight() >= 0) {
                System.out.println("✅ Успешное создание объекта Person");
            } else {
                System.out.println("❌ Ошибка при создании объекта Person");
            }
        } catch (Exception e) {
            System.out.println("❌ Ошибка при тестировании создания объекта: " + e.getMessage());
        }
    }

    private static void testValueRanges() {
        try {
            RandomStrategy strategy = new RandomStrategy();
            boolean ageInRange = true;
            boolean weightInRange = true;
            boolean nameValid = true;

            // Проверяем 100 сгенерированных объектов
            for (int i = 0; i < 100; i++) {
                Person.Builder builder = new Person.Builder();
                strategy.getPerson(builder);
                Person person = builder.build();

                // Проверка диапазона возраста: 18 + [0..49] = [18..67]
                if (person.getAge() < 18 || person.getAge() > 67) {
                    ageInRange = false;
                }

                // Проверка диапазона веса: 40 + [0..59] = [40..99]
                if (person.getWeight() < 40 || person.getWeight() > 99) {
                    weightInRange = false;
                }

                // Проверка имени: должно начинаться с "User" и содержать число
                if (!person.getName().startsWith("User") ||
                        person.getName().length() <= 4) {
                    nameValid = false;
                }
            }

            if (ageInRange) {
                System.out.println("✅ Диапазон возраста соответствует ожиданиям [18..67]");
            } else {
                System.out.println("❌ Ошибка в диапазоне возраста");
            }

            if (weightInRange) {
                System.out.println("✅ Диапазон веса соответствует ожиданиям [40..99]");
            } else {
                System.out.println("❌ Ошибка в диапазоне веса");
            }

            if (nameValid) {
                System.out.println("✅ Формат имени соответствует ожиданиям (UserXXXX)");
            } else {
                System.out.println("❌ Ошибка в формате имени");
            }

        } catch (Exception e) {
            System.out.println("❌ Ошибка при тестировании диапазонов: " + e.getMessage());
        }
    }

    private static void testUniqueness() {
        try {
            RandomStrategy strategy = new RandomStrategy();
            Person.Builder builder1 = new Person.Builder();
            strategy.getPerson(builder1);
            Person person1 = builder1.build();

            Person.Builder builder2 = new Person.Builder();
            strategy.getPerson(builder2);
            Person person2 = builder2.build();

            // Проверяем, что хотя бы одно из значений отличается
            // (маловероятно, но возможно совпадение)
            boolean isDifferent = !person1.getName().equals(person2.getName()) ||
                    person1.getAge() != person2.getAge() ||
                    person1.getWeight() != person2.getWeight();

            if (isDifferent) {
                System.out.println("✅ Сгенерированные объекты уникальны");
            } else {
                System.out.println("⚠️  Возможное совпадение объектов (статистически допустимо)");
            }

        } catch (Exception e) {
            System.out.println("❌ Ошибка при тестировании уникальности: " + e.getMessage());
        }
    }

    private static void testMultipleCreations() {
        try {
            RandomStrategy strategy = new RandomStrategy();
            boolean allCreated = true;

            // Создаем 10 объектов подряд
            for (int i = 0; i < 10; i++) {
                try {
                    Person.Builder builder = new Person.Builder();
                    strategy.getPerson(builder);
                    Person person = builder.build();

                    // Проверяем, что объект корректно создан
                    if (person.getName() == null ||
                            person.getName().isEmpty() ||
                            person.getAge() < 0 ||
                            person.getWeight() < 0) {
                        allCreated = false;
                        break;
                    }
                } catch (Exception e) {
                    allCreated = false;
                    break;
                }
            }

            if (allCreated) {
                System.out.println("✅ Множественное создание объектов работает корректно");
            } else {
                System.out.println("❌ Ошибка при множественном создании объектов");
            }

        } catch (Exception e) {
            System.out.println("❌ Ошибка при тестировании множественного создания: " + e.getMessage());
        }
    }
}
