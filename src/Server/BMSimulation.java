package Server;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TreeMap;
import java.util.Vector;

import Client.BMClientPanel;
import Utilities.BMLibrary;
import Utilities.BMNodeType;
import Utilities.BMResult;
import node.BMBomb;
import node.BMBombing;
import node.BMNode;
import node.BMNodeItem;
import node.BMRoad;
import node.BMTile;
import node.BMWall;

public class BMSimulation extends Thread {
	volatile private BMNode[][] board = new BMNode[16][16];
	private ArrayList<BMPlayer> players;
	private BMPlayer host;
	private int timeLeft = 1000;
	private BMHostServer hs;
	private int totalHP = 5;
	private int numPlayer;
	private BMClientPanel clientPanel;
	
	public final static int start_game = 0;
	public final static int start_with_AI = 1;
	
	public BMSimulation(int port, int numPlayer, BMClientPanel bmClientPanel){
		clientPanel = bmClientPanel;
		System.out.println("num of player "+ numPlayer);
		this.numPlayer = numPlayer; 
		if (this.numPlayer > 4) numPlayer = 4;
		players = new ArrayList<BMPlayer>();
		hs = new BMHostServer(port,numPlayer);
		hs.setSimulation(this);
		loadBoard(BMLibrary.getGameMap());
	}
	private void loadBoard(int[][] board){
//		System.out.println("Board " +board[0][0] );
		for (int i = 0; i < 16; i++){
			for (int j = 0; j < 16; j++){
				this.board[i][j] = initNode(i,j,board[i][j]);
			}
		}
	}
	private BMNode initNode(int i, int j, int id){
		if (id == BMNodeType.tile){
			return new BMTile(i,j,board);
		}
		else if (id == BMNodeType.wall){
			return new BMWall(i,j,board);
		}
		else return new BMRoad(i,j,board);
	}
	public void startTimer(){
		new Thread(new Runnable(){
			@Override
			public void run() {
				while (timeLeft >= 0){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println("Start Timer Exception: " + e.getMessage());
					}
					timeLeft--;
				}
				BMSimulation.this.endGame();
			}			
		}).start();
	}
	public void popError(String msg){
		clientPanel.popError(msg);
	}
	
	public void setVariables(int time, int HP){
		timeLeft = time;
		totalHP = HP;
	}
	public int getNumClients(){
		return hs.getClients().size();
	}
	
	public Vector<String> getHPs(){
		Vector<String> HPs = new Vector<String>();
		for (BMPlayer player : players){
			String hp = player.getHP() + "/" + totalHP;
			HPs.add(hp);
		}
		return HPs;
	}
	public Vector<TreeMap<String,Object>> getResult(){
		Vector<TreeMap<String,Object>> results = new Vector<TreeMap<String,Object>>();
		for (BMPlayer player : players){
			TreeMap<String,Object> result = player.getResult();
			if (player instanceof BMAIPlayer) result.put("username", "AI player " + player.getid());
			results.add(result);
		}
		return results;
	}
	public boolean canMove(int x, int y){
		return board[x][y].canPass();
	}
	public BMNode getNode(int x, int y){
		return board[x][y];
	}
	public void dropBomb(int x, int y, BMPlayer player){
		board[x][y] = new BMBomb(x,y,board,player);
	}
	public void startGame(int type){
		if (numPlayer > 4) numPlayer = 4;
		Vector<BMClient> clients = hs.getClients();
		for (int i = 0; i < clients.size(); i++){
			System.out.println("Client name");
			BMPlayer player = new BMRealPlayer(i,totalHP, clients.get(i).getUserName(), clients.get(i).vip);
			clients.get(i).setPlayer(player);
			player.setSimulation(this);
			players.add(player);
		}
		if (type == start_with_AI){
			for (int i = clients.size(); i < numPlayer; i++){
				BMAIPlayer player = new BMAIPlayer(i,totalHP);
				player.setSimulation(this);
				players.add(player);
			}
		}
		TreeMap<String,Object> map = new TreeMap<String,Object>();
		map.put("type", "start");
		map.put("time", timeLeft);
		map.put("board", getBoard());
		map.put("players", playersInfo());
		hs.sendMapToClients(map);
		startTimer();
		new Thread(new Runnable(){
			@Override
			public void run() {
				while (true){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (checkEnd()) {
						endGame();
						return;
					}
					else BMSimulation.this.sendMove();
				}
			}			
		}).start();
	}
	protected boolean checkEnd() {
		int livePlayer = 0;
		for (BMPlayer player : players) if (!player.hasLost()) livePlayer++;
		if (livePlayer > 1) return false;
		else return true;
	}
	public void endGame() {	
		TreeMap<String,Object> map = new TreeMap<String,Object>();
		map.put("type", "end");
		map.put("result", getResult());
		hs.sendMapToClients(map);
		System.out.println("End game");
		gameOver();
	}
	public void gameOver(){
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hs.endGame();
		for (BMPlayer player : players) player.setLose();
		this.interrupt();
	}
	public Integer[][] getBoard(){
		Integer[][] myBoard = new Integer[16][16];
		for (int i = 0; i < 16; i++){
			for (int j = 0; j < 16; j++){
				myBoard[i][j] = board[i][j].getType();
			}
		}
		return myBoard;
	}
	public ArrayList<BMPlayer> getPlayers(){
		return players;
	}
	public void joinGame(){
		TreeMap<String,Object> map = new TreeMap<String,Object>();
		map.put("type", "join");
		map.put("time", timeLeft);
		map.put("hp", totalHP);
		map.put("players", hs.getNames());
		hs.sendMapToClients(map);
	}
	public ArrayList<BMPlayer> getAllPlayers(){
		return players;
	}
	public Vector<TreeMap<String,Object>> playersInfo(){
		Vector<TreeMap<String, Object>> info = new Vector<TreeMap<String,Object>>();
		for (BMPlayer player : players){
			info.add(player.getInfo());
		}
		return info;
	}
	public void addKill(int id) {
		players.get(id).addKill();
	}
	public void sendMove() {
		TreeMap<String, Object> info = new TreeMap<String,Object>();
		info.put("type", "move");
		info.put("time", timeLeft);
		info.put("board", getBoard());
		info.put("players", playersInfo());
		hs.sendMapToClients(info);
	}
	public int getTime() {
		return timeLeft;
	}
	public void playerQuit(BMPlayer player) {
		players.set(player.getid(), new BMAIPlayer(player));
	}
}
