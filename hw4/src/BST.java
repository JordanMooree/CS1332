import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Your implementation of a binary search tree.
 *
 * @author Hwuiwon Kim
 * @userid hkim944
 * @version 1.0
 */
public class BST<T extends Comparable<? super T>> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private BSTNode<T> root;
    private int size;

    /**
     * A no-argument constructor that should initialize an empty BST.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Initializes the BST with the data in the Collection. The data
     * should be added in the same order it is in the Collection.
     *
     * Hint: Not all Collections are indexable like Lists, so a regular for loop
     * will not work here. However, all Collections are Iterable, so what type
     * of loop would work?
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public BST(Collection<T> data) {
        if (data == null || data.contains(data))
            throw new IllegalArgumentException("Data can't be null");
        size = 0;
        for (T item : data)
            add(item);
    }

    /**
     * Add the data as a leaf in the BST. Should traverse the tree to find the
     * appropriate location. If the data is already in the tree, then nothing
     * should be done (the duplicate shouldn't get added, and size should not be
     * incremented).
     * 
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
     *
     * @throws IllegalArgumentException if the data is null
     * @param data the data to be added
     */
    public void add(T data) {
        if (data == null)
            throw new IllegalArgumentException("Data is null");
        if (this.root == null) {
            root = new BSTNode<>(data);
            size++;
        } else
            addHelper(root, data);

    }

    /**
     * Helper for adding to the BST.
     * 
     * @param root - the parent node
     * @param data - the data to be added to the BST
     */
    private void addHelper(BSTNode<T> node, T data) {
        if (data.compareTo(node.getData()) > 0) {
            if (node.getRight() == null) {
                node.setRight(new BSTNode<>(data));
                size++;
            } else
                addHelper(node.getRight(), data);
        } else if (data.compareTo(node.getData()) < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new BSTNode<>(data));
                size++;
            } else
                addHelper(node.getLeft(), data);
        }
    }

    /**
     * Removes the data from the tree. There are 3 cases to consider:
     *
     * 1: the data is a leaf (no children). In this case, simply remove it.
     * 2: the data has one child. In this case, simply replace it with its
     * child.
     * 3: the data has 2 children. Use the predecessor to replace the data.
     * You MUST use recursion to find and remove the predecessor (you will
     * likely need an additional helper method to handle this case efficiently).
     *
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
     *
     * @throws IllegalArgumentException         if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to remove from the tree.
     * @return the data removed from the tree. Do not return the same data
     *         that was passed in. Return the data that was stored in the tree.
     */
    public T remove(T data) {
        if (data == null)
            throw new IllegalArgumentException("Data is null");

        BSTNode<T> removeNode = new BSTNode<>(null);
        root = removeHelper(data, root, removeNode);
        return removeNode.getData();
    }

    /**
     * Helper method for removing data from the tree
     * 
     * @param data   - the data to remove from the tree
     * @param node   - Current node
     * @param remove - the node that needs to be removed
     */
    private BSTNode<T> removeHelper(T data, BSTNode<T> node, BSTNode<T> removeNode) {
        if (node == null)
            throw new NoSuchElementException("Data is not found");
        else {
            int val = data.compareTo(node.getData());
            if (val > 0)
                node.setRight(removeHelper(data, node.getRight(), removeNode));
            else if (val < 0)
                node.setLeft(removeHelper(data, node.getLeft(), removeNode));
            else {
                removeNode.setData(node.getData());
                size--;
                if (node.getRight() == null)
                    return node.getLeft();
                else if (node.getLeft() == null)
                    return node.getRight();

                else {
                    BSTNode<T> child = new BSTNode<>(null);
                    node.setLeft(predecessorHelper(node.getLeft(), child));
                    node.setData(child.getData());
                }
            }
            return node;
        }
    }

    /**
     * Helper method for remove
     * Find predecessor node
     * 
     * @param node  - the current node
     * @param child - the child of a node that will be removed
     * @return predecessor node of an element that will be removed
     */
    private BSTNode<T> predecessorHelper(BSTNode<T> node, BSTNode<T> child) {
        if (node.getRight() == null) {
            child.setData(node.getData());
            return node.getLeft();
        }
        node.setRight(predecessorHelper(node.getRight(), child));
        return node;
    }

    /**
     * Returns the data in the tree matching the parameter passed in (think
     * carefully: should you use value equality or reference equality?).
     *
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
     *
     * @throws IllegalArgumentException         if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to search for in the tree.
     * @return the data in the tree equal to the parameter. Do not return the
     *         same data that was passed in. Return the data that was stored in the
     *         tree.
     */
    public T get(T data) {
        if (data == null)
            throw new IllegalArgumentException("Data is null");

        return getHelper(root, data);
    }

    private T getHelper(BSTNode<T> node, T data) {
        if (node == null) {
            throw new NoSuchElementException("Data is not found");
        }
        int value = data.compareTo(node.getData());
        if (data.equals(node.getData())) {
            return node.getData();
        } else if (value > 0) {
            return getHelper(node.getRight(), data);
        }

        else if (value < 0) {
            return getHelper(node.getLeft(), data);
        }
        return null;
    }

    /**
     * Returns whether or not data equivalent to the given parameter is
     * contained within the tree. The same type of equality should be used as
     * in the get method.
     *
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
     *
     * @throws IllegalArgumentException if the data is null
     * @param data the data to search for in the tree.
     * @return whether or not the parameter is contained within the tree.
     */
    public boolean contains(T data) {
        try {
            get(data);
        } catch (NoSuchElementException e) {
            return false;
        }
        return true;
    }

    /**
     * Should run in O(n).
     *
     * @return a preorder traversal of the tree
     */
    public List<T> preorder() {
        List<T> data = new ArrayList<>();
        preorderHelper(root, data);
        return data;
    }

    /**
     * preorder helper method
     * 
     * @param node - current node you are on
     * @param list - the list to append to
     * @return list of tree in preorder traversal
     */

    private void preorderHelper(BSTNode<T> node, List<T> list) {
        // CLR
        if (node == null)
            return;
        else {
            list.add(node.getData());
            preorderHelper(node.getLeft(), list);
            preorderHelper(node.getRight(), list);
        }

    }

    /**
     * Should run in O(n).
     *
     * @return an inorder traversal of the tree
     */
    public List<T> inorder() {
        List<T> data = new ArrayList<>();
        inorderHelper(root, data);
        return data;
    }

    /**
     * Helper method for inorder()
     *
     * @param node the root node of a tree
     * @param list the list that stores the data
     */
    private void inorderHelper(BSTNode<T> node, List<T> list) {
        if (node == null)
            return;
        else {
            inorderHelper(node.getLeft(), list);
            list.add(node.getData());
            inorderHelper(node.getRight(), list);
        }

    }

    /**
     * Should run in O(n).
     *
     * @return a postorder traversal of the tree
     */
    public List<T> postorder() {
        List<T> data = new ArrayList<>();
        postorderHelper(root, data);
        return data;
    }

    /**
     * Helper method for postorder()
     *
     * @param node the root node of a tree
     * @param list the list that stores the data
     */
    private void postorderHelper(BSTNode<T> node, List<T> list) {
        if (node == null)
            return;
        else {
            postorderHelper(node.getLeft(), list);
            postorderHelper(node.getRight(), list);
            list.add(node.getData());
        }

    }

    /**
     * Generate a level-order traversal of the tree.
     *
     * To do this, add the root node to a queue. Then, while the queue isn't
     * empty, remove one node, add its data to the list being returned, and add
     * its left and right child nodes to the queue. If what you just removed is
     * {@code null}, ignore it and continue with the rest of the nodes.
     *
     * Should run in O(n). This does not need to be done recursively.
     *
     * @return a level order traversal of the tree
     */
    public List<T> levelorder() {
        Queue<BSTNode<T>> queue = new LinkedList<>();
        List<T> data = new ArrayList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            BSTNode<T> curr = queue.poll();
            data.add(curr.getData());
            if (curr.getLeft() != null) {
                queue.offer(curr.getLeft());
            }
            if (curr.getRight() != null) {
                queue.offer(curr.getRight());
            }
        }
        return data;
    }

    /**
     * This method checks whether a binary tree meets the criteria for being
     * a binary search tree.
     *
     * This method is a static method that takes in a BSTNode called
     * {@code treeRoot}, which is the root of the tree that you should check.
     *
     * You may assume that the tree passed in is a proper binary tree; that is,
     * there are no loops in the tree, the parent-child relationship is
     * correct, that there are no duplicates, and that every parent has at
     * most 2 children. So, what you will have to check is that the order
     * property of a BST is still satisfied.
     *
     * Should run in O(n). However, you should stop the check as soon as you
     * find evidence that the tree is not a BST rather than checking the rest
     * of the tree.
     *
     * @param <T>      the generic typing
     * @param treeRoot the root of the binary tree to check
     * @return true if the binary tree is a BST, false otherwise
     */
    public static <T extends Comparable<? super T>> boolean isBST(BSTNode<T> treeRoot) {
        return BSTHelper(treeRoot);
    }

    /**
     * Helper method for isBST()
     * 
     * @param root - the root of the binary tree
     */

    private static <T extends Comparable<? super T>> boolean BSTHelper(BSTNode<T> node) {
        if (node != null) {
            if (node.getLeft() != null) {
                if (node.getData().compareTo(node.getLeft().getData()) < 0)
                    return false;
                else
                    return BSTHelper(node.getLeft());
            }

            if (node.getRight() != null) {
                if (node.getData().compareTo(node.getRight().getData()) > 0)
                    return false;
                else
                    return BSTHelper(node.getRight());
            }

        }
        return true;
    }

    /**
     * Clears the tree.
     *
     * Should run in O(1).
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Calculate and return the height of the root of the tree. A node's
     * height is defined as {@code max(left.height, right.height) + 1}. A leaf
     * node has a height of 0 and a null child should be -1.
     *
     * Should be calculated in O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return heightHelper(root);
    }

    /**
     * Helper method for the height() method
     * 
     * @return the height of tree
     */

    private int heightHelper(BSTNode<T> node) {
        if (node == null)
            return -1;

        BSTNode<T> left = node.getLeft();
        BSTNode<T> right = node.getRight();

        return Math.max(heightHelper(left), heightHelper(right)) + 1;
    }

    /**
     * Returns the size of the BST.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the number of elements in the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }

    /**
     * Returns the root of the BST.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }
}