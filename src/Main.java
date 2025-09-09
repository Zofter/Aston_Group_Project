import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {


        boolean workInMainCycle = true;
        int collectionSize = 0;

        Person person = PersonPull.createPerson(new ManualStrategy());
        System.out.println(person);

//        Scanner in = new Scanner(System.in);
//        while (workInMainCycle) {
//            System.out.println("Укажите длину кастомной коллекции");
//            collectionSize = Integer.parseInt(in.next());
//
//            System.out.println("Для выбора заполнения коллекции случайными параметрами, введите \"random\"");
//            System.out.println("Для выбора заполнения коллекции собственными значениями, введите \"self\"");
//            System.out.println("Для выбора заполнения коллекции значениями из файла, введите \"file\"");
//            System.out.println("Для выхода из программы введите команду \"exit\"");
//            //String typeFillingCollection = in.next();
//            if (in.next().equals("exit")) {
//                in.close();
//                workInMainCycle = false;
//            }
//            //List<Person> personCollection = fillCustomCollection(typeFillingCollection, collectionSize);
//        }
//        System.out.println("Выход из главного цикла");



    }
    public static CustomCollection<Person> fillCustomCollection(String arg, int collectionSize) {
        if (arg.equals("random")) {
//            for (int i = 0; i < collectionSize; i++) {
//                personCollection.add(new Person(randInt(), randDouble(), randName(), randInt()));
//            }
        }
        if (arg.equals("self")) {

        }
        if (arg.equals("file")) {

        }
        return null;
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

        System.out.println("Укажите Имя");
        String name = in.next();

        System.out.println("Укажите возраст");
        int age = Integer.parseInt(in.next());

        System.out.println("Укажите вес");
        double weight = Double.parseDouble(in.next());

        b.name(name).age(age).weight(weight);
    }
}