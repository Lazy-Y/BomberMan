package Utilities;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import Utilities.BMFontLibrary;
import customUI.PaintedPanel;

public class Help {

	private static final String filePath = "src/Utilities/help.txt";
	private static final PaintedPanel helpText;
	private static final JDialog helpDisplay;

	static {
		helpText = new PaintedPanel(BMLibrary.readImages("help.png"));
		helpText.setBackground(new Color(250, 200, 100));
//		helpText.setEditable(false);
//		helpText.setLineWrap(true);
//		helpText.setWrapStyleWord(true);
//		helpText.setTabSize(4);
		helpText.setFont(BMFontLibrary.getFont("font.ttf", Font.PLAIN, 14));
		
//		try {
//			Scanner sc = new Scanner(new File(filePath));
//			while (sc.hasNext()) {
//				helpText.append(sc.nextLine()+"\n");
//			}
//			sc.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
		//helpText.setCaretPosition(0);
		
		helpDisplay = new JDialog();
		helpDisplay.setTitle("Help");
		helpDisplay.setModal(true);
		helpDisplay.setSize(633, 474);
		helpDisplay.setResizable(false);
		
		JScrollPane jsp = new JScrollPane(helpText);
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		helpDisplay.add(jsp);
	}
	
	public static void display() {
		helpDisplay.setLocationRelativeTo(null);
		helpDisplay.setVisible(true);
	}

}
