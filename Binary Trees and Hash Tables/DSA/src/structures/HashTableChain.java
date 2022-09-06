package structures;

import java.util.ArrayList;

import nodes.NodeHashChain;

public class HashTableChain {
	private ArrayList<NodeHashChain> elements = new ArrayList<>();
	private int realSize; // size of non null elements
	
	public HashTableChain(int capacity) {
		populateList(capacity);
		realSize = 0;
	}
	
	public void populateList(int length) {
		for(int i = 0; i<length; i++) {
			elements.add(null);
		}
	}
	
	public double getLoadFactor() {
		return (double) realSize / (double) elements.size();
	}
	
	public int getSize() {
		return realSize;
	}
	
	public int getIndex(int key) {
		int index = hashCode(key) % elements.size();
		if(index >= 0) {
			return index;
		}
		return -1;
	}
	
	public String getElement(int key) {
		int index = getIndex(key);
		int hash = hashCode(key);
		NodeHashChain elementAtIndex = elements.get(index);
		if(index >= 0) {
			while(elementAtIndex != null) {
				if(elementAtIndex.hash == hash && elementAtIndex.key == key) {
					return elementAtIndex.value;
				}
				elementAtIndex = elementAtIndex.next;
			}
		}
		return null;
	}
	
	public NodeHashChain addElement(int key, String value) {
		int hash = hashCode(key);
		int index = getIndex(key);
		if(index >= 0) {
			NodeHashChain elementAtIndex = elements.get(index);
			if(elementAtIndex == null) { // if linked list doesnt exist
				elements.set(index, new NodeHashChain(key, value, hash));
			}
			while(elementAtIndex != null) { // if key is present
				if(elementAtIndex.hash == hash && elementAtIndex.key == key) {
					elementAtIndex.value = value; // update the value and dont add
					return null;
				}
				elementAtIndex = elementAtIndex.next;
			}
			elementAtIndex = elements.get(index);
			
			// add new element to the start of the chain
			NodeHashChain newElement = new NodeHashChain(key, value, hash);
			elements.set(index, newElement);
			newElement.next = elementAtIndex;
			realSize++;
			
			// check size factor
			if(getLoadFactor() >= 0.7) {
				ArrayList<NodeHashChain> tempList = elements;
				elements = new ArrayList<>();
				realSize = 0;
				populateList(tempList.size() * 2);
				for(NodeHashChain element : tempList) {
					while(element != null) {
						addElement(element.key, element.value);
						element = element.next;
					}
				}
			}
		}
		return null;
	}
	
	public String deleteElement(int key) {
		int index = getIndex(key);
		int hash = hashCode(key);
		NodeHashChain elementAtIndex = elements.get(index);
		NodeHashChain previous = null;
		boolean first = true;
		if(index >= 0) {
			while(elementAtIndex != null) {
				if(first && elementAtIndex.hash == hash && elementAtIndex.key == key) { // if first in list
					elements.set(index, elementAtIndex.next);
					realSize--;
					return elementAtIndex.value;
				} else if(elementAtIndex.hash == hash && elementAtIndex.key == key) { // if not first in list
					previous.next = elementAtIndex.next;
					realSize--;
					return elementAtIndex.value;
				}
				first = false;
				previous = elementAtIndex;
				elementAtIndex = elementAtIndex.next;
			}
		}
		return null;
	}
	
	public void printList() {
		for(int i = 0; i<realSize; i++) {
			if(elements.get(i) != null) {
				System.out.println(elements.get(i).value);
			}
		}
	}

	public boolean isEmpty() {
		if(realSize == 0) return true;
		return false;
	}
	
	public int hashCode(int key) {
		return (int) Math.floor(elements.size() * (key * (0.3 % 1)));
	}
}
