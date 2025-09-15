package io;
import model.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileReaderUtil {

    public static List<Person> readPersonsFromCSV(String filePath) throws FileFormatException {
        List<Person> persons = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length != 3) {
                    throw new FileFormatException("Invalid person line: " + line);
                }
                try {
                    String name = parts[0].trim();
                    int age = Integer.parseInt(parts[1].trim());
                    double weight = Double.parseDouble(parts[2].trim());

                    Person person = new Person.Builder()
                            .name(name)
                            .age(age)
                            .weight(weight)
                            .build();

                    persons.add(person);
                } catch (NumberFormatException e) {
                    throw new FileFormatException("Invalid number format in: " + line, e);
                }
            }
        } catch (IOException e) {
            throw new FileFormatException("Error reading file: " + filePath, e);
        }
        return persons;
    }
}


