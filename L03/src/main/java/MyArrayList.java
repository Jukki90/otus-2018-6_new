import java.util.*;

class MyArrayList<T> implements List<T> {
    private static final int DEFAULT_SIZE = 10;
    private int size = 0;
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
        return size == 0;
    }

    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if (items[i].equals(o)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("This method is not realized");
    }


    public Object[] toArray() {
        return Arrays.copyOf(items, size);
    }

    public <T1> T1[] toArray(T1[] a) {
        throw new UnsupportedOperationException("This method is not realized");
    }

    public boolean add(T t) {
        if (items.length < size - 1) {
            items[items.length] = t;
        } else {
            Object[] newArr = Arrays.copyOf(items,
                    size + 1);
            size = size + 1;
            newArr[size - 1] = t;
            items = newArr;
        }
        return true;
    }

    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (items[i].equals(o)) {
                {
                    System.arraycopy(items, i + 1, items, i, size - i);
                    size = size - 1;
                }
            }
        }
        return true;
    }

    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException("This method is not realized");
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
        return true;
    }


    public boolean removeAll(Collection<?> c) {
        items = null;
        size = 0;
        return true;
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("This method is not realized");
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
        throw new UnsupportedOperationException("This method is not realized");
    }

    public T remove(int index) {
        throw new UnsupportedOperationException("This method is not realized");
    }

    public int indexOf(Object o) {
        throw new UnsupportedOperationException("This method is not realized");
    }

    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException("This method is not realized");
    }

    public ListIterator<T> listIterator() {
        return new ListIter();
    }

    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException("This method is not realized");
    }

    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException("This method is not realized");
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
            throw new UnsupportedOperationException("This method is not realized");
        }

        @Override
        public void set(T t) {
            items[cursor - 1] = t;
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException("This method is not realized");
        }
    }


    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < items.length; i++) {
            builder.append(i).append(":").append(items[i]).append(",");
        }
        return builder.toString();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sort(Comparator<? super T> c) {
        Arrays.sort((T[]) items, 0, size, c);
    }
}