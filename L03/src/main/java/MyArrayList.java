import java.util.*;
import java.util.function.Consumer;

class MyArrayList<T> implements List<T> {
    private static final int DEFAULT_SIZE = 10;
    private int size = 0;
    private int current = 0;
    private Object[] items;

    public MyArrayList() {
        items = new Object[DEFAULT_SIZE];
    }

    public MyArrayList(int initSize) {
        if (initSize > 0) {
            this.items = new Object[initSize];
            size = initSize;
        } else {
            throw new IllegalArgumentException("Длина списка должна быть больше 0!");
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0 ? true : false;
    }

    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (items[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    public Iterator<T> iterator() {
        return new Iter();
    }


    private class Iter implements Iterator<T> {
        int cursor;

        public boolean hasNext() {
            return cursor < size;
        }

        public T next() {
            if (cursor <= items.length) {
                return (T) items[cursor++];
            } else {
                throw new IndexOutOfBoundsException();
            }
        }

        public void remove() {
            for (int i = cursor; i < items.length; i++) {
                items[i] = items[i + 1];
                size--;
            }
        }

        public void forEachRemaining(Consumer<? super T> action) {

        }
    }

    public Object[] toArray() {
        return Arrays.copyOf(items, size);
    }

    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    public boolean add(T t) {
        if (current < size) {
            items[current] = t;
            current++;
        } else {
            Object[] newArr = Arrays.copyOf(items,
                    size + 1);
            size = size + 1;
            current++;
            newArr[size - 1] = t;
            items = newArr;
        }
        return true;
    }

    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (items[i].equals(o)) {
                current = i;
                {
                    for (int j = i; j < size; j++) {
                        items[j] = items[j + 1];
                    }
                    size = size - 1;
                }
            }
        }
        return true;
    }

    public boolean containsAll(Collection<?> c) {
        return false;
    }

    public boolean addAll(Collection<? extends T> c) {
        Object[] operand = c.toArray();
        if (size + operand.length > items.length) {
            Object[] newItems = new Object[size + operand.length];
            System.arraycopy(items, 0, newItems, 0, size);
            System.arraycopy(operand, 0, newItems, size, operand.length);
            items = newItems;
            size = size + c.size();

        }
        return true;
    }


    public boolean addAll(int index, Collection<? extends T> c) {
        if (c.size() > items.length - size) {
            items = Arrays.copyOf(items, items.length + c.size()
            );
        }
        Object[] operand = c.toArray();
        System.arraycopy(operand, index, items, items.length, operand.length);

        return false;
    }


    public boolean removeAll(Collection<?> c) {
        items = null;
        size = 0;
        return true;
    }

    public boolean retainAll(Collection<?> c) {
        return false;
    }

    public void clear() {
        items = null;
        size = 0;
    }

    public T get(int index) {
        if ((index >= 0)) {
            return (T) items[index];
        }
        throw new IndexOutOfBoundsException();
    }

    public T set(int index, T element) {
        items[index] = element;
        return element;
    }

    public void add(int index, T element) {

    }

    public T remove(int index) {
        return null;
    }

    public int indexOf(Object o) {
        return 0;
    }

    public int lastIndexOf(Object o) {
        return 0;
    }

    public ListIterator<T> listIterator() {
        return new ListIter();
    }

    public ListIterator<T> listIterator(int index) {
        return null;
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return null;
    }

    private class ListIter implements ListIterator<T> {
        int cursor = 0;

        @Override
        public boolean hasNext() {
            return cursor < items.length;
        }

        @Override
        public T next() {
            return (T) items[cursor++];
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 1;
        }

        @Override
        public T previous() {
            return (T) items[cursor--];
        }

        @Override
        public int nextIndex() {
            return cursor++;
        }

        @Override
        public int previousIndex() {
            return cursor--;
        }

        @Override
        public void remove() {

        }

        @Override
        public void set(T t) {
            items[cursor - 1] = t;
        }

        @Override
        public void add(T t) {

        }
    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            builder.append(i + ":" + items[i] + ",");
        }
        return builder.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super T> c) {
        Arrays.sort((T[]) items, 0, size, c);
    }


}