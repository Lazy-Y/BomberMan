package centralServer;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Utilities.BMLibrary;
import Utilities.CommandLineMonitor;
import customUI.PaintedButton;
import customUI.PaintedPanel;

/*
 * Copied from Factory Code: client.HostAndPortGUI
 */
public class HostAndPortGUI extends JFrame {
	public static final long serialVersionUID = 1;
	private JTextField portTextField, hostnameTextField;
	private JLabel descriptionLabel, portLabel, hostnameLabel, errorLabel;
	private PaintedButton connectButton;
	private Lock hostAndPortLock;
	private Condition hostAndPortCondition;
	private Socket socket;
	
	private PaintedPanel outerPanel;
	
	private BMCentralServerClient serverClient;
	public HostAndPortGUI(BMCentralServerClient serverClient) {
		super("Central Server Host and Port");
		this.serverClient = serverClient;
		initializeVariables();
		createGUI();
		addActionAdapters();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private void initializeVariables() {
		outerPanel = new PaintedPanel(BMLibrary.readImages("background4.png"));
		
		socket = null;
		descriptionLabel = new JLabel(ServerConstants.hostAndPortDescriptionString);
		portLabel = new JLabel(ServerConstants.portLabelString);
		hostnameLabel = new JLabel(ServerConstants.hostnameLabelString);
		errorLabel = new JLabel();
		portTextField = new JTextField(20);
		portTextField.setText("" + ServerConstants.defaultPort);
		hostnameTextField = new JTextField(20);
		hostnameTextField.setText(ServerConstants.defaultHostname);
		connectButton = new PaintedButton(ServerConstants.connectButtonString,
				BMLibrary.readImages("button3-0.png"),
				BMLibrary.readImages("button3.png"),
				24);
		hostAndPortLock = new ReentrantLock();
		hostAndPortCondition = hostAndPortLock.newCondition();
	}
	
	private void createGUI() {
		setSize(ServerConstants.hostAndPortGUIwidth, ServerConstants.hostAndPortGUIheight);
		outerPanel.setLayout(new GridLayout(5, 1));
		outerPanel.add(descriptionLabel);
		outerPanel.add(errorLabel);
		JPanel hostFieldPanel = new JPanel();
		hostFieldPanel.setLayout(new FlowLayout());
		hostFieldPanel.add(hostnameLabel);
		hostFieldPanel.add(hostnameTextField);
		hostFieldPanel.setOpaque(false);
		outerPanel.add(hostFieldPanel);
		JPanel portFieldPanel = new JPanel();
		portFieldPanel.setLayout(new FlowLayout());
		portFieldPanel.add(portLabel);
		portFieldPanel.add(portTextField);
		portFieldPanel.setOpaque(false);
		outerPanel.add(portFieldPanel);
		outerPanel.add(connectButton);
		
		add(outerPanel);
		
	}
	
	private void addActionAdapters() {
		class ConnectListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				String portStr = portTextField.getText();
				int portInt = -1;
				try {
					portInt = Integer.parseInt(portStr);
				} catch (Exception e) {
					errorLabel.setText(ServerConstants.portErrorString);
					return;
				}
				if (portInt > ServerConstants.lowPort && portInt < ServerConstants.highPort) {
					// try to connect
					String hostnameStr = hostnameTextField.getText();
					try {
						socket = new Socket(hostnameStr, portInt);
						hostAndPortLock.lock();
						hostAndPortCondition.signal();
						hostAndPortLock.unlock();
						serverClient.setUp();
						HostAndPortGUI.this.setVisible(false);
					} catch (IOException ioe) {
						errorLabel.setText(ServerConstants.unableToConnectString);
						CommandLineMonitor.printExceptionToCommand(ioe);
						return;
					}
				}
				else { // port value out of range
					errorLabel.setText(ServerConstants.portErrorString);
					return;
				}
			}
		}
		connectButton.addActionListener(new ConnectListener());
		hostnameTextField.addActionListener(new ConnectListener());
		portTextField.addActionListener(new ConnectListener());
	}
	
	public Socket getSocket() {
		while (socket == null) {
			hostAndPortLock.lock();
			try {
				hostAndPortCondition.await();
			} catch (InterruptedException ie) {
				CommandLineMonitor.printExceptionToCommand(ie);
			}
			hostAndPortLock.unlock();
		}
		return socket;
	}
}
