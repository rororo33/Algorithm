package SearchStructure;

import java.util.ArrayList;

public class BinarySearchTree<K extends Comparable<K>, V> {
    static class Node<K, V>{
        K key;
        V value;
        Node<K, V> left, right, parent;
        int N;
        int aux;

        public Node(K key, V value){
            this.key = key; this.value = value;
            this.N = 1;
        }

        public int getAux(){
            return aux;
        }

        public void setAux(int aux){
            this.aux = aux;
        }
    }
    private Node<K, V> root;

    private Node<K, V> treeSearch(K key){
        Node<K, V> x = root;
        while(true){
            int cmp = key.compareTo(x.key);
            if(cmp == 0) return x;
            else if(cmp < 0){
                if(x.left == null) return x;
                else x = x.left;
            }
            else{
                if(x.right == null) return x;
                else x = x.right;
            }
        }
    }

    public V get(K key){
        if(root == null ) return null;
        Node<K, V> x = treeSearch(key);
        if(key.equals(key))
            return x.value;
        else
            return null;
    }
    public void put(K key, V value){
        if(root == null) {
            root = new Node<K, V>(key, value); return;
        }
        Node<K, V> x = treeSearch(key);
        int cmp = key.compareTo(x.key);
        if(cmp == 0) x.value = value;
        else{
            Node<K, V> newNode = new Node<K, V>(key, value);
            if(cmp < 0) x.left = newNode;
            else x.right = newNode;
            newNode.parent = x;
            rebalanceInsert(newNode);
        }
    }

    protected void rebalanceInsert(Node<K, V> x){
        resetSize(x.parent, 1);
    }
    protected void rebalanceDelete(Node<K, V> p, Node<K, V> deleted){
        resetSize(p, -1);
    }
    private void resetSize(Node<K, V> x, int value){
        for(; x != null; x = x.parent)
            x.N += value;
    }
    public Iterable<K> keys(){
        if(root == null) return null;
        ArrayList<K> keyList = new ArrayList<K>(size());
        inorder(root, keyList);
        return keyList;
    }
    private void inorder(Node<K, V> x, ArrayList<K> keyList){
        if(x != null){
            inorder(x.left, keyList);
            keyList.add(x.key);
            inorder(x.right, keyList);
        }
    }


}
