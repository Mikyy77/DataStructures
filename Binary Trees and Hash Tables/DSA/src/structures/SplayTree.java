package structures;

import nodes.SplayNode;

public class SplayTree {
	public SplayNode root;
	
	public SplayTree() {
		
	}
	
	public SplayNode leftRotate(SplayNode x) {
		SplayNode y = x.right;
		x.right = y.left;
		y.left = x;
		return y;
	}
	
	public SplayNode rightRotate(SplayNode x) {
		SplayNode y = x.left;
		x.left = y.right;
		y.right = x;
		return y;
	}
	
	public SplayNode insertToSplay(int value, SplayNode root) {
		// if root is null or same value as inserted value
		if(root == null || root.value == value) {
			root = new SplayNode(value);
			return root;
		}
		
		root = splay(value, root);
		if(root.value == value) {
			root = new SplayNode(value);
			return root;
		}
		SplayNode nodeToInsert = new SplayNode(value);
		if(root.value < value) { 
			nodeToInsert.left = root;
			nodeToInsert.right = root.right;
			root.right = null;
		}else if(root.value > value) {
			nodeToInsert.right = root;
			nodeToInsert.left = root.left;
			root.left = null;
		}
		
		return nodeToInsert; // new root of tree
	}
	
	public SplayNode searchInSplay(int value, SplayNode root) {
		return splay(value, root);
	}
	
	public SplayNode splay(int value, SplayNode root) {
		
		if(root == null || root.value == value) return root;  // node is root

		if(value < root.value) { // left of root
			if (root.left == null) return root; // if no left child just return root
			
			// Zig zag
			// right child of left child
			SplayNode rightOfLeft = root.left.right;
			if(value > root.left.value) {
				rightOfLeft = splay(value, rightOfLeft); // recursive splaying called by making right of left the root
				if(rightOfLeft != null) root.left = leftRotate(root.left); // first left rotation
			}
			// zig zig
			// left child of left child
			SplayNode leftOfLeft = root.left.left;
			if(value < root.left.value) {
				leftOfLeft = splay(value, leftOfLeft); // recursive splaying by making left of left the root
				root = rightRotate(root); // first right rotation
			}
			
			// second rotate for both scenarios -> rotate root right
			if(root.left != null) {
				return rightRotate(root);
			}
			return root;
			
		} else if(value >= root.value) { // right child of root
			if(root.right == null) {
				return root;
			}
			// zig zag
			// left child of right child
			SplayNode leftOfRight = root.right.left;
			if(value < root.right.value) {
				leftOfRight = splay(value, leftOfRight);
				if(leftOfRight != null) root.right = rightRotate(root.right);
			}
			// zig zig
			// right of right child
			SplayNode rightOfRight = root.right.right;
			if(value > root.right.value) {
				rightOfRight = splay(value, rightOfRight);
				root = leftRotate(root);
			}
			// second rotate for both scenarios -> rotate root left
			if(root.right != null) {
				return leftRotate(root);
			}
			return root;
		}
		return null;
	}
	
	public SplayNode max(SplayNode x) {
		while(x.right != null) {
			x = x.right;
		}
		return x;
	}
	
	public SplayNode deleteFromSplay(int value, SplayNode root) {
		
		if(root == null) return root;
		root = splay(value, root);
		
		if(root.value != value) { // value not present - nothing to delete
			return root;
		}
		
		SplayNode leftTree = root.left;
		SplayNode rightTree = root.right;
		
		if(leftTree == null) { // left tree is null
			root = rightTree;
			return rightTree;
		}
		
		if(rightTree == null) { // right tree is null
			root = leftTree;
			return leftTree;
		}
		
		leftTree = splay(value, max(leftTree)); // splay for the max value in left sub
		leftTree.right = rightTree; // right root as right child of root
		return leftTree; // return new root
	}
}
