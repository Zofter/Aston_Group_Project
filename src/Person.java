public class Person {
    private final String name;
    private final int age;
    private final double weight;
    private final int passNum;

    public Person(Builder builder) {
        this.name = builder.name;
        this.age = builder.age;
        this.weight = builder.weight;
        this.passNum = builder.passNum;
    }

    public static class Builder {
        private final String name;
        private int age = 0;
        private double weight = 0.0;
        private int passNum = 0;
        public Builder(String name) {
            this.name = name;
        }
        public Builder age(int age) {
            this.age = age;
            return this;
        }
        public Builder weight(double weight) {
            this.weight = weight;
            return this;
        }
        public Builder passNum(int passNum) {
            this.passNum = passNum;
            return this;
        }
        public Person Build() {
            return new Person(this);
        }
    }

    @Override
    public String toString() {
        return "Person {Имя: " + name + " Вес: " + weight + " Возраст: " + age + " Номер паспорта: " + passNum + "}\n";
    }
}

