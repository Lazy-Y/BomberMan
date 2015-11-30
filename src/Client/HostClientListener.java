package Client;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.NotActiveException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Dictionary;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.table.TableModel;
import Server.BMPlayer;
public class HostClientListener  extends Thread{
	private Socket mSocket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	public String username;
	private BMClientPanel clientpanel;
	
	public HostClientListener(BMClientPanel clientpanel, String ip, int host) {		
		try
		{
			mSocket = new Socket(ip,host);
			System.out.println("Hello");
			this.clientpanel = clientpanel;		
			System.out.println("world");
			boolean socketReady = initializeVariables();
			System.out.println("Fuck");
			if (socketReady) {
				start();
				System.out.println(".....");
			}
		}
		catch(IOException ioe)
		{
			System.out.println("IOE in SorryClient constructor: " + ioe.getMessage());
		}
	
	}
	
	
	private boolean initializeVariables() {
		try {
			oos = new ObjectOutputStream(mSocket.getOutputStream());
			System.out.println("Break321");
			ois = new ObjectInputStream(mSocket.getInputStream());
			System.out.println("Break123");
		} catch (IOException ioe) {
//			System.out.println(ioe);
			ioe.printStackTrace();
			return false;
		}
		return true;
	}
	
	 public void sendMove(int key ) {
		 TreeMap<String, Object> tempMap = new TreeMap<String , Object>();
		 tempMap.put("type", "move");
		 tempMap.put("move", key);
	        try {
	        		oos.writeObject(tempMap);
				oos.flush();
	        } catch (ArrayIndexOutOfBoundsException e) {
	        		e.printStackTrace();
	        } catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e){
//				e.printStackTrace();
			}
	    }
	 public void sendMsg(String msg) {
		 TreeMap<String, Object> tempMap = new TreeMap<String , Object>();
		 tempMap.put("type", "msg");
		 tempMap.put("content", msg);		 
	        try {
	        	oos.writeObject(tempMap);
				oos.flush();
	        } catch (Exception e) {
	            System.out.println(e.toString());
	        }
	    }
	 public void sendJoin(String username) {
		 TreeMap<String, Object> tempMap = new TreeMap<String , Object>();
		 tempMap.put("type", "join");
		 tempMap.put("username", username);	
		 tempMap.put("vip", vipClass.vip);
		 System.out.println("This user's name " + username);
	        try {
	        	oos.writeObject(tempMap);
				oos.flush();
	        } catch (Exception e) {
	            System.out.println(e.toString());
	        }
	    }
	 
	 public void run() {
		 	System.gc();
		 	try {
				while(true) {
					try {
					
						// in case the server sends another factory to us
						
						Object obj = ois.readObject();
						if (!(obj instanceof TreeMap<?,?>)) continue;
						TreeMap<String,Object>map = (TreeMap<String,Object>)obj;
	
						if (((String)map.get("type")).equals("join")){
							int time = (int )map.get("time");
							int hp = (int) map.get("hp");
//							System.out.println( map.get("players"));
							Vector<String> player = (Vector<String>) map.get("players");				
							
							clientpanel.set_join(player, hp, time);
						}
						else if (((String)map.get("type")).equals("error")){
							String errMsg= (String) map.get("errMsg");
						}
						else if (((String)map.get("type")).equals("start")){
							//receive { type = start, board = 2D array of integers represent the board, time = xxx, players =  (Vector<Dictionary>) [ { username = xxx, posX =  x, posY = y, hp = xxx, speed = xxx, power = xxx, item1 = xxx, item2 = xxx } ] }
							Integer [][] board = (Integer[][]) map.get("board");
							int time = (int) map.get("time");
							Vector<TreeMap<String, Object>> players =  (Vector<TreeMap<String, Object>>) map.get("players");
							System.out.println(players);
							clientpanel.set_start(board, time, players);
						}
						else if (((String)map.get("type")).equals("move")){
	//						System.out.println("Direction " + ((Vector)map.get("players")).get(0));
							int time = (int) map.get("time");
							Vector<TreeMap<String, Object>> players =  (Vector<TreeMap<String, Object>>) map.get("players");
							Integer[][] board = (Integer[][])map.get("board");
							clientpanel.boardPanel.set_move(time, players,board);
						
						}
						else if (((String)map.get("type")).equals("msg")){
							String username = (String)map.get("username");
							String content = (String)map.get("content");
							BMBoardPanel.set_chat_text (username, content);
						}
						else if (((String)map.get("type")).equals("end")){
							
							Vector<TreeMap<String,Object>> result = (Vector<TreeMap<String,Object>>)map.get("result");
							
							clientpanel.boardPanel.Gameover(result);
							clientpanel.game_over(result);
							return;
						}
					}catch (ClassNotFoundException cnfe) {
						System.out.println(cnfe);
						cnfe.printStackTrace();
					} catch (ClassCastException e){
						e.printStackTrace();
					} catch (ArrayStoreException e){
						e.printStackTrace();
					}
				} 
		 	}
			catch (IOException ioe) {
				//mFClientGUI.addMessage(Constants.serverCommunicationFailed);
				System.out.println("serverCommunicationFailed");
				ioe.printStackTrace();
				clientpanel.server_quit();
				
			} 
	 }


	public void close() {
		if (mSocket!=null){
			try {
				mSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}