import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class MyArrayListTest {

    @Test
    public void addAllTest() {
        String[] arrExpected = {"qwe", "rty", "u", "i"};
        List<String> arr2 = new MyArrayList<>();
        Collections.addAll(arr2, "qwe", "rty", "u", "i");
        Assert.assertArrayEquals(arrExpected, arr2.toArray());
    }


    @Test
    public void copy01Test() {
        List<Integer> arr1 = new MyArrayList<>();
        Collections.addAll(arr1, 1, 3, 5, 7);
        List<Integer> arr2 = new MyArrayList<>();
        Collections.addAll(arr2, 2, 4, 6, 8);
        Collections.copy(arr2, arr1);
        Assert.assertArrayEquals(arr1.toArray(), arr2.toArray());
    }


    @Test
    public void copy02Test() {
        List<Integer> arr1 = new MyArrayList<>();
        Collections.addAll(arr1, 1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21, 23);
        List<Integer> arr2 = new MyArrayList<>();
        Collections.addAll(arr2, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24);
        Collections.copy(arr2, arr1);
        Assert.assertArrayEquals(arr1.toArray(), arr2.toArray());
    }


    @Test
    public void sortTest() {
        List<Integer> arr = new MyArrayList<>();
        Collections.addAll(arr, 7, 1, 3, 6, 22);
        Collections.sort(arr);
        Assert.assertArrayEquals(arr.toArray(), new Integer[]{1, 3, 6, 7, 22});
    }

}
