package tests;
import model.Person;
import java.util.*;
import service.BinarySearch;


public class BinarySearchTest {
    public static void main(String[] args) {
        List<Person> people = new ArrayList<>();
        people.add(new Person.Builder().name("Alice").age(25).weight(55.0).build());
        people.add(new Person.Builder().name("Bob").age(30).weight(70.0).build());
        people.add(new Person.Builder().name("Charlie").age(35).weight(80.0).build());

        people.sort(Comparator.comparing(Person::getAge));

        // === Случай 1: успешный поиск ===
        var result1 = BinarySearch.search(
                people,
                new Person.Builder().name("Bob").age(30).weight(70.0).build(),
                Comparator.comparing(Person::getAge)
        );
        System.out.println(result1.isPresent() ? "✅ Найден Bob" : "❌ Bob не найден");

        // === Случай 2: элемент отсутствует ===
        var result2 = BinarySearch.search(
                people,
                new Person.Builder().name("Daniel").age(40).weight(85.0).build(),
                Comparator.comparing(Person::getAge)
        );
        System.out.println(result2.isEmpty() ? "✅ Отсутствующий элемент не найден" : "❌ Лишний элемент найден");

        // === Случай 3: поиск в пустом списке ===
        var result3 = BinarySearch.search(
                new ArrayList<>(),
                new Person.Builder().name("Nobody").age(50).weight(60).build(),
                Comparator.comparing(Person::getAge)
        );
        System.out.println(result3.isEmpty() ? "✅ Пустой список обработан" : "❌ Ошибка при пустом списке");

        // === Случай 4: поиск первого элемента ===
        var result4 = BinarySearch.search(
                people,
                new Person.Builder().name("Alice").age(25).weight(55.0).build(),
                Comparator.comparing(Person::getAge)
        );
        System.out.println(result4.isPresent() && result4.getAsInt() == 0 ? "✅ Первый элемент найден" : "❌ Ошибка поиска первого элемента");

        // === Случай 5: поиск последнего элемента ===
        var result5 = BinarySearch.search(
                people,
                new Person.Builder().name("Charlie").age(35).weight(80.0).build(),
                Comparator.comparing(Person::getAge)
        );
        System.out.println(result5.isPresent() && result5.getAsInt() == people.size() - 1 ? "✅ Последний элемент найден" : "❌ Ошибка поиска последнего элемента");
    }
}
