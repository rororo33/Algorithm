package SequentialSearch;

public interface ST<K, V> {
    V get(K key);               // 값 검색
    public void put(K key, V value);   // (key, value) 저장
    public void delete(K key);         // key 삭제
    public Iterable<K> keys();  // key 목록 반환
    public boolean contains(K key);     // key 존재 여부
    public boolean isEmpty();
    public int size();

}

