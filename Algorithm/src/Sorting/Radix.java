package Sorting;

public class Radix {
    public static void sort(int[] A){
        int i, m = A[0], exp = 1, n = A.length;
        int[] B = new int[n];

        for(i = 1; i < n; i++)
            if(A[i] > m) m = A[i];

        while(m / exp > 0){
            int[] C = new int[10];
            for(i = 0; i < n; i++) C[A[i] / exp % 10]++;
            for(i = 1; i < 10; i++) C[i] += C[i-1];
            for(i = n-1; i >= 0; i--) B[--C[(A[i] / exp) %10]] = A[i];
            /*for(i = 0; i < n; i++)
                A[i] = B[i];
            exp *= 10;*/

            //배열 참조를 바꾸고 최종 정렬 배열 리턴 방식
            int[] tmp = A;
            A = B;
            B = tmp;

            exp *= 10;
        }
    }

    public static void main(String[] args) {
        int[] A = {10, 27, 2, 13, 8, 9, 82, 27};

        System.out.println("Before sorting:");
        for (int num : A) {
            System.out.print(num + " ");
        }

        sort(A);

        System.out.println("\nAfter sorting:");
        for (int num : A) {
            System.out.print(num + " ");
        }
    }
}
