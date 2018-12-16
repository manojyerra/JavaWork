import java.awt.Font;
 
import javax.swing.JFrame;
import javax.swing.JTextArea;

import java.io.*;
import java.net.*;
import java.util.*;

/**
* Copy gautami.ttf from windows\fonts to \java_160_10\jre\lib\fonts\
*
* References:
*
* http://unicode.org/charts/PDF/U0C00.pdf
* http://java.sun.com/javase/technologies/core/basic/intl/faq.jsp
* http://java.sun.com/j2se/1.4.2/docs/guide/intl/fontprop.html
*
* Load font from file
* http://www.java2s.com/Code/Java/2D-Graphics-GUI/Loadfontfromttffile.htm
*
* http://www.roseindia.net/java/example/java/swing/graphics2D/font-paint.shtml
*
* How to get unicode for telugu from lekhini? type in lekhini. copy text to
* following location. Get unicode.
*
* http://rishida.net/tools/conversion/
*
* When you type in text area, we will get squares. Use Baraha IME. Then you can
* type in telugu.
*/

public class JavaTeluguDemo extends JFrame
{ 
	private static final long serialVersionUID = 123123123L;
	private static final Font fontTeluguGautami32 = new Font("gautami",	Font.PLAIN, 32);
	private static final Font fontTeluguGautami10 = new Font("gautami",	Font.PLAIN, 22);
	
	private static String telugu = "\u0C05\u0C2E\u0C4D\u0C2E";
	private static String teluguHeading = "\u0C1C\u0C3E\u0C35\u0C3E \u0C24\u0C46\u0C32\u0C41\u0C17\u0C41";
 
	public JavaTeluguDemo() throws Exception
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("SaveHere.rtf"), "UTF-8"));				

		String totText = "";
		String line = "";
		
		while((line = br.readLine()) != null)
			totText += line;

		JTextArea taDisplay = new JTextArea(totText);
		taDisplay.setFont(fontTeluguGautami32);
		this.getContentPane().add(taDisplay);
	}
	 
	public static void main(String[] args)  throws Exception
	{
		// We can't change title font in default system look and feel.
		JFrame.setDefaultLookAndFeelDecorated(true);
		 
		JFrame frame1 = new JavaTeluguDemo();
		frame1.setSize(400, 150);
		frame1.setVisible(true);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 
		frame1.getLayeredPane().getComponent(1).setFont(fontTeluguGautami10);
		frame1.setTitle(teluguHeading);	 
	}
}