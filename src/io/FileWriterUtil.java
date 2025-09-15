package io;
import model.Person;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileWriterUtil {

    public static void writePersonsToCSV(String filePath, List<Person> persons) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            for (Person p : persons) {
                writer.write(p.getName() + "," + p.getAge() + "," + p.getWeight() + "\n");
            }
        }
    }
}
