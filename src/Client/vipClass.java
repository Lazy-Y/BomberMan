package Client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import Utilities.BMFontLibrary;

class vipClass extends JFrame{
	public static final String secret = "handsomeMiller";
	// is vip 
	public static boolean vip = false;
	private JLabel reminder;
	private JTextField input;
	private JTextField textField;
	private BMMenuPanel panel;
	public vipClass(BMMenuPanel panel1)
	{
		panel = panel1;
		setTitle("Become VIP");
		setSize(new Dimension(320,200));
		setResizable(false);
		setLocationRelativeTo(null);
		
		
		JLabel lblPleaseInputYour = new JLabel("Please input your Secret Code");
		lblPleaseInputYour.setFont(BMFontLibrary.getFont("font.ttf", Font.PLAIN, 10));
		textField = new JTextField();
		textField.setColumns(10);
		textField.setFont(BMFontLibrary.getFont("font.ttf", Font.PLAIN, 10));
		
		
		JButton btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (textField.getText().equals(secret))
				{
					vip = true;
					panel.btnBecomeVip.setText("Welcome vip");
				}
				else {
					vip = false;
					panel.btnBecomeVip.setText("Become vip");
				}
				setVisible(false);
			}
		});
		btnConfirm.setFont(BMFontLibrary.getFont("font.ttf", Font.PLAIN, 10));
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(74)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(lblPleaseInputYour)
							.addGap(60))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(btnConfirm)
								.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
							.addGap(82)))
					.addGap(0, 0, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(30)
					.addComponent(lblPleaseInputYour)
					.addGap(18, 18, Short.MAX_VALUE)
					.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(28)
					.addComponent(btnConfirm)
					.addGap(31))
		);
		getContentPane().setLayout(groupLayout);
		
	}
}