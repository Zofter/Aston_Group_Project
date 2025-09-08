public class Person {
    private int age;
    private double weight;
    private String name;
    private int passNum;

    public Person(int age, double weight, String name, int passNum) {
        this.age=age;
        this.weight = weight;
        this.name = name;
        this.passNum = passNum;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassNum() {
        return passNum;
    }

    public void setPassNum(int passNum) {
        this.passNum = passNum;
    }
}
