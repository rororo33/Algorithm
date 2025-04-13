import java.io.*;
import java.util.*;

public class HW2<K extends Comparable<K>, V> {
    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left, right;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node<K, V> root;

    public void put(K key, V val) {
        if (root == null) {
            root = new Node<>(key, val);
            return;
        }
        Node<K, V> x = treeSearch(key);
        int cmp = key.compareTo(x.key);
        if (cmp == 0) {
            x.value = val;
        } else {
            Node<K, V> newNode = new Node<>(key, val);
            if (cmp < 0) {
                x.left = newNode;
            } else {
                x.right = newNode;
            }
        }
    }

    public V get(K key) {
        if (root == null) return null;
        Node<K, V> x = treeSearch(key);
        if (key.equals(x.key)) return x.value;
        else return null;
    }

    protected Node<K, V> treeSearch(K key) {
        Node<K, V> x = root;
        while (true) {
            int cmp = key.compareTo(x.key);
            if (cmp == 0) return x;
            else if (cmp < 0) {
                if (x.left == null) return x;
                else x = x.left;
            } else {
                if (x.right == null) return x;
                else x = x.right;
            }
        }
    }

    public Iterable<K> keys() {
        if (root == null) return Collections.emptyList();
        ArrayList<K> keyList = new ArrayList<>();
        inorder(root, keyList);
        return keyList;
    }

    private void inorder(Node<K, V> x, ArrayList<K> keyList) {
        if (x != null) {
            inorder(x.left, keyList);
            keyList.add(x.key);
            inorder(x.right, keyList);
        }
    }

    public int totalShingles() {
        return totalShingles(root);
    }

    private int totalShingles(Node<K, V> x) {
        if (x == null) return 0;
        return (Integer) x.value + totalShingles(x.left) + totalShingles(x.right);
    }

    public int commonShingles(HW2<K, V> other) {
        int count = 0;
        for (K key : this.keys()) {
            if (other.get(key) != null) count++;
        }
        return count;
    }

    public int intersection(HW2<K, V> other) {
        int size = 0;
        for (K key : this.keys()) {
            Integer v1 = (Integer) this.get(key);
            Integer v2 = (Integer) other.get(key);
            if (v2 != null) {
                if (v1 < v2) size += v1;
                else size += v2;
            }
        }
        return size;
    }

    public int union(HW2<K, V> other) {
        int size = 0;
        for (K key : this.keys()) {
            Integer v1 = (Integer) this.get(key);
            Integer v2 = (Integer) other.get(key);
            if (v2 != null) {
                if (v1 > v2) size += v1;
                else size += v2;
            } else {
                size += v1;
            }
        }
        for (K key : other.keys()) {
            if (this.get(key) == null) {
                size += (Integer) other.get(key);
            }
        }
        return size;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("첫번째 파일 이름? ");
        String fileA = scanner.nextLine();

        System.out.print("두번째 파일 이름? ");
        String fileB = scanner.nextLine();

        HW2<String, Integer> bstA = new HW2<>();
        HW2<String, Integer> bstB = new HW2<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileA));
            ArrayList<String> tokens = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, " \t\n=;,<>()");
                while (st.hasMoreTokens()) tokens.add(st.nextToken());
            }
            for (int i = 0; i <= tokens.size() - 5; i++) {
                String shingle = String.join(" ", tokens.subList(i, i + 5));
                Integer count = bstA.get(shingle);
                if (count == null) {
                    bstA.put(shingle, 1);
                } else {
                    bstA.put(shingle, count + 1);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("파일 " + fileA + " 읽기 오류: " + e.getMessage());
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileB));
            ArrayList<String> tokens = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, " \t\n=;,<>()");
                while (st.hasMoreTokens()) tokens.add(st.nextToken());
            }
            for (int i = 0; i <= tokens.size() - 5; i++) {
                String shingle = String.join(" ", tokens.subList(i, i + 5));
                Integer count = bstB.get(shingle);
                if (count == null) {
                    bstB.put(shingle, 1);
                } else {
                    bstB.put(shingle, count + 1);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("파일 " + fileB + " 읽기 오류: " + e.getMessage());
        }

        int shinglesA = bstA.totalShingles();
        int shinglesB = bstB.totalShingles();
        int commonShingles = bstA.commonShingles(bstB);
        int union = bstA.union(bstB);
        int intersection = bstA.intersection(bstB);
        double similarity;

        if (union == 0) {
            similarity = 0.0;
        } else {
            similarity = (double) intersection / union;
        }

        System.out.println("파일 " + fileA + "의 Shingle의 수 = " + shinglesA);
        System.out.println("파일 " + fileB + "의 Shingle의 수 = " + shinglesB);
        System.out.println("두 파일에서 공통된 shingle의 수 = " + commonShingles);
        System.out.println(fileA + "과 " + fileB + "의 유사도 = " + similarity);

        scanner.close();
    }
}
