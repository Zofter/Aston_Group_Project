package service;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CollectionSorter {

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(2);
    
    public static <T> void sort(List<T> list, Comparator<T> comp) {
        try {
            Future<?> future = threadPool.submit(() -> {
                quickSort(list, 0, list.size() - 1, comp);
            });
            
            future.get();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        while (left <= right) {
            while (left <= right && comp.compare(list.get(left), pivot) <= 0) {
                left++;
            }
            while (left <= right && comp.compare(list.get(right), pivot) >= 0) {
                right--;
            }
            if (left < right) {
                swap(list, left, right);
                left++;
                right--;
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
    
    public static void shutdown() {
        threadPool.shutdown();
    }
}
