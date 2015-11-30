package node;

import Utilities.BMNodeType;

public class BMWall extends BMNode {

	public BMWall(int x, int y, BMNode[][] board) {
		super(x, y, BMNodeType.wall, false, board);
	}
	
	

	@Override
	public boolean vanish(int id) {
		return true;
	}

}
