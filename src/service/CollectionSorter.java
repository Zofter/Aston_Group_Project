package service;

import java.util.Comparator;
import java.util.List;

public class CollectionSorter {

    public static <T> void sort(List<T> list, Comparator<T> comp) {
        quickSort(list, 0, list.size() - 1, comp);
    }

    private static <T> void quickSort(List<T> list, int low, int high, Comparator<T> comp) {
        if (low < high) {
            // поиск индекса разделителя
            int pivotIndex = partition(list, low, high, comp);
            // рекурсивное деление коллекции элементов на левые части: от первого до разделителя - 1
            quickSort(list, low, pivotIndex - 1, comp);
            // рекурсивное деление коллекции элементов на правые части: от разделителя + 1 до последнего
            quickSort(list, pivotIndex + 1, high, comp);
        }
    }
// Поиск индекса разделителя
    private static <T> int partition(List<T> list, int low, int high, Comparator<T> comp) {
        T pivot = list.get(low); // получение опорного элемента - первый в передаваемом массиве
        int left = low + 1;      // задание левого указателя - соседним с опорным элементом
        int right = high;        // задание правого указателя - последний в передаваемом массиве

        while (left <= right) {             // пока указатели не пересекли друг-друга, или не указывают на 1 элемент
            while (left <= right && comp.compare(list.get(left), pivot) <= 0) left++;   // поиск первого элемента >= опорного
            while (left <= right && comp.compare(list.get(right), pivot) >= 0) right--; // поиск первого элемента <= опорного
            if (left < right) {             // если указатели не пересекли друг-друга
                swap(list, left, right);    // перенос двух элементов на позиции указателей друг-друга
                left++;
                right--;
            }
        }
        swap(list, low, right); // перенос опорного элемента на место куда указывает указатель right
        return right; // возврат в качестве разделителя массива (int partition) - указателя right
    }
// Перестановка элементов
    private static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}