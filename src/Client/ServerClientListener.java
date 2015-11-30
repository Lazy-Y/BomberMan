package Client;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Dictionary;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.table.TableModel;


public class ServerClientListener  extends Thread{
	private Socket mSocket;
	private ObjectOutputStream oos;

	private ObjectInputStream ois;
	private PrintWriter pw;
	public String username;
	private BMClientPanel clientpanel;
	//private Card card;

	//private FactoryClientGUI mFClientGUI;
	
	public ServerClientListener(BMClientPanel clientpanel, String ip, int host) {
		try
		{
			mSocket = new Socket(ip,host);
			
		}
		catch(IOException ioe)
		{
			System.out.println("IOE in SorryClient constructor: " + ioe.getMessage());
		}
		
		//mGManager = inGManager;
		//mFClientGUI = inFClientGUI;
		boolean socketReady = initializeVariables();
		if (socketReady) {
			start();
		}
	}
	

	private boolean initializeVariables() {
		try {
			ois = new ObjectInputStream(mSocket.getInputStream());
			pw = new PrintWriter(mSocket.getOutputStream());
			oos = new ObjectOutputStream(mSocket.getOutputStream());

		} catch (IOException ioe) {
			System.out.println(ioe);
			return false;
		}
		return true;
	}
	
	 
	 public void sendLogin(String username, String password) {
		 TreeMap<String, Object> tempMap = new TreeMap<String , Object>();
		 this.username= username;
		 tempMap.put("type", "join");
		 tempMap.put("username", username);		
		 tempMap.put("password", password);
	        try {
	        	oos.writeObject(tempMap);
				oos.flush();
	        } catch (Exception e) {
	            System.out.println(e.toString());
	        }
	    }
	 
	 public void sendSignUp(String username, String password) {
		 TreeMap<String, Object> tempMap = new TreeMap<String , Object>();
		 tempMap.put("type", "signup");
		 tempMap.put("username", username);		
		 tempMap.put("password", password);	 
	        try {
	        	oos.writeObject(tempMap);
				oos.flush();
	        } catch (Exception e) {
	            System.out.println(e.toString());
	        }
	    }
	 public void sendmy_rank(String username, String password) {
		 TreeMap<String, Object> tempMap = new TreeMap<String , Object>();
		 tempMap.put("type", "my_rank");
		 tempMap.put("username", username);		
	        try {
	        	oos.writeObject(tempMap);
				oos.flush();
	        } catch (Exception e) {
	            System.out.println(e.toString());
	        }
	    }
	 
	 public void sendworld_rank(){
		 TreeMap<String, Object> tempMap = new TreeMap<String , Object>();
		 tempMap.put("type", "world_rank");
	        try {
	        	oos.writeObject(tempMap);
				oos.flush();
	        } catch (Exception e) {
	            System.out.println(e.toString());
	        }
	 }
	 	 
	 public void sendgame_result(Vector<Dictionary<String, Object>> game_result) {
		 TreeMap<String, Object> tempMap = new TreeMap<String , Object>();
		 tempMap.put("type", "game_result");		 
		 tempMap.put("result" , game_result);
		 
	        try {
	        	oos.writeObject(tempMap);
				oos.flush();
	        } catch (Exception e) {
	            System.out.println(e.toString());
	        }
	    }
	 
	 public void run() {
			try {
				
				while(true) {
					// in case the server sends another factory to us
					TreeMap<String,Object>map = (TreeMap<String,Object>)ois.readObject();

					if(((String)map.get("type")).equals("login")) {
						
						boolean status 	= (boolean) map.get("status");
						String errMsg = (String) map.get("errMsg");
						if (status){System.out.println("login successfully");}
						else
						{System.out.println("login error " + errMsg);}
						

					}
					else if (((String)map.get("type")).equals("signup")){
						boolean status 	= (boolean) map.get("status");
						String errMsg = (String) map.get("errMsg");
						if (status){System.out.println("signup successfully");}
						else
						{System.out.println("signup error " + errMsg);}
						
					}
					else if (((String)map.get("type")).equals("my_rank")){
						boolean status 	= (boolean) map.get("status");
						String errMsg = (String) map.get("errMsg");
						TableModel rank_table = (TableModel) map.get("table");
//						if (status)BMRankPanel.set_my_rank(rank_table);
//						else {System.out.println("my_rank error " + errMsg);}
						
					}
					else if (((String)map.get("type")).equals("world_rank")){
						boolean status 	= (boolean) map.get("status");
						String errMsg = (String) map.get("errMsg");
						TableModel rank_table = (TableModel) map.get("table");
//						if (status)BMRankPanel.set_world_rank(rank_table);
//						else {System.out.println("world_rank error " + errMsg);}

						
					}
					else if (((String)map.get("type")).equals("game_result")){
						
						boolean status 	= (boolean) map.get("status");
						String errMsg = (String) map.get("errMsg");
						if (status){System.out.println("game_result successfully");}
						else
						{System.out.println("game_result error " + errMsg);}
								
					}
		
					
				}
			} catch (IOException ioe) {
				//mFClientGUI.addMessage(Constants.serverCommunicationFailed);
				System.out.println("serverCommunicationFailed");
			} catch (ClassNotFoundException cnfe) {
				System.out.println(cnfe);
			}finally{
				try{
					if(pw != null) {
						pw.close();
					}
					if (oos != null) {
						oos.close();
					}
					if (ois !=null) {
						ois.close();
					}
				} catch(IOException ioe) {
					System.out.println("lose connection");
				}
			}
		}
		
	 
}