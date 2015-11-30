package Client;

import java.awt.Dimension;
import java.util.Queue;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import Utilities.BMLibrary;
import centralServer.GameRecord;
import centralServer.RankContainer;
import customUI.PaintedPanel;

public class BMRankPanel extends JFrame{
	/**
	 * Vector for myrank
	 * Queue for wrdrank 
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private PaintedPanel main;
	private JTable myRcdTable;
	private JTable wrdRcdTable; 
	
	BMRankPanel(Queue<RankContainer> queue, Vector<GameRecord> vector)
	{
		setSize(new Dimension(640,480));
		main = new PaintedPanel(BMLibrary.readImages("resultBG.png"));
		main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		
		JPanel myRecord = new JPanel();
		JPanel wrdRecord = new JPanel();
		
		myRecord.setOpaque(false);
		wrdRecord.setOpaque(false);
		
		
//		String[] columnNames = {"Rank",
//                "Name",
//                "Games",
//                "Total Points",
//                "Total Time"};
//		
//		Object[][] data = {
//			    {"Kathy", "Smith",
//			     "Snowboarding", new Integer(5), new Boolean(false)},
//			    {"John", "Doe",
//			     "Rowing", new Integer(3), new Boolean(true)},
//			    {"Sue", "Black",
//			     "Knitting", new Integer(2), new Boolean(false)},
//			    {"Jane", "White",
//			     "Speed reading", new Integer(20), new Boolean(true)},
//			    {"Joe", "Brown",
//			     "Pool", new Integer(10), new Boolean(false)}
//			};
//		data[0][0] = "adsf";
//		myRcdTable = new JTable(data, columnNames);
//		wrdRcdTable = new JTable(data, columnNames);
		
		
		String[] myColumnNames = {
                "Points",
                "Kills",
                "Death", 
                "Time"};
		 myRcdTable = new JTable();
		 DefaultTableModel dtm = new DefaultTableModel(0, 0);
		 dtm.setColumnIdentifiers(myColumnNames);
		       myRcdTable.setModel(dtm);
		if (vector == null ) {
			System.out.println("Nothing received");
			
		} else {
			System.out.println(vector.size());
		}
		for (GameRecord gr : vector) {
			dtm.addRow(new Object[] { gr.getPoints(), gr.getKillCount(), gr.getDeathCount(),
	                gr.getTime() });
		}
		
		String[] wrdColumnNames = {
                "Name", 
                "Total Points"};
		wrdRcdTable = new JTable();
		 DefaultTableModel dtm1 = new DefaultTableModel(0, 0);
		 dtm1.setColumnIdentifiers(wrdColumnNames);
		       wrdRcdTable.setModel(dtm1);
		
		while(!queue.isEmpty()) {
			RankContainer temp = queue.poll();
			dtm1.addRow(new Object[] { temp.getUsername(), temp.getRating() });
		}
		
		
		
		
		JScrollPane myRcdScrollPane = new JScrollPane(myRcdTable);
		myRcdScrollPane.setOpaque(false);
		myRcdTable.setFillsViewportHeight(true);
		
		JScrollPane wrdRcdScrollPane = new JScrollPane(wrdRcdTable);
		wrdRcdScrollPane.setOpaque(false);
		wrdRcdTable.setFillsViewportHeight(true);
		
		wrdRecord.add(wrdRcdScrollPane);
		myRecord.add(myRcdScrollPane);
		
		tabbedPane.add("My Record", myRecord);
		tabbedPane.add("World Record", wrdRecord);
		tabbedPane.setOpaque(false);
		
//		btnReturn.addActionListener(returnMain);
		
		main.add(tabbedPane);
//		main.add(btnReturn);
		add(main);
		
	}
	
	public void setupTable(Queue<RankContainer> wrdRankQueue, Vector<GameRecord> myRankVector) {
		/* personal record
		this.points = points;
		this.kills = kills;
		this.deaths = deaths;
		this.time = time;
		*/
		
		String[] myColumnNames = {
                "Points",
                "Kills",
                "Death", 
                "Time"};
		 myRcdTable = new JTable();
		 DefaultTableModel dtm = new DefaultTableModel(0, 0);
		 dtm.setColumnIdentifiers(myColumnNames);
		       myRcdTable.setModel(dtm);
		
		for (GameRecord gr : myRankVector) {
			dtm.addRow(new Object[] { gr.getPoints(), gr.getKillCount(), gr.getDeathCount(),
	                gr.getTime() });
		}
		
		String[] wrdColumnNames = {
                "Name", 
                "Total Points"};
		wrdRcdTable = new JTable();
		 DefaultTableModel dtm1 = new DefaultTableModel(0, 0);
		 dtm1.setColumnIdentifiers(wrdColumnNames);
		       myRcdTable.setModel(dtm1);
		
		while(!wrdRankQueue.isEmpty()) {
			RankContainer temp = wrdRankQueue.poll();
			dtm1.addRow(new Object[] { temp.getUsername(), temp.getRating() });
		}
	}
}
