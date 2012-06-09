package model;

public class Position {
	
	private int max;
	private int pos;
	
	public Position(int max, int pos) {
		this.max = max;
		this.pos = pos;
	}

	public int getMax() {
		return max;
	}

	public int getPos() {
		return pos;
	}
	
	public boolean isFirst() {
		return pos == 0;
	}
	
	public boolean isLast() {
		return max == pos;
	}
	
	public void increasePosition() {
		pos++;
	}
	
	@Override
	public String toString() {
		return "(" + pos + "," + max + ")";
	}
}
