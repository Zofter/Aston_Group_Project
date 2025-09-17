package service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.function.ToIntFunction;

public class CollectionEvenSorter {

    public static <T> void sort(List<T> list, Comparator<T> comp) {
        int n = list.size();
        int[] evenIndexArr = new int[n];
        int m = 0;
        for (int i = 0; i < n;  i++)
            if (i % 2 == 0) evenIndexArr[m++] = i;
        evenIndexArr = Arrays.copyOf(evenIndexArr, m);
        if (m <= 1) return; // Нет элементов для сортировки

        try (ForkJoinPool forkJoinPool = new ForkJoinPool(3)) {
            forkJoinPool.invoke(new QuickSortTask<>(list, evenIndexArr, 0, m - 1, comp));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class QuickSortTask<T> extends RecursiveAction {
        private final List<T> list;
        private final int low;
        private final int high;
        private final Comparator<T> comp;
        private final int[] evenIndexArr;

        QuickSortTask(List<T> list, int[] evenIndexArr, int low, int high, Comparator<T> comp) {
            this.list = list;
            this.low = low;
            this.high = high;
            this.comp = comp;
            this.evenIndexArr = evenIndexArr;
        }

        @Override
        protected void compute() {
            if (low < high) {
                // поиск индекса разделителя
                int pivotIndex = partition(list, evenIndexArr, low, high, comp);
                // рекурсивное деление коллекции элементов на левые части: от первого до разделителя - 1
                QuickSortTask<T> left = new QuickSortTask<>(list, evenIndexArr, low, pivotIndex - 1, comp);
                // рекурсивное деление коллекции элементов на правые части: от разделителя + 1 до последнего
                QuickSortTask<T> right = new QuickSortTask<>(list, evenIndexArr, pivotIndex + 1, high, comp);
                invokeAll(left, right);
            }
        }
    }

    // Partition поверх массива индексов чётных элементов.
    // Меняем местами элементы list только на позициях evenIndexArr[left] и evenIndexArr[right] не трогая нечётные элементы в list
    private static <T> int partition(List<T> list, int[] evenIndexArr, int low, int high, Comparator<T> comp) {
        int pivotPos = evenIndexArr[low];   // первый индекс из массива четных индексов
        T pivot = list.get(pivotPos);       // опорный = элемент на позиции первого четного индекса
        int left = low + 1;                 // задание левого указателя - соседним с опорным элементом
        int right = high;                   // задание правого указателя - последний в передаваемом массиве

        while (left <= right) {             // пока указатели не пересекли друг-друга, или не указывают на 1 элемент
            while (left <= right && comp.compare(list.get(evenIndexArr[left]), pivot) <= 0)     // поиск первого элемента >= опорного
                left++;
            while (left <= right && comp.compare(list.get(evenIndexArr[right]), pivot) >= 0)    // поиск первого элемента <= опорного
                right--;
            if (left < right) {             // если указатели не пересекли друг-друга
                swap(list, evenIndexArr[left], evenIndexArr[right]);                            // замена только чётных между собой
                left++;
                right--;
            }
        }
        swap(list, pivotPos, evenIndexArr[right]);      // ставим pivot - на последнюю позицию из чётных индексов
        return right;                                   // индекс в массиве evenIndexArr
    }

    // Перестановка элементов
    private static <T> void swap(List<T> list, int i, int j) {
        if (i == j) return;
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}




class CollectionEvenSorterУ {

    public static <T> void sortByEvenField(List<T> list, ToIntFunction<? super T> key) {
        int n = list.size();
        int[] evenIndexArr = new int[n];
        int m = 0;
        for (int i = 0; i < n; i++) {
            if ((key.applyAsInt(list.get(i)) & 1) == 0) { // чётное значение поля
                evenIndexArr[m++] = i;
            }
        }
        if (m <= 1) return;
        evenIndexArr = Arrays.copyOf(evenIndexArr, m);

        Comparator<? super T> comp = Comparator.comparingInt(key); // натуральный порядок по полю

        // Лучше так: общая FJP, без try-with-resources
        ForkJoinPool.commonPool().invoke(new QuickSortTask<>(list, evenIndexArr, 0, m - 1, comp));
    }

    private static class QuickSortTask<T> extends RecursiveAction {
        private static final int THRESHOLD = 64;

        private final List<T> list;
        private final int[] evenIndexArr;
        private final int low, high;
        private final Comparator<? super T> comp;

        QuickSortTask(List<T> list, int[] evenIndexArr, int low, int high, Comparator<? super T> comp) {
            this.list = list;
            this.evenIndexArr = evenIndexArr;
            this.low = low;
            this.high = high;
            this.comp = comp;
        }

        @Override
        protected void compute() {
            if (low >= high) return;
            if (high - low < THRESHOLD) {
                // можно сделать маленькую последовательную быструю сортировку
                quickSortSeq(list, evenIndexArr, low, high, comp);
                return;
            }
            int p = partition(list, evenIndexArr, low, high, comp);
            invokeAll(
                    new QuickSortTask<>(list, evenIndexArr, low, p - 1, comp),
                    new QuickSortTask<>(list, evenIndexArr, p + 1, high, comp)
            );
        }

        private static <T> void quickSortSeq(List<T> list, int[] idx, int lo, int hi, Comparator<? super T> comp) {
            if (lo >= hi) return;
            int p = partition(list, idx, lo, hi, comp);
            quickSortSeq(list, idx, lo, p - 1, comp);
            quickSortSeq(list, idx, p + 1, hi, comp);
        }
    }

    // Partition поверх массива индексов чётных по значению элементов.
    private static <T> int partition(List<T> list, int[] evenIndexArr, int low, int high, Comparator<? super T> comp) {
        int pivotPos = evenIndexArr[low];
        T pivot = list.get(pivotPos);
        int left = low + 1;
        int right = high;

        while (left <= right) {
            // Ищем первый элемент > pivot
            while (left <= right && comp.compare(list.get(evenIndexArr[left]), pivot) <= 0) left++;
            // Ищем первый элемент < pivot
            while (left <= right && comp.compare(list.get(evenIndexArr[right]), pivot) >= 0) right--;
            if (left < right) {
                swap(list, evenIndexArr[left], evenIndexArr[right]);
                left++; right--;
            }
        }
        swap(list, pivotPos, evenIndexArr[right]);
        return right;
    }

    private static <T> void swap(List<T> list, int i, int j) {
        if (i == j) return;
        T t = list.get(i);
        list.set(i, list.get(j));
        list.set(j, t);
    }
}