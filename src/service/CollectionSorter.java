package service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class CollectionSorter {

    public static <T> void sort(List<T> list, Comparator<T> comp) {
        try (ForkJoinPool forkJoinPool = new ForkJoinPool(3))
        {
            forkJoinPool.invoke(new QuickSortTask<>(list, 0, list.size() - 1, comp));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class QuickSortTask<T> extends RecursiveAction {
        private final List<T> list;
        private final int low;
        private final int high;
        private final Comparator<T> comp;

        QuickSortTask(List<T> list, int low, int high, Comparator<T> comp) {
            this.list = list;
            this.low = low;
            this.high = high;
            this.comp = comp;
        }

        @Override
        protected void compute() {
            if (low < high) {
                // поиск индекса разделителя
                int pivotIndex = partition(list, low, high, comp);
                // рекурсивное деление коллекции элементов на левые части: от первого до разделителя - 1
                QuickSortTask<T> left = new QuickSortTask<>(list, low, pivotIndex - 1, comp);
                // рекурсивное деление коллекции элементов на правые части: от разделителя + 1 до последнего
                QuickSortTask<T> right = new QuickSortTask<>(list, pivotIndex + 1, high, comp);
                invokeAll(left, right);
            }
        }
    }

    // Поиск индекса разделителя
    private static <T> int partition(List<T> list, int low, int high, Comparator<T> comp) {
        T pivot = list.get(low); // получение опорного элемента - первый в передаваемом массиве
        int left = low + 1;      // задание левого указателя - соседним с опорным элементом
        int right = high;        // задание правого указателя - последний в передаваемом массиве

        while (left <= right) {             // пока указатели не пересекли друг-друга, или не указывают на 1 элемент
            while (left <= right && comp.compare(list.get(left), pivot) <= 0)
                left++;                     // поиск первого элемента >= опорного
            while (left <= right && comp.compare(list.get(right), pivot) >= 0)
                right--;                    // поиск первого элемента <= опорного
            if (left < right) {             // если указатели не пересекли друг-друга
                swap(list, left, right);    // перенос двух элементов на позиции указателей друг-друга
                left++;
                right--;
            }
        }
        swap(list, low, right); // перенос опорного элемента на место куда указывает указатель right
        return right;           // возврат в качестве разделителя массива (int partition) - указателя right
    }

    // Перестановка элементов
    private static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}