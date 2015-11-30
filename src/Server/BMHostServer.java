package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Vector;

public class BMHostServer extends Thread {
	private Vector<BMClient> ctVector = new Vector<BMClient>();
	private int port;
	private int numPlayer;
	private BMSimulation simulation;
	private ServerSocket ss;
	public Vector<BMClient> getClients(){
		return ctVector;
	}
	public BMHostServer(int port, int numPlayer) {
		this.port = port;
		this.numPlayer = numPlayer;
		this.start();
	}
	public void removeChatThread(BMClient ct) {
		ctVector.remove(ct);
	}
	public void sendMapToClients(TreeMap<String,Object> output) {
		for (BMClient ct : ctVector) {
//			System.out.println("Direction " + ((Vector)output.get("players")).get(0));
			ct.sendMap(output);
		}
	}
	public void run(){

		try {
			ss = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			simulation.popError(e.getMessage());
			return;
		}
		
		try {
			System.out.println("Starting Chat Server");
			while (true) {
				System.out.println("Waiting for client to connect...");
				Socket s = ss.accept();
				System.out.println("Client " + s.getInetAddress() + ":" + s.getPort() + " connected");
				BMClient ct = new BMClient(s, this,simulation);
				System.out.println("size " + ctVector.size());
				if (ctVector.size() < numPlayer){
					ctVector.add(ct);
				}
				else {
					TreeMap<String,Object>map = new TreeMap<String,Object>();
					map.put("type", "error");
					map.put("errMsg","This room is full, please try another one");
					ct.sendMap(map);
				}
			}
		} catch (IOException ioe) {
			System.out.println("IOE: " + ioe.getMessage());
		} finally {
			if (ss != null) {
				try {
					ss.close();
				} catch (IOException ioe) {
					System.out.println("IOE closing ServerSocket: " + ioe.getMessage());
				}
			}
		}
	}
	public Vector<String> getNames() {
		Vector<String> names = new Vector<String>();
		for (BMClient client : ctVector){
			names.add(client.getName());
		}
		return names;
	}
	public void setSimulation(BMSimulation bmSimulation) {
		this.simulation = bmSimulation;
	}
	public void clientDisconnected(BMClient bmClient) {
		if (bmClient.getPlayer() != null) simulation.playerQuit(bmClient.getPlayer());
		ctVector.remove(bmClient);	
	}
	public void endGame() {
		try {
			for (BMClient client : ctVector) client.close();
			ss.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
