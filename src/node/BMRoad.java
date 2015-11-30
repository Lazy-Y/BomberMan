package node;

import Utilities.BMNodeType;

public class BMRoad extends BMNode {

	public BMRoad(int x, int y, BMNode[][] board) {
		super(x, y, BMNodeType.road, true, board);
	}
}
