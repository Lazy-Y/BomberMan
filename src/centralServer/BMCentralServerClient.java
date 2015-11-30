package centralServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeMap;
import java.util.Vector;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Utilities.CommandLineMonitor;

//For connecting to MySQL Server, which contains all the scores, user accounts, etc. 
public class BMCentralServerClient extends Thread {
	
	private Socket s;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	private Lock mLock;
	//private Lock successLock;
	private Condition mRanksArrived;
	private Condition loginResult;
	private Condition signupResult;
	private Condition vipResult;
	
	
	private volatile boolean running;
	private Boolean loggedIn;
	private Boolean vipStatus;
	private Boolean signupSuccess;
	
	private Queue<RankContainer> ranks;
	private Vector<GameRecord> gameRecords;
	private HostAndPortGUI hapgui;
	
	{
		mLock = new ReentrantLock();
		//successLock = new ReentrantLock();
		mRanksArrived = mLock.newCondition();
		loginResult = mLock.newCondition();
		signupResult = mLock.newCondition();
		vipResult = mLock.newCondition();
		
		
		running = false;
		loggedIn = false;
		vipStatus = false;
		
		ranks = new LinkedList<RankContainer>();
		gameRecords = new Vector<GameRecord>();
	}
	
	public BMCentralServerClient(int port) throws UnknownHostException {
		try{
			s = new Socket("localhost", port);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			running = true;
			start();
		} catch(IOException ioe) {
			//ioe.printStackTrace();
			running = false;
			System.out.println(ServerConstants.CannotCompleteRequest + "Can't a connect to localhost" + ":" + port);
		}
	}
	
	public BMCentralServerClient(String ip, int port) {
		try{
			s = new Socket(ip,port);
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			running = true;
			start();
		} catch(IOException ioe) {
			//ioe.printStackTrace();
			running = false;
			System.out.println(ServerConstants.CannotCompleteRequest + "Can't a connect to localhost" + ":" + port);
		}
	}
	
	public BMCentralServerClient() {
		
		hapgui = new HostAndPortGUI(this);
		
		
		//
	}
	
	public void setUp(){
		s = hapgui.getSocket();
		//System.out.println("3");
		try {
			//System.out.println("4");
			oos = new ObjectOutputStream(s.getOutputStream());
			ois = new ObjectInputStream(s.getInputStream());
			running = true;
			start();
		} catch (IOException ioe) {
			running = false;
			CommandLineMonitor.printExceptionToCommand(ioe);
		}
	}
	
	/*
	 * Login screen methods
	 */
	public Boolean login(String username, String password) {
		System.out.println("**LOGGING IN**");
		
		UserPasswordInfo upi = new UserPasswordInfo(username, password, ServerConstants.LOGIN);
		try {
			mLock.lock();
			sendObject(upi);
			if (running) loginResult.await();
			mLock.unlock();
		} catch (IOException | NullPointerException e) {
			//e.printStackTrace();
			System.out.println(ServerConstants.CannotCompleteRequest + ServerConstants.NotLoggedIn);
		} catch (InterruptedException ie) {
			System.out.println("LOGIN INTERRUPTED");
		}
		
		if (loggedIn) return true;
		else return false;
	}
	public Boolean signup(String username, String password) {
		System.out.println("**SIGNING UP**");
		
		UserPasswordInfo upi = new UserPasswordInfo(username, password, ServerConstants.SIGNUP);
		try {
			mLock.lock();
			sendObject(upi);
			signupResult.await();
			mLock.unlock();
		} catch (IOException | NullPointerException e) {
			//e.printStackTrace();
			signupSuccess = false;
			CommandLineMonitor.printMessageToCommand(ServerConstants.CannotCompleteRequest + ServerConstants.NotLoggedIn);
		} catch (InterruptedException ie) {
			signupSuccess = false;
			CommandLineMonitor.printMessageToCommand("SIGNUP INTERRUPTED");
		}
		
		if (signupSuccess) return true;
		else return false;
	}
	public Boolean getVIPStatus() {
		if (!loggedIn) {
			System.out.println(ServerConstants.CannotCompleteRequest + ServerConstants.NotLoggedIn);
			return null;
		}
		
		mLock.lock();
		try {
			sendObject(ServerConstants.VIPSTATUSREQUEST);
			vipResult.await();
		} catch (IOException | InterruptedException e) {
			CommandLineMonitor.printExceptionToCommand(e);
		} 
		
		mLock.unlock();
		return vipStatus;
	}
	public void makeVIP() {
		if (loggedIn) {
			try{
				sendObject(ServerConstants.UPGRADETOVIP);
			} catch (IOException e) {
				CommandLineMonitor.printExceptionToCommand(e);
			}
		} 
		else {
			CommandLineMonitor.printMessageToCommand(ServerConstants.CannotCompleteRequest + ServerConstants.NotLoggedIn);
		}
	}
	
	/*
	 * Access/Update Rankings and Records
	 */
	public Queue<RankContainer> requestWorldRankings() {
		mLock.lock();
		try {
			sendObject(ServerConstants.REQUESTWORLDRANKING);
			mRanksArrived.await();
		} catch (IOException | InterruptedException e) {
			CommandLineMonitor.printMessageToCommand(ServerConstants.WorldRankingFetchFailure);
			CommandLineMonitor.printExceptionToCommand(e);
		} 
		mLock.unlock();
		
		return ranks;
	}
	
	public Vector<GameRecord> requestPersonalRecords() {
		mLock.lock();
		try {
			sendObject(ServerConstants.REQUESTPERSONALRECORDS);
			mRanksArrived.await();
		} catch (IOException | InterruptedException e) {
			CommandLineMonitor.printMessageToCommand(ServerConstants.PersonalRankingFetchFailure);
			CommandLineMonitor.printExceptionToCommand(e);
		} 
		
		mLock.unlock();
		
		return gameRecords;
	}
	
	public void updateWorldRankings(Vector<TreeMap<String, Object>> newRanks) {
		try {
			sendObject(newRanks);
		} catch (NullPointerException | IOException e) {
			// TODO Auto-generated catch block
			CommandLineMonitor.printExceptionToCommand(e);
		}
	}
	
	/*
	 * Logs out of the server
	 */
	public void logout() {
		if (running) {
			System.out.println("**LOGGING OUT**");
			try {
				sendObject(ServerConstants.LOGOUT);
				ranks = null;
				gameRecords = null;
			} catch (IOException e) {
				//e.printStackTrace();
				CommandLineMonitor.printMessageToCommand(ServerConstants.LOGOUTFAILED);
			}
		}
	}
	public void disconnect() {
		try {
			sendObject(ServerConstants.DISCONNECT);
		} catch (IOException e) {
			//e.printStackTrace();
			CommandLineMonitor.printExceptionToCommand(e);
		}
	}
	
	/*
	 * Helper Methods
	 */
	private synchronized boolean sendObject(Object obj) throws IOException, NullPointerException {
		oos.writeObject(obj);
		oos.flush();
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@SuppressWarnings("unchecked")
	public void run() {
		try {
			while(running) {
				try {
					Object obj = ois.readObject();
					mLock.lock();
					if (obj instanceof Queue<?>){
						ranks = (Queue<RankContainer>) obj;
						mRanksArrived.signal();
					}
					else if (obj instanceof Vector<?>){
						gameRecords = (Vector<GameRecord>) obj;
						mRanksArrived.signal();
					}
					
					if (obj instanceof String) {
						String str = (String) obj;
						if (str.equals(ServerConstants.SUCCESSFULLOGIN)){
							loggedIn = true;
							loginResult.signal();
						} else if (str.equals(ServerConstants.LOGINFAILED)) {
							loggedIn = false;
							loginResult.signal();
						} else if (str.equals(ServerConstants.SUCCESSFULSIGNUP)) {
							signupSuccess = true;
							signupResult.signal();
						} else if (str.equals(ServerConstants.SIGNUPFAILED)) {
							signupSuccess = false;
							signupResult.signal();
						} else if (str.equals(ServerConstants.SUCCESSFULLOGOUT)) {
							loggedIn = false;
							vipStatus = null;
						} else if (str.equals(ServerConstants.VIPSTATUSTRUE)) {
							vipStatus = true;
							vipResult.signal();
						} else if (str.equals(ServerConstants.VIPSTATUSFALSE)) {
							vipStatus = false;
							vipResult.signal();
						} else if (str.equals(ServerConstants.DISCONNECT)) {
							running = false;
						}
					}
					mLock.unlock();
				} catch (ClassNotFoundException cnfe) {
					CommandLineMonitor.printExceptionToCommand(cnfe);
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
			System.out.println("Lost connection to Central Server at " + s.getInetAddress() + ":" + s.getPort());
		} finally {
			if (s != null) {
				try {
					s.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					CommandLineMonitor.printExceptionToCommand(e);
				}
			}
		}
	}
	
	public static void main(String args[]) throws UnknownHostException {
		BMCentralServerClient csc = new BMCentralServerClient();
		
		csc.login("Brandon", "cocacola");
		
		Vector<TreeMap<String, Object>> rankings = new Vector<TreeMap<String, Object>>();
		
		TreeMap<String, Object> bTree = new TreeMap<String, Object>();
		bTree.put(ServerConstants.usernameString, "TurdFerguson");
		bTree.put(ServerConstants.pointsString, (double) 72);
		bTree.put(ServerConstants.killString, 7);
		bTree.put(ServerConstants.deathString, 100);
		rankings.add(bTree);
		
		/*
		TreeMap<String, Object> bTree2 = new TreeMap<String, Object>();
		bTree2.put(ServerConstants.usernameString, "Brandon");
		bTree2.put(ServerConstants.pointsString, (double) 500);
		bTree2.put(ServerConstants.killString, 7);
		bTree2.put(ServerConstants.deathString, 5);
		
		TreeMap<String, Object> zTree = new TreeMap<String, Object>();
		zTree.put(ServerConstants.usernameString, "Zoe");
		zTree.put(ServerConstants.pointsString, (double)1000);
		zTree.put(ServerConstants.killString, 300);
		zTree.put(ServerConstants.deathString, 1);
		
		rankings.add(bTree);
		rankings.add(bTree2);
		rankings.add(zTree);
		*/
		for (TreeMap<String, Object> map : rankings) {
			String name = (String) map.get("username");
			System.out.println("THERE IS A " + name + " IN THIS VECTOR<TREEMAP>");
		}
		
		csc.updateWorldRankings(rankings);
		
		Queue<RankContainer> worldRanks = csc.requestWorldRankings();
		for (int i=0; i<worldRanks.size(); i++) {
			RankContainer rc = worldRanks.poll();
			System.out.println(rc.getUsername() + "   " + rc.getRating());
		}
		
		Vector<GameRecord> gamerec = csc.requestPersonalRecords();
		System.out.println("Getting Record for whoever is logged in. Vector Size: " + gamerec.size());
		
		Queue<RankContainer> worldrec = csc.requestWorldRankings();
		System.out.println("Getting Record for whoever is logged in. Queue Size: " + worldrec.size());
		
		int i = 1;
		for (GameRecord gr : gamerec) {
			System.out.println(i++ + ": " + gr.getPoints() + " " + gr.getKillCount() + " " + gr.getDeathCount() + " " + gr.getTime());
		}
		i = 1;
		for (RankContainer rc : worldrec) {
			System.out.println(i++ + ": " + rc.getUsername() + " " + rc.getRating());
		}
		
		csc.makeVIP();
		csc.logout();
		csc.disconnect();
	}
}
