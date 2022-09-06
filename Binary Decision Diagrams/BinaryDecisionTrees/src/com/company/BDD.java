package com.company;

import java.util.*;

class Node {
    int value = -1;
    int height;
    String exp;
    Node left;
    Node right;
    ArrayList<Node> parents = new ArrayList<>();

    public Node(int key) {
        this.value = key;
    }

    public Node(String exp) {
        this.exp = exp;
    }
}


public class BDD {

    Node root;
    private String order;
    private Node one = new Node(1);
    private Node zero = new Node(0);

    private ArrayList<HashMap<String, Node>> mapList = new ArrayList<>(); // hashmaps for every level of tree

    private int totalNodes = 2; // since we have one and zero


    public BDD(String function, String order) {
        String expression = convertInput(function);
        this.root = new Node(expression);
        this.order = order;
        populateList();
        decomposition(root, String.valueOf(order.charAt(0)), 0); // recursive function that decomposes the function completely
//        printLevelOrder(root); // prints bdd in level order
    }


    public int getTotalNodes() {
        return totalNodes;
    }

    public void populateList() {
        int length = order.length();
        for (int i = 0; i < length; i++) {
            mapList.add(new HashMap<>());
        }
    }

    public String convertInput(String input) {
        String result = "";
        boolean skipNext = false;
        for(int i = 0; i<input.length(); i++) {
            if(input.charAt(i) == '!' && i != input.length()-1) {
                String letter = String.valueOf(input.charAt(i+1)).toLowerCase(Locale.ROOT);
                result += letter;
                i++;
                skipNext = true;
            }
            if(!skipNext) {
                result += input.charAt(i);
            }
            skipNext = false;
        }
        return result;
    }

    void printLevelOrder(Node root) {
        // Base Case
        if(root == null)
            return;

        // Create an empty queue for level order traversal
        Queue<Node> queue =new LinkedList<Node>();

        // Enqueue Root and initialize height
        queue.add(root);


        while(true) {

            // count (queue size) indicates number of nodes
            // at current level.
            int count = queue.size();
            if(count == 0)
                break;

            // Dequeue all nodes of current level and Enqueue all
            // nodes of next level
            while(count > 0){
                Node node = queue.peek();
                System.out.print(node.exp + " " + node.value + " ");
                queue.remove();
                if(node.left != null)
                    queue.add(node.left);
                if(node.right != null)
                    queue.add(node.right);
                count--;
            }
            System.out.println();
        }
    }

    public boolean isNotInExpression(String expression, String part) {
        part = sortString(part);
        String[] arr = expression.split("\\+");
        for(String exp : arr) {
            exp = sortString(exp);
            if(exp.equals(part)) {
                return false;
            }
        }
        return true;
    }

    public String sortString(String string) {
        char[] array = string.toCharArray();
        Arrays.sort(array);
        return new String(array);
    }

    public Node decomposition(Node root, String letter, int level) {

        if(root.value == 1 || root.value == 0) {
            return root;
        }

        String expression = root.exp;
        char charLetter = letter.charAt(0);
        root.height = level;

        HashMap<String, Node> map = mapList.get(level);
        if(map.containsKey(root.exp)) {
            for(Node parent : root.parents) {
                if(parent.equals(root.left)) {
                    parent.left = map.get(root.exp);
                } else {
                    parent.right = map.get(root.exp);
                }
            }
            root = map.get(root.exp);
            return root;
        } else {
            map.put(root.exp, root);
        }

        String[] expArray = expression.split("\\+");

        // case 0
        StringBuilder exp1 = new StringBuilder();
        for(String s : expArray) {
            if((expArray.length == 1 && s.contains(letter))) {
                exp1 = new StringBuilder("0");
                break;
            }
            if(s.length() == 1 && s.contains(letter.toLowerCase(Locale.ROOT))) {
                exp1 = new StringBuilder("1");
                break;
            }
            if(!s.contains(letter) && !s.contains(letter.toLowerCase(Locale.ROOT))) { // if it doesn't contain the letter, add to expression
                if(isNotInExpression(exp1.toString(), s)) {
                    exp1.append(s).append("+");
                }
            } else {
                // if contains !A and A = 0, write everything except the letter
                if(s.contains(letter.toLowerCase(Locale.ROOT))) {
                    boolean appended = false;
                    StringBuilder exp = new StringBuilder();
                    for(int j = 0; j < s.length(); j++) {
                        if(!String.valueOf(s.charAt(j)).equals(letter.toLowerCase(Locale.ROOT))) {
                            exp.append(s.charAt(j));
                            appended = true;
                        }
                    }
                    if(appended) {
                        if(isNotInExpression(exp1.toString(), exp.toString())) {
                            exp1.append(exp).append("+");
                        }
                    }
                }
            }
        }
        if(exp1.length() > 0 && exp1.charAt(exp1.length()-1) == '+') { // remove the last +
            exp1 = new StringBuilder(exp1.substring(0, exp1.length() - 1));
        }

        // case 1
        StringBuilder exp2 = new StringBuilder();
        for(String s : expArray) {
            if(s.equals(letter)) { // A = 1 and A
                exp2 = new StringBuilder("1");
                break;
            }
            if(!s.contains(letter) && !s.contains(letter.toLowerCase(Locale.ROOT))) { // if it doesn't contain the letter, add to expression
                if(isNotInExpression(exp2.toString(), s)){
                    exp2.append(s).append("+");
                }
            } else if(s.contains(letter.toLowerCase(Locale.ROOT))) { // contains !A and A = 1 =>> don't add to expression
                // if contains only one item, create zero node
                if(expArray.length == 1) {
                    exp2 = new StringBuilder("0");
                    break;
                }
            } else {
                StringBuilder exp = new StringBuilder();
                for(int j = 0; j < s.length(); j++) { // if expression contains A and A = 1, add everything but A
                    if(s.charAt(j) != charLetter) {
                        exp.append(s.charAt(j));
                    }
                }
                if(isNotInExpression(exp2.toString(), exp.toString())) {
                    exp2.append(exp).append("+");
                }
            }
        }
        if(exp2.length() > 0 && exp2.charAt(exp2.length()-1) == '+') { // remove the last + if there is one
            exp2 = new StringBuilder(exp2.substring(0, exp2.length() - 1));
        }

        // if string builder == 0 or is empty - assign children to zero
        if(exp1.length() == 0 || exp1.toString().equals("0")) {
            root.left = zero;
            root.left.parents.add(root);
            exp1 = new StringBuilder();
        }
        if(exp2.length() == 0 || exp2.toString().equals("0")) {
            root.right = zero;
            root.right.parents.add(root);
            exp2 = new StringBuilder();
        }
        // if string builder == 1 or is empty - assign children to one
        if(exp1.toString().equals("1")) {
            root.left = one;
            root.left.parents.add(root);
            exp1 = new StringBuilder();
        }
        if(exp2.toString().equals("1")) {
            root.right = one;
            root.right.parents.add(root);
            exp2 = new StringBuilder();
        }

        // assign children
        if(exp1.length() > 0) {
            root.left = new Node(exp1.toString());
            totalNodes++;
        }
        if(exp2.length() > 0) {
            root.right = new Node(exp2.toString());
            totalNodes++;
        }

        // call recursion
        if(root.left.value == 0 || root.left.value == 1) {
            root.left = decomposition(root.left, String.valueOf(order.charAt(order.indexOf(charLetter))), order.indexOf(charLetter) + 1);
        } else {
            root.left = decomposition(root.left, String.valueOf(order.charAt(order.indexOf(charLetter) + 1)), order.indexOf(charLetter) + 1);
        }
        if(root.right.value == 0 || root.right.value == 1) {
            root.right = decomposition(root.right, String.valueOf(order.charAt(order.indexOf(charLetter))), order.indexOf(charLetter) + 1);
        } else {
            root.right = decomposition(root.right, String.valueOf(order.charAt(order.indexOf(charLetter) + 1)), order.indexOf(charLetter) + 1);
        }

        reductionS(root);
        return root;
    }

    public void reductionS(Node root) {
        if(root.equals(one) || root.equals(zero) || root.left.equals(one) || root.left.equals(zero) || root.right.equals(one) || root.right.equals(zero)) {
            return;
        }
        // if pointing to the same node
        if(root.left.exp.equals(root.right.exp)) {
            totalNodes -= 2;
            for(Node parent : root.parents) { // assign all parents of root as parents to roots child
                if(parent.left.equals(root)) { // if root is left child
                    parent.left = root.left; // assign
                    root.left.parents.remove(root);
                    root.left.parents.add(parent);
                }
                if(parent.right.equals(root)) { // same for right
                    parent.right = root.right;
                    root.right.parents.remove(root);
                    root.right.parents.add(parent);
                }
            }
        }
    }
}
