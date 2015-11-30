package centralServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Queue;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class BMCentralServerClientCommunicator extends Thread {
	
	private Socket s;
	private BMCentralServer bmcs;
	
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	private boolean running;
	private UserPasswordInfo currentUPI;
	
	private Lock recordRetrieval;
	private Lock signuploginLock;
	
	public BMCentralServerClientCommunicator(Socket s, BMCentralServer bmcs) throws IOException {
		super();
		this.s = s;
		this.bmcs = bmcs;
		oos = new ObjectOutputStream(s.getOutputStream());
		ois = new ObjectInputStream(s.getInputStream());
		recordRetrieval = new ReentrantLock();
		signuploginLock = new ReentrantLock();
		
		running = true;
		
	}
	
	/*
	 * Login and Sign up
	 */
	private void login(UserPasswordInfo upi) {
		signuploginLock.lock();
		
		try {
			if (bmcs.login(upi.getUsername(), upi.getPassword())) {
				//If login is successful, then set the currentUPI and assign their VIP status
				currentUPI = upi;
				if (bmcs.isVIP(currentUPI.getUsername())) currentUPI.setVIPStatus(ServerConstants.VIPSTATUSTRUE);
				sendObject(ServerConstants.SUCCESSFULLOGIN);
				
				BMCentralServerGUI.addMessage(ServerConstants.SUCCESSFULLOGIN + upi.getUsername());
			} else {
				currentUPI = null;
				sendObject(ServerConstants.LOGINFAILED);
				
				BMCentralServerGUI.addMessage(ServerConstants.LOGINFAILED + upi.getUsername());
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Problem connecting with Server/Client:" + s.getInetAddress() + ":" + s.getPort());
		}
		
		signuploginLock.unlock();
	}
	private void signup(UserPasswordInfo upi) {
		
		signuploginLock.lock();
		try {
			if (bmcs.signup(upi.getUsername(), upi.getPassword())) {
				sendObject(ServerConstants.SUCCESSFULSIGNUP);
				BMCentralServerGUI.addMessage(ServerConstants.SUCCESSFULSIGNUP + upi.getUsername());
			} else {
				BMCentralServerGUI.addMessage(ServerConstants.SIGNUPFAILED + upi.getUsername());
				sendObject(ServerConstants.SIGNUPFAILED);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Problem connecting with Server/Client:" + s.getInetAddress() + ":" + s.getPort());
		}
		signuploginLock.unlock();
	}
	
	/*
	 * Helpers and Accessors
	 */
	private synchronized boolean sendObject(Object obj) throws IOException {
		oos.writeObject(obj);
		oos.flush();
		
		return true;
	}
	public String getUsername() {
		if (currentUPI != null) {
			return currentUPI.getUsername();
		} else {
			System.out.println("No user currently logged in to this connection");
			return null;
		}
	}
	
	public void run() {
		try {
			while (running) {
				try {
					Object obj = ois.readObject();
					
					//STRING CASES
					if (obj instanceof String) {
						String str = (String) obj;
						if (str.equals(ServerConstants.LOGOUT)) {
							BMCentralServerGUI.addMessage(ServerConstants.SUCCESSFULLOGOUT + currentUPI.getUsername());
							
							currentUPI = null;
							sendObject(ServerConstants.SUCCESSFULLOGOUT);
						}
						else if (str.equals(ServerConstants.REQUESTPERSONALRECORDS)) {
							recordRetrieval.lock();
							Vector<GameRecord> records = bmcs.retrievePersonalRecords(currentUPI.getUsername());
							sendObject(records);
							recordRetrieval.unlock();
						}
						else if (str.equals(ServerConstants.REQUESTWORLDRANKING)) {
							recordRetrieval.lock();
							Queue<RankContainer> rankings = bmcs.retrieveWorldRankings();
							sendObject(rankings);
							recordRetrieval.unlock();
						}
						else if (str.equals(ServerConstants.VIPSTATUSREQUEST)) {
							if (currentUPI != null) {
								sendObject(currentUPI.getVIPStatus());
								BMCentralServerGUI.addMessage(ServerConstants.VIPSTATUSREQUEST + currentUPI.getUsername());
							}
						} 
						else if (str.equals(ServerConstants.UPGRADETOVIP)) {
							if (currentUPI != null) {
								String vipStatus = bmcs.makeVIP(currentUPI.getUsername());
								currentUPI.setVIPStatus(vipStatus);
							}
						}
						else if (str.equals(ServerConstants.DISCONNECT)) {
							running = false;
						}
					}
					//CLient is attempting a login/signup
					else if (obj instanceof UserPasswordInfo) {
						UserPasswordInfo upi = (UserPasswordInfo) obj;
						if (upi.isLogin()) login(upi);
						else if (upi.isSignup()) signup(upi);
					}
					//Client wants to update Rating database stuff/add gamerecords
					else if (obj instanceof Vector<?>){
						@SuppressWarnings("unchecked")
						Vector<TreeMap<String, Object>> rankings = (Vector<TreeMap<String, Object>>) obj;
						bmcs.updateRankings(rankings);
					}
				} catch (ClassNotFoundException cnfe) {
					cnfe.printStackTrace();
				}
				
			}
		} catch (IOException ioe) {
			System.out.println("IOE: " + ioe.getMessage());
			BMCentralServerGUI.addMessage("Trouble connecting to client");
		} finally {
			System.out.println("Disconnect");
			bmcs.removeServerClientCommunicator(this);
			BMCentralServerGUI.addMessage(ServerConstants.clientDisconnected + " - " + s.getInetAddress() + ":" + s.getPort());
			// this means that the socket is closed since no more lines are being received
			try {
				s.close();
			} catch (IOException ioe1) {
				ioe1.printStackTrace();;
			}
		}
	}
}
