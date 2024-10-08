package model.Datastructure;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Haochen Gong
 * @uid u7776634
 * @description: RBTree class
 **/
public class RBTree<V> {
    RBTreeNode<V> root;
    private final boolean RED = false;
    private final boolean BLACK = true;

    // Query nodes by key value
    public RBTreeNode<V> search(int key) {
        RBTreeNode<V> tmp = root;
        while (tmp != null) {
            if (tmp.getKey() == key)
                return tmp;
            else if (tmp.getKey() > key)
                tmp = tmp.getLeft();
            else
                tmp = tmp.getRight();
        }
        return null;
    }

    // Insert node
    public void insert(int key, V value) {
        RBTreeNode<V> existingNode = search(key);
        if (existingNode != null) {
            existingNode.setValue(value); // 更新已有节点的值
            return;
        }

        RBTreeNode<V> node = new RBTreeNode<V>(key, value);
        if (root == null) {
            root = node;
            node.setColor(BLACK);
            return;
        }
        RBTreeNode<V> parent = root;
        RBTreeNode<V> son = null;
        if (key <= parent.getKey()) {
            son = parent.getLeft();
        } else {
            son = parent.getRight();
        }
        //find the position
        while (son != null) {
            parent = son;
            if (key <= parent.getKey()) {
                son = parent.getLeft();
            } else {
                son = parent.getRight();
            }
        }
        if (key <= parent.getKey()) {
            parent.setLeft(node);
        } else {
            parent.setRight(node);
        }
        node.setParent(parent);

        //fix up
        insertFix(node);
    }

    private void insertFix(RBTreeNode<V> node) {
        RBTreeNode<V> father, grandFather;
        while ((father = node.getParent()) != null && father.getColor() == RED) {
            grandFather = father.getParent();
            if (grandFather.getLeft() == father) {  // The case where F is the left son of G
                RBTreeNode<V> uncle = grandFather.getRight();
                if (uncle != null && uncle.getColor() == RED) {
                    setBlack(father);
                    setBlack(uncle);
                    setRed(grandFather);
                    node = grandFather;
                    continue;
                }
                if (node == father.getRight()) {
                    leftRotate(father);
                    RBTreeNode<V> tmp = node;
                    node = father;
                    father = tmp;
                }
                setBlack(father);
                setRed(grandFather);
                rightRotate(grandFather);
            } else {  // The case where F is the right son of G. Symmetric operations
                RBTreeNode<V> uncle = grandFather.getLeft();
                if (uncle != null && uncle.getColor() == RED) {
                    setBlack(father);
                    setBlack(uncle);
                    setRed(grandFather);
                    node = grandFather;
                    continue;
                }
                if (node == father.getLeft()) {
                    rightRotate(father);
                    RBTreeNode<V> tmp = node;
                    node = father;
                    father = tmp;
                }
                setBlack(father);
                setRed(grandFather);
                leftRotate(grandFather);
            }
        }
        setBlack(root);
    }

    // Delete nodes by key value
    public void delete(int key) {
        delete(search(key));
    }

    private void delete(RBTreeNode<V> node) {
        if (node == null)
            return;
        if (node.getLeft() != null && node.getRight() != null) {
            RBTreeNode<V> replaceNode = node;
            RBTreeNode<V> tmp = node.getRight();
            while (tmp != null) {
                replaceNode = tmp;
                tmp = tmp.getLeft();
            }
            int t = replaceNode.getKey();
            replaceNode.setKey(node.getKey());
            node.setKey(t);
            delete(replaceNode);
            return;
        }
        RBTreeNode<V> replaceNode = null;
        if (node.getLeft() != null)
            replaceNode = node.getLeft();
        else
            replaceNode = node.getRight();

        RBTreeNode<V> parent = node.getParent();
        if (parent == null) {
            root = replaceNode;
            if (replaceNode != null)
                replaceNode.setParent(null);
        } else {
            if (replaceNode != null)
                replaceNode.setParent(parent);
            if (parent.getLeft() == node)
                parent.setLeft(replaceNode);
            else {
                parent.setRight(replaceNode);
            }
        }
        if (node.getColor() == BLACK)
            removeFix(parent, replaceNode);

    }

    // extra color in node
    private void removeFix(RBTreeNode<V> father, RBTreeNode<V> node) {
        while ((node == null || node.getColor() == BLACK) && node != root) {
            if (father.getLeft() == node) {  // The case where S is the left son of P
                RBTreeNode<V> brother = father.getRight();
                if (brother != null && brother.getColor() == RED) {
                    setRed(father);
                    setBlack(brother);
                    leftRotate(father);
                    brother = father.getRight();
                }
                if (brother == null || (isBlack(brother.getLeft()) && isBlack(brother.getRight()))) {
                    setRed(brother);
                    node = father;
                    father = node.getParent();
                    continue;
                }
                if (isRed(brother.getLeft())) {
                    setBlack(brother.getLeft());
                    setRed(brother);
                    rightRotate(brother);
                    brother = brother.getParent();
                }

                brother.setColor(father.getColor());
                setBlack(father);
                setBlack(brother.getRight());
                leftRotate(father);
                node = root;//跳出循环
            } else { // The case where S is the right son of P. Symmetric operations
                RBTreeNode<V> brother = father.getLeft();
                if (brother != null && brother.getColor() == RED) {
                    setRed(father);
                    setBlack(brother);
                    rightRotate(father);
                    brother = father.getLeft();
                }
                if (brother == null || (isBlack(brother.getLeft()) && isBlack(brother.getRight()))) {
                    setRed(brother);
                    node = father;
                    father = node.getParent();
                    continue;
                }
                if (isRed(brother.getRight())) {
                    setBlack(brother.getRight());
                    setRed(brother);
                    leftRotate(brother);
                    brother = brother.getParent();
                }

                brother.setColor(father.getColor());
                setBlack(father);
                setBlack(brother.getLeft());
                rightRotate(father);
                node = root;  // Jump out of the loop
            }
        }

        if (node != null)
            node.setColor(BLACK);
    }

    private boolean isBlack(RBTreeNode<V> node) {
        if (node == null)
            return true;
        return node.getColor() == BLACK;
    }

    private boolean isRed(RBTreeNode<V> node) {
        if (node == null)
            return false;
        return node.getColor() == RED;
    }

    private void leftRotate(RBTreeNode<V> node) {
        RBTreeNode<V> right = node.getRight();
        RBTreeNode<V> parent = node.getParent();
        if (parent == null) {
            root = right;
            right.setParent(null);
        } else {
            if (parent.getLeft() != null && parent.getLeft() == node) {
                parent.setLeft(right);
            } else {
                parent.setRight(right);
            }
            right.setParent(parent);
        }
        node.setParent(right);
        node.setRight(right.getLeft());
        if (right.getLeft() != null) {
            right.getLeft().setParent(node);
        }
        right.setLeft(node);
    }

    private void rightRotate(RBTreeNode<V> node) {
        RBTreeNode<V> left = node.getLeft();
        RBTreeNode<V> parent = node.getParent();
        if (parent == null) {
            root = left;
            left.setParent(null);
        } else {
            if (parent.getLeft() != null && parent.getLeft() == node) {
                parent.setLeft(left);
            } else {
                parent.setRight(left);
            }
            left.setParent(parent);
        }
        node.setParent(left);
        node.setLeft(left.getRight());
        if (left.getRight() != null) {
            left.getRight().setParent(node);
        }
        left.setRight(node);
    }

    private void setBlack(RBTreeNode<V> node) {
        if(node != null) {
            node.setColor(BLACK);
        }
    }

    private void setRed(RBTreeNode<V> node) {
        if(node != null) {
            node.setColor(RED);
        }
    }

    // Iterate over the printed red-black tree
    public void inOrder() {
        inOrder(root);
    }

    private void inOrder(RBTreeNode<V> node) {
        if (node == null)
            return;
        inOrder(node.getLeft());
        System.out.println(node);
        inOrder(node.getRight());
    }

    @NonNull
    public String toString(){
        return "root" + root;
    }

    /**
     * Xing Chen
     * Below are usd wo get the size of this tree
     */
    public int size() {
        return countNodes(root);
    }
    private int countNodes(RBTreeNode<V> node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.getLeft()) + countNodes(node.getRight());
    }

    // Add methods for traversal
    private void inorderTraversal(RBTreeNode<V> node, List<V> set) {
        if (node != null) {
            inorderTraversal(node.getLeft(), set);
            set.add(node.getValue());
            inorderTraversal(node.getRight(), set);
        }
    }
    // for getAll
    public List<V> getAllElements() {
        List<V> resultSet = new ArrayList<>();
        inorderTraversal(root, resultSet);
        return resultSet;
    }
}

