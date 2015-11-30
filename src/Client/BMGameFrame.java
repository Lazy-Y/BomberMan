package Client;

/*
 * Zhongyang Gao zhongyag@usc.edu
 * Jiahui Wei jiahuiwe@usc.edu
 * Zhongzhen Yang zhongyanz@usc.edu
 * Yiqing Xu yiqingxu@usc.edu
 * Brandon Keiji Horton bkhorton@usc.edu
 * Cheuk Yin Matthew Lam cheukyin@usc.edu
 * 
 * 
 * 
 * 
 * 
 */


import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import Utilities.BMLibrary;
import Utilities.Help;
import Utilities.MusicLibrary;
public class BMGameFrame extends JFrame implements KeyListener{
private static final long serialVersionUID = 5147395078473323173L;
	
	private final static Dimension minSize = new Dimension(640,480);
	private final static Dimension maxSize = new Dimension(960,640);
	private static JMenuBar menuBar;
	private static Cursor cursor;
	private static Cursor cursor2;

	private static Clip clip;

	//new comment
	{
		BMGameFrame.playSound();
		setTitle("Bomberman");
		setSize(minSize);
//		setMinimumSize(minSize);
//		setMaximumSize(maxSize);
//		this.setFocusable(true);
//		this.requestFocusInWindow();
		add(new BMClientPanel());
		setLocationRelativeTo(null);
		menuBar = new JMenuBar();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
    	Image cursorImg =  BMLibrary.readImages("mouse2.png");
    	cursor = toolkit.createCustomCursor(cursorImg,new Point(0,0),"hand");
    	
    	Image cursorImg2 =  BMLibrary.readImages("mouse1.png");
    	cursor2 = toolkit.createCustomCursor(cursorImg2,new Point(0,0),"hand2");
    	
    	JMenuItem help = new JMenuItem("Help");
    	//help.setMnemonic('H');
    	//help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,ActionEvent.CTRL_MASK));
    	help.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent ae) {
    			Help.display();
    		}
    	});
    	
    	JMenuItem info = new JMenuItem("info");
    	//help.setMnemonic('H');
    	//help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,ActionEvent.CTRL_MASK));
    	info.addActionListener(new ActionListener() {
    		@Override
    		public void actionPerformed(ActionEvent ae) {
    			if(Desktop.isDesktopSupported())
				{
					try {
						Desktop.getDesktop().browse(new URI("http://jiahuiwe.wix.com/bomberman#!our-story/c2sa"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (URISyntaxException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
    		}
    	});
    	menuBar.add(info);
    	menuBar.add(help);
	}
	
	public static void main(String[] args) {
		BMGameFrame bmgf = new BMGameFrame();
		//MusicLibrary.startGame();
		bmgf.setJMenuBar(menuBar);
		bmgf.setVisible(true);
		bmgf.setCursor(cursor);
		bmgf.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent e){
				bmgf.setCursor(cursor2);				
				
			}
			public void mouseReleased(MouseEvent e){
				bmgf.setCursor(cursor);

				}

			
		});
		

	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("type");
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("press");
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Release");
	}
	
	public static void playSound() {
		try {
			
			URL url = MusicLibrary.class.getResource("opening-fmv.wav");

			System.out.println(url);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			System.out.println(audioIn);
			clip = AudioSystem.getClip();
			System.out.println(clip);
			clip.open(audioIn);
			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true){
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (!clip.isActive()){
							BMGameFrame.clip.start();
						}
					}
				}
			}).start();
		}
		catch (IOException e){
			System.out.println("IOE");
		} catch (UnsupportedAudioFileException e) {
			System.out.println("IOUnsupportedAudioFileExceptionE");
		} catch (LineUnavailableException e) {
			System.out.println("LineUnavailableException");
		}
	}
	
}