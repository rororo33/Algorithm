// 22112026 우영민
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class HW1 {

    static class GasStation implements Comparable<GasStation> {
        double x, y;
        double distance;

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

        sc.close();

        Scanner fileScanner = null;

        try {
            fileScanner = new Scanner(new File(fname));

            double currX = fileScanner.nextDouble();
            double currY = fileScanner.nextDouble();

            int fileK = fileScanner.nextInt();
            int n = fileScanner.nextInt();

            GasStation[] stations = new GasStation[n];

            for (int i = 0; i < n; i++) {
                double x = fileScanner.nextDouble();
                double y = fileScanner.nextDouble();
                double dist = Distance(currX, currY, x, y);
                stations[i] = new GasStation(x, y, dist);
            }

            long startTime = System.currentTimeMillis();

            BottomUpMergeSort.sort(stations);

            long endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;

            int k = (kInput == -1 || kInput > n) ? n : kInput;

            System.out.printf("k = %d일 때의 실행시간 = %dms\n", k, elapsedTime);

            for (int i = 0; i < k; i++) {
                GasStation gs = stations[i];
                System.out.printf("%d: (%.6f, %.6f) 거리 = %.6f\n", i, gs.x, gs.y, gs.distance);
            }

        } catch (IOException e) {
            System.out.println(e);
        } finally {
            if (fileScanner != null) fileScanner.close();
        }
    }

    static double Distance(double x1, double y1, double x2, double y2) {
        double dx = x1 - x2;
        double dy = y1 - y2;
        return Math.sqrt(dx * dx + dy * dy);
    }
}

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

