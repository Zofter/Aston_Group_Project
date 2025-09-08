import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean workInMainCycle = true;
        Scanner in = new Scanner(System.in);
        while (workInMainCycle) {
            System.out.println("Укажите длину кастомной коллекции");
            int collectionSize = Integer.parseInt(in.next());
            System.out.println("Для выбора заполнения коллекции случайными параметрами, введите \"random\"");
            System.out.println("Для выбора заполнения коллекции собственными значениями, введите \"self\"");
            System.out.println("Для выбора заполнения коллекции значениями из файла, введите \"file\"");
            System.out.println("Для выхода из программы введите команду \"exit\"");
            String typeFillingCollection = in.next();
            if (in.next().equals("exit")) {
                in.close();
                workInMainCycle = false;
            }
            List<Person> personCollection = fillCustomCollection(typeFillingCollection, collectionSize);
        }
        System.out.println("Выход из главного цикла");


        // Привет, это Вадим, отпишитесь ниже - кто получил ссылку-приглашение.
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