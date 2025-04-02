// 20241234 홍길동
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class HW1 {

    static class GasStation implements Comparable<GasStation> {
        double x, y, distance;

        GasStation(double x, double y, double distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }

        @Override
        public int compareTo(GasStation other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("파일 이름? ");
        String fname = sc.nextLine();

        System.out.print("k의 값? ");
        int kInput = sc.nextInt();

        System.out.print("실행할 방법 선택 (1: 기본, 2: 개선)? ");
        int method = sc.nextInt();

        sc.close();

        if (method != 1 && method != 2) {
            System.out.println("1 또는 2를 선택하세요.");
            return;
        }

        Scanner fileScanner = null;

        try {
            fileScanner = new Scanner(new File(fname));

            double currX = fileScanner.nextDouble();
            double currY = fileScanner.nextDouble();

            int fileK = fileScanner.nextInt(); // 무시
            int n = fileScanner.nextInt();

            GasStation[] allStations = new GasStation[n];
            for (int i = 0; i < n; i++) {
                double x = fileScanner.nextDouble();
                double y = fileScanner.nextDouble();
                double dist = euclideanDistance(currX, currY, x, y);
                allStations[i] = new GasStation(x, y, dist);
            }

            int k = (kInput == -1 || kInput > n) ? n : kInput;
            GasStation[] result;

            long startTime = System.currentTimeMillis();

            if (method == 1 || k == n) {
                // 기본 방법 또는 k == n → 전체 정렬
                BottomUpMergeSort.sort(allStations);
                result = new GasStation[k];
                System.arraycopy(allStations, 0, result, 0, k);
            } else {
                // 개선된 방법 → MaxHeap 기반 Top-k 추출
                MaxHeap heap = new MaxHeap(k);
                for (GasStation gs : allStations) {
                    heap.offer(gs);
                }
                result = heap.toSortedArray(); // 오름차순 정렬된 k개 결과
            }

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            System.out.printf("k = %d일 때의 실행시간 = %dms\n", k, elapsedTime);
            for (int i = 0; i < result.length; i++) {
                GasStation gs = result[i];
                System.out.printf("%d: (%.6f, %.6f) 거리 = %.6f\n", i, gs.x, gs.y, gs.distance);
            }

        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (fileScanner != null) fileScanner.close();
        }
    }

    static double euclideanDistance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }
}

// Bottom-Up Merge Sort
class BottomUpMergeSort {
    private static boolean less(Comparable a, Comparable b) {
        return a.compareTo(b) < 0;
    }

    private static void merge(Comparable[] in, Comparable[] out, int lo, int mid, int hi) {
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) out[k] = in[j++];
            else if (j > hi) out[k] = in[i++];
            else if (less(in[j], in[i])) out[k] = in[j++];
            else out[k] = in[i++];
        }
    }

    public static void sort(Comparable[] a) {
        Comparable[] src = a, dst = new Comparable[a.length], tmp;
        int N = a.length;
        for (int n = 1; n < N; n *= 2) {
            for (int i = 0; i < N; i += 2 * n)
                merge(src, dst, i, i + n - 1, Math.min(i + 2 * n - 1, N - 1));
            tmp = src; src = dst; dst = tmp;
        }
        if (src != a) System.arraycopy(src, 0, a, 0, N);
    }
}

// MaxHeap 기반 Top-k 추출
class MaxHeap {
    private HW1.GasStation[] heap;
    private int size;

    public MaxHeap(int capacity) {
        heap = new HW1.GasStation[capacity + 1];
        size = 0;
    }

    public void offer(HW1.GasStation gs) {
        if (size < heap.length - 1) {
            heap[++size] = gs;
            swim(size);
        } else if (gs.distance < heap[1].distance) {
            heap[1] = gs;
            sink(1);
        }
    }

    public HW1.GasStation[] toSortedArray() {
        HW1.GasStation[] result = new HW1.GasStation[size];
        for (int i = 1; i <= size; i++) result[i - 1] = heap[i];
        BottomUpMergeSort.sort(result);
        return result;
    }

    private void swim(int k) {
        while (k > 1 && heap[k].distance > heap[k / 2].distance) {
            swap(k, k / 2);
            k /= 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= size) {
            int j = 2 * k;
            if (j < size && heap[j].distance < heap[j + 1].distance) j++;
            if (heap[k].distance >= heap[j].distance) break;
            swap(k, j);
            k = j;
        }
    }

    private void swap(int i, int j) {
        HW1.GasStation tmp = heap[i];
        heap[i] = heap[j];
        heap[j] = tmp;
    }
}
