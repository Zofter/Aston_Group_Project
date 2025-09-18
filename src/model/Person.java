package model;

import java.util.Objects;

public class Person {
    private final String name;
    private final int age;
    private final double weight;

    public Person(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.weight = builder.weight;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getWeight() {
        return weight;
    }

    public static class Builder {
        private String name;
        private int age = 0;
        private double weight = 0.0;

        public Builder name(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Имя не может быть пустым");
            }
            this.name = name;
            return this;
        }

        public Builder age(int age) {
            if (age < 0 || age > 150) {
                throw new IllegalArgumentException("Возраст должен быть от 0 до 150");
            }
            this.age = age;
            return this;
        }

        public Builder weight(double weight) {
            if (weight < 0 || weight > 500) {
                throw new IllegalArgumentException("Вес должен быть от 0 до 500");
            }
            this.weight = weight;
            return this;
        }

        public Person build() {
            if (name == null) {
                throw new IllegalStateException("Имя должно быть указано");
            }
            return new Person(this);
        }
    }

    @Override
    public String toString() {
        return "model.Person {Имя: " + name + "; Вес: " + weight + "; Возраст: " + age + "}\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && weight == person.weight && Objects.equals(name, person.name);
    }
}
