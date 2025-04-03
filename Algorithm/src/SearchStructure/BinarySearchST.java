package SearchStructure;

import Sorting.AbstractSort;

import java.util.ArrayList;

public class BinarySearchST<K extends Comparable<K>, V> {
    private static final int INIT_CAPACITY = 10;
    private K[] keys;
    private V[] vals;
    private int N;

    public BinarySearchST() {
        keys = (K[]) new Comparable[INIT_CAPACITY];
        vals = (V[]) new Object[INIT_CAPACITY];
    }

    public BinarySearchST(int capacity) {
        keys = (K[]) new Comparable[capacity];
        vals = (V[]) new Object[capacity];
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public int size() {
        return N;
    }

    public void resize(int capacity) {
        K[] tempk = (K[]) new Comparable[capacity];
        V[] tempv = (V[]) new Object[capacity];

        for (int i = 0; i < N; i++) {
            tempk[i] = keys[i];
            tempv[i] = vals[i];
        }
        vals = tempv;
        keys = tempk;
    }

    public int search(K key) {
        int lo = 0;
        int hi = N - 1;
        while (lo <= hi) {
            int mid = (hi + lo) / 2;
            int cmp = key.compareTo(keys[mid]);
            if (cmp < 0) hi = mid - 1;
            else if (cmp > 0) lo = mid + 1;
            else return mid;
        }
        return lo;
    }

    public V get(K key) {
        if (key == null) throw new IllegalArgumentException("key cannot be null");
        if (isEmpty()) return null;
        int i = search(key);
        if (i < N && keys[i].compareTo(key) == 0) return vals[i];
        else return null;
    }

    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("key cannot be null");
        if (value == null) {
            delete(key);
            return;
        }
        int i = search(key);
        if (i < N && keys[i].compareTo(key) == 0) {
            vals[i] = value; // 수정된 값 할당
            return;
        }
        if (N == keys.length) resize(2 * keys.length);
        for (int j = N; j > i; j--) {
            keys[j] = keys[j - 1];
            vals[j] = vals[j - 1];
        }
        keys[i] = key;
        vals[i] = value;
        N++;
    }

    public void delete(K key) {
        if (key == null) throw new IllegalArgumentException("key cannot be null");
        if (isEmpty()) return;
        int i = search(key);
        if (i == N || keys[i].compareTo(key) != 0) return;

        for (int j = i; j < N - 1; j++) {
            keys[j] = keys[j + 1];
            vals[j] = vals[j + 1];
        }

        keys[N - 1] = null;
        vals[N - 1] = null;
        N--;

        if (N > INIT_CAPACITY && N == keys.length / 4)
            resize(keys.length / 2);
    }


    public Iterable<K> keys() {
        ArrayList<K> keyList = new ArrayList<K>(N);
        for (int i = 0; i < N; i++)
            keyList.add(keys[i]);
        return keyList;
    }

    public static void main(String[] args) {
        BinarySearchST<String, Integer> st = new BinarySearchST<>();

        // put() 테스트
        st.put("A", 1);
        st.put("C", 3);
        st.put("B", 2);
        st.put("D", 4);

        System.out.println("현재 size: " + st.size()); // 4
        System.out.println("B의 값: " + st.get("B")); // 2
        System.out.println("Z가 있나? " + st.contains("Z")); // false

        // 덮어쓰기 테스트
        st.put("B", 20);
        System.out.println("B의 수정된 값: " + st.get("B")); // 20

        // delete() 테스트
        st.delete("C");
        System.out.println("C 삭제 후 contains(C): " + st.contains("C")); // false
        System.out.println("size: " + st.size()); // 3

        // 모든 키 출력
        System.out.println("전체 키:");
        for (String key : st.keys()) {
            System.out.println(key + " -> " + st.get(key));
        }
    }
}

