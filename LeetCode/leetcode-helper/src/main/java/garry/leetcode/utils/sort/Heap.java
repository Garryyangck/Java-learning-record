package garry.leetcode.utils.sort;

import java.util.Comparator;

/**
 * @author Garry
 * ---------2024/5/28 16:41
 **/
public class Heap {
    // 堆数组，下标从0开始
    private int[] arr;
    // 默认大小
    @SuppressWarnings("FieldCanBeLocal")
    private final int DEFAULT_CAPACITY = 10;

    private int capacity;

    private int size;

    private Comparator<Integer> comparator = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1; // 默认大根堆
        }
    };

    public Heap() {
        this.capacity = DEFAULT_CAPACITY;
        this.size = 0;
        arr = new int[DEFAULT_CAPACITY];
    }

    public Heap(Comparator<Integer> comparator) {
        this.capacity = DEFAULT_CAPACITY;
        this.size = 0;
        arr = new int[DEFAULT_CAPACITY];
        this.comparator = comparator;
    }

    public Heap(int capacity) {
        if (capacity <= 0)
            throw new RuntimeException("size is not proper!");
        this.capacity = capacity;
        this.size = 0;
        arr = new int[capacity];
    }

    public Heap(int capacity, Comparator<Integer> comparator) {
        if (capacity <= 0)
            throw new RuntimeException("size is not proper!");
        this.capacity = capacity;
        this.size = 0;
        arr = new int[capacity];
        this.comparator = comparator;
    }

    public void add(int elem) {
        checkSize();
        arr[size++] = elem;
        siftUp(size - 1);
    }

    public void add(int elem, boolean print) {
        checkSize();
        arr[size++] = elem;
        siftUp(size - 1);
        if (print)
            printArr();
    }

    public void siftUp(int index) {
        while (index != 0 && comparator.compare(arr[parent(index)], arr[index]) >= 0) {
            swap(index, parent(index));
            index = parent(index);
        }
    }

    public int peek() {
        return arr[0];
    }

    public int poll() {
        int res = peek();
        swap(0, --size);
        siftDown(0);
        return res;
    }

    public int poll(boolean print) {
        int res = peek();
        swap(0, --size);
        siftDown(0);
        if (print)
            printArr();
        return res;
    }

    public void siftDown(int index) {
        while (leftChild(index) < size) {
            int i = leftChild(index);
            if (i + 1 < size && comparator.compare(arr[i], arr[i + 1]) >= 0) {
                i = rightChild(index);
            }
            if (comparator.compare(arr[index], arr[i]) >= 0) {
                swap(index, i);
                index = i;
            } else {
                break;
            }
        }
    }

    private int parent(int index) {
        checkIndex(index);
        return (index - 1) / 2;
    }

    private int leftChild(int index) {
        checkIndex(index);
        return index * 2 + 1;
    }

    private int rightChild(int index) {
        checkIndex(index);
        return index * 2 + 2;
    }

    private void checkIndex(int index) {
        if (index < 0 || index > arr.length)
            throw new RuntimeException("index out of bounder!");
    }

    private void checkSize() {
        if (size >= capacity) {
            int newCapacity = capacity << 1;
            int[] newArr = new int[newCapacity];
            System.arraycopy(arr, 0, newArr, 0, capacity);
            capacity = newCapacity;
            arr = newArr;
        }
    }

    private void swap(int index1, int index2) {
        int temp = arr[index1];
        arr[index1] = arr[index2];
        arr[index2] = temp;
    }

    private void printArr() {
        for (int i = 0; i < size; i++) {
            if (i == 0) {
                if (size == 1) {
                    System.out.print("[" + arr[i] + "]\n");
                } else {
                    System.out.print("[" + arr[i] + ",");
                }
            } else if (i == size - 1)
                System.out.print(arr[i] + "]\n");
            else
                System.out.print(arr[i] + ",");
        }
    }
}
