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

    /**
     * Вложенный класс-Builder для пошагового создания объектов Person
     * Обеспечивает гибкость при создании объектов с валидацией параметров
     */
    public static class Builder {
        private String name = "NoName";
        private int age = 0;
        private double weight = 0.0;

        /**
         * Устанавливает имя человека с валидацией
         * @param name имя человека (не может быть пустым)
         * @return текущий экземпляр строителя для цепочки вызовов
         * @throws IllegalArgumentException если имя пустое или null
         */
        public Builder name(String name) {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalArgumentException("Имя не может быть пустым");
            }
            this.name = name;
            return this;
        }

        /**
         * Устанавливает возраст человека с валидацией
         * @param age возраст в годах (должен быть от 0 до 150)
         * @return текущий экземпляр строителя для цепочки вызовов
         * @throws IllegalArgumentException если возраст вне допустимого диапазона
         */
        public Builder age(int age) {
            if (age < 0 || age > 150) {
                throw new IllegalArgumentException("Возраст должен быть от 0 до 150");
            }
            this.age = age;
            return this;
        }

        /**
         * Устанавливает вес человека с валидацией
         * @param weight вес в килограммах (должен быть от 0 до 500)
         * @return текущий экземпляр строителя для цепочки вызовов
         * @throws IllegalArgumentException если вес вне допустимого диапазона
         */
        public Builder weight(double weight) {
            if (weight < 0 || weight > 500) {
                throw new IllegalArgumentException("Вес должен быть от 0 до 500");
            }
            this.weight = weight;
            return this;
        }

        /**
         * Создает объект Person с установленными параметрами
         * @return новый экземпляр Person
         * @throws IllegalStateException если имя не было установлено
         */
        public Person build() {
            if (name == null || name.trim().isEmpty()) {
                throw new IllegalStateException("Имя должно быть указано");
            }
            return new Person(this);
        }
    }

    /**
     * Возвращает строковое представление объекта Person
     * @return форматированная строка с информацией о человеке
     */
    @Override
    public String toString() {
        return "model.Person {Имя: " + name + "; Возраст: " + age + "; Вес: " + weight + "}\n";
    }

    /**
     * Сравнивает данный объект с другим объектом на равенство
     * @param o объект для сравнения
     * @return true если объекты равны, false в противном случае
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && weight == person.weight && Objects.equals(name, person.name);
    }
}
