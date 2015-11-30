package Client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import Utilities.BMFontLibrary;
import Utilities.BMLibrary;
import customUI.PaintedButton;
import customUI.PaintedPanel;

public class BMResultFrame  extends JFrame{
	public BMResultFrame( Vector<TreeMap<String, Object>> result, ActionListener playingGame)
	{

		setTitle("Bomberman Result");
		setSize(640, 480);
		setLocationRelativeTo(null);
		PaintedPanel all = new PaintedPanel (BMLibrary.readImages("resultBG.png"));
	
		all.setLayout(new BoxLayout(all, BoxLayout.Y_AXIS));
		
		PaintedPanel topP = new PaintedPanel(null);
	
		JLabel topLabel = new JLabel ("Top");
		topLabel.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 35));
		topP.add(topLabel);
		PaintedPanel midP = new PaintedPanel(null);
		midP.setLayout(new BoxLayout(midP, BoxLayout.X_AXIS));
		System.out.println(result);
		int highest = -1;

		for (int i=0; i< result.size(); i++) {
			PaintedPanel single =new PaintedPanel( BMLibrary.readImages("frame5.png"));
			
			single.setLayout(new BoxLayout(single, BoxLayout.Y_AXIS));
			JLabel zero = new JLabel ("  "+(String)result.get(i).get("username") );
			zero.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 15));
			zero.setHorizontalAlignment(SwingConstants.CENTER);


			JLabel one = new JLabel ("      "+(int)result.get(i).get("points") +"   "+ "points  ");
			one.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 15));

			if ((int)result.get(i).get("points")> highest) {
				highest = (int)result.get(i).get("points");
				topLabel.setText("Champion is " + (String)result.get(i).get("username"));
			}
			one.setHorizontalAlignment(SwingConstants.CENTER);
			JLabel two = new JLabel ("      "+(int)result.get(i).get("kill") +"   "+"kill ");
			two.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 15));

			two.setHorizontalAlignment(SwingConstants.CENTER);
			JLabel three = new JLabel ("      "+(int)result.get(i).get("death") +"   "+"death ");
			three.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 15));

			three.setHorizontalAlignment(SwingConstants.CENTER);
			JLabel four = new JLabel ("      "+ (int)result.get(i).get("item") +"   "+"items ");
			four.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 15));

			four.setHorizontalAlignment(SwingConstants.CENTER);
			
			single.add(Box.createVerticalGlue());
			single.add(zero);
			single.add(Box.createVerticalGlue());
			single.add(one);
			single.add(Box.createVerticalGlue());
			single.add(two);
			single.add(Box.createVerticalGlue());
			single.add(three);
			single.add(Box.createVerticalGlue());
			single.add(four);
			single.add(Box.createVerticalGlue());
//			single.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			midP.add(Box.createHorizontalGlue());
			midP.add(single);
			
		}
		midP.add(Box.createHorizontalGlue());
		PaintedPanel btmP = new PaintedPanel(null);
		PaintedButton okButton = new PaintedButton("Quit", BMLibrary.readImages("button2.png"), BMLibrary.readImages("button2-0.png"),20);
		okButton.setVerticalAlignment(SwingConstants.BOTTOM);
		btmP.add(Box.createVerticalGlue());
		btmP.add(okButton);
//		btmP.setPreferredSize(new Dimension(50, 480));
		all.add(topP);
		all.add(midP);
		all.add(btmP);
		okButton.addActionListener( playingGame);
		okButton.addActionListener(new ActionListener (){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Component component = (Component) e.getSource();
		        JFrame frame = (JFrame) SwingUtilities.getRoot(component);
		        frame.setVisible(false);
			}
			
		});
		
		
		
		final long serialVersionUID = 5147395078473323173L;		
		final Dimension minSize = new Dimension(320,480);
		
		this.add(all);
		repaint();
	}
	
//	public static main() {
//		
//	}

}
