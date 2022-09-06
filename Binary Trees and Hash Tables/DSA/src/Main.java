
import structures.AVLTree;
import structures.SplayTree;
import structures.HashTableChain;
import structures.HashTableLinear;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Main {
	
	public static int stringToInteger(String str) {
        int sum=0;
        for(int i = 0; i < str.length(); i++) {
            int number = str.charAt(i);
            sum = sum + number;
        }
       return sum - 97;
    }
	
	public static String generateRandomString(int maxSize) {
		int length = (int)(Math.random() * maxSize) + 1;
		String randomString = "";
		for(int i = 0; i<length; i++) {
			randomString += generateRandomChar();
		}
		return randomString;
	}
	
	public static char generateRandomChar() {
		Random rand = new Random();
		return (char) (97 + rand.nextInt(25)); // generate a - z
	}
	
	
	public static void main(String[] args) {
		AVLTree avlTree = new AVLTree();
		SplayTree splayTree = new SplayTree();
		HashTableChain hashChain = new HashTableChain(200);
		HashTableLinear hashLinear = new HashTableLinear(200);
			
		
		/*
		 * 	INSERT OPERATION
		 */
		
		int numberOfElements = 1000; // change this  <<<<<<<-------
		
		hashChain = new HashTableChain(numberOfElements * 2);
		hashLinear = new HashTableLinear(numberOfElements * 2);
		
		// generate dataset
		Integer[] array = new Integer[numberOfElements];
		String[] strArray = new String[numberOfElements];
		for(int i = 0; i<numberOfElements; i++) {
			String randomString = generateRandomString(1000);
			strArray[i] = randomString;
			array[i] = i;
		}
		
		// shuffle int array so integers dont come in the same order
		List<Integer> intList = Arrays.asList(array);
		Collections.shuffle(intList);
		intList.toArray(array);
		
		long t1 = System.nanoTime();
		for(int i = 0; i<numberOfElements; i++) {
			avlTree.insertToAVL(array[i], avlTree.root);
		}
		long t2 = System.nanoTime();
		System.out.println("AVL inserted " + numberOfElements + " elements in: " + (t2-t1) + " nanoseconds");
		
		t1 = System.nanoTime();
		for(int i = 0; i<numberOfElements; i++) {
			splayTree.insertToSplay(array[i], splayTree.root);
		}
		t2 = System.nanoTime();
		System.out.println("splay inserted " + numberOfElements + " elements in: " + (t2-t1) + " nanoseconds");
		
		t1 = System.nanoTime();
		for(int i = 0; i<numberOfElements; i++) {
			hashChain.addElement(array[i], strArray[i]);
		}
		t2 = System.nanoTime();
		System.out.println("hash table chain inserted " + numberOfElements + " elements in: " + (t2-t1) + " nanoseconds");
		
		t1 = System.nanoTime();
		for(int i = 0; i<numberOfElements; i++) {
			hashLinear.addElement(array[i], strArray[i]);
		}
		t2 = System.nanoTime();
		System.out.println("hash table linear inserted " + numberOfElements + " elements in: " + (t2-t1) + " nanoseconds");
		
		
		// shuffle int array so integers dont come in the same order
		intList = Arrays.asList(array);
		Collections.shuffle(intList);
		intList.toArray(array);
		System.out.println();
	
		/*
		 * SEARCH OPERATION
		 */
		t1 = System.nanoTime();
		for(int i = 0; i<numberOfElements; i++) {
			avlTree.find(array[i]);
		}
		t2 = System.nanoTime();
		System.out.println("AVL found " + numberOfElements + " elements in: " + (t2-t1) + " nanoseconds");
		
		t1 = System.nanoTime();
		for(int i = 0; i<numberOfElements; i++) {
			splayTree.searchInSplay(array[i], splayTree.root);
		}
		t2 = System.nanoTime();
		System.out.println("splay tree found " + numberOfElements + " elements in: " + (t2-t1) + " nanoseconds");
		
		t1 = System.nanoTime();
		for(int i = 0; i<numberOfElements; i++) {
			hashChain.getElement(array[i]);
		}
		t2 = System.nanoTime();
		System.out.println("hash table chain found " + numberOfElements + " elements in: " + (t2-t1) + " nanoseconds");
		
		t1 = System.nanoTime();
		for(int i = 0; i<numberOfElements; i++) {
			hashLinear.getElement(array[i]);
		}
		t2 = System.nanoTime();
		System.out.println("hash table linear found " + numberOfElements + " elements in: " + (t2-t1) + " nanoseconds");
		
		
		// shuffle int array so integers dont come in the same order
		intList = Arrays.asList(array);
		Collections.shuffle(intList);
		intList.toArray(array);
		System.out.println();
		
		/*
		 * DELETE OPERATION
		 */
		
		t1 = System.nanoTime();
		for(int i = 0; i<numberOfElements; i++) {
			avlTree.deleteFromAVL(array[i], avlTree.root);
		}
		t2 = System.nanoTime();
		System.out.println("AVL deleted " + numberOfElements + " elements in: " + (t2-t1) + " nanoseconds");
		t1 = System.nanoTime();
		for(int i = 0; i<numberOfElements; i++) {
			avlTree.deleteFromAVL(array[i], avlTree.root);
		}
		t2 = System.nanoTime();
		System.out.println("splay tree deleted " + numberOfElements + " elements in: " + (t2-t1) + " nanoseconds");
		
		t1 = System.nanoTime();
		for(int i = 0; i<numberOfElements; i++) {
			avlTree.deleteFromAVL(array[i], avlTree.root);
		}
		t2 = System.nanoTime();
		System.out.println("hash table chain deleted " + numberOfElements + " elements in: " + (t2-t1) + " nanoseconds");
		
		t1 = System.nanoTime();
		for(int i = 0; i<numberOfElements; i++) {
			avlTree.deleteFromAVL(array[i], avlTree.root);
		}
		t2 = System.nanoTime();
		System.out.println("hash table linear deleted " + numberOfElements + " elements in: " + (t2-t1) + " nanoseconds");
	
	}
	
	
}
