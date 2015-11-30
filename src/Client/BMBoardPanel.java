package Client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Dictionary;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import Server.BMPlayer;
import Server.BMRealPlayer;
import node.BMTile;
import Utilities.BMFontLibrary;
import Utilities.BMLibrary;
import Utilities.BMMove;
import Utilities.BMNodeType;
import customUI.PaintedButton;
import customUI.PaintedLabel;
import customUI.PaintedPanel;

public class BMBoardPanel extends JPanel{
	private final static int boardSize = 16;
	//private final TilePanel[][] tileGrid;

	private PaintedPanel chatPanel, boardPanel;

	private BMBoard_Player playerPanel;
	private static JTextArea chatArea ;
	private JTextField chatTF;
  	private final NodePanel[][] nodeGrid;

	private PaintedButton chatButton;
	
	private Integer[][]map;
	private KeyAdapter keylistener;
	private HostClientListener clientListener;
	private String local_username;
	private int local_hp;
	private int total_hp;
	private Vector<int[]> location; 
	private int time;
	private Vector<TreeMap<String, Object>> players;

	volatile int keyPressed = 0;
	private Thread sending;
	public static long a;
	private JScrollPane jsp;
	private ActionListener playingGame;
	private ActionListener quit2;
	private boolean endGame =false;


	public void setupMap(Integer[][] map, int time, Vector<TreeMap<String, Object>> players2 , String username, HostClientListener clientListener){

		this.clientListener = clientListener;
		this.map=map;
		this.time =time;
		location = new Vector<int[]>();
		this.local_username = username;
//		System.out.println(players2);
		for (int i = 0; i<players2.size(); i++) {
			if (((String)players2.get(i).get("username")).equals(local_username))
			{
				local_hp = (int)(players2.get(i).get("hp"));
				total_hp=local_hp;
			}
		}
		//boardPanel initialize

				boardPanel.setLayout(new GridLayout(boardSize,boardSize));
				for(int y = 0; y < boardSize; ++y) {
					for(int x = 0; x < boardSize; ++x) {
						if(map[x][y]==1) {
							nodeGrid[x][y] = new NodePanel(1); }
						else if (map[x][y]==2) {
							nodeGrid[x][y] = new NodePanel(2);}
						else if (map[x][y]==0) {
							nodeGrid[x][y] = new NodePanel (0);
						}
						else {System.out.println("wrong read from input");}
						boardPanel.add(nodeGrid[x][y]);
					}
				}
		this.players = players2;
		playerPanel.set_up(players2, username);
		endGame= false;

		repaint();
		boardPanel.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				getFocus();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

				getFocus();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void getFocus(){
		System.out.println("GEt focus");
		this.requestFocusInWindow();
		this.requestFocus();
	}
	
	public BMBoardPanel(ActionListener playingGame){

	//	setSize(1000,600);
		this.playingGame=playingGame;
		
		this.setLayout(new BorderLayout());
		chatPanel= new PaintedPanel(null);
		boardPanel = new PaintedPanel( null);
		playerPanel = new BMBoard_Player( BMLibrary.readImages("frame.png"), playingGame );
		playerPanel.setPreferredSize(new Dimension(150, BMBoardPanel.this.getHeight()));

		chatPanel.setPreferredSize(new Dimension(150, BMBoardPanel.this.getHeight()));
		keylistener = null;
		nodeGrid = new NodePanel[boardSize][boardSize];


//chatPanel initialize
		
		chatPanel.setLayout(new BorderLayout());
		chatArea = new JTextArea();
		chatArea.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 15));

		chatTF = new JTextField(20);
		chatTF.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 15));

		chatButton = new PaintedButton("send" , BMLibrary.readImages("button2.png"), BMLibrary.readImages("button2-0.png"), 10);	
		chatButton.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 15));
		chatButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				clientListener.sendMsg(chatTF.getText());
				chatTF.setText("");
				
			}
			
		});
        jsp = new JScrollPane(chatArea);
		jsp.setPreferredSize(new Dimension(chatPanel.getWidth(), 380));

        chatArea.setEditable(false);
		

		
//add component into chatPanel
		
		chatPanel.add(jsp, BorderLayout.NORTH);
		chatPanel.add(chatTF, BorderLayout.CENTER);
		chatPanel.add(chatButton, BorderLayout.SOUTH);
//add players
		
		
//add all panels		
		this.add(chatPanel, BorderLayout.WEST);
		this.add(boardPanel, BorderLayout.CENTER);
		this.add(playerPanel, BorderLayout.EAST);
		addAction();
		revalidate();
		repaint();
	}
	

	@Override 
	public void paint(Graphics g){

//		  System.out.println("Paint " + (System.currentTimeMillis()-BMBoardPanel.a) + " ms");
//  			BMBoardPanel.a=System.currentTimeMillis();
		super.paint(g);
		if (players == null) return;
//		System.out.println("Player size " + players.size());
		for (int i=0; i<players.size(); i++) {
			int x = (int)(players.get(i).get("posX"));
			int y = (int)(players.get(i).get("posY"));
			int d = (int)(players.get(i).get("direction"));
			Image image= BMLibrary.readImages("player" + i + d + ".png");
			g.drawImage(image, (x-25)*boardPanel.getWidth()/1024+boardPanel.getX(), (y-31)*boardPanel.getHeight()/1024+boardPanel.getY(),boardPanel.getWidth()/16, boardPanel.getHeight()/16, boardPanel);
		}
	}
	
	public void set_move(int time, Vector<TreeMap<String, Object>>  players_, Integer[][] board){
		this.players = players_;
		playerPanel.set_move(time, players_);
//		paintComponent(this.getGraphics());
//		System.out.println("Start Repaint " +(System.currentTimeMillis()-a) + " ms");
//		a=System.currentTimeMillis();
		this.repaint();
		repaintBoard(board);
		validate();	
//		System.out.println("After Repaint " + (System.currentTimeMillis()-a) + " ms");
//		a=System.currentTimeMillis();
	}
	
	public void startSending(){
		sending = new Thread(new Runnable(){

			@Override
			public void run() {
					try{
						while (!endGame){
							Thread.sleep(10);
							clientListener.sendMove(keyPressed);
						}
					}
					catch (InterruptedException e) {
						// TODO Auto-generated catch block
//						System.out.println("Exception " + e.getMessage());
					} 
				}
			
		});
		sending.start();
	}
	
	public void addAction(){
		this.addKeyListener(new KeyAdapter(){
	        @Override
	        public void keyPressed(KeyEvent e) {
	        	int c = e.getKeyCode();
				  if(c == KeyEvent.VK_LEFT)
				    {
					  if (keyPressed == 0){
						  keyPressed = BMMove.left;
						  startSending();
					  }
				    }
				  else if(c == KeyEvent.VK_RIGHT)
				    {
					  if (keyPressed == 0){
						  keyPressed = BMMove.right;
						  startSending();
					  }
				    }
				  else if (c == KeyEvent.VK_UP){
					  if (keyPressed == 0){
						  keyPressed = BMMove.up;
						  startSending();
					  }
				  }
				  else if (c == KeyEvent.VK_DOWN){
					  if (keyPressed == 0){
						  keyPressed = BMMove.down;
						  startSending();
					  }
				  }
				  else if (c == KeyEvent.VK_SPACE){
					  if (keyPressed == 0){
						  keyPressed = BMMove.bomb;
						  startSending();
					  }
				  }
	        }
	        @Override
	        public void keyReleased(KeyEvent e) {
				int c = e.getKeyCode();
				  if(c == KeyEvent.VK_LEFT)
				    {
					  if (keyPressed == BMMove.left){
						  sending.interrupt();
						  keyPressed = 0;
						  sending = null;
					  }
				    }
				  else if(c == KeyEvent.VK_RIGHT)
				    {
					  if (keyPressed == BMMove.right){
						  sending.interrupt();
						  keyPressed = 0;
						  sending = null;
					  }
				    }
				  else if (c == KeyEvent.VK_UP){
					  if (keyPressed == BMMove.up){
						  sending.interrupt();
						  keyPressed = 0;
						  sending = null;
					  }
				  }
				  else if (c == KeyEvent.VK_DOWN){
					  if (keyPressed == BMMove.down){
						  sending.interrupt();
						  keyPressed = 0;
						  sending = null;
					  }
				  }
				  else if (c == KeyEvent.VK_SPACE){
					  if (keyPressed == BMMove.bomb){
						  sending.interrupt();
						  keyPressed = 0;
						  sending = null;
					  }
				  }	    	
	        }
		});
	}
	
	public static void set_chat_text(String name, String content){
		String orgin = chatArea.getText();
	
		chatArea.setText(orgin + '\n' +name + " : " + content);

		chatArea.setCaretPosition(chatArea.getDocument().getLength());
		
	}

	
	public void Gameover(Vector<TreeMap<String, Object>> result){
		
		if (endGame) return;
		else {
			System.out.println("Result " + result);
			BMResultFrame bmrf = new BMResultFrame(result, playingGame);
			bmrf.setVisible(true);
			System.out.println("BMBP.GAMEOVER");
			bmrf.setVisible(true);
			endGame= true;
		}
		

	}

	public void repaintBoard(Integer[][] board){
//		nodeGrid
//		System.out.println("repaint board");
		for (int i = 0; i < 16; i++){
			for (int j = 0; j < 16; j++){
				if (board != null && nodeGrid[i][j].node_type != board[i][j]) nodeGrid[i][j].update(board[i][j]);
			}
		}
	}
	
	
	class NodePanel extends PaintedPanel {

		public int node_type=-1;
		
		NodePanel(int node_type) {		
			super(null);
			this.node_type=node_type;
			setLayout(new GridBagLayout());
			update(node_type);
		}
		
		public void update(int new_type){
			node_type =new_type;
//			System.out.println("node"+node_type+".png");
			Image image = BMLibrary.readImages("node"+node_type+".png");
			setImage(image);
			revalidate();
			repaint();
		}	
	}
}