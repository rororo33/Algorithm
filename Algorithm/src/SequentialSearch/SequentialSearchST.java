package SequentialSearch;

import java.util.ArrayList;

class Node<K,V>{
    K key; V value; Node<K,V> next;

    public Node(K key, V value, Node<K,V> next){
        this.key = key; this.value = value; this.next = next;
    }
}
public class SequentialSearchST<K,V> implements ST<K,V>{
    private  Node<K,V> first;
    int N;

    public V get(K key){
        for(Node<K,V> x = first; x != null; x = x.next) // 연결 리스트 스캔
            if(key.equals(x.key)) // 검색 성공
                return x.value;
        return null; // 검색 실패
    }
    public void put(K key, V value){
        for(Node<K,V> x = first; x != null; x = x.next)
            if(key.equals(x.key)){ // 키가 있다면 값 변경(update)
                x.value = value;
                return;
            }
        first = new Node<K,V>(key, value, first); // 키가 없으면 노드 추가
        N++;
    }
    public void delete(K key){
        if(key.equals(first.key)){
            first = first.next; N--;
            return;
        }
        for(Node<K,V> x = first; x.next != null; x = x.next){
            if(key.equals(x.next.key)) {
                x.next = x.next.next;
                N--;
                return;
            }
        }
    }
    public Iterable<K> keys(){
        ArrayList<K> keyList = new ArrayList<K>(N);
        for(Node<K,V> x = first; x != null; x = x.next)
            keyList.add(x.key);
        return keyList;
    }
    @Override
    public boolean contains(K key) {
        return get(key) != null;
    }
    @Override
    public boolean isEmpty() { return N == 0;}
    @Override
    public int size() { return N;}


    public static void main(String[] args) {
        SequentialSearchST<String, Integer> st = new SequentialSearchST<>();

        // put 테스트
        st.put("apple", 1);
        st.put("banana", 2);
        st.put("cherry", 3);
        st.put("apple", 10); // key 중복 시 value 업데이트 확인

        // get 테스트
        System.out.println("Get 'apple': " + st.get("apple"));   // 10
        System.out.println("Get 'banana': " + st.get("banana")); // 2
        System.out.println("Get 'grape': " + st.get("grape"));   // null

        // keys() 테스트
        System.out.println("All keys:");
        for (String key : st.keys()) {
            System.out.println(key + " -> " + st.get(key));
        }

        // delete 테스트
        st.delete("banana");
        System.out.println("After deleting 'banana':");
        for (String key : st.keys()) {
            System.out.println(key + " -> " + st.get(key));
        }

        // N 확인
        System.out.println("Current size: " + st.N); // 2
    }


}
