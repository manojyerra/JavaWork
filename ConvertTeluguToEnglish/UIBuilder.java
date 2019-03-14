import java.awt.Font;
 
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import java.io.*;
import java.net.*;
import java.util.*;

import java.awt.*;
import java.awt.event.*;
import utils.*;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.*;


class UIBuilder
{  
	ConvertTeluguToEnglish ref;

	UIBuilder(ConvertTeluguToEnglish ref)
	{
		this.ref = ref;
	}
	
	void build()
	{
		int sw = ref.config.screenInfo.screenW; 
		int sh = ref.config.screenInfo.screenH;
		
		Dimension monitorSize = Toolkit.getDefaultToolkit().getScreenSize();	
		int monitorW = monitorSize.width;
		int monitorH = monitorSize.height;
				
		int visibleW = sw+18;
		int visibleH = sh+30;

		ref.setSize(visibleW, visibleH);
		ref.setLocation((monitorW-visibleW)/2, (monitorH-visibleH)/2);
		
		ref.setLayout(null);
		ref.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ref.getContentPane().setBackground(Color.GRAY);

		createTextRel(0, 0, sw, sh-50, ref.getContentPane());
		createButtons(0, sh-50, sw, 50, ref.getContentPane());

		ref.setVisible(true);
	}
	
	private void createTextRel(int x, int y, int w, int h, Container contentPane)
	{
		int textAreaH = (int)(h/4) - 5;		
		
		ref.teluguTextArea 		= createLabel(x, y, w, textAreaH); y += h/4;
		ref.writeTextArea 		= createTextArea(x, y, w, textAreaH); y += h/4;
		ref.googleTextArea 		= createTextArea(x, y, w, textAreaH); y += h/4;
		ref.prevWriteTextArea	= createTextArea(x, y, w, textAreaH); 
		
		ref.teluguTextArea.setFont(createTeluguFont("Mandali", Font.PLAIN, 16));
		ref.writeTextArea.setFont(new Font("Times Roman",Font.PLAIN, 20));		
		ref.googleTextArea.setFont(new Font("Times Roman",Font.PLAIN, 20));
		ref.prevWriteTextArea.setFont(new Font("Times Roman",Font.PLAIN, 20));		
		
		contentPane.add(ref.teluguTextArea);
		contentPane.add(ref.writeTextArea);
		contentPane.add(ref.googleTextArea);
		contentPane.add(ref.prevWriteTextArea);
	}

	private void createButtons(int x, int y, int w, int h, Container contentPane)
	{
		int btnH = 30;
		int btnW = 175;
		int gapBtw = 1;
		
		ref.goToPageStart = new JButton("Page Start");
		ref.goToPageEnd   = new JButton("Page End");
		ref.goToLine = new JButton("Go to line");
		ref.goToUnansweredLine = new JButton("Go To Unanswered Line");
		ref.prevPage  = new JButton("Prev Page");
		ref.nextPage  = new JButton("Next Page");
		ref.goToPage  = new JButton("Go To Page");
		ref.convertRawPageToPage = new JButton("Convert RawPage");
		
		ref.goToPageStart.setBounds(x, y, btnW, btnH); 		x = x+btnW+gapBtw;
		ref.goToPageEnd.setBounds(x, y, btnW, btnH); 		x = x+btnW+gapBtw;
		ref.goToLine.setBounds(x, y, btnW, btnH); 			x = x+btnW+gapBtw;
		ref.goToUnansweredLine.setBounds(x, y, btnW, btnH); x = x+btnW+gapBtw;
		ref.prevPage.setBounds(x, y, btnW, btnH); 			x = x+btnW+gapBtw;
		ref.nextPage.setBounds(x, y, btnW, btnH); 			x = x+btnW+gapBtw;
		ref.goToPage.setBounds(x, y, btnW, btnH); 			x = x+btnW+gapBtw;		
		ref.convertRawPageToPage.setBounds(x, y, btnW, btnH); x = x+btnW+gapBtw;
		
		contentPane.add(ref.goToPageStart);
		contentPane.add(ref.goToPageEnd);
		contentPane.add(ref.goToLine);
		contentPane.add(ref.goToUnansweredLine);
		contentPane.add(ref.prevPage);
		contentPane.add(ref.nextPage);
		contentPane.add(ref.goToPage);		
		contentPane.add(ref.convertRawPageToPage);		
	}
	
	JLabel createLabel(int x, int y, int w, int h)
	{
		JLabel label = new JLabel();
		label.setOpaque(true);	
		label.setBackground(Color.BLACK);
		label.setForeground(Color.WHITE);
		label.setBounds(x, y, w, h);
		//label.setLineWrap(true);
        //label.setWrapStyleWord(true);
		return label;
	}

	JTextArea createTextArea(int x, int y, int w, int h)
	{
		JTextArea textArea = new JTextArea();
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.WHITE);		
		textArea.setBounds(x, y, w, h);
		textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
		textArea.setCaretColor(Color.WHITE);
		return textArea;
	}
	
	Font createTeluguFont(String fontName, int fontStyle, int fontSize)
	{
		createFont("fonts/"+fontName+".ttf");
		return new Font(fontName, fontStyle, fontSize);		
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


//Font family names
//Lohit
//Vemana2000
//Pothana2000

		// createFont("fonts/Pothana.ttf");
		// createFont("fonts/Lohit.ttf");
		// createFont("fonts/Vemana.ttf");
		// createFont("fonts/Gautami.ttf");
		// createFont("fonts/Sree Krushnadevaraya.ttf");
		// createFont("fonts/Mandali.ttf");
		
		// Font fontLohit = new Font("Lohit Telugu", Font.PLAIN, 22);
		// Font fontPothana = new Font("Pothana2000", Font.PLAIN, 22);
		// Font fontVemana = new Font("Vemana2000", Font.PLAIN, 22);
		// Font fontGautami = new Font("Gautami", Font.PLAIN, 22);
		// Font fontKrishna = new Font("Sree Krushnadevaraya", Font.PLAIN, 22);
		// Font fontMandali = new Font("Mandali", Font.PLAIN, 22);


		// Page page = new Page("Pages/1");
		
		// while(page.hasNextEnglishSentence())
		// {
			// String englishLine = page.nextEnglishSentence();
			// String teluguLine = page.nextTeluguSentence();
			
			// System.out.println(englishLine);
		// }


		// Page page = new Page("Pages/1");
		
		// String teluguLine = page.nextTeluguSentence();
		// teluguLine = page.nextTeluguSentence();


	//writeTextArea.getDocument().addDocumentListener(this); 
	
	// @Override
	// public void insertUpdate(DocumentEvent e) 
	// {
		// try
		// {
			// Document doc = e.getDocument();
			// String text = doc.getText(0, doc.getLength());
			// char lastChar = text.charAt(text.length()-1);
			
			// if(lastChar == '\n')
			// {
				// if(page.hasNextTeluguSentence())
				// {
					// teluguTextArea.setText(page.nextTeluguSentence());
					// page.nextEnglishSentence();
				// }
			// }			
		// }
		// catch(Exception exce) {
			// exce.printStackTrace();
		// }
	// }

	// @Override public void removeUpdate(DocumentEvent e){}
	// @Override public void changedUpdate(DocumentEvent e){}


	//private String telugu = "\u0C05\u0C2E\u0C4D\u0C2E";
	//private String teluguHeading = "\u0C1C\u0C3E\u0C35\u0C3E \u0C24\u0C46\u0C32\u0C41\u0C17\u0C41";
	
