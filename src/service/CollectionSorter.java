package service;

import java.util.Comparator;
import java.util.List;

public class CollectionSorter {

    public static <T> void sort(List<T> list, Comparator<T> comp) {
        quickSort(list, 0, list.size() - 1, comp);
    }

    private static <T> void quickSort(List<T> list, int low, int high, Comparator<T> comp) {
        if (low < high) {
            int pivotIndex = partition(list, low, high, comp);
            quickSort(list, low, pivotIndex - 1, comp);
            quickSort(list, pivotIndex + 1, high, comp);
        }
    }

    private static <T> int partition(List<T> list, int low, int high, Comparator<T> comp) {
        T pivot = list.get(low);
        int left = low + 1;
        int right = high;

        while (true) {
            while (left <= right && comp.compare(list.get(left), pivot) <= 0) {
                left++;
            }
            while (right >= left && comp.compare(list.get(right), pivot) >= 0) {
                right--;
            }
            if (right < left) {
                break;
            } else {
                swap(list, left, right);
            }
        }

        swap(list, low, right);
        return right;
    }

    private static <T> void swap(List<T> list, int i, int j) {
        T temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}