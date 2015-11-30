package centralServer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/*
 * Copied from Factory Code
 */
public class BMCentralServerGUI extends JFrame {

	public static final long serialVersionUID = 1;
	private static JTextArea textArea;
	private JScrollPane textAreaScrollPane;
	private JButton connectButton;
	private JButton disconnectButton;

	public BMCentralServerGUI(MySQLDriver driver) {
		super("Central Server Monitor");
		initializeVariables();
		createGUI();
		addActionAdapters(driver);
		setVisible(false);
	}
	
	private void initializeVariables() {
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		 caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		textAreaScrollPane = new JScrollPane(textArea);
		
		connectButton = new JButton("Connect");
		connectButton.setEnabled(true);
		disconnectButton = new JButton("Disconnect");
		disconnectButton.setEnabled(false);
	}
	
	private void createGUI() {
		setSize(500, 500);
		JPanel northPanel = new JPanel();
		northPanel.add(connectButton);
		northPanel.add(disconnectButton);
		add(northPanel, BorderLayout.NORTH);
		add(textAreaScrollPane, BorderLayout.CENTER);
	}
	
	private void addActionAdapters(MySQLDriver driver) {
		addWindowListener(new WindowAdapter() {
			  public void windowClosing(WindowEvent we) {
				  System.exit(0);
			  }
		});
		
		connectButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				driver.connect();
				connectButton.setEnabled(false);
				disconnectButton.setEnabled(true);
			}
		});
		disconnectButton.addActionListener(
		new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				driver.stop();
				connectButton.setEnabled(true);
				disconnectButton.setEnabled(false);
			}
		});
	}
	
	public static void addMessage(String msg) {
		if (textArea.getText() != null && textArea.getText().trim().length() > 0) {
			textArea.append("\n" + msg);
		}
		else {
			textArea.setText(msg);
		}
	}
}

