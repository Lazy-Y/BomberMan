package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.TreeMap;

public class BMClient extends Thread {
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private BMHostServer hs;
	private Socket s;
	private BMSimulation mSimulation;
	private BMPlayer player;
	private String userName;
	public boolean vip = false;
	
	public BMClient(Socket s, BMHostServer hs, BMSimulation mSimulation) {
		this.hs = hs;
		this.s = s;
		this.mSimulation = mSimulation;
		try {
			ois = new ObjectInputStream(s.getInputStream());
			oos = new ObjectOutputStream(s.getOutputStream());
			this.start();
		} catch (IOException ioe) {
			System.out.println("IOE in ChatThread constructor: " + ioe.getMessage());
		}
	}

	public void sendMap(TreeMap<String,Object> output) {
		try {
//			System.out.println("Output " + output);
			oos.writeObject(output);
			oos.flush();
		} catch (IOException e) {
			System.out.println("BMClient sendMap IOE: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void setPlayer(BMPlayer player){
		this.player = player;
	}
	
	public void run() {
		try {
			while (true) {
				try {
					@SuppressWarnings("unchecked")
					Object obj = ois.readObject();
					if (!(obj instanceof TreeMap<?,?>)) continue;
					TreeMap<String,Object> map = (TreeMap<String,Object>)obj;
					String type = (String)map.get("type");  
					if (type.equals("msg")) {
						sendMsgMap(map);
					}
					else if (type.equals("join")){
						this.userName = (String)(map.get("username"));
						this.vip = (boolean)(map.get("vip"));
						mSimulation.joinGame();
					}
					else if (type.equals("move")){
//						System.out.println("Start move " + map.get("move"));
						player.startMove((Integer)(map.get("move")));
					}
					else if (type.equals("msg")){
						sendMsgMap(map);
					}
				} catch (ClassNotFoundException e) {
					System.out.println("BMClient Run CNFE: " + e.getMessage());
				} catch (ClassCastException e){
//					e.printStackTrace();
				} catch (ArrayStoreException e){
//					e.printStackTrace();
				}
			}
		} catch (IOException ioe) {
			System.out.println(s.getInetAddress() + ":" + s.getPort() + " disconnected.");
			ioe.printStackTrace();
			hs.clientDisconnected(this);
			if (mSimulation != null) mSimulation.joinGame();
		} 
	}

	private void sendMsgMap(TreeMap<String, Object> map) {
		TreeMap<String,Object> tempMap = new TreeMap<String, Object>();
		tempMap.put("type", "msg");
		tempMap.put("username", userName);
		tempMap.put("content", (String)map.get("content"));
		hs.sendMapToClients(tempMap);
	}

	public String getUserName() {
		return userName;
	}

	public void close() throws IOException {
		s.close();
	}

	public BMPlayer getPlayer() {
		return player;
	}
}
