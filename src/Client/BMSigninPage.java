package Client;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import Utilities.BMFontLibrary;
import Utilities.BMLibrary;
import customUI.PaintedPanel;
public class BMSigninPage extends JFrame{
	private static final long serialVersionUID = 5147395078473323173L;
	private final static Dimension minSize = new Dimension(320,480);
		private final static Dimension maxSize = new Dimension(960,640);
	public JTextField txtUsername;
	public JTextField txtPassword;
	public JButton connectServer;

public JLabel label;
	public BMSigninPage(ActionListener quickG, ActionListener signup, ActionListener login, ActionListener connect_Server)
	{
		setResizable(false);
//		setIconImage(Toolkit.getDefaultToolkit().getImage(BMSigninPage.class.getResource("/Utilities/images/resultBG.png")));
		setTitle("Bomberman Sign In");
		setSize(minSize);
		setLocationRelativeTo(null);
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		
//		setContentPane(new JLabel(new ImageIcon("/Utilities/images/resultBG.png")));
		
		
		txtUsername = new JTextField(SwingConstants.CENTER);
		txtUsername.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 20));
		txtUsername.setHorizontalAlignment(SwingConstants.CENTER);
		txtUsername.setText("Username");
		txtUsername.setColumns(10);
		txtUsername.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                txtUsername.setText("");
            }
        });
		
		txtPassword = new JTextField(SwingConstants.CENTER);
		txtPassword.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 20));
		txtPassword.setHorizontalAlignment(SwingConstants.CENTER);
		txtPassword.setText("Password");
		txtPassword.setColumns(10);
		txtPassword.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                txtPassword.setText("");
            }
        });
		
		JButton btnQuickgame = new JButton("QuickGame");
		btnQuickgame.addActionListener(quickG);
		btnQuickgame.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 20));
		
		JButton btnSignup = new JButton("Signup");
		btnSignup.addActionListener(signup);
		btnSignup.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 20));
		btnSignup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		
		
		PaintedPanel mainPanel = new PaintedPanel(BMLibrary.readImages("resultBG.png"));
		
		JButton btnLogin_1 = new JButton("Login");
		btnLogin_1.addActionListener(login);
		btnLogin_1.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 20));
		
		JButton btnConnectServer = new JButton("Connect Server");
		GroupLayout groupLayout = new GroupLayout(mainPanel);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(60)
					.addComponent(btnQuickgame, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
					.addGap(60))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(105)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnSignup, GroupLayout.PREFERRED_SIZE, 110, Short.MAX_VALUE)
						.addComponent(btnLogin_1, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
					.addGap(105))
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(60)
							.addComponent(btnConnectServer, GroupLayout.PREFERRED_SIZE, 201, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(60)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(txtPassword, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
								.addComponent(txtUsername, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE))))
					.addGap(60))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(69)
					.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
					.addComponent(btnConnectServer)
					.addGap(18)
					.addComponent(btnQuickgame)
					.addGap(18)
					.addComponent(btnSignup)
					.addGap(18)
					.addComponent(btnLogin_1)
					.addGap(67))
		);
		

		label = new JLabel("");
		btnConnectServer.addActionListener(connect_Server);
		btnConnectServer.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 20));

		
		mainPanel.setLayout(groupLayout);
		getContentPane().add(mainPanel);
	}
}
//
//
//package Client;
//
//import java.awt.Dimension;
//import java.awt.Font;
//import java.awt.Image;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//
//import javax.swing.GroupLayout;
//import javax.swing.GroupLayout.Alignment;
//import javax.swing.JButton;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//import javax.swing.LayoutStyle.ComponentPlacement;
//import javax.swing.SwingConstants;
//
//import Utilities.BMFontLibrary;
//import Utilities.BMLibrary;
//import customUI.PaintedPanel;
//import javax.swing.JLabel;
//
//public class BMSigninPage extends JFrame{
//	private static final long serialVersionUID = 5147395078473323173L;
//
//	private final static Dimension minSize = new Dimension(320,480);
//		private final static Dimension maxSize = new Dimension(960,640);
//	public JTextField txtUsername;
//	public JTextField txtPassword;
//public JLabel label;
//
//	public BMSigninPage(ActionListener quickG, ActionListener signup, ActionListener login)
//	{
//		setResizable(false);
////		setIconImage(Toolkit.getDefaultToolkit().getImage(BMSigninPage.class.getResource("/Utilities/images/resultBG.png")));
//		setTitle("Bomberman Sign In");
//		setSize(minSize);
//		setLocationRelativeTo(null);
//		//setDefaultCloseOperation(EXIT_ON_CLOSE);
//		
////		setContentPane(new JLabel(new ImageIcon("/Utilities/images/resultBG.png")));
//		
//		txtUsername = new JTextField(SwingConstants.CENTER);
//		txtUsername.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 20));
//		txtUsername.setHorizontalAlignment(SwingConstants.CENTER);
//		txtUsername.setText("Username");
//		txtUsername.setColumns(10);
//		txtUsername.addMouseListener(new MouseAdapter(){
//            @Override
//            public void mouseClicked(MouseEvent e){
//                txtUsername.setText("");
//            }
//        });
//		
//		txtPassword = new JTextField(SwingConstants.CENTER);
//		txtPassword.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 20));
//		txtPassword.setHorizontalAlignment(SwingConstants.CENTER);
//		txtPassword.setText("Password");
//		txtPassword.setColumns(10);
//		txtPassword.addMouseListener(new MouseAdapter(){
//            @Override
//            public void mouseClicked(MouseEvent e){
//                txtPassword.setText("");
//            }
//        });
//		
//		JButton btnQuickgame = new JButton("QuickGame");
//		btnQuickgame.addActionListener(quickG);
//		btnQuickgame.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 20));
//		
//		JButton btnSignup = new JButton("Signup");
//		btnSignup.addActionListener(signup);
//		btnSignup.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 20));
//		btnSignup.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//			}
//		});
//		
//		
//		
//		PaintedPanel mainPanel = new PaintedPanel(BMLibrary.readImages("resultBG.png"));
//		
//		JButton btnLogin_1 = new JButton("Login");
//		btnLogin_1.addActionListener(login);
//		btnLogin_1.setFont(BMFontLibrary.getFont("font2.otf", Font.PLAIN, 20));
//		
//		label = new JLabel("");
//		GroupLayout groupLayout = new GroupLayout(mainPanel);
//		groupLayout.setHorizontalGroup(
//			groupLayout.createParallelGroup(Alignment.TRAILING)
//				.addGroup(groupLayout.createSequentialGroup()
//					.addGap(60)
//					.addComponent(btnQuickgame, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
//					.addGap(60))
//				.addGroup(groupLayout.createSequentialGroup()
//					.addGap(105)
//					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
//						.addComponent(btnSignup, GroupLayout.PREFERRED_SIZE, 110, Short.MAX_VALUE)
//						.addComponent(btnLogin_1, GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
//					.addGap(105))
//				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
//					.addGap(100)
//					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
//						.addGroup(groupLayout.createSequentialGroup()
//							.addComponent(label)
//							.addContainerGap())
//						.addGroup(groupLayout.createSequentialGroup()
//							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
//								.addComponent(txtPassword, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
//								.addComponent(txtUsername, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE))
//							.addGap(100))))
//		);
//		groupLayout.setVerticalGroup(
//			groupLayout.createParallelGroup(Alignment.LEADING)
//				.addGroup(groupLayout.createSequentialGroup()
//					.addGap(35)
//					.addComponent(label)
//					.addGap(18)
//					.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//					.addGap(18)
//					.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
//					.addPreferredGap(ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
//					.addComponent(btnQuickgame)
//					.addGap(18)
//					.addComponent(btnSignup)
//					.addGap(18)
//					.addComponent(btnLogin_1)
//					.addGap(67))
//		);
//
//		mainPanel.setLayout(groupLayout);
//		getContentPane().add(mainPanel);
//
//	}
//}
