import java.awt.Font;
 
import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.io.*;
import java.net.*;
import java.util.*;

import java.awt.*;


public class ConvertTeluguToEnglish
{ 
	private String telugu = "\u0C05\u0C2E\u0C4D\u0C2E";
	private String teluguHeading = "\u0C1C\u0C3E\u0C35\u0C3E \u0C24\u0C46\u0C32\u0C41\u0C17\u0C41";
		
	public static void main(String[] args)  throws Exception
	{
		new ConvertTeluguToEnglish();
	}
 
	public ConvertTeluguToEnglish() throws Exception
	{
		createFont("fonts/Pothana.ttf");
		createFont("fonts/Lohit.ttf");
		
		//Lohit
		//Vemana2000
		//Pothana2000
		
		Font fontLohit = new Font("Lohit Telugu", Font.PLAIN, 32);
		Font fontPothana = new Font("Pothana2000", Font.PLAIN, 22);

		JFrame.setDefaultLookAndFeelDecorated(true);
		 
		JFrame frame1 = new JFrame();
		frame1.setSize(800, 600);
		frame1.setVisible(true);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		frame1.getLayeredPane().getComponent(1).setFont(fontLohit);
		frame1.setTitle(teluguHeading);	 

		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("data.rtf"), "UTF-8"));				

		String totText = "";
		String line = "";
		
		while((line = br.readLine()) != null)
			totText += line;

		JTextArea taDisplay = new JTextArea(totText);
		taDisplay.setFont(fontPothana);
		frame1.getContentPane().add(taDisplay);
	}
	
	public void createFont(String fontFilePath)
	{
		try {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(fontFilePath)));
		} catch (IOException|FontFormatException e) {			
		}		
	}
}