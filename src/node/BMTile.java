package node;

import java.util.Random;

import Utilities.BMNodeType;

public class BMTile extends BMNode {

	Random rn;

	public BMTile(int x, int y, BMNode[][] board){
		super(x, y, BMNodeType.tile, false, board);
		rn = new Random();
	}
	
	@Override
	public boolean vanish(int id) {
		int temp = Math.abs(rn.nextInt());
		
		// 30% get item, 70% get nothing(just become road)
		if (temp % 10 < 4) {
			// Generate random item type
			int tempItemID = Math.abs(rn.nextInt());
			tempItemID = tempItemID % 8;
			tempItemID += 5;
			System.out.println("temp Item ID " + tempItemID);
			// Let this tile be the randomly generated item type after bombing
			board[x][y] = new BMBombing(x, y, board, tempItemID, id);
		} else {
			board[x][y] = new BMBombing(x, y, board, BMNodeType.road, id);
		}
		return true;
	}

}
