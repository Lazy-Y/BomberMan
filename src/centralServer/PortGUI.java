package centralServer;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
public class PortGUI extends JFrame {
	public static final long serialVersionUID = 1;
	private JTextField portTextField;
	private JLabel descriptionLabel, portLabel, portErrorLabel;
	private JButton submitPortButton;
	private Lock portLock;
	private Condition portCondition;
	private ServerSocket ss;
	public PortGUI() {
		super("BMServer Port Selection");
		initializeVariables();
		createGUI();
		addActionAdapters();
		setVisible(true);
	}
	
	private void initializeVariables() {
		descriptionLabel = new JLabel(ServerConstants.portDescriptionString);
		portLabel = new JLabel(ServerConstants.portLabelString);
		portErrorLabel = new JLabel();
		portTextField = new JTextField(20);
		portTextField.setText("" + ServerConstants.defaultPort);
		submitPortButton = new JButton(ServerConstants.submitPortString);
		portLock = new ReentrantLock();
		portCondition = portLock.newCondition();
		ss = null;
	}
	
	private void createGUI() {
		setSize(ServerConstants.portGUIwidth, ServerConstants.portGUIheight);
		GridLayout gl = new GridLayout(4, 1);
		setLayout(gl);
		add(descriptionLabel);
		JPanel portFieldPanel = new JPanel();
		portFieldPanel.setLayout(new FlowLayout());
		portFieldPanel.add(portLabel);
		portFieldPanel.add(portTextField);
		add(portErrorLabel);
		add(portFieldPanel);
		add(submitPortButton);
	}
	
	private void addActionAdapters() {
		class PortListener implements ActionListener {
			public void actionPerformed(ActionEvent ae) {
				String portStr = portTextField.getText();
				int portNumber = -1;
				try {
					portNumber = Integer.parseInt(portStr);
				} catch (Exception e) {
					portErrorLabel.setText(ServerConstants.portErrorString);
					return;
				}
				if (portNumber > ServerConstants.lowPort && portNumber < ServerConstants.highPort) {
					try {
						ServerSocket tempss = new ServerSocket(portNumber);
						portLock.lock();
						ss = tempss;
						portCondition.signal();
						portLock.unlock();
						PortGUI.this.setVisible(false);
					} catch (IOException ioe) {
						// this will get thrown if I can't bind to portNumber
						portErrorLabel.setText(ServerConstants.portAlreadyInUseString);
					}
				}
				else {
					portErrorLabel.setText(ServerConstants.portErrorString);
					return;
				}
			}
		}
		submitPortButton.addActionListener(new PortListener());
		portTextField.addActionListener(new PortListener());
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
	}
	
	public ServerSocket getServerSocket() {
		while (ss == null) {
			portLock.lock();
			try {
				portCondition.await();
			} catch (InterruptedException ie) {
				System.out.println("Interrupted Exception: PortGUI.getServerSocket");
			}
			portLock.unlock();
		}
		return ss;
	}
}