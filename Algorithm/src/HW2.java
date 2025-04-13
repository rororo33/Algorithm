import java.io.*;
import java.util.*;

public class HW2<K extends Comparable<K>, V extends Number & Comparable<V>> {
    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left, right;
        int N;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.N = 1;
        }
    }

    private Node<K, V> root;

    private int size(Node<K, V> x) {
        return (x == null) ? 0 : x.N;
    }

    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private Node<K, V> put(Node<K, V> x, K key, V value) {
        if (x == null) return new Node<>(key, value);
        int cmp = key.compareTo(x.key);
        if (cmp < 0) x.left = put(x.left, key, value);
        else if (cmp > 0) x.right = put(x.right, key, value);
        else x.value = value;
        x.N = 1 + size(x.left) + size(x.right);
        return x;
    }

    public V get(K key) {
        Node<K, V> x = root;
        while (x != null) {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) return x.value;
            else if (cmp < 0) x = x.left;
            else x = x.right;
        }
        return null;
    }

    public Iterable<K> keys() {
        ArrayList<K> keyList = new ArrayList<>();
        inorder(root, keyList);
        return keyList;
    }

    private void inorder(Node<K, V> x, ArrayList<K> keyList) {
        if (x == null) return;
        inorder(x.left, keyList);
        keyList.add(x.key);
        inorder(x.right, keyList);
    }

    public int totalShingles() {
        return totalShingles(root);
    }

    private int totalShingles(Node<K, V> x) {
        if (x == null) return 0;
        return x.value.intValue() + totalShingles(x.left) + totalShingles(x.right);
    }

    public int commonShingles(HW2<K, V> other) {
        int count = 0;
        for (K key : this.keys()) {
            if (other.get(key) != null) {
                count++;
            }
        }
        return count;
    }

    public int intersection(HW2<K, V> other) {
        int size = 0;
        for (K key : this.keys()) {
            V valueThis = this.get(key);
            V valueOther = other.get(key);
            if (valueOther != null) {
                if (valueThis.doubleValue() <= valueOther.doubleValue()) {
                    size += valueThis.intValue();
                } else {
                    size += valueOther.intValue();
                }
            }
        }
        return size;
    }

    public int union(HW2<K, V> other) {
        int size = 0;
        for (K key : this.keys()) {
            V valueThis = this.get(key);
            V valueOther = other.get(key);
            if (valueOther != null) {
                if (valueThis.doubleValue() >= valueOther.doubleValue()) {
                    size += valueThis.intValue();
                } else {
                    size += valueOther.intValue();
                }
            } else {
                size += valueThis.intValue();
            }
        }
        for (K key : other.keys()) {
            if (this.get(key) == null) {
                size += other.get(key).intValue();
            }
        }
        return size;
    }

    private static HW2<String, Integer> createBSTFromFile(String fileName) {
        HW2<String, Integer> bst = new HW2<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            ArrayList<String> tokens = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, " \t\n=;,<>()");
                while (st.hasMoreTokens()) {
                    tokens.add(st.nextToken());
                }
            }
            if (tokens.size() >= 5) {
                for (int i = 0; i <= tokens.size() - 5; i++) {
                    String shingle = tokens.get(i) + " " + tokens.get(i+1) + " " +
                            tokens.get(i+2) + " " + tokens.get(i+3) + " " + tokens.get(i+4);
                    Integer count = bst.get(shingle);
                    if (count == null) {
                        bst.put(shingle, 1);
                    } else {
                        bst.put(shingle, count + 1);
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("파일 읽기 오류: " + e.getMessage());
        }
        return bst;
    }

    private static double Similarity(HW2<String, Integer> bstA, HW2<String, Integer> bstB) {
        int intersectionSize = bstA.intersection(bstB);
        int unionSize = bstA.union(bstB);
        if (unionSize == 0) {
            return 0.0;
        }
        return (double) intersectionSize / unionSize;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("첫번째 파일 이름? ");
        String fileA = scanner.nextLine();

        System.out.print("두번째 파일 이름? ");
        String fileB = scanner.nextLine();

        HW2<String, Integer> bstA = createBSTFromFile(fileA);
        HW2<String, Integer> bstB = createBSTFromFile(fileB);

        int shinglesA = bstA.totalShingles();
        int shinglesB = bstB.totalShingles();

        int commonShingles = bstA.commonShingles(bstB);

        double similarity = Similarity(bstA, bstB);

        System.out.println("파일 " + fileA + "의 Shingle의 수 = " + shinglesA);
        System.out.println("파일 " + fileB + "의 Shingle의 수 = " + shinglesB);
        System.out.println("두 파일에서 공통된 shingle의 수 = " + commonShingles);
        System.out.println(fileA + "과 " + fileB + "의 유사도 = " + similarity);

        scanner.close();
    }


}
