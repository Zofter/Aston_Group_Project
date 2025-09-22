package factory;

import model.Person;
import strategy.PersonStrategy;

/**
 * Фабричный класс для создания объектов Person с использованием стратегий
 * Реализует паттерн Factory Method для инкапсуляции логики создания объектов
 */
public final class PersonPull {
    /**
     * Создается объект Person с использованием переданной стратегии
     * Применяет паттерн Строитель (Builder) для пошагового конструирования объекта
     * param: стратегия, определяющая параметры создаваемого Person
     * return: новый экземпляр Person, сконфигурированный согласно стратегии
     */
    public static Person createPerson(PersonStrategy personStrategy) {
        var b = new Person.Builder();
        personStrategy.getPerson(b);
        return b.build();
    }
}