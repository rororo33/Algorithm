package SearchStructure;

public class AVLTree<K, V> {

    protected static class Node<K, V> {
        K key;
        V value;
        int N; // 서브트리 크기
        int aux; // 노드의 높이: 리프 노드의 높이 = 1
        Node<K, V> left, right, parent;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.N = 1;
            this.aux = 1; // 리프 노드의 높이 = 1
        }

        public int getAux() {
            return aux;
        }

        public void setAux(int value) {
            aux = value;
        }
    }

    protected Node<K, V> root;

    // 높이 반환 메서드
    private int height(Node<K, V> x) {
        return (x == null) ? 0 : x.getAux();
    }

    protected void relink(Node<K, V> parent, Node<K, V> child, boolean makeLeft) {
        if (child != null)
            child.parent = parent;
        if (makeLeft)
            parent.left = child;
        else
            parent.right = child;
    }

    protected void rotate(Node<K, V> x) {
        Node<K, V> y = x.parent;
        Node<K, V> z = y.parent;
        if (z == null) {
            root = x;
            x.parent = null;
        } else {
            relink(z, x, y == z.left);
        }

        if (x == y.left) {
            relink(y, x.right, true);
            relink(x, y, false);
        } else {
            relink(y, x.left, false);
            relink(x, y, true);
        }
    }

    protected Node<K, V> restructure(Node<K, V> x) {
        Node<K, V> y = x.parent;
        Node<K, V> z = y.parent;

        if ((x == y.left) == (y == z.left)) {
            rotate(y);
            return y;
        } else {
            rotate(x);
            rotate(x);
            return x;
        }
    }

    private void setHeight(Node<K, V> x, int height) {
        x.setAux(height);
    }

    private void recomputeHeight(Node<K, V> x) {
        setHeight(x, 1 + Math.max(height(x.left), height(x.right)));
    }

    private boolean isBalanced(Node<K, V> x) {
        return Math.abs(height(x.left) - height(x.right)) <= 1;
    }

    private Node<K, V> tallerChild(Node<K, V> x) {
        int leftHeight = height(x.left);
        int rightHeight = height(x.right);

        if (leftHeight > rightHeight)
            return x.left;
        if (leftHeight < rightHeight)
            return x.right;

        if (x == root)
            return x.left;
        if (x == x.parent.left)
            return x.left;
        else
            return x.right;
    }

    private void rebalance(Node<K, V> x) {
        do {
            if (!isBalanced(x)) {
                x = restructure(tallerChild(tallerChild(x)));
                recomputeHeight(x.left);
                recomputeHeight(x.right);
                for (Node<K, V> p = x; p != null; p = p.parent)
                    recomputeHeight(p);
            }
            x = x.parent;
        } while (x != null);
    }
}
