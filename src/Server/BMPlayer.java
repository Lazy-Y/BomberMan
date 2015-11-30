package Server;

import java.awt.Point;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import node.BMBomb;
import node.BMBombing;
import node.BMNode;
import node.BMNodeItem;
import node.BMTile;
import node.BMWall;

import Utilities.BMItem;
import Utilities.BMMove;
import Utilities.BMNodeType;
import Utilities.BMResult;

public abstract class BMPlayer extends Thread implements Serializable{
//	Store all the information of a player. The functions support all possible actions of the player.
//	Variables:
//		- Point : location
//		- int : speed
//		- int : power
//		- int : coolingTime
//		- int : detonatedTime
//		- Vector<BMItem> : items
//		- int : deaths
//		- int : initialLives
//		- int : HP
//		- int : kills
//		- int : ID
//		- bool : lost
//		- BMSimulation : simulation
	private static final long serialVersionUID = 4139291657328559403L;
	//Move 2 or 4 spaces per second 
	private static final int decreasedSpeed = 2;
	private static final int normalSpeed = 3;
	private static final int increasedSpeed = 6;
	//Bomb blasts 3 or 5 spaces at each direction
	private static final int decreasedPower = 2;
	private static final int normalPower = 3;
	private static final int increasedPower = 5;
	//Wait 10 or 5 seconds before player dropping another bomb 
	private static final int increasedCoolingTime = 8;
	private static final int normalCoolingTime = 4;
	private static final int decreasedCoolingTime = 3;
	//Wait 5 or 3 seconds between a bomb is dropped and it detonates
	private static final int increasedDetonatedTime = 7;
	private static final int normalDetonatedTime = 4;
	private static final int decreasedDetonatedTime = 3;
	//Inclusive small coordinates limit:7, 247
	private static final int smallCoordinateUpperLimit = 1005;
	private static final int smallCoordinateLowerLimit = 16;
	//Inclusive big coordinates limit:0, 15
	private static final int bigCoordinateUpperLimit = 15;
	private static final int bigCoordinateLowerLimit = 0;
	
	protected static final int coordinatesRatio = 64;
	
	protected Point initialLocation;
	protected Point location;
	protected int speed;
	protected int power;
	protected int coolingTime;
	protected int detonatedTime;
	//protected Queue<BMItem> items;
	protected Vector<Integer> items;
	protected int deaths;
	protected int initialHP;
	protected int HP;
	protected int kills;
	protected int ID;
	protected boolean lost;
	protected int playerNumber;
	protected BMSimulation simulation;
	protected int direction;
	
	protected volatile boolean respawning;
	protected volatile boolean cooling;
	
	private int itemCount = 0;
	
	boolean vip = false;
	protected String username = "AI Player";
	
//	Functions:
//		+ BMPlayer(int ID, int initialLives, boolean isVIP)
//		+ setSimulation(BMSimulation simulation)
//		protected killed() : void     //should check at every moves
//		protected getLocation() : Point
//		protected getItemsPossessed() : Vector<BMItem>
//		protected getNumOfItemsAcquired() : int
//		protected getLivesLeft() : int
//		protected getInitialLives() : int
//		protected getNumOfPlayerKilled() : int
//		protected startMove(int move) : void
//		protected getCurrentNode(Point p) : Node
//		protected getSpeed() : int
//		protected getPower() : int
//		protected getCoolingTime() : int
//		protected getDetonatedTime() : int
//		- canMove() : Boolean
	private volatile boolean enableMove = true;
	public BMPlayer(int ID, int initialLives){
		this.ID = ID;
		this.initialHP = initialLives;
		location = new Point(-1, -1);
		setInitialLocation(ID);
		speed = normalSpeed;
		power = normalPower;
		coolingTime = normalCoolingTime;
		detonatedTime = normalDetonatedTime;
		//itemQueue = new LinkedList<BMItem>();
		items = new Vector<Integer>();
		HP = initialLives;
		kills = 0;
		lost = false;
		respawning = false;
		items.add(-1);
		items.add(-1);
		direction = BMMove.face_down; //down
		new Thread(new Runnable(){
			@Override
			public void run() {
				while (!hasLost()){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					BMNode currentNode = simulation.getNode(location.x/coordinatesRatio,  location.y/coordinatesRatio);
					if(currentNode.getType() == BMNodeType.bombing){
						BMPlayer.this.killed(((BMBombing)currentNode).getPlayerID());
					}
				}
			}			
		}).start();
	}
	
	public void setSimulation(BMSimulation simulation){
		this.simulation = simulation;
	}
	
	public void killed(int id){
		if (respawning) return;
		respawning = true;
				HP--;
				if (HP <= 0) lost = true;
				location.x = initialLocation.x;
				location.y = initialLocation.y;
				simulation.addKill(id);
				try{
				Thread.sleep(3000);
				} catch (InterruptedException ie){
					ie.printStackTrace();
				} finally{
//					System.out.println("Recovered " + HP);
					respawning = false;
				}
		//		new Thread(new Runnable(){
//			@Override
//			public void run() {
//				
//			}
//		}).start();
	}
	
	public Point getLocation(){
		return location;
	}
	
	public Vector<Integer> getItemsProcessed(){
		itemCount++;
		Vector<Integer> returnVector = new Vector<Integer>();
		//Object [] array = itemQueue.toArray();
		
//		if (items.size() > 2 || items.size() < 0)
//			System.out.println("Player has " + items.size() + " items. Error in BMPlayer getItemsProcessed.");
		for(int i=0; i< items.size(); i++){
			returnVector.add(items.get(i));
		}
		return returnVector;
	}
	
	public int getNumOfItemsAcquired(){
		return items.size();
	}
	
	public int getHP(){
		return HP;
	}
	public int getInitialLives(){
		return initialHP;
	}
	public int getNumOfPlayerKilled(){
		return kills;
	}
	public int getSpeed(){
		return speed;
	}
	public int getPower(){
		return power;
	}
	public int getCoolingTime(){
		return coolingTime;
	}
	public int getDetonatedTime(){
		return detonatedTime;
	}
	public boolean hasLost(){
		return lost;
	}
	public boolean isRealPlayer(){
		return ID >= 0;
	}
	private void addItem(BMItem item){
		items.set(0, items.get(1));
		items.set(1, item.getValue());
		
		speed = normalSpeed;
		power = normalPower;
		coolingTime = normalCoolingTime;
		detonatedTime = normalDetonatedTime;
		for(int i=0; i<items.size(); i++){
			switch(items.get(i)){
				case BMItem.speedup: speed++; break;
				case BMItem.speeddown: speed--; break;
				case BMItem.powerup: power++; break;
				case BMItem.powerdown: power--; break;
				case BMItem.coolingfast: coolingTime/=2; break;
				case BMItem.coolingslow: coolingTime*=2; break;
				case BMItem.detonatingfast: detonatedTime--; break;
				case BMItem.detonatingslow: detonatedTime++; break;
			}
			if (vip) {
				speed+=2;
				power+=1;
				coolingTime-=1;
				detonatedTime-=1;
			}
		}
								
	
	}
	
	public String getUserName(){
		return username;
	}
	
	public void startMove(int moveType){
		for(int i=0; i<speed; i++){
			if (canMove(moveType)){
//				System.out.println("Move enabled");
				moveHelper(moveType);
			}

		}
	}
	protected void moveHelper(int moveType){
		int initX = location.x;
		int initY = location.y;
		switch(moveType){
			//Stop
			case BMMove.stop: location.setLocation(initX, initY);
					
					break;
			//Up
			case BMMove.up: location.setLocation(initX, initY-1);
					direction = BMMove.face_up;
					break;
			//Down
			case BMMove.down: location.setLocation(initX, initY+1);
					direction = BMMove.face_down;
					break;
			//Left
			case BMMove.left: location.setLocation(initX-1, initY);
					direction = BMMove.face_left;
					break;
			//Right
			case BMMove.right: location.setLocation(initX+1, initY);
					direction = BMMove.face_right;
					break;
			//Drop a bomb
			case BMMove.bomb: simulation.dropBomb(initX/coordinatesRatio, initY/coordinatesRatio, this);
					cooling = true;
					new Thread(new Runnable(){
						@Override
						public void run() {
							try {
								Thread.sleep(1000*coolingTime);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							finally {
//								System.out.println("Cooled");
								cooling = false;
							}
						}
					}).start();
		}
		BMNode nextNode = simulation.getNode(location.x/64, location.y/64);
		if (nextNode instanceof BMNodeItem){
			BMNodeItem itemNode = (BMNodeItem)nextNode;
			int value = itemNode.getValue();
			addItem(new BMItem(value));
			itemNode.getItem();
		}	
	}
	
	public int getid(){return ID;}
	
	protected boolean canMove(int moveType) {
		if (hasLost()) return false;
		if (moveType < 0 || moveType > 5){
//			System.out.println("Error input of moveType. BMPlayer canMove.");
			return false;
		}
		else if (moveType == 0) return true;
		else if (moveType == 5){
			if (cooling) return false;
			BMNode node = simulation.getNode(location.x/coordinatesRatio, location.y/coordinatesRatio);
			if (node instanceof BMBombing || node instanceof BMBomb){
//				System.out.println("cant drop bomb because tile is bomb or bombing");
				return false;
			}
			else return true;
		}
		else{
			int initBigX = location.x/coordinatesRatio;
			int initBigY = location.y/coordinatesRatio;
			int initSmallX = location.x;
			int initSmallY = location.y;
			
			int xthreshold = coordinatesRatio/4;
			int ythreshold = 2*coordinatesRatio/5;
			switch(moveType){
				case BMMove.up: initSmallY-= ythreshold; break;
				case BMMove.down: initSmallY+= ythreshold; break;
				case BMMove.left: initSmallX-= xthreshold; break;
				case BMMove.right: initSmallX+= xthreshold; break;
			}
			//int finalBigX = initBigX;
			//int finalBigY = initBigY;
			int finalSmallX = initSmallX;
			int finalSmallY = initSmallY;
//			System.out.println("X " + finalSmallX + " Y " + finalSmallY);
			if (pointInSmallBounds(new Point(finalSmallX, finalSmallY))){
				BMNode nextNode = simulation.getNode(finalSmallX/coordinatesRatio, finalSmallY/coordinatesRatio);
				
				BMNode currNode = simulation.getNode(initBigX, initBigY);
				if (currNode instanceof BMBomb) return true;
				else if (nextNode instanceof BMWall || nextNode instanceof BMTile || nextNode instanceof BMBomb) return false;
				else {
					
					int nextBigX = initBigX;
					int nextBigY = initBigY;
					switch(moveType){
						case BMMove.up: nextBigY--; break;
						case BMMove.down: nextBigY++; break;
						case BMMove.left: nextBigX--; break;
						case BMMove.right: nextBigX++; break;
					}
					if (pointInBigBounds(nextBigX, nextBigY)){
//						System.out.println("helper case");
						if (moveType/3 == 0){
							int leftBigX = nextBigX-1;
							int leftBigY = nextBigY;
							if(!canMoveHelper(leftBigX, leftBigY,BMMove.left,xthreshold)){
//								System.out.println("helper");
								return false;
							}
							int rightBigX = nextBigX+1;
							int rightBigY = nextBigY;
							if(!canMoveHelper(rightBigX, rightBigY,BMMove.right, xthreshold)){
//								System.out.println("helper");
								return false;
							}
						}
						else{
							int upBigX = nextBigX;
							int upBigY = nextBigY-1;
							if(!canMoveHelper(upBigX, upBigY,BMMove.up, ythreshold)){
//								System.out.println("helper");
								return false;
							}
							int downBigX = nextBigX;
							int downBigY = nextBigY+1;
							if(!canMoveHelper(downBigX, downBigY, BMMove.down, ythreshold)){
//								System.out.println("helper");
								return false;
							}
								
						}
					}
					
					return true;
				}
			}
			else return false;
			//System.out.println("Error in BMPlayer canMove");			
		}
	}
	
	private boolean canMoveHelper(int BigX, int BigY, int direction, int threshold){
		if(pointInBigBounds(BigX, BigY)){
			//BMNode nextNode = simulation.getNode(BigX, BigY);
			//int nodetype = (int)nextNode.getId();
			//if(nodetype == BMNodeType.tile || nodetype == BMNodeType.wall || nodetype == BMNodeType.bomb || nodetype == BMNodeType.bombing){
				int SmallX = location.x/coordinatesRatio;
				int SmallY = location.y/coordinatesRatio;
				//threshold /= 2;
				switch(direction){
					case BMMove.up: SmallY -= threshold; 
									SmallX = BigX/coordinatesRatio + coordinatesRatio/2;
									break;
					case BMMove.down: SmallY += threshold; 
									SmallX = BigX/coordinatesRatio + coordinatesRatio/2;
									break;
					case BMMove.left: SmallX -= threshold; 
									SmallY = BigY/coordinatesRatio + coordinatesRatio/2;
									break;
					case BMMove.right: SmallX += threshold; 
									SmallY = BigY/coordinatesRatio + coordinatesRatio/2;	
									break;
				}
				if(pointInSmallBounds(new Point(SmallX, SmallY))){
					BMNode node = simulation.getNode(SmallX/coordinatesRatio, SmallY/coordinatesRatio);
					int nodetype2 = (int)node.getId();
					if(nodetype2 == BMNodeType.tile || nodetype2 == BMNodeType.wall || nodetype2 == BMNodeType.bomb || nodetype2 == BMNodeType.bombing){
						return false;
					}
				}
				else{
					//System.out.println("Should not happen, logic error");
				}
		//	} 
			
		}
		return true;
	}
	//Note that Point stores small coordinates. Use ints for big coordinates
	protected boolean pointInSmallBounds(Point p){
		return inSmallBounds(p.x) && inSmallBounds(p.y);
	}
	private boolean inSmallBounds(int x){
		return x >= smallCoordinateLowerLimit && x <= smallCoordinateUpperLimit; 
	}
	protected boolean pointInBigBounds(int bigX, int bigY){
		return inBigBounds(bigX) && inBigBounds(bigY); 
	}
	private boolean inBigBounds(int bigC){
		return bigC >= bigCoordinateLowerLimit && bigC <= bigCoordinateUpperLimit;
	}
	
	
	public void setInitialLocation(int playerNumber){
//		if (playerNumber > 3 || playerNumber < 0) System.out.println("Input out of bounds, needs 0 to 3. In BMPlayer setInitialLocation.");
		switch(playerNumber){
		
			case 0:
				location.x = smallCoordinateLowerLimit;
				location.y = smallCoordinateLowerLimit;
				break;
			case 1:
				location.x = smallCoordinateUpperLimit;
				location.y = smallCoordinateUpperLimit;
				break;
			case 2:
				location.x = smallCoordinateUpperLimit;
				location.y = smallCoordinateLowerLimit;
				break;
			case 3:
				location.x = smallCoordinateLowerLimit;
				location.y = smallCoordinateUpperLimit;
				break;
		}
		initialLocation = new Point(location.x, location.y);
		this.playerNumber = playerNumber;
	}
	
	private int calculatePoints(){
		return 10*kills - 5*(initialHP-HP);
	}
	public TreeMap<String,Object> getResult() {
		TreeMap<String,Object> resultMap = new TreeMap<String,Object>();
		resultMap.put("ID", ID);
		resultMap.put("points", new Integer(calculatePoints()));
		resultMap.put("kill", kills);
		resultMap.put("death", new Integer(initialHP-HP));
		resultMap.put("item", itemCount);
		resultMap.put("username", this.getUserName());
		System.out.println("Result username " + this.getUserName());
		return resultMap;
	}

	
	public TreeMap<String,Object> getInfo(){
		TreeMap<String,Object> info = new TreeMap<String,Object>();
		info.put("username",getUserName());
		info.put("posX", location.x);
		info.put("posY", location.y);
		info.put("hp", HP);
		info.put("speed", speed);
		info.put("power", power);
		info.put("direction", direction);
		info.put("item1", items.get(0));
		info.put("item2", items.get(1));
		info.put("coolingTime", coolingTime);
		info.put("denotated", this.detonatedTime);
		int time = simulation.getTime();
		String timeLeft = "";
		if (time%60 > 9 ) timeLeft = (Integer.toString(time/60)) + ":" + (Integer.toString(time%60));
		else timeLeft = (Integer.toString(time/60)) + ":0" + (Integer.toString(time%60));
//		System.out.println("time " + time + " left " + timeLeft);
		info.put("time", timeLeft);
//		System.out.println("Info " +info);
		return info;
	}
	public void addKill() {
		kills++;
	}

	public void setLose() {
		lost = true;
	}
}
