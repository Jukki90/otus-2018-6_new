import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class MultiThreadSorter {
    private static Logger logger = LoggerFactory.getLogger(MultiThreadSorter.class);

    public static int[] sort(int[] arr, int thNumber) throws ExecutionException, InterruptedException {
        int actualThNumber = thNumber;
        // если в массиве мало эл-тов
        if (arr.length <= thNumber) {
            logger.warn("Уменьшаем кол-во потоков!");
            actualThNumber = (int) Math.ceil((double) arr.length / 10); // 10 выбрано методом "пальцем в небо" без каких-либо мат. расчетов оптимальной длины
        }

        List<int[]> parts = divideToArrParts(arr.clone(), actualThNumber);
        // Определяем пул из actualThNumber потоков
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(actualThNumber);
        List<Future<int[]>> futures = new ArrayList<Future<int[]>>();
        for (int i = 0; i < actualThNumber; i++) {
            SingleThreadSorter callable = new SingleThreadSorter(parts.get(i));
            Future<int[]> future = executor.submit(callable);
            futures.add(future);
        }

        // собираем результирующий массив
        int[] result = new int[arr.length];
        result = futures.get(0).get();
        for (int i = 1; i < futures.size(); i++) {
            result = mergeArr(result, futures.get(i).get());
        }
        executor.shutdown();
        logger.info("result: {}", result);
        return result;
    }

    // Разделяем массив на несколько частей
    private static List<int[]> divideToArrParts(int[] arr, int thNumber) {
        List<int[]> parts = new ArrayList();
        int length = arr.length / thNumber;
        for (int i = 0; i < thNumber - 1; i++) {
            parts.add(Arrays.copyOfRange(arr, i * length, i * length + length));
        }

        if (arr.length % thNumber != 0) {
            parts.add((Arrays.copyOfRange(arr, length * (thNumber - 1), arr.length)));
        } else {
            parts.add((Arrays.copyOfRange(arr, arr.length - length, arr.length)));
        }
        return parts;
    }

    // мержим 2 отсоритрованных массива
    private static int[] mergeArr(int[] a, int[] b) {
        int[] answer = new int[a.length + b.length];
        int i = 0, j = 0, k = 0;
        while (i < a.length && j < b.length)
            answer[k++] = a[i] < b[j] ? a[i++] : b[j++];
        while (i < a.length)
            answer[k++] = a[i++];
        while (j < b.length)
            answer[k++] = b[j++];
        return answer;
    }
}
