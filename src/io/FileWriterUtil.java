package io;

import model.Person;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileWriterUtil {

    public static void writePersonsToCSV(String filePath, List<Person> persons, boolean append) throws IOException {
        try (FileWriter writer = new FileWriter(filePath, append)) {
            for (Person p : persons) {
                writer.write(p.getName() + "," + p.getAge() + "," + p.getWeight() + "\n");
            }
        }
    }

    public static void writePersonToCSV(String filePath, Person person, boolean append) throws IOException {
        try (FileWriter writer = new FileWriter(filePath, append)) {
            writer.write(person.getName() + "," + person.getAge() + "," + person.getWeight() + "\n");
        }
    }
}
