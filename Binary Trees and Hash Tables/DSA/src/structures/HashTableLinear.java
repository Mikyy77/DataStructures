package structures;

import java.util.ArrayList;

import nodes.NodeHashLinear;

public class HashTableLinear {
	ArrayList<NodeHashLinear> elements = new ArrayList<>();
	int existingSize; // size of non null elements
	
	public HashTableLinear(int capacity) {
		populateList(capacity);
		existingSize = 0;
	}
	
	public void populateList(int length) {
		for(int i = 0; i<length; i++) {
			elements.add(null);
		}
	}
	
	public int hashCode(int key) {
		return key % elements.size();
	}
	
	
	public String getElement(int key) {
		int index = hashCode(key);
		int i = 0; // counter for overflow
		while(elements.get(index) != null && elements.get(index).key != -1) {
			if(i > elements.size()) return null;
			if(elements.get(index).key == key) {
				return elements.get(index).value;
			}
			index++; // move to next index
			index = hashCode(index); // hash the index number
			i++; // increment counter
		}
		return null;
	}
	
	public double getLoadFactor() {
		return (double)existingSize / (double) elements.size();
	}
	
	public NodeHashLinear addElement(int key, String value) {
		int index = hashCode(key);
		while(elements.get(index) != null && elements.get(index).key != -1) {
			if(elements.get(index).key == key) break;
			index = (index + 1) % elements.size();
		}
		// if found free space or deleted element -> insert
		if(elements.get(index) == null || elements.get(index).key == -1) {
			NodeHashLinear newNode = new NodeHashLinear(key, value);
			elements.set(index, newNode);
			existingSize++;

			// increase size if necessary
			/*if(getLoadFactor() > 0.7) {
				ArrayList<Node> tempList = elements;
				elements = new ArrayList<>();
				size = 0;
				populateList(findNextPrime(tempList.size() * 2));
				for(Node element : elements) {
					addElement(element.key, element.value);
				}
			}*/
			return newNode;
		}
		return null;
	}
	
	public NodeHashLinear deleteElement(int key) {
		if(key < 0) return null;
		int index = hashCode(key);
		boolean found = false;
		while(elements.get(index) != null) { // find the item to be deleted
			if(elements.get(index).key == key) {
				found = true;
				break;
			}
			index = (index + 1) % elements.size();
		}
		if(!found) return null;
		
		NodeHashLinear nodeToDelete = elements.get(index); // delete the node itself
		nodeToDelete.key = -1;
		nodeToDelete.value = null;
		existingSize--;
		
		// re-add other affected elements
		index = (index + 1) % elements.size();
		while(elements.get(index) != null) {
			int tempKey = elements.get(index).key;
			String tempValue = elements.get(index).value;
			elements.get(index).key = -1;
			elements.get(index).value = null;
			addElement(tempKey, tempValue);
			existingSize--;
			index = (index + 1) % elements.size();
		}
		return nodeToDelete;
	}
	
	public int findNextPrime(int number) {
		while(!isPrime(number)) {
			number++;
		}
		return number;
	}
	
	public boolean isPrime(int number) {
		if(number == 2 || number == 3) return true;
		for(int i = 2; i <= number / 2; i++) {
			if(number % i == 0) return false;
		}
		return true;
	}
	
	public void printTable() {
		for(NodeHashLinear el : elements) {
			if(el != null) {
				System.out.println(el.value);
			}
		}
	}
}
