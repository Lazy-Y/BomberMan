package node;

import Server.BMPlayer;
import Utilities.BMNodeType;

public class BMBomb extends BMNode{

	private final int power; 
	private final int timeToLive;
	private final BMPlayer mPlayer;

	public BMBomb(int x, int y, BMNode[][] board, BMPlayer player) {
		super(x, y, BMNodeType.bomb, false, board);
		this.power = player.getPower();
		this.timeToLive = player.getDetonatedTime();
		mPlayer = player;
		start();
	}

	public boolean vanish(int id){
		vanish();
		this.interrupt();
		return false;
	}
	
	public boolean vanish() {
		board[x][y] = new BMBombing(x, y, board, BMNodeType.road, mPlayer.getid());
//		System.out.println("Power " + power);
		for (int i = 1; i <= power; i++) {
//			System.out.println((x+i) + " " + y);
			if (x + i >= 16 || (board[x+i][y].vanish(mPlayer.getid()))) break;
		}
		
		for (int i = 1; i <= power; i++) {
//			System.out.println((x-i) + " " + y);
			if (x - i < 0 || (board[x-i][y].vanish(mPlayer.getid()))) break;
		}
		
		for (int i = 1; i <= power; i++) {
//			System.out.println(x + " " + (y+i));
			if (y + i >= 16 || (board[x][y+i].vanish(mPlayer.getid()))) break;
		}
		
		for (int i = 1; i <= power; i++) {
//			System.out.println(x + " " + (y-i));
			if (y - i < 0 || (board[x][y-i].vanish(mPlayer.getid()))) break;
		}
		return true;
	}

	@Override
	public void run() {
		try {
			java.lang.Thread.sleep(timeToLive*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		vanish(mPlayer.getid());
	}
	
	public int getPower() {
		return power;
	}
	
	public int getID() {
		if (mPlayer == null) return -1;
		return mPlayer.getid();
	}

}
