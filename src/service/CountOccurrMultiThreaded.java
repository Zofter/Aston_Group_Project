package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class CountOccurrMultiThreaded {

    // Метод для многопоточного подсчета вхождений (дополнительное задание #4)
    public static <T> int countOccurrencesMultiThreaded(List<T> list, T target, int threadCount) {

        // Если передали 0 или отрицательное число — берём количество доступных ядер.
        if (threadCount <= 0) threadCount = Runtime.getRuntime().availableProcessors();

        // Список задач: каждая задача посчитает вхождения в своём кусочке списка.
        List<Callable<Integer>> tasks = new ArrayList<>();

        // Размер "куска" данных для одной задачи: длина списка / число потоков, округляя вверх.
        int chunkSize = (int) Math.ceil((double) list.size() / threadCount);

        // Создаём ровно threadCount задач (даже если часть будет пустых при маленьком списке).
        for (int i = 0; i < threadCount; i++) {
            // Начальный индекс поддиапазона для этой задачи.
            final int start = i * chunkSize;
            // Конечный индекс (не включительно), не выходим за пределы списка.
            final int end = Math.min(start + chunkSize, list.size());

            // Лямбда — тело задачи, возвращает Integer (количество найденных элементов).
            tasks.add(() -> {
                int count = 0; // локальный счётчик для этой задачи
                for (int j = start; j < end; j++) {
                    if (Objects.equals(target, list.get(j))) {
                        count++;
                    }
                }
                return count;
            });
        }

        try {
            // Создаём пул потоков фиксированного размера.
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);

            // Отправляем все задачи в пул и ждём их завершения. Получаем «фьючерсы» с результатами.
            List<Future<Integer>> results = executor.invokeAll(tasks);

            // общий счётчик
            int totalCount = 0;

            for (Future<Integer> future : results) {
                // Достаём результат каждой задачи (get() может бросать исключения).
                totalCount += future.get();
            }

            // Останавливаем пул (новые задачи не принимаются).
            executor.shutdown();

            // возвращаем итог
            return totalCount;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}