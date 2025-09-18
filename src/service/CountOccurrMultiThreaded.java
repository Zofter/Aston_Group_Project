package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class CountOccurrMultiThreaded {

    // Метод для многопоточного подсчета вхождений (дополнительное задание #4)
    public static <T> int countOccurrencesMultiThreaded(List<T> list, T target, int threadCount) {

        int n = list.size();
        if (n == 0) return 0;

        // Если передали 0 или отрицательное число — берём количество доступных ядер.
        if (threadCount <= 0) threadCount = Runtime.getRuntime().availableProcessors(); // Берём число ядер

        // Больше, чем элементов, потоков не нужно
        threadCount = Math.min(threadCount, n);

        // Размер "куска" данных для одной задачи: длина списка / число потоков, округляя вверх.
        int chunkSize = (n + threadCount - 1) / threadCount; // «Потолковое» деление (ceil)

        // Список задач: каждая задача посчитает вхождения в своём кусочке списка.
        List<Callable<Integer>> tasks = new ArrayList<>();

        // Создаём ровно threadCount задач (даже если часть будет пустых при маленьком списке).
        for (int start = 0; start < n; start += chunkSize) {
            // Делаем задачи только для реально существующих кусков
            int s = start;                                 // Начальный индекс поддиапазона для этой задачи.
            int e = Math.min(s + chunkSize, n);            // Конечный индекс (не включительно), не выходим за пределы списка.

            tasks.add(() -> {                              // Одна задача на кусок - — тело задачи, возвращает Integer (количество найденных элементов).
                int count = 0;
                for (int j = s; j < e; j++) {
                    // Безопасное сравнение: корректно работает и с null
                    if (Objects.equals(target, list.get(j))) {
                        count++;
                    }
                }
                return count;
            });
        }


        // Создаём пул потоков фиксированного размера.
        ExecutorService executor = Executors.newFixedThreadPool(threadCount); // Пул потоков

        try {
            List<Future<Integer>> results = executor.invokeAll(tasks); // Отправляем все задачи в пул и ждём их завершения.
            int total = 0;
            for (Future<Integer> f : results) {
                total += f.get();                    // Собираем результаты - достаём результат каждой задачи (get() может бросать исключения).
            }
            return total;
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();      // Восстанавливаем флаг прерывания
            throw new RuntimeException("Подсчёт прерван", ie);
        } catch (ExecutionException ee) {
            throw new RuntimeException("Одна из задач упала", ee.getCause());
        } finally {
            executor.shutdown();                     // Гарантированно закрываем пул
        }
    }
}









