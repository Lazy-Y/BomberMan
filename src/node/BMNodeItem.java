package node;

public class BMNodeItem extends BMNode {

	public BMNodeItem(int x, int y, BMNode[][] board, int type) {
		super(x, y, type, true, board);
	}

	public int getValue() {
		return type;
	}

	public void getItem(){
		board[x][y] = new BMRoad(x,y,board);
	}
}
