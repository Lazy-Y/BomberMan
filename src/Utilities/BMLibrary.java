package Utilities;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
public class BMLibrary {
//	public static final String path = BMLibrary.class.getResource(".").getPath();
	public static final String path = "/Utilities/images/";
	private File file;
	public final String SQLName = "SQLName";
	
	private static Map<String, Image> imageMap;
	
	static{
		imageMap = new HashMap<String,Image>();
	}
	//read Images
	public BMLibrary(String path){
		
	}
	
	public static Image readImages(String path) {
		Image img = imageMap.get(path);
		if(img == null) {
			try {
				if(path.startsWith("http")) {
					img = ImageIO.read(new URL(path));
				}
				else img = ImageIO.read(BMLibrary.class.getResource(BMLibrary.path+path));
			}
			catch (IOException e) { System.out.println("Error reading image: " + path + e); return null; }
			imageMap.put(path, img);
		}
		return img;
	}
//	
//	
//	public static Image readImages(String path){
//		Image img = null;
//		try {
//			img = ImageIO.read(new File(BMLibrary.path+path));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return img;
//	}
//	
	public static int[ ][ ] getGameMap(){
		int [][] gamemap = new int[16][16];
		
		
		List<String[]> data = new ArrayList<>();		 
		try {
			Scanner in = new Scanner(BMLibrary.class.getResource(path+"map1.txt").openStream());
		 
		    while (in.hasNextLine()) {
		           String str = in.nextLine();
		 
		            data.add(str.split(" ,"));
		    }
		} catch (Exception e) {
		            e.printStackTrace();
		}
		
		 
		String[][] result = data.toArray(new String[][] {});
		
		for (int i=0 ; i<16 ; i++){
			for (int j =0; j<16 ; j++){
				gamemap[i][j] = Integer.parseInt(result[i][j]);
			}
		}

		return gamemap;
	}
}