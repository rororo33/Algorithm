package SearchStructure;

public class AVL <K, V>{
    public static class Node<K, V>{
        K key;
        V value;
        int N;
        int aux;
        Node<K, V> left, right, parent;

        public Node(K key, V value){
            this.key = key; this.value = value; this.N = 1;
        }
        public int getAux(){return aux;}
        public void setAux(int value){aux = value;}

        protected Node<K, V> root;

        private int height(Node<K, V> x){
            return (x == null) ? 0 : x.getAux();
        }

        protected void relink(Node<K, V> parent, Node<K, V> child, boolean makeleft){
            if(child != null) child.parent = parent;
            if(makeleft) parent.left = child;
            else parent.right = child;
        }

        protected void rotate(Node<K, V> x){
            Node<K, V> y = x.parent;
            Node<K, V> z = y.parent;
            if(z == null){ root = x; x.parent = null;}
            else relink(z,x,y == z.left);

            if(x == y.left){ relink(y, x.right, true); relink(x, y, false);}
            else {relink(y,x.left, false); relink(x, y, true);}
        }

        protected Node<K, V> restructure(Node<K, V> x){
            Node<K, V> y = x.parent;
            Node<K, V> z = y.parent;
            if((x == y.left) == (y == z.left)) {rotate(y); return y;}
            else{rotate(x); rotate(x); return x;}
        }
    }
}
