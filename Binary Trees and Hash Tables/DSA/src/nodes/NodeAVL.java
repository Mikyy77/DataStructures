package nodes;

public class NodeAVL {
	public int value;
	public int height;
	public NodeAVL left;
	public NodeAVL right;
	
	public NodeAVL(int key) {
		this.value = key;
		height = 1;
	}
}
