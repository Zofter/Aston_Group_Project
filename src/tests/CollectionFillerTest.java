package tests;
import model.Person;

import strategy.PersonStrategy;
import service.CollectionFiller;
import java.util.List;
import java.util.NoSuchElementException;

public class CollectionFillerTest {

    public static void main(String[] args) {
        testFillWithFixedSize();
        testFillWithEmptyStrategy();
        testFillWithThrowingStrategy();
    }

    // Тест: заполнение коллекции фиксированного размера
    static void testFillWithFixedSize() {
        System.out.println("=== Тест 1: Заполнение коллекции фиксированного размера ===");
        PersonStrategy strategy = b -> b.name("Иван").age(30).weight(75.5);
        List<Person> result = CollectionFiller.fill(strategy, 3);

        boolean success = result.size() == 3;
        for (Person p : result) {
            if (!p.getName().equals("Иван") || p.getAge() != 30 || p.getWeight() != 75.5) {
                success = false;
                break;
            }
        }

        System.out.println(success ? "✅ УСПЕХ" : "❌ ПРОВАЛ");
        result.forEach(System.out::print);
        System.out.println();
    }

    // Тест: стратегия, которая не заполняет данные (пустой билдер) - проверка полей на дефолтные значения
    static void testFillWithEmptyStrategy() {
        System.out.println("=== Тест 2: Стратегия без заполнения данных ===");
        PersonStrategy strategy = b -> {}; // Ничего не делает
        List<Person> result = CollectionFiller.fill(strategy, 2);

        boolean success = result.size() == 2;
        for (Person p : result) {
            if (p.getName() != null || p.getAge() == 0 || p.getWeight() == 0.0) {
                success = true;
                break;
            }
        }

        System.out.println(success ? "✅ УСПЕХ" : "❌ ПРОВАЛ");
        result.forEach(System.out::print);
        System.out.println();
    }

    // Тест: стратегия, которая бросает исключение после первого элемента
    static void testFillWithThrowingStrategy() {
        System.out.println("=== Тест 3: Стратегия с исключением ===");
        class ThrowingStrategy implements PersonStrategy {
            private int count = 0;
            @Override
            public void getPerson(Person.Builder b) {
                if (count++ > 0) {
                    throw new NoSuchElementException("Искусственное завершение");
                }
                b.name("Первый").age(25).weight(68.0);
            }
        }

        List<Person> result = CollectionFiller.fill(new ThrowingStrategy(), 5);
        boolean success = result.size() == 1 &&
                "Первый".equals(result.get(0).getName()) &&
                result.get(0).getAge() == 25 &&
                result.get(0).getWeight() == 68.0;

        System.out.println(success ? "✅ УСПЕХ" : "❌ ПРОВАЛ");
        result.forEach(System.out::print);
        System.out.println();
    }
}

