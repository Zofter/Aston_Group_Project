package service;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;

/**
 * Утилитарный класс для выполнения бинарного поиска в отсортированных коллекциях
 * Реализует алгоритм бинарного поиска для эффективного нахождения элементов
 */
public class BinarySearch {

    /**
     * Выполняет бинарный поиск элемента в отсортированном списке
     * Использует компаратор для сравнения элементов
     * @param <T> тип элементов в списке
     * @param list отсортированный список для поиска (должен быть отсортирован согласно компаратору)
     * @param key искомый элемент
     * @param comparator компаратор для сравнения элементов
     * @return OptionalInt с индексом найденного элемента или пустой OptionalInt если элемент не найден
     */
    public static <T> OptionalInt search(List<T> list, T key, Comparator<? super T> comparator) {
        int left = 0;
        int right = list.size() - 1;

        // Основной цикл бинарного поиска
        while (left <= right) {
            int mid = (left + right) / 2; // Вычисление среднего индекса
            T midVal = list.get(mid);     // Получение элемента по среднему индексу
            int cmp = comparator.compare(midVal, key);  // Сравнение с искомым элементом

            if (cmp == 0) {
                // Элемент найден
                return OptionalInt.of(mid);
            } else if (cmp < 0) {
                // Искомый элемент находится в правой половине
                left = mid + 1;
            } else {
                // Искомый элемент находится в левой половине
                right = mid - 1;
            }
        }
        // Элемент не найден
        return OptionalInt.empty();
    }
}