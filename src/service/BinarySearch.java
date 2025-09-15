package service;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalInt;

public class BinarySearch {

    public static <T> OptionalInt search(List<T> list, T key, Comparator<? super T> comparator) {
        int left = 0;
        int right = list.size() - 1;

        while (left <= right) {
            int mid = (left + right) / 2;
            T midVal = list.get(mid);
            int cmp = comparator.compare(midVal, key);

            if (cmp == 0) {
                return OptionalInt.of(mid);
            } else if (cmp < 0) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return OptionalInt.empty();
    }
}
