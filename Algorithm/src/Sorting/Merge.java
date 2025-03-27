package Sorting;

import java.util.Arrays;

public class Merge extends AbstractSort{

    // 비교 함수: Comparable 인터페이스 기반
    public static boolean less(Comparable v, Comparable w) {
        return v.compareTo(w) < 0;
    }

    // 병합 함수
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi){
        for(int k = lo; k <= hi; k++)
            aux[k] = a[k];

        int i = lo, j = mid + 1;
        for(int k = lo; k <= hi; k++){
            if(i > mid) a[k] = aux[j++];
            else if(j > hi) a[k] = aux[i++];
            else if(less(aux[j], aux[i])) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }

    // 정렬 함수 (재귀)
    public static void sort(Comparable[] a, Comparable[] aux, int lo, int hi){
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid + 1, hi);
        merge(a, aux, lo, mid, hi);
    }

    // 외부 호출용 정렬 함수
    public static void sort(Comparable[] a) {
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length - 1);
    }

    // 테스트 메인 메서드
    public static void main(String[] args) {
        String[] testData = { "banana", "apple", "grape", "cherry", "fig", "elderberry" };

        System.out.println("정렬 전: " + Arrays.toString(testData));
        Merge.sort(testData);
        System.out.println("정렬 후: " + Arrays.toString(testData));
    }
}

