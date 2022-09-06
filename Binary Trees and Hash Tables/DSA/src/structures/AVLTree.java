package structures;

import nodes.NodeAVL;

public class AVLTree {
	public NodeAVL root;
	
	public AVLTree() {
		
	}
	
	public AVLTree(NodeAVL root) {
		this.root = root;
	}
	
	public AVLTree(int value) {
		this.root = new NodeAVL(value);
	}
	
	// rotations of the tree
	
	// left rotation
	public NodeAVL leftRotateAB(NodeAVL a) {
		// perform rotation
		NodeAVL b = a.right;
		NodeAVL leftB = b.left;
		b.left = a;
		a.right = leftB;
		
		// set new heights
		a.height = max(height(a.left), height(a.right));
		b.height = max(height(b.left), height(b.right));
		
		a.height++;
		b.height++;
		
		return b; // return the top node after rotation
	}
	
	// right rotation
	public NodeAVL rightRotateAB(NodeAVL a) {
		NodeAVL b = a.left;
		NodeAVL rightB = b.right;
		b.right = a;
		a.left = rightB;
		
		// set new heights
		a.height = max(height(a.left), height(a.right));
		b.height = max(height(b.left), height(b.right));
		
		a.height++; // increase each by one
		b.height++;
		
		return b; // return the top node after rotation
	}
	
	// returns the height of a node
	public int height(NodeAVL node) {
		if(node == null) return -1;
		return node.height;
	}
	
	// returns max value of a and b
	public int max(int a, int b) {
		if(a > b) return a;
		return b;
	}
	
	public int calculateBalanceFactor(NodeAVL node) {
		if(node == null) return 0;
		return height(node.left) - height(node.right);
	}
	
	public NodeAVL findRightSubtreeMin(NodeAVL root) {
		NodeAVL rightChild = root.right;
		while(rightChild.left != null) {
			rightChild = rightChild.left;
		}
		return rightChild;
	}
	
	public NodeAVL insertToAVL(int value, NodeAVL root) {
		// insert node
		if(root == null) {
			root = new NodeAVL(value);
			//System.out.println("inserted " + value);
			return root;
		}
		if(root.value > value) {
			root.left = insertToAVL(value, root.left);
		} else if(root.value < value) {
			root.right = insertToAVL(value, root.right);
		} else if(root.value == value) {
			return root;
		}
		
		// update the height of previous node
		root.height = max(height(root.left), height(root.right));
		root.height++;
		
		// balance AVL tree
		int balance = calculateBalanceFactor(root); // calculate balance factor
		
		// 4 imbalance cases
		// left left
		if(balance > 1) {
			if(value < root.left.value) {
				return rightRotateAB(root);
			}
		}
		// left right
		if(balance > 1) {
			if(value > root.left.value) {
				root.left = leftRotateAB(root.left);
				return rightRotateAB(root);
			}
		}
		// right right
		if(balance < -1) {
			if(value > root.right.value) {
				return leftRotateAB(root);
			}
		}
		// right left
		if(balance < -1) {
			if(value < root.right.value) {
				root.right = rightRotateAB(root.right);
				return leftRotateAB(root);
			}
		}
		return root;
	}
	
	public NodeAVL find(int value) {
		NodeAVL curr = root;
		while(curr != null) {
			if(value == curr.value) return curr;
			if(value > curr.value) curr = curr.right;
			else curr = curr.left;
		}
		return curr;
	}
	
	public NodeAVL deleteFromAVL(int value, NodeAVL root) {
		if(root == null) {
			return root;
		}
		if(root.value < value) {
			root.right = deleteFromAVL(value, root.right);
		} else if(root.value > value) {
			root.left = deleteFromAVL(value, root.left);
		}
		else if(root.value == value) {
			if(root.left == null) { // left is null
				root = root.right;
			} else if(root.right == null) { // right is null
				root = root.left;
			} else { // root has both left and right
				NodeAVL rightSubMin = findRightSubtreeMin(root);
				root.value = rightSubMin.value; // copy the value of successor
				root.right = deleteFromAVL(rightSubMin.value, root.right); // delete successor
			}
		}
		if(root == null) return root;
		
		// update the height of root
		root.height = max(height(root.left), height(root.right));
		root.height++;
		
		// rebalance the tree
		int balance = calculateBalanceFactor(root);
		if(balance > 1) { // LL case
			if(calculateBalanceFactor(root.left) >= 0) {
				return rightRotateAB(root);
			}
		}
		if(balance > 1) { // LR case
			if(calculateBalanceFactor(root.left) < 0) {
				root.left = leftRotateAB(root.left);
				return rightRotateAB(root);
			}
		}
		if(balance < -1) { // RR case
			if(calculateBalanceFactor(root.right) <= 0) {
				return leftRotateAB(root);
			}
		}
		if(balance < -1) { // RL case
			if(calculateBalanceFactor(root.right) > 0) {
				root.right = rightRotateAB(root.right);
				return leftRotateAB(root.left);
			}
		}
		return root;
	}
}
