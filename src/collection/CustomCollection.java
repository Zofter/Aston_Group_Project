package collection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CustomCollection<T> extends ArrayList<T> {
    
    public CustomCollection(int initialCapacity) {
        super(initialCapacity);
    }
    
    public CustomCollection() {
        super();
    }
    
    // Метод для сортировки с использованием компаратора
    public void sort(Comparator<? super T> comparator) {
        super.sort(comparator);
    }
    
    // Реализация дополнительного задания 1: сортировка по четным значениям поля
    public void sortWithEvenOdd(java.util.function.Function<T, Integer> fieldExtractor, 
                               Comparator<T> comparator) {
        // Создаем временный список для четных элементов с индексами
        java.util.List<Object[]> evenElements = new ArrayList<>();
        
        for (int i = 0; i < this.size(); i++) {
            T element = this.get(i);
            Integer fieldValue = fieldExtractor.apply(element);
            if (fieldValue % 2 == 0) {
                evenElements.add(new Object[]{element, i});
            }
        }
        
        // Сортируем четные элементы
        evenElements.sort((o1, o2) -> 
            comparator.compare((T) o1[0], (T) o2[0]));
        
        // Восстанавливаем коллекцию
        for (int i = 0; i < evenElements.size(); i++) {
            Object[] item = evenElements.get(i);
            this.set((Integer) item[1], (T) item[0]);
        }
    }
    
    // Метод для стримов (дополнительное задание 3)
    public Stream<T> stream() {
        return super.stream();
    }
    
    // Метод для многопоточного подсчета вхождений (дополнительное задание 4)
    public int countOccurrencesMultiThreaded(T target, int threadCount) {
        if (threadCount <= 0) threadCount = Runtime.getRuntime().availableProcessors();
        
        java.util.List<java.util.concurrent.Callable<Integer>> tasks = new ArrayList<>();
        int chunkSize = (int) Math.ceil((double) this.size() / threadCount);
        
        for (int i = 0; i < threadCount; i++) {
            final int start = i * chunkSize;
            final int end = Math.min(start + chunkSize, this.size());
            
            tasks.add(() -> {
                int count = 0;
                for (int j = start; j < end; j++) {
                    if (target.equals(this.get(j))) {
                        count++;
                    }
                }
                return count;
            });
        }
        
        try {
            java.util.concurrent.ExecutorService executor = 
                java.util.concurrent.Executors.newFixedThreadPool(threadCount);
            java.util.List<java.util.concurrent.Future<Integer>> results = 
                executor.invokeAll(tasks);
            
            int totalCount = 0;
            for (java.util.concurrent.Future<Integer> future : results) {
                totalCount += future.get();
            }
            
            executor.shutdown();
            return totalCount;
            
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
