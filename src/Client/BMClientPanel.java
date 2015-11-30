package Client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Server.BMHostServer;
import Server.BMSimulation;
import Utilities.BMFontLibrary;
import Utilities.BMLibrary;
import centralServer.BMCentralServerClient;
import centralServer.ServerConstants;
import customUI.PaintedButton;
import customUI.PaintedPanel;
public class BMClientPanel extends JPanel{
	private static final long serialVersionUID = 6415716059554739910L;
	private BMLoginPanel loginPanel;
	private BMMenuPanel menuPanel;
	private BMRoomPanel roomPanel;
	private BMRankPanel rankPanel;
	public BMBoardPanel boardPanel;
	public BMSigninPage detailSignin;
	private Integer[][] board = null;
	private int time = 0;	
	HostClientListener hostClient;

	private Vector<TreeMap<String, Object>> players;
	String username;
	String password;
	private int hp;
	protected BMHostServer hs;
	protected BMSimulation simulation;
	//true = host the game
	private boolean identity = true;
	BMCentralServerClient centralServerClient;

	{
		players = new Vector<TreeMap<String,Object>>();
		loginPanel = new BMLoginPanel(new ActionListener() {
			@Override

			public void actionPerformed(ActionEvent ae) {
				BMClientPanel.this.username = "Guest";
				simulation = new BMSimulation(5555,4,null);
				simulation.setVariables(60, 2);
				hostClient = new HostClientListener(BMClientPanel.this, "localhost", 5555);
				hostClient.sendJoin("Guest");
				init_board();
				BMClientPanel.this.removeAll();				
				BMClientPanel.this.add(boardPanel);
				BMClientPanel.this.revalidate();
				BMClientPanel.this.repaint();
				loginPanel.closeSignup();
				roomPanel = null;
				simulation.startGame(1);
			}
		},
				new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent ae) {
				BMClientPanel.this.removeAll();
				username = loginPanel.getSignin().txtUsername.getText().trim();
				password = loginPanel.getSignin().txtPassword.getText().trim();
//				centralServerClient = new BMCentralServerClient("172.20.10.3", 6789);
				/*try {
					//centralServerClient = new BMCentralServerClient( 6789);
									} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				//centralServerClient = new BMCentralServerClient();
				if(centralServerClient != null){
					if (centralServerClient.signup(username, password))
					{
						loginPanel.getSignin().label.setText("Signed up successfully! ");
						loginPanel.getSignin().txtPassword.setText("");
						loginPanel.getSignin().txtUsername.setText("");
						System.out.println("");
					}
				}
				else{
					loginPanel.getSignin().label.setText("Please connect to login server first");
					loginPanel.getSignin().label.setForeground(Color.RED);
					loginPanel.getSignin().label.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 15));
				}
				
			}
		},
				new ActionListener(){
			@Override				
			public void actionPerformed(ActionEvent ae) {
				/*add check the correctness of the username and password*/
				username = loginPanel.getSignin().txtUsername.getText().trim();
				password = loginPanel.getSignin().txtPassword.getText().trim();
//				centralServerClient = new BMCentralServerClient("172.20.10.3", 6789);
				/*try {
					centralServerClient = new BMCentralServerClient( 6789);
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				//centralServerClient = new BMCentralServerClient();
				if (centralServerClient != null){
					if (centralServerClient.login(username, password))
					{
						BMClientPanel.this.removeAll();				
						BMClientPanel.this.add(menuPanel);
						BMClientPanel.this.revalidate();
						BMClientPanel.this.repaint();
						System.out.println("login success");
						loginPanel.closeSignup();
					}
					else
					{
						loginPanel.getSignin().label.setText("Sign in failed");
						loginPanel.getSignin().label.setForeground(Color.RED);
						loginPanel.getSignin().label.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 15));
						loginPanel.getSignin().txtPassword.setText("");
						loginPanel.getSignin().txtUsername.setText("");
					}
				}
				else{
					loginPanel.getSignin().label.setText("Please connect to login server first");
					loginPanel.getSignin().label.setForeground(Color.RED);
					loginPanel.getSignin().label.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 15));
				}
				
			}},
				new ActionListener(){

					@Override
					public void actionPerformed(ActionEvent e) {
						centralServerClient = new BMCentralServerClient();

						
					}
				
				
				
			},BMLibrary.readImages("menu.png"));


		//Set up the panel to display
		setLayout(new BorderLayout());
		add(loginPanel);
		refreshComponents();
	}


	private void refreshComponents()
	{
		initMenuPanel();


		initRoomPanel(1);
		
	}
	private void initRoomPanel(int i){
		roomPanel = new BMRoomPanel(i, identity,
				new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//enter the game
				if (simulation.getNumClients() == 1)
				{
					JFrame jf = new JFrame();
					jf.setSize(new Dimension(300,300));
					jf.setLocationRelativeTo(null);
				}
				else
				{
					System.out.println("Set variables " + roomPanel.sendTime + "  " + roomPanel.sendhp);
					simulation.setVariables(roomPanel.sendTime, roomPanel.sendhp);
					roomPanel = null;
					simulation.startGame(0);
				}
			}
		},
				new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{			
				//enter Game
				System.out.println("Set variables " + roomPanel.sendTime + "  " + roomPanel.sendhp);
				simulation.setVariables(roomPanel.sendTime, roomPanel.sendhp);
				roomPanel = null;
				simulation.startGame(1);
			}
		},
		new ActionListener()
		{
				@Override
					public void actionPerformed(ActionEvent e)
					{
					//Login
						if (simulation != null)simulation.gameOver();
						roomPanel = null;
						initMenuPanel();
						BMClientPanel.this.removeAll();
						BMClientPanel.this.add(menuPanel);
						BMClientPanel.this.revalidate();
						
					}
				},BMLibrary.readImages("vs.png")
		);
		init_board();
	}
	private void init_board(){
		boardPanel = new BMBoardPanel(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (simulation != null) simulation.gameOver();
				hostClient.interrupt();
				simulation = null;
				hostClient = null;
				roomPanel = null;
				System.gc();
				if (!username.equals("Guest")){
					BMClientPanel.this.removeAll();
					BMClientPanel.this.add(menuPanel);
					BMClientPanel.this.revalidate();
					BMClientPanel.this.repaint();
					//					BMClientPanel.requestFocusInWindow();

					loginPanel.requestFocus();
					loginPanel.requestFocusInWindow();
				}
				else {
					BMClientPanel.this.removeAll();
					BMClientPanel.this.add(loginPanel);
					BMClientPanel.this.revalidate();
					BMClientPanel.this.repaint();
				}
				if (BMClientPanel.this.hostClient != null) BMClientPanel.this.hostClient.close();
				if (simulation != null) {
					System.out.println("host calls simulation game over");
					simulation.gameOver();
				}
			}

		});
	}
	private void initMenuPanel() {
		menuPanel = new BMMenuPanel(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//host the game
				initRoomPanel(1);
				BMClientPanel.this.removeAll();
				BMClientPanel.this.add(roomPanel);
				BMClientPanel.this.revalidate();
				String temp1 = menuPanel.ipField.getText();
				String temp2 = menuPanel.portField.getText();
				int port = 0;
				if (temp2.length() != 0)
				{
					try
					{
						port = Integer.parseInt(temp2);
					}catch(NumberFormatException nfe)
					{
						popError("number format is wrong");
						System.out.println("number format is wrong");
					}
				}
				else
				{
					String temp = menuPanel.portField.getText().trim();
					try
					{
						port = Integer.parseInt(temp2);
					}catch(NumberFormatException nfe)
					{
						popError("Port format is wrong!");
						System.out.println("number format is wrong");
					}
				}
				String ip = null;
				if (temp1.length() == 0)
				{
					System.out.println("ip is empty");
					popError("IP is empty!");

				}
				else
				{
					ip = temp1;
				}



				simulation = new BMSimulation(menuPanel.getPort(),4,BMClientPanel.this);
				System.out.println("start server");
				hostClient = new HostClientListener(BMClientPanel.this, "localhost", menuPanel.getPort());
				System.out.println("connect server");
				hostClient.sendJoin(username);
				System.out.println("Join game");

				identity = true;
			}
		},
				new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				hostClient = new HostClientListener(BMClientPanel.this, menuPanel.getIP(), menuPanel.getPort());
				hostClient.sendJoin(username);
				identity = false;
			}
		},
		new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//Login
				rankPanel = new BMRankPanel(centralServerClient.requestWorldRankings(), centralServerClient.requestPersonalRecords());
//				rankPanel.setupTable();
				rankPanel.setVisible(true);
				
//				
//					
//				BMClientPanel.this.removeAll();
//				
//				BMClientPanel.this.revalidate();




			}
		}, BMLibrary.readImages("menu.png")
				);		
	}
	void set_start(Integer [][] board, int time, Vector<TreeMap<String, Object>> players)
	{
		this.board = board;
		this.time = time;
		this.players = players;
		boardPanel.setupMap(board, time, players , username, hostClient);
		BMClientPanel.this.removeAll();
		BMClientPanel.this.add(boardPanel);		
		BMClientPanel.this.revalidate();
		boardPanel.setFocusable(true);
		boardPanel.requestFocusInWindow();
		boardPanel.requestFocus();
	}
	void set_join(Vector<String> players, int hp, int time)
	{
//		this.players = players;
		if (roomPanel == null) return;
		this.time = time;
		this.hp = hp;
		this.initRoomPanel(players.size());
		BMClientPanel.this.removeAll();
		BMClientPanel.this.add(roomPanel);		
		BMClientPanel.this.revalidate();
	}

	public void popError(String error)
	{
		System.out.println("popError");
		ipChecking popup = new ipChecking(error, this, menuPanel);
	}
	public void server_quit() {
		if (roomPanel == null) return;
		if (simulation != null) simulation.gameOver();
		simulation = null;
		hostClient = null;
		roomPanel = null;
		System.gc();
		
		removeAll();
		add(menuPanel);
		revalidate();
		this.repaint();
		
		BMClientPanel.this.hostClient.close();
		if (simulation != null) simulation.gameOver();	
		}
	public void game_over(Vector<TreeMap<String, Object>> result) {
		Vector<TreeMap<String, Object>> tmVector = new Vector<TreeMap<String, Object>>();
		for(TreeMap<String, Object> tm : result) {
			//TreeMap<String, Object> temp = new TreeMap<String, Object>();
			
			String usr = (String)tm.get("username");
			if (!usr.startsWith("AI")) {
				tmVector.add(tm);
			}
			
			
		}
		System.out.println("Update world rankings number of real players: " + tmVector.size());
<<<<<<< HEAD
		if (simulation!=null) centralServerClient.updateWorldRankings(tmVector);
=======
		if (simulation != null) {
			centralServerClient.updateWorldRankings(tmVector);	
		}
		
>>>>>>> origin/hat
		
	}
}
class ipChecking extends JFrame
{
	private BMClientPanel clientPanel;
	private BMMenuPanel menuPanel;
	ipChecking(String error, BMClientPanel clientPanel, BMMenuPanel menuPanel)
	{
		setSize(new Dimension(320,480));
		setLocationRelativeTo(null);
		this.clientPanel = clientPanel;
		this.menuPanel = menuPanel;
		System.out.println("FRAME");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Notice notice = new Notice(this,error,BMLibrary.readImages("frame1.png"));
		add(notice);
		setVisible(true);

	}	
	public void close() {
		clientPanel.removeAll();				
		clientPanel.add(menuPanel);
		clientPanel.revalidate();
		clientPanel.repaint();
		this.setVisible(false);

	}
}
class Notice extends PaintedPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel message; 
	private PaintedButton ok;
	ipChecking frame;
	Notice(ipChecking frame,String error, Image image)
	{
		super(image);
		this.frame = frame;
		System.out.println("notice");
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		message = new JLabel(error);
		JPanel mp = new JPanel();
		mp.setOpaque(false);
		mp.add(message);
		ok = new PaintedButton("OK",BMLibrary.readImages("button0.png") , BMLibrary.readImages("button0-0.png"), 30);
		ok.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				System.out.println("login 3");
				frame.close();
			}
		});

		JPanel okp = new JPanel();
		okp.setOpaque(false);
		okp.add(ok);
		gbc.gridy = 1;
		add(mp,gbc);
		gbc.gridy = 2;
		add(okp,gbc);

	}
}