import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class SortingTest {
    private static Logger logger = LoggerFactory.getLogger(SortingTest.class);

    @Test
    public void sort4xElements() throws ExecutionException, InterruptedException {
        logger.info("--- Sorting started -------------------");
        int[] arr = {1, 4, 2, 6, 8, 9, 11, 3, 99, 33, 44, 55, 0, 88, 22, 55};
        logger.info("initial array: {}", arr);
        int thNumber = 4;
        int[] arrExpected = arr.clone();
        Arrays.sort(arrExpected);
        Assert.assertArrayEquals(arrExpected, MultiThreadSorter.sort(arr, thNumber));
        MultiThreadSorter.sort(arr, thNumber);
    }

    @Test
    public void sortNot4xElements() throws ExecutionException, InterruptedException {
        logger.info("--- Sorting started ----------------");
        int[] arr = {121, 1, 4, 2, 6, 8, 9, 11, 3, 99, 33, 44, 55, 0, 88, 22, 55};
        int thNumber = 4;
        int[] arrExpected = arr.clone();
        Arrays.sort(arrExpected);
        Assert.assertArrayEquals(arrExpected, MultiThreadSorter.sort(arr, thNumber));
    }

    @Test
    public void sortLessThen4Elements() throws ExecutionException, InterruptedException {
        logger.info("--- Sorting started ----------------");
        int[] arr = {121, 1, 4};
        int thNumber = 4;
        int[] arrExpected = arr.clone();
        Arrays.sort(arrExpected);
        Assert.assertArrayEquals(arrExpected, MultiThreadSorter.sort(arr, thNumber));
    }


    @Test
    public void sortVeryBigArray() throws ExecutionException, InterruptedException {
        logger.info("--- Large array sorting began ----------------");
        int length = 101;
        int thNumber = 4;
        int[] arr = new int[length];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (100 * Math.random() + 1);
        }
        logger.info("Initial array: {}", arr);
        int[] arrExpected = arr.clone();
        Arrays.sort(arrExpected);
        Assert.assertArrayEquals(arrExpected, MultiThreadSorter.sort(arr, thNumber));
    }
}
