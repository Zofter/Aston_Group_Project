package model;

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
            this.name = name;
            return this;
        }
        public Builder age(int age) {
            this.age = age;
            return this;
        }
        public Builder weight(double weight) {
            this.weight = weight;
            return this;
        }
        public Person build() {
            return new Person(this);
        }
    }

    @Override
    public String toString() {
        return "model.Person {Имя: " + name + "; Вес: " + weight + "; Возраст: " + age + "}\n";
    }
}