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

    // 교집합 크기 계산 (Map 사용하지 않음)
    public int intersectionSize(HW2<K, V> other) {
        int size = 0;
        for (K key : this.keys()) {
            V valueThis = this.get(key);
            V valueOther = other.get(key);
            if (valueOther != null) {
                // 두 값 중 작은 값을 더함 (최소값)
                if (valueThis.doubleValue() <= valueOther.doubleValue()) {
                    size += valueThis.intValue();
                } else {
                    size += valueOther.intValue();
                }
            }
        }
        return size;
    }

    // 합집합 크기 계산 (Map 사용하지 않음)
    public int unionSize(HW2<K, V> other) {
        int size = 0;

        // this의 모든 키에 대해 처리
        for (K key : this.keys()) {
            V valueThis = this.get(key);
            V valueOther = other.get(key);
            if (valueOther != null) {
                // 두 값 중 큰 값을 더함 (최대값)
                if (valueThis.doubleValue() >= valueOther.doubleValue()) {
                    size += valueThis.intValue();
                } else {
                    size += valueOther.intValue();
                }
            } else {
                // other에 없는 키는 this의 값을 더함
                size += valueThis.intValue();
            }
        }

        // other에만 있는 키 처리
        for (K key : other.keys()) {
            if (this.get(key) == null) {
                size += other.get(key).intValue();
            }
        }

        return size;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("첫 번째 파일 이름을 입력하세요: ");
        String fileA = scanner.nextLine();

        System.out.print("두 번째 파일 이름을 입력하세요: ");
        String fileB = scanner.nextLine();

        // 각 파일에 대한 BST 생성
        HW2<String, Integer> bstA = createBSTFromFile(fileA);
        HW2<String, Integer> bstB = createBSTFromFile(fileB);

        // 유사도 계산
        double similarity = calculateSimilarity(bstA, bstB);

        System.out.println("두 파일의 유사도: " + similarity);

        scanner.close();
    }

    // 파일로부터 BST 생성
    private static HW2<String, Integer> createBSTFromFile(String fileName) {
        HW2<String, Integer> bst = new HW2<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            String line;
            ArrayList<String> tokens = new ArrayList<>();

            // 파일의 모든 토큰을 추출
            while ((line = reader.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line, " \t\n=;,<>()");
                while (st.hasMoreTokens()) {
                    tokens.add(st.nextToken());
                }
            }

            // 5개의 연속된 단어로 shingle 생성
            if (tokens.size() >= 5) {
                for (int i = 0; i <= tokens.size() - 5; i++) {
                    String shingle = tokens.get(i) + " " + tokens.get(i+1) + " " +
                            tokens.get(i+2) + " " + tokens.get(i+3) + " " + tokens.get(i+4);

                    // BST에 shingle 추가 또는 빈도수 증가
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

    // 두 BST의 유사도 계산 (Map 사용하지 않음)
    private static double calculateSimilarity(HW2<String, Integer> bstA, HW2<String, Integer> bstB) {
        int intersectionSize = bstA.intersectionSize(bstB);
        int unionSize = bstA.unionSize(bstB);

        // 유사도 계산
        if (unionSize == 0) {
            return 0.0; // 두 파일 모두 비어있거나 shingle이 없는 경우
        }

        return (double) intersectionSize / unionSize;
    }
}
