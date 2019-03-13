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


public class ConvertTeluguToEnglish extends JFrame //implements DocumentListener
{ 
	JLabel teluguTextArea = null;
	JTextArea googleTextArea = null;
	JTextArea prevWriteTextArea = null;
	JTextArea writeTextArea = null;
	
	JButton goToPageStart = null;
	JButton goToPageEnd = null;
	JButton nextPage = null;
	JButton prevPage = null;
	
	Page page = null;
	ConfigReader config = null;
	
	UIBuilder uiBuilder;

	public static void main(String[] args)  throws Exception
	{
		new ConvertTeluguToEnglish();
	}
 
	public ConvertTeluguToEnglish() throws Exception
	{	
		config = new ConfigReader("config.json");
		
		uiBuilder = new UIBuilder(this);
		uiBuilder.build();
		
		page = new Page(config.currentPage);		
		setSentence(page.nextSentence());

		onEnter(writeTextArea);
		onShiftAndEnter(writeTextArea);
		onJFrameClose(this);
		onGoToPageStart(goToPageStart);
		onGoToPageEnd(goToPageEnd);		
		setVisible(true);
		
		updateTitle();
	}
	
	void onGoToPageStart(JButton btn)
	{
		btn.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  page.goToStart();
			  setSentence(page.nextSentence());
		  }
		});
	}

	void onGoToPageEnd(JButton btn)
	{
		btn.addActionListener(new ActionListener()
		{
		  public void actionPerformed(ActionEvent e)
		  {
			  page.goToEnd();
			  setSentence(page.prevSentence());
		  }
		});
	}
	
	void updateTitle()
	{
		int pageNum = config.currentPage.pageNumber;
		int lineNum = config.currentPage.lineNumber;
		int totLines = page.getTotalLines();
		
		String str = "Practie telugu to english conversion.";
		String info = "Page Number : "+pageNum+"   Line Number : "+lineNum+" / "+totLines;
		
		setTitle(str+"    "+info);
	}
	
	void setSentence(Sentence sentence)
	{
		teluguTextArea.setText(sentence.telugu);
		googleTextArea.setText("Google Tranlation : \n"+sentence.english);
		prevWriteTextArea.setText(sentence.conversion);
	}
		
	void onJFrameClose(JFrame frame)
	{	
		frame.addWindowListener(new java.awt.event.WindowAdapter() 
		{
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) 
			{
				try{
					config.write("config.json");
					page.saveConversion();
				}catch(Exception e){e.printStackTrace();}
			}
		});	
	}
	
	void onEnter(JTextArea textArea)
	{
        int condition = JComponent.WHEN_FOCUSED;
        InputMap inputMap = textArea.getInputMap(condition);
        ActionMap actionMap = textArea.getActionMap();

        KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        inputMap.put(enterKey, enterKey.toString());
		
        actionMap.put(enterKey.toString(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea txtArea = (JTextArea) e.getSource();
				
				if(txtArea.getText().length() > 0)
					page.setConversion(txtArea.getText());

                txtArea.setText("");
				
				if(page.hasNextSentence())
					setSentence(page.nextSentence());
				
				updateTitle();
            }
        });		
	}

	void onShiftAndEnter(JTextArea textArea)
	{
        int condition = JComponent.WHEN_FOCUSED;
        InputMap inputMap = textArea.getInputMap(condition);
        ActionMap actionMap = textArea.getActionMap();

        KeyStroke enterKey = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.SHIFT_DOWN_MASK);
        inputMap.put(enterKey, enterKey.toString());
		
        actionMap.put(enterKey.toString(), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea txtArea = (JTextArea) e.getSource();
                txtArea.setText("");

				if(page.hasPrevSentence())
					setSentence(page.prevSentence());
				
				updateTitle();
            }
        });		
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
	
