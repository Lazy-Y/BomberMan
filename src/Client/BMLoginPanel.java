package Client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Utilities.BMFontLibrary;
import customUI.PaintedPanel;
public class BMLoginPanel extends PaintedPanel implements KeyListener{
	
	
	public JTextField nameInput;
	private JTextField passwordInput;
	private JButton signupB;
	private JButton quickGameB;
	private JButton loginB;
	private PaintedPanel titlePanel = new PaintedPanel(null);
	private PaintedPanel passwordPanel = new PaintedPanel(null);
	private JPanel panel;
	private JLabel label;
	private JPanel panel_1;
	ActionListener signup;
	ActionListener quickG;
	ActionListener login;
	ActionListener connect_Server;
	 BMSigninPage signin;
	
	BMLoginPanel (ActionListener signup, ActionListener quickG, ActionListener login, ActionListener connect_Server,Image image)
	{
		super(image);
		this.setFocusable(true);
		this.requestFocusInWindow();
		this.addKeyListener(this);
		setLayout(new BorderLayout(0, 0));
		this.signup = signup;
		this.quickG = quickG;
		this.login = login;
		this.connect_Server = connect_Server;
		

		
		panel_1 = new JPanel();
		panel_1.setOpaque(false);
		add(panel_1, BorderLayout.SOUTH);
		
		label = new JLabel("Press Any Key To Proceed");
		//String str = label.getText();
		
		
		panel_1.add(label);
		label.setFont(BMFontLibrary.getFont("font.ttf", Font.PLAIN, 30));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		
	
		new Thread(new Runnable(){
			@Override
			public void run() {
				int color = 0;
				boolean add = true;
				while (true){
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.out.println(e.getMessage());
					}
					if (add == true) color++;
					else color--;
					BMLoginPanel.this.label.setForeground(new Color(color,255-color,color));
					if (color <= 0) add = true;
					else if (color >= 255) add = false;
				}
			}			
		}).start();
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Hello1");
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Hello2");
		
		signin = new BMSigninPage(signup,quickG,login,connect_Server);
		signin.setVisible(true);
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Hello3");
		
	}
	public void closeSignup() {
		signin.setVisible(false);
		
	}
	public BMSigninPage getSignin() {
		// TODO Auto-generated method stub
		return signin;
	}
}
