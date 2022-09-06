package nodes;

public class NodeHashChain {
	public int key;
	public String value;
	public NodeHashChain next; // chaining - link to next node
	public int hash;
	
	public NodeHashChain(int key, String value, int hash) {
		this.key = key;
		this.value = value;
		this.hash = hash;
	}
}
