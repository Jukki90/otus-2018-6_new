import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        MyArrayList<Integer> arr = new MyArrayList<>();
        arr.add(1);
        arr.add(3);
        arr.add(4);
        arr.add(2);
        System.out.println("arr:" + arr);

        List<Integer> arr2 = new MyArrayList<>();
        arr2.add(9);
        arr2.add(7);
        arr2.add(8);
        System.out.println("arr2:" + arr2);
        arr.addAll(arr2);
        System.out.println("arr + arr2 =  " + arr);
        Collections.copy(arr, arr2);
        System.out.println("После копированиия: " + arr);
        Collections.sort(arr);
        System.out.println("После сортировки:" + arr);
        System.out.println("--------------------");

        MyArrayList<String> str = new MyArrayList<>();
        str.add("qwe");
        str.add("rty");
        str.add("zxv");
        str.add("aaaaa");
        str.add("ggg");
        str.add("mmn");
        System.out.println("str:" + str);
        Collections.sort(str);
        System.out.println("После сортировки: " + str);
        MyArrayList<String> str2 = new MyArrayList<>();
        str2.add("poi");
        str.addAll(str2);
        System.out.println("str + str2 = " + str);
        Collections.copy(str, str2);
    }
}
