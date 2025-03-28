package Sorting;

import java.util.Arrays;

public class MergeTD extends AbstractSort{
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
    public static void sort(Comparable[] a){
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length-1);
    }
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi){
        //기존 방식
        if(hi <= lo) return;

        //성능 개선 1(특정값 보다 작으면 insertion 사용)
        /*if(hi <= lo + CUTOFF -1){
            Insertion.sort(a, lo, hi);
            return;
        }*/

        int mid = lo + (hi -lo) / 2;
        sort(a, aux, lo, mid);
        sort(a, aux, mid+1, hi);
        //성능 개선 2(이미 정렬 되어 있으면 병합 x)
        if(less(a[mid], a[mid+1]))
            return;
        merge(a, aux, lo, mid, hi);

        //성능개선 3(aux[]와 a[]의 역할을 switch/sort, merge에서)
    }
    public static void main(String[] args) {
        String[] testData = { "banana", "apple", "grape", "cherry", "fig", "elderberry" };

        System.out.println("정렬 전: " + Arrays.toString(testData));
        Merge.sort(testData);
        System.out.println("정렬 후: " + Arrays.toString(testData));
    }
}
