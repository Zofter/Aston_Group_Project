import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean workInMainCycle = true;
        int collectionSize = 0;
        List<Person> personCollection = List.of();
        Scanner in = new Scanner(System.in);

        while (workInMainCycle) {
            System.out.println("Укажите длину новой кастомной коллекции");
            collectionSize = Integer.parseInt(in.next());

            System.out.println("Для выбора заполнения коллекции случайными параметрами, введите \"random\"");
            System.out.println("Для выбора заполнения коллекции собственными значениями, введите \"self\"");
            System.out.println("Для выбора заполнения коллекции значениями из файла, введите \"file\"");
            System.out.println("Для выхода из программы введите команду \"exit\"");
            String typeFillingCollection = in.next();

            if (typeFillingCollection.equals("random")) personCollection = fillingCollection(new RandomStrategy(), collectionSize);
            if (typeFillingCollection.equals("self")) personCollection = fillingCollection(new ManualStrategy(), collectionSize);

            System.out.println(personCollection);
            
            if (typeFillingCollection.equals("exit")) {
                in.close();
                workInMainCycle = false;
            }
        }
        System.out.println("Выход из главного цикла");
    }

    static List<Person> fillingCollection(PersonStrategy personStrategy, int size) {
        List<Person> tempCollection = new CustomCollection<>(size);
        for (int i = 0; i < size; i++) {
            tempCollection.add(PersonPull.createPerson(personStrategy));
        }
        return tempCollection;
    }
}

final class PersonPull {
    public static Person createPerson(PersonStrategy personStrategy) {
        var b = new Person.Builder();
        personStrategy.getPerson(b);
        return b.build();
    }
}

interface PersonStrategy {
    void getPerson(Person.Builder b);
}

class RandomStrategy implements PersonStrategy {
    private final Random rnd = new Random();
    @Override
    public void getPerson(Person.Builder b) {
        b.name("User" + rnd.nextInt(100))
                .age(18 + rnd.nextInt(50))
                .weight(40 + rnd.nextInt(60));
    }
}

class ManualStrategy implements PersonStrategy {
    @Override
    public void getPerson(Person.Builder b) {
        Scanner in = new Scanner(System.in);

        System.out.println("Укажите имя создаваемого человека");
        String name = in.next();

        System.out.println("Укажите возраст создаваемого человека");
        int age = Integer.parseInt(in.next());

        System.out.println("Укажите вес создаваемого человека");
        double weight = Double.parseDouble(in.next());

        b.name(name).age(age).weight(weight);
    }
}