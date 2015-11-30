package Utilities;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.IOException;
import java.net.URL;

import javax.print.attribute.standard.Media;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.omg.CORBA.portable.InputStream;

public class MusicLibrary {
//	public static void main(String[] args){
//		victory();
//	}
	public static final String path = "/Utilities/bgm/";
	
	
	public static void startGame(){
		playSound("roomBGM.mp3");
	}
	
	public static void playSound(final String address) {
		
		
		try {
			System.out.println(MusicLibrary.class);
			URL url = MusicLibrary.class.getResource(path+address);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			System.out.println(audioIn);
			Clip clip = AudioSystem.getClip();
			System.out.println(clip);
			clip.open(audioIn);
			clip.start();
		}
		catch (IOException e){
			System.out.println("IOE");
		} catch (UnsupportedAudioFileException e) {
			System.out.println("IOUnsupportedAudioFileExceptionE");
		} catch (LineUnavailableException e) {
			System.out.println("LineUnavailableException");
		}
	}
}