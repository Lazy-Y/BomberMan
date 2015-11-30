package Client;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;

import Utilities.BMFontLibrary;
import Utilities.BMLibrary;
import customUI.PaintedButton;
import customUI.PaintedLabel;
import customUI.PaintedPanel;

public class BMBoard_Player  extends PaintedPanel {
	private JLabel TimeLabel, HPLabel,AbilityLabel,NameLabel;
	private PaintedLabel SpeedLabel, PowerLabel, Item1Label, Item2Label,Item0Label,DetonatedTime_Label,CoolingTimeLabel ;
	private PaintedButton QuitButton;
	private int time;
	private int total_hp;
	private int curr_hp;
	private String local_username;
	
			public BMBoard_Player(Image image, ActionListener quit){
				super(	image,true);
				setLayout(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				NameLabel = new JLabel ("Name");
				NameLabel.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 30));


				TimeLabel = new JLabel("Time  "  );
				TimeLabel.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 25));

				HPLabel = new JLabel("HP ");
				HPLabel.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 25));

				AbilityLabel = new JLabel("Ability ");
				AbilityLabel.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 20));

				SpeedLabel = new PaintedLabel(null);
				SpeedLabel.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 11));

				PowerLabel = new PaintedLabel(null);
				PowerLabel.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 11));

				Item1Label= new PaintedLabel (null);
				Item1Label.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 15));

				Item2Label= new PaintedLabel (null);
				Item2Label.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 15));

				Item0Label= new PaintedLabel (null);
				Item0Label.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 15));


				CoolingTimeLabel = new PaintedLabel (null);
				CoolingTimeLabel.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 10));

				
				DetonatedTime_Label = new PaintedLabel (null);
				DetonatedTime_Label.setFont(BMFontLibrary.getFont("font3.ttf", Font.PLAIN, 10));

				SpeedLabel.setText("Speed");
				PowerLabel.setText("Power");
				Item0Label.setText("Item");

				Item1Label.setText(" Empty");
				Item2Label.setText(" Empty");;
//
//				AbilityLabel.setText("Ability");
//				CoolingTimeLabel.setText("CoolingTime");
//				DetonatedTime_Label.setText("DetonatedTime_Label");
				
				QuitButton = new PaintedButton ("Quit", BMLibrary.readImages("button2.png"), BMLibrary.readImages("button2-0.png"), 10);
				QuitButton.setFont(BMFontLibrary.getFont("font.ttf", Font.PLAIN, 15));

				
				QuitButton.addActionListener(quit);
			
				
				Item1Label.setPreferredSize(new Dimension(60, 50));
				SpeedLabel.setPreferredSize(new Dimension(60, 50));
				Item2Label.setPreferredSize(new Dimension(60, 50));
				PowerLabel.setPreferredSize(new Dimension(60, 50));
				CoolingTimeLabel.setPreferredSize(new Dimension(120, 30));
				DetonatedTime_Label.setPreferredSize(new Dimension(120, 30));
				
				//QuitButton.setPreferredSize(new Dimension(60, 50));


		//playerPanel add component
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.gridwidth = 2;
				gbc.insets = new Insets(-10,0,10,0);
				this.add(NameLabel,gbc);
				
				
				gbc.gridx = 0;
				gbc.gridy = 1;
				gbc.gridwidth = 2;
				gbc.insets = new Insets(0,0,0,0);
				this.add(TimeLabel,gbc);
				
				gbc.gridx = 0;
				gbc.gridy = 2;
				
				this.add(HPLabel,gbc);
				
				gbc.gridx = 0;
				gbc.gridy = 3;
				gbc.gridwidth = 1;
				this.add(AbilityLabel,gbc);
				
				gbc.gridx = 0;
				gbc.gridy = 4;
				gbc.gridwidth = 1;
				gbc.insets = new Insets(0,5,0,5);

				this.add(SpeedLabel,gbc);
				
				gbc.gridx = 1;
				gbc.gridy = 4;
				gbc.gridwidth = 1;
				
				this.add(PowerLabel,gbc);
				
				gbc.gridx = 0;
				gbc.gridy = 8;
				gbc.gridwidth = 2;
				gbc.insets = new Insets(0,0,0,0);

				this.add(Item1Label,gbc);
				
				gbc.gridx = 0;
				gbc.gridy = 7;
				gbc.gridwidth = 2;
				this.add(Item0Label,gbc);
				
				gbc.gridx = 0;
				gbc.gridy = 9;
				gbc.gridwidth = 2;
				this.add(Item2Label,gbc);
				
				gbc.gridx = 0;
				gbc.gridy = 6;
				gbc.gridwidth = 2;
				this.add(CoolingTimeLabel,gbc);
				
				gbc.gridx = 0;
				gbc.gridy = 5;
				gbc.gridwidth = 2;
				this.add(DetonatedTime_Label,gbc);


				gbc.gridx = 0;
				gbc.gridy = 10;
				gbc.gridwidth = 2;
				gbc.insets = new Insets(0,0,0,0);
				this.add(QuitButton,gbc);

			}
	//PlayerPanel initialize
			
			public void set_up (Vector<TreeMap<String, Object>> players , String username){
				this.local_username = username;
//				System.out.println("local_username" +username );
				NameLabel.setText(username);

				for (int i = 0; i<players.size(); i++) {
//					System.out.println("all_user" +players.get(i).get("username") );

					if (((String)players.get(i).get("username")).equals(username))
					{
						
						curr_hp = (int)(players.get(i).get("hp"));
						total_hp=curr_hp;
//						System.out.println("inside Current_hp" +curr_hp + "total_hp" +total_hp );

						
					}
				}
				
				HPLabel.setText("HP " + curr_hp+ '/' + total_hp);
//				System.out.println("Current_hp" +curr_hp + "total_hp" +total_hp );
				repaint();

				
			}
			
			public void set_move(int time, Vector<TreeMap<String, Object>> players  ){
				for (int i = 0; i<players.size(); i++) {

					if (((String)players.get(i).get("username")).equals(local_username))
					{
//						System.out.println("Speedlabel " +SpeedLabel + " text " +SpeedLabel.getText() );
						curr_hp = (int)(players.get(i).get("hp"));
//						System.out.println("inside Current_hp" +curr_hp + "total_hp" +total_hp );
						SpeedLabel.setText("Speed : " + (players.get(i).get("speed")) );
						CoolingTimeLabel.setText("CoolingTime : " + (players.get(i).get("coolingTime")) );
						DetonatedTime_Label.setText("DetonatedTime : " + (players.get(i).get("denotated")) );
						PowerLabel.setText("Power : " + (players.get(i).get("power")) );
						
						//Item1Label.setText("I1 : " + (players.get(i).get("item1")) );
						
						int item1 = (int)players.get(i).get("item1") + 5;
						int item2 = (int)players.get(i).get("item2") + 5;
						if (item1 == 4) {}
						else{ Image image1 = BMLibrary.readImages("node"+item1+"_0.png");						
							Item1Label.setImage(image1);
							Item1Label.setText("");
						}
						
						if (item2 == 4) {}
						else{ Image image2 = BMLibrary.readImages("node"+item2+"_0.png");						
							Item2Label.setImage(image2);
							Item2Label.setText("");

						}
				



						
					}
				}
				HPLabel.setText("HP " + curr_hp+ '/' + total_hp);
				
				TimeLabel.setText("Time : " +(String)players.get(0).get("time"));
				repaint();
			}
			
			
}
