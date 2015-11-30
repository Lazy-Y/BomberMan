package Server;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

import Utilities.BMMove;
import Utilities.BMNodeType;
import node.BMBomb;
import node.BMBombing;
import node.BMNode;
import node.BMNodeItem;
import node.BMTile;
import node.BMWall;

public class BMAIPlayer extends BMPlayer {
//	The AI players of the game. Besides all the functions of BMPlayer, AIPlayer has its own algorithm to make the best move.
//	Function:
//		- run() : void
//		- aiMove() : void
//		- aiGetBestTry : int
//		- aiTry(int move) : int
	private static final int potentialBombingMultiplier = 10;
	public BMAIPlayer(int id, int initialLives){
		super(id, initialLives);
		this.start();
	}
	
	public BMAIPlayer(BMPlayer bmPlayer) {
		super(bmPlayer.ID, bmPlayer.initialHP);
		this.location = new Point(bmPlayer.location);
		speed = bmPlayer.speed;
		power = bmPlayer.power;
		coolingTime = bmPlayer.coolingTime;
		detonatedTime = bmPlayer.detonatedTime;
		HP = bmPlayer.HP;
		kills = bmPlayer.kills;
		lost = bmPlayer.lost;
		respawning = bmPlayer.respawning;
		items.clear();
		items.add(bmPlayer.items.get(0));
		items.add(bmPlayer.items.get(1));
		direction = bmPlayer.direction;	
		this.setSimulation(bmPlayer.simulation);
		this.start();
	}

	@Override
	public void run(){
		while (!hasLost()){
			try {
				Queue<Point> path = null;
				Vector<Point> reachable = getReachablePoints();
				int [][] board = searchSave();
				Point p = null;
				Random r = new Random();
				while (path == null){
					if (reachable.isEmpty()) break;
					int index = Math.abs(r.nextInt())%reachable.size();
					p = reachable.get(index);
					reachable.remove(index);
					if (board[p.x][p.y] == safe){ 
						path = findBFSPath(p);
						break;
					}
				}
	//			if (p != null) System.out.println("Safe point " + p);
	//			if (p == null) System.out.println("Cannot find safe point");
				if (path == null) continue;
				while (!path.isEmpty()){
					board = this.searchSave();
					if (board[p.x][p.y] == unsafe || !AIMove(path.peek())) break;
					path.remove(); 
				}
			}
			catch (InterruptedException e){
				System.out.println("Exception " + e.getMessage());
			}
		}
	}
	
	public Vector<Point> getReachablePoints(){
		Vector<Point> results = new Vector<Point>();
		Set<Point> visitedPoints = new HashSet<Point>();
		Queue<Point> pointQueue = new LinkedList<Point>();
		pointQueue.add(currPoint());
		visitedPoints.add(currPoint());
		while(!pointQueue.isEmpty()){
			Point p = pointQueue.poll();
			//Up
			Point upPoint = new Point(p.x, p.y-1);
			if(gridInBounds(new Point(p.x, p.y-1))){
				BMNode upNode = simulation.getNode(p.x, p.y-1);
				if(upNode instanceof BMWall || upNode instanceof BMTile || upNode instanceof BMBomb || upNode instanceof BMBombing);
				else {
					if (!visitedPoints.contains(upPoint)){
						pointQueue.add(new Point(p.x, p.y-1));
						results.add(upPoint);
					}
					
				}
			}
			visitedPoints.add(upPoint);
			
			//Down
			Point downPoint = new Point(p.x, p.y+1);
			if(gridInBounds(new Point(p.x, p.y+1))){
				BMNode downNode = simulation.getNode(p.x, p.y+1);
				if(downNode instanceof BMWall || downNode instanceof BMTile || downNode instanceof BMBomb || downNode instanceof BMBombing);
				else {
					if (!visitedPoints.contains(downPoint)){
						pointQueue.add(new Point(p.x, p.y+1));
						results.add(downPoint);
					}
				}
			}
			visitedPoints.add(downPoint);
			
			//Left
			Point leftPoint = new Point(p.x-1, p.y);
			if(gridInBounds(new Point(p.x-1, p.y))){
				BMNode leftNode = simulation.getNode(p.x-1, p.y);
				if(leftNode instanceof BMWall || leftNode instanceof BMTile || leftNode instanceof BMBomb || leftNode instanceof BMBombing);
				else {
					if (!visitedPoints.contains(leftPoint)){
						pointQueue.add(new Point(p.x-1, p.y));
						results.add(leftPoint);
					}
				}
			}
			visitedPoints.add(leftPoint);
			//Right
			Point rightPoint = new Point(p.x+1, p.y);
			if(gridInBounds(new Point(p.x+1, p.y))){
				BMNode rightNode = simulation.getNode(p.x+1, p.y);
				if(rightNode instanceof BMWall || rightNode instanceof BMTile || rightNode instanceof BMBomb || rightNode instanceof BMBombing);
				else {
					if (!visitedPoints.contains(rightPoint)){
						pointQueue.add(new Point(p.x+1, p.y));
						results.add(rightPoint);
					}
				}
			}
			visitedPoints.add(rightPoint);
		}
		return results;
	}
	
	private Queue<Point> findBFSPath(Point point){
		
//		System.out.println("Target " + point + " curr point " + currPoint());
		if (!pointInBigBounds(point.x, point.y)) return null;
		if (point.equals(currPoint())) return null;
		Queue<Point> queue = new LinkedList<Point>();
		Set<Point> visitedPoints = new HashSet<Point>();
		Vector<Point> pointQueue = new Vector<Point>();
		Vector<Integer> parentArray = new Vector<Integer>();
		boolean pointFound = false;
		visitedPoints.add(currPoint());
		pointQueue.add(currPoint());
		parentArray.add(-1);
		int endptr = 0;
		int parentptr = -1;
		while(!pointFound && endptr > parentptr){
			parentptr++;
			Point p = pointQueue.get(parentptr);
			
			
//			System.out.println("Searching " + p);
			//Up
			if(gridInBounds(new Point(p.x, p.y-1))){
				BMNode upNode = simulation.getNode(p.x, p.y-1);
				if(upNode instanceof BMWall || upNode instanceof BMTile || upNode instanceof BMBomb || upNode instanceof BMBombing);
				else {
					Point newpoint = new Point(p.x, p.y-1);
					if (!visitedPoints.contains(newpoint)){
						pointQueue.add(new Point(p.x, p.y-1));
						parentArray.add(parentptr);
						visitedPoints.add(newpoint);
						endptr++;
						if(newpoint.equals(point)){
							pointFound = true;
							break;
						}
					}
					
				}
			}
			//Down
			if(gridInBounds(new Point(p.x, p.y+1))){
				BMNode downNode = simulation.getNode(p.x, p.y+1);
				if(downNode instanceof BMWall || downNode instanceof BMTile || downNode instanceof BMBomb || downNode instanceof BMBombing);
				else {
					Point newpoint = new Point(p.x, p.y+1);
					if (!visitedPoints.contains(newpoint)){
						pointQueue.add(new Point(p.x, p.y+1));
						parentArray.add(parentptr);
						visitedPoints.add(newpoint);
						endptr++;
						if(newpoint.equals(point)){
							pointFound = true;
							break;
						}

					}
				}
			}
			//Left
			if(gridInBounds(new Point(p.x-1, p.y))){
				BMNode leftNode = simulation.getNode(p.x-1, p.y);
				if(leftNode instanceof BMWall || leftNode instanceof BMTile || leftNode instanceof BMBomb || leftNode instanceof BMBombing);
				else {
					Point newpoint = new Point(p.x-1, p.y);
					if (!visitedPoints.contains(newpoint)){
						pointQueue.add(new Point(p.x-1, p.y));
						parentArray.add(parentptr);
						visitedPoints.add(newpoint);
						endptr++;
						if(newpoint.equals(point)){
							pointFound = true;
							break;
						}

					}
				}
			}
			//Right
			if(gridInBounds(new Point(p.x+1, p.y))){
				BMNode rightNode = simulation.getNode(p.x+1, p.y);
				if(rightNode instanceof BMWall || rightNode instanceof BMTile || rightNode instanceof BMBomb || rightNode instanceof BMBombing);
				else {
					
					Point newpoint = new Point(p.x+1, p.y);
					if (!visitedPoints.contains(newpoint)){
						pointQueue.add(new Point(p.x+1, p.y));
						parentArray.add(parentptr);
						visitedPoints.add(newpoint);
						endptr++;
						if(newpoint.equals(point)){
							pointFound = true;
							break;
						}

					}
				}
			}
		}
		if (!pointFound) return null;
//		System.out.println("Found path to point");
		Vector<Point> path = new Vector<Point>();
		path.add(point);
		while(parentptr != 0){
			path.add(pointQueue.get(parentptr));
			parentptr = parentArray.get(parentptr);
		}
		for(int i=path.size()-1; i>=0; i--){
			queue.add(path.get(i));
		}
		return queue;
	}
	
	
	
	
//	public void run(){
//		int [] moveRatings = new int[5];
//		Random rand = new Random();
////		System.out.println("Start running");
//		while(!hasLost()){
//			for(int i=0; i<5; i++) {
//				moveRatings[i] = 0;
//				if(canMove(i)) moveRatings[i] = calculateMoveRatings(i);
//				else moveRatings[i] = Integer.MIN_VALUE;
//			}
//			int maxindex = 0;
//			int maxvalue = Integer.MIN_VALUE;
//			for(int i=0; i<5; i++){
//				if(maxvalue < moveRatings[i]){
//					maxvalue = moveRatings[i];
//					maxindex = i;
//				}
//			}
////			System.out.println("Player " + ID + " moves " + maxindex);
//			//Stay
//			if (maxindex == 0){
//				/*try{
//					//Thread.sleep(10);
//					
//				} catch(InterruptedException ie){
//					ie.printStackTrace();
//				}*/
//				int randomMove = rand.nextInt(5);
//				
//				if (canMove(randomMove)) maxindex = randomMove;
//			}
//			//Move
//			
//				for(int i=0; i<coordinatesRatio/speed; i++){
//					startMove(maxindex);
//					try{
//						Thread.sleep(10);
//					} catch(InterruptedException ie){
//						ie.printStackTrace();
//					}
//				}
//			
//			//Drop bomb
//			if(!cooling){
//				startMove(5);
//			}
//			
//			for(int i=0; i<5; i++)
//				moveRatings[i] = Integer.MIN_VALUE;
//			
//		}
//	}

//	private int calculateMoveRatings(int i) {
//		int rating = 0;
//		int initX = location.x/coordinatesRatio;
//		int initY = location.y/coordinatesRatio;
//		switch(i){
//			case 1: initY--; break;
//			case 2: initY++; break;
//			case 3: initX--; break;
//			case 4: initX++; break;
//		}
//		if(!pointInBigBounds(initX, initY)) return 0;
//		BMNode nextNode = simulation.getNode(initX, initY);
//		if (nextNode instanceof BMBombing) rating -= 100;
//		else rating -= potentialBombing(new Point(initX, initY));
//		rating += closestItem(new Point(initX, initY));
//		//rating += closestPlayer(new Point(initX, initY));
//		rating -= closestBomb(new Point(initX, initY))*5;
//		return rating;
//	}

	private int closestItem(Point point) {
		int distance = -1;
		Set<Point> visitedPoints = new HashSet<Point>();
		Queue<Point> pointQueue = new LinkedList<Point>();
		Vector<Integer> stepsArray = new Vector<Integer>();
		int steps = 0;
		boolean itemFound = false;
		pointQueue.add(point);
		stepsArray.add(steps);
		int count = 0;
		while(!itemFound && !pointQueue.isEmpty()){
			Point p = pointQueue.poll();
			BMNode node = simulation.getNode(p.x, p.y);
			
			if (node instanceof BMNodeItem){
				itemFound = true;
				distance = stepsArray.get(count);
			}
			else{
				//Up
				if(gridInBounds(new Point(p.x, p.y-1))){
					BMNode upNode = simulation.getNode(p.x, p.y-1);
					if(upNode instanceof BMWall || upNode instanceof BMTile);
					else {
						Point newpoint = new Point(p.x, p.y-1);
						if (!visitedPoints.contains(newpoint)){
							pointQueue.add(new Point(p.x, p.y-1));
							stepsArray.add(stepsArray.get(count)+1);
							visitedPoints.add(newpoint);
						}
						
					}
				}
				//Down
				if(gridInBounds(new Point(p.x, p.y+1))){
					BMNode downNode = simulation.getNode(p.x, p.y+1);
					if(downNode instanceof BMWall || downNode instanceof BMTile);
					else {
						Point newpoint = new Point(p.x, p.y+1);
						if (!visitedPoints.contains(newpoint)){
							pointQueue.add(new Point(p.x, p.y+1));
							stepsArray.add(stepsArray.get(count)+1);
							visitedPoints.add(newpoint);
						}
					}
				}
				//Left
				if(gridInBounds(new Point(p.x-1, p.y))){
					BMNode leftNode = simulation.getNode(p.x-1, p.y);
					if(leftNode instanceof BMWall || leftNode instanceof BMTile);
					else {
						Point newpoint = new Point(p.x-1, p.y);
						if (!visitedPoints.contains(newpoint)){
							pointQueue.add(new Point(p.x-1, p.y));
							stepsArray.add(stepsArray.get(count)+1);
							visitedPoints.add(newpoint);
						}
					}
				}
				//Right
				if(gridInBounds(new Point(p.x+1, p.y))){
					BMNode rightNode = simulation.getNode(p.x++, p.y);
					if(rightNode instanceof BMWall || rightNode instanceof BMTile);
					else {
						
						Point newpoint = new Point(p.x+1, p.y);
						if (!visitedPoints.contains(newpoint)){
							pointQueue.add(new Point(p.x++, p.y));
							stepsArray.add(stepsArray.get(count)+1);
							visitedPoints.add(newpoint);
						}
					}
				}
				
			}
			count++;
		}
		if (itemFound){
			int result = (int)Math.pow((16+16-distance), 2);
			return result;
		}
		else return 0;
	}
	
//	private int closestPlayer(Point point){
//		ArrayList<BMPlayer> players = simulation.getAllPlayers();
//		Vector<Point> playerPoints = new Vector<Point>();
//		for (BMPlayer player : players){
//			Point tmp = player.getLocation();
//			playerPoints.add(new Point(tmp.x/16, tmp.y/16));
//		}
//		
//		int distance = -1;
//		Set<Point> visitedPoints = new HashSet<Point>();
//		Queue<Point> pointQueue = new LinkedList<Point>();
//		Vector<Integer> stepsArray = new Vector<Integer>();
//		int steps = 0;
//		boolean playerFound = false;
//		pointQueue.add(point);
//		stepsArray.add(steps);
//		int count = 0;
//		while(!playerFound && !pointQueue.isEmpty()){
//			Point p = pointQueue.poll();
//			BMNode node = simulation.getNode(p.x, p.y);
//			
//			if (hasPlayer(playerPoints, p)){
//				playerFound = true;
//				distance = stepsArray.get(count);
//			}
//			else{
//				//Up
//				if(gridInBounds(new Point(p.x, p.y-1))){
//					BMNode upNode = simulation.getNode(p.x, p.y-1);
//					if(upNode instanceof BMWall || upNode instanceof BMTile);
//					else {
//						Point newpoint = new Point(p.x, p.y-1);
//						if (!visitedPoints.contains(newpoint)){
//							pointQueue.add(new Point(p.x, p.y-1));
//							stepsArray.add(stepsArray.get(count)+1);
//							visitedPoints.add(newpoint);
//						}
//					}
//				}
//				//Down
//				if(gridInBounds(new Point(p.x, p.y+1))){
//					BMNode downNode = simulation.getNode(p.x, p.y+1);
//					if(downNode instanceof BMWall || downNode instanceof BMTile);
//					else {
//						Point newpoint = new Point(p.x, p.y+1);
//						if (!visitedPoints.contains(newpoint)){
//							pointQueue.add(new Point(p.x, p.y+1));
//							stepsArray.add(stepsArray.get(count)+1);
//							visitedPoints.add(newpoint);
//						}
//					}
//				}
//				//Left
//				if(gridInBounds(new Point(p.x-1, p.y))){
//					BMNode leftNode = simulation.getNode(p.x-1, p.y);
//					if(leftNode instanceof BMWall || leftNode instanceof BMTile);
//					else {
//						Point newpoint = new Point(p.x-1, p.y);
//						if (!visitedPoints.contains(newpoint)){
//							pointQueue.add(new Point(p.x-1, p.y));
//							stepsArray.add(stepsArray.get(count)+1);
//							visitedPoints.add(newpoint);
//						}
//					}
//				}
//				//Right
//				if(gridInBounds(new Point(p.x+1, p.y))){
//					BMNode rightNode = simulation.getNode(p.x+1, p.y);
//					if(rightNode instanceof BMWall || rightNode instanceof BMTile);
//					else {
//						Point newpoint = new Point(p.x+1, p.y);
//						if (!visitedPoints.contains(newpoint)){
//							pointQueue.add(new Point(p.x+1, p.y));
//							stepsArray.add(stepsArray.get(count)+1);
//							visitedPoints.add(newpoint);
//						}
//					}
//				}
//				
//			}
//			count++;
//		}
//		int result = (int)Math.pow((16+16-distance), 2);
//		return result;
//	}
	
//	private int closestBomb(Point point){
//		/*Vector<BMPlayer> players = simulation.getAllPlayers();
//		Vector<Point> playerPoints = new Vector<Point>();
//		for (BMPlayer player : players){
//			Point tmp = player.getLocation();
//			playerPoints.add(new Point(tmp.x/16, tmp.y/16));
//		}*/
//		
//		int distance = -1;
//		Set<Point> visitedPoints = new HashSet<Point>();
//		Queue<Point> pointQueue = new LinkedList<Point>();
//		Vector<Integer> stepsArray = new Vector<Integer>();
//		int steps = 0;
//		boolean bombFound = false;
//		pointQueue.add(point);
//		stepsArray.add(steps);
//		int count = 0;
//		while(!bombFound && !pointQueue.isEmpty()){
//			Point p = pointQueue.poll();
//			BMNode node = simulation.getNode(p.x, p.y);
//			
//			if (node instanceof BMBomb || node instanceof BMBombing){
//				bombFound = true;
//				distance = stepsArray.get(count);
//			}
//			else{
//				//Up
//				if(gridInBounds(new Point(p.x, p.y-1))){
//					BMNode upNode = simulation.getNode(p.x, p.y-1);
//					if(upNode instanceof BMWall || upNode instanceof BMTile);
//					else {
//						Point newpoint = new Point(p.x, p.y-1);
//						if (!visitedPoints.contains(newpoint)){
//							pointQueue.add(new Point(p.x, p.y-1));
//							stepsArray.add(stepsArray.get(count)+1);
//							visitedPoints.add(newpoint);
//						}
//					}
//				}
//				//Down
//				if(gridInBounds(new Point(p.x, p.y+1))){
//					BMNode downNode = simulation.getNode(p.x, p.y+1);
//					if(downNode instanceof BMWall || downNode instanceof BMTile);
//					else {
//						Point newpoint = new Point(p.x, p.y+1);
//						if (!visitedPoints.contains(newpoint)){
//							pointQueue.add(new Point(p.x, p.y+1));
//							stepsArray.add(stepsArray.get(count)+1);
//							visitedPoints.add(newpoint);
//						}
//					}
//				}
//				//Left
//				if(gridInBounds(new Point(p.x-1, p.y))){
//					BMNode leftNode = simulation.getNode(p.x-1, p.y);
//					if(leftNode instanceof BMWall || leftNode instanceof BMTile);
//					else {
//						Point newpoint = new Point(p.x-1, p.y);
//						if (!visitedPoints.contains(newpoint)){
//							pointQueue.add(new Point(p.x-1, p.y));
//							stepsArray.add(stepsArray.get(count)+1);
//							visitedPoints.add(newpoint);
//						}
//					}
//				}
//				//Right
//				if(gridInBounds(new Point(p.x+1, p.y))){
//					BMNode rightNode = simulation.getNode(p.x++, p.y);
//					if(rightNode instanceof BMWall || rightNode instanceof BMTile);
//					else {
//						Point newpoint = new Point(p.x+1, p.y);
//						if (!visitedPoints.contains(newpoint)){
//							pointQueue.add(new Point(p.x++, p.y));
//							stepsArray.add(stepsArray.get(count)+1);
//							visitedPoints.add(newpoint);
//						}
//					}
//				}
//				
//			}
//			count++;
//		}
//		int result = 0;
//		if(bombFound)
//			result = (int)Math.pow((16+16-distance), 2);
//		
//		return result;
//	}
//
//	private boolean hasPlayer(Vector<Point> v, Point p){
//		for (Point pt : v){
//			if (pt.x == p.x && pt.y == p.y) return true;
//		}
//		return false;
//	}

//	private int potentialBombing(Point point) {
//		int sum = 0;
//		BMNode node = simulation.getNode(point.x, point.y);
//		if (node instanceof BMBomb) sum += ((BMBomb)node).getPower()*potentialBombingMultiplier;
//		
//		int upX = point.x;
//		int upY = point.y;
//		for (int i=1; i<6; i++){
//			upY--;
//			if (upY >= 0){
//				BMNode upNode = simulation.getNode(upX, upY);
//				if (upNode instanceof BMTile ||  upNode instanceof BMWall) break;
//				else if (upNode instanceof BMBomb){
//					int power = ((BMBomb)upNode).getPower();
//					if (i <= power){
//						sum += (power-i)*potentialBombingMultiplier;
//					}
//				}
//			} else break;
//		}
//		
//		int downX = point.x;
//		int downY = point.y;
//		for (int i=1; i<6; i++){
//			downY++;
//			if (downY <= 15){
//				BMNode downNode = simulation.getNode(downX, downY);
//				if (downNode instanceof BMTile || downNode instanceof BMWall) break;
//				else if (downNode instanceof BMBomb){
//					int power = ((BMBomb)downNode).getPower();
//					if (i <= power){
//						sum += (power-i)*potentialBombingMultiplier;
//					}
//				}
//			} else break;
//		}
//		
//		int leftX = point.x;
//		int leftY = point.y;
//		for (int i=1; i<6; i++){
//			leftX--;
//			if (leftX >= 0){
//				BMNode leftNode = simulation.getNode(leftX, leftY);
//				if (leftNode instanceof BMTile || leftNode instanceof BMWall) break;
//				else if (leftNode instanceof BMBomb){
//					int power = ((BMBomb)leftNode).getPower();
//					if (i <= power){
//						sum += (power-i)*potentialBombingMultiplier;
//					}
//				}
//			} else break;
//		}
//		
//		int rightX = point.x;
//		int rightY = point.y;
//		for (int i=1; i<6; i++){
//			leftX++;
//			if (leftX <= 15){
//				BMNode rightNode = simulation.getNode(rightX, rightY);
//				if (rightNode instanceof BMTile || rightNode instanceof BMWall) break;
//				else if (rightNode instanceof BMBomb){
//					int power = ((BMBomb)rightNode).getPower();
//					if (i <= power){
//						sum += (power-i)*potentialBombingMultiplier;
//					}
//				}
//			} else break;
//		}
//		return sum;
//	}
	private boolean gridInBounds(Point p){
		return p.x >= 0 && p.x <= 15 && p.y >= 0 && p.y <= 15;
	}
	
	
	public final static int safe = 0;
	public final static int unsafe = 1;
	
	public int[][] searchSave(){
		int board[][] = new int [16][16];
		for (int i = 0; i < 16; i++){
			for (int j = 0; j < 16; j++){
				board[i][j] = -1;
			}
		}
		for (int i = 0; i < 16; i++){
			for (int j = 0; j < 16; j++){
				int type = simulation.getNode(i, j).getType();
				if (type == BMNodeType.bombing
						|| type == BMNodeType.wall || type == BMNodeType.tile){
					type = unsafe;
				}
				else if (type == BMNodeType.bomb){
					for (int k = 1; k < 5; k++){
						if (i+k < 16) board[i+k][j] = unsafe;
						if (i-k >= 0) board[i-k][j] = unsafe;
						if (j+k < 16) board[i][j+k] = unsafe;
						if (j-k >= 0) board[i][j-k] = unsafe;
					}
					type = unsafe;
				}
				else type = safe;
				if (board[i][j] == -1) board[i][j] = type;
			}
		}
		return board;
	}
	
	public Point currPoint(){
		return new Point(location.x/coordinatesRatio,location.y/coordinatesRatio);
	}
	
	public boolean AIMove(Point p) throws InterruptedException{
		int moveType = 0;
		Point curr = currPoint();
		if (p.x > curr.x) moveType = BMMove.right;
		else if (p.x < curr.x) moveType = BMMove.left;
		else if (p.y > curr.y) moveType = BMMove.down;
		else if (p.y < curr.y) moveType = BMMove.up;

		if (canMove(5)){
			startMove(BMMove.bomb);
			Thread.sleep(10);
		}
		while (Math.abs(p.x*coordinatesRatio-location.x) > 30 ||
				Math.abs(p.y*coordinatesRatio-location.y) > 30){
//		while (!p.equals(currPoint())){
			if (!canMove(moveType)) return false;
			startMove(moveType);
//			System.out.println("Move Type " + moveType);
			Thread.sleep(10);
		}
		return true;
	}
}
