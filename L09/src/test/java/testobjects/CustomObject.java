package testobjects;

import java.io.Serializable;
import java.util.*;

public class CustomObject implements Serializable {
    private double number = 5.15;
    private String str = "It's a wonderful, wonderful word!";
    private String[] arr = {"abc", "def", "ghi"};
    private Map<String, String> map = new HashMap();
    private List<String> list;
    //protected CustomObject2 obj = new CustomObject2();

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }


    public CustomObject() {
        map.put("qaz", "wsx");
        list = new ArrayList<String>();
        list.add("one");
        list.add("two");
    }


    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public String[] getArr() {
        return arr;
    }

    public void setArr(String[] arr) {
        this.arr = arr;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        CustomObject obj2 = (CustomObject) obj;
        if (this.number != obj2.getNumber()) {
            return false;
        }
        if (!this.str.equals(obj2.getStr())) {
            return false;
        }
        if (!Arrays.equals(this.arr, obj2.getArr())) {
            return false;
        }

        if (!this.list.equals(obj2.getList())) {
            return false;
        }

        if (!this.map.equals(obj2.getMap())) {
            return false;
        }

        return true;
    }
}
