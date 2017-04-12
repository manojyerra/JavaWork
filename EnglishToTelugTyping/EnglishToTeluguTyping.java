import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.AWTException;
import java.awt.HeadlessException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

//class TeluguToEnglishTyping extends JFrame implements TextListener, ActionListener//, DocumentListener
class EnglishToTeluguTyping extends JFrame implements ActionListener, DocumentListener//, WindowListener
{     
    Robot robot = new Robot();
	
	//TextArea inputArea = new TextArea("input", 4, 30, TextArea.SCROLLBARS_VERTICAL_ONLY);
	//TextArea outputArea = new TextArea("output", 4, 30, TextArea.SCROLLBARS_VERTICAL_ONLY);
	JTextArea inputArea = new JTextArea("");
	JTextArea outputArea = new JTextArea("");
	String outputString = "";
	
    Button temp = new Button("Switch Window");

	HashMap<String, String> map = new HashMap<String, String>();
	HashMap<String, String> signMap = new HashMap<String, String>();
	HashMap<String, String> oneMap = new HashMap<String, String>();
    String pollu = "\u0C4D";
	String sunna = "\u0C02";
	
	HashMap<String, String> database = new HashMap<String, String>();
	
	int frameW = 877;
	int frameH = 192;
	
	boolean emptyInputArea = false;
	Thread thread = null;
	
	boolean displayAsItIs = false;
	
	public static void main(String args[]) throws Exception
	{
		new EnglishToTeluguTyping();
	}

	EnglishToTeluguTyping() throws Exception
	{
		Container c = getContentPane();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // to close the JFrame window
		setVisible(true);
		//setLayout(null);
		c.setLayout(null);
		setBounds(100,100,frameW,frameH);
		setTitle("Telugu To English Typing");
		c.setBackground(Color.GRAY);
		
		
		SetBounds();
		FillMaps();
		
		//inputArea.addTextListener(this);
		inputArea.getDocument().addDocumentListener(this);
		
        //int btnW = 120;
        //int btnH = 30;
        //temp.setBounds( inputArea.getX() + (inputArea.getWidth()-btnW) / 2, inputArea.getY()+inputArea.getHeight()+20, btnW, btnH );
        //temp.addActionListener(this);
        
		inputArea.setWrapStyleWord(true);
		outputArea.setWrapStyleWord(true);
		
		inputArea.setLineWrap(true);
		outputArea.setLineWrap(true);
		add(outputArea);
		add(inputArea);
        //add(temp);

        //inputArea.setFont(inputArea.getFont().deriveFont(17f));
        //outputArea.setFont(outputArea.getFont().deriveFont(17f));
		
        //inputArea.setFont (new Font("gautami", Font.PLAIN, 17));
        //outputArea.setFont (new Font("gautami", Font.PLAIN, 17));
        
		//inputArea.setFont( new Font("gautami", Font.PLAIN, 15) );
		//outputArea.setFont( new Font("gautami", Font.PLAIN, 15) );
		//inputArea.setText("\u0C39\u0C32\u0C4B\u0C01\u0C2A\u0C2A\u0C02\u0C1A\u0C02");
		//inputArea.setText("\u0C2A\u0C4D\u0C30");
		//inputArea.setWrapStyleWord(true);
		//outputArea.setWrapStyleWord(true);
        
		Font teluguFont = CreateFont("Gidugu.ttf");
        //Font teluguFont = CreateFont("/Users/kh1755/Desktop/javaWork/Gidugu.ttf");
        //Font teluguFont = CreateFont("/Users/kh1755/Desktop/javaWork/Mandali_Regular.ttf");
        
        //inputArea.setFont(teluguFont);
        outputArea.setFont(teluguFont);
		
		
		thread = new Thread()
		{	
			public void run()
			{				
				while(true)
				{
					try
					{
						Thread.sleep(100);
					}
					catch(Exception e){e.printStackTrace();}
					
					if(emptyInputArea)
					{
						inputArea.setText("");
						emptyInputArea = false;
					}
				}
			}
		};
		
		thread.start();
		
		
		addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // This is only called when the user releases the mouse button.
                //System.out.println("componentResized");
				
				frameH = getHeight();
				frameW = getWidth();
				//System.out.println(frameW+","+frameH);
				SetBounds();
            }
        });		
	}
	
	void SetBounds()
	{
		int textAreaStartY = 10;
		int textAreaH = (int)(frameH-(2*textAreaStartY)-40)/2;
		//System.out.println("textAreaH : "+textAreaH);
		int textAreaW = frameW - 35;
		int textAreaX = 10;
		
		outputArea.setBounds(textAreaX, textAreaStartY, textAreaW, textAreaH);
		inputArea.setBounds(textAreaX, outputArea.getY()+outputArea.getHeight()+5, textAreaW, textAreaH);		
	}
	
    Font CreateFont(String fontFilePath)
    {
        Font customFont = null;
        
        try
        {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File(fontFilePath)).deriveFont(17f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return customFont;
    }
    
	
	@Override public void removeUpdate(DocumentEvent e)
	{
		//System.out.println(" removeUpdate ");
		textValueChanged(null);
	}
	
	@Override public void insertUpdate(DocumentEvent e) 
	{
		//System.out.println(" insertUpdate ");
		textValueChanged(null);
    }

	@Override public void changedUpdate(DocumentEvent arg0)
	{
		//System.out.println(" changeUpdate ");
		textValueChanged(null);
	}
    
    public void actionPerformed(ActionEvent ae)
    {
    }
	
	public void textValueChanged(TextEvent event)
	{
		String inputStr = inputArea.getText();
			
		int inputStrLen = inputStr.length();
		
		outputString = "";
		
		if(inputStrLen == 0)
		{
			outputArea.setText(outputString);
			return;
		}
		
		char lastCh = inputStr.charAt(inputStrLen - 1);
		boolean enter = (int)lastCh == KeyEvent.VK_ENTER;
		
		if(inputStrLen > 1000 && lastCh != ' ' && !enter)
			return;
		
		if(inputStr.length() == 1)
		{
			if(isEnglishChar(inputStr.charAt(0)))
			{
				AddWordToOutputStr(""+inputStr.charAt(0));
				outputArea.setText(outputString);
			}
			else
			{
				outputArea.setText(""+inputStr.charAt(0));
			}
			
			return;
		}
		
		
		String word = ""+inputStr.charAt(0);
		
		for(int i=1; i<inputStr.length(); i++)
		{
			char ch0 = inputStr.charAt(i-1);
			char ch1 = inputStr.charAt(i);
			
			if(isEnglishChar(ch0) != isEnglishChar(ch1))
			{
				AddWordToOutputStr(word);
				word = "";
			}
		
			if( i == inputStr.length()-1 )
			{
				word += ch1;
				AddWordToOutputStr(word);
				break;
			}
			
			word += ch1;
		}
	
		/*String outputAreaStr = outputArea.getText();
		
		if(outputString.indexOf( outputAreaStr ) == 0)
		{
			String extraStr = outputString.substring(outputAreaStr.length());
			
			System.out.println(" extra string : "+extraStr);
			
			outputArea.append(extraStr);
		}
		else*/
		{		
			outputArea.setText(outputString);
		}
		
		if(enter)
		{
			try
			{
				//System.out.println("Action triggered...");
				
				copy(outputString);
				
				PressAltTab();
				
				paste();
								
				TypeKey(KeyEvent.VK_ENTER);
				
				PressAltTab();
				
				//inputArea.setText("");
				
				emptyInputArea = true;
				
				outputArea.setText("");
				
				displayAsItIs = false;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}			
		}
		
	}
	
	//public void run()
	//{
		
	//}
	
	/*
	public void updateTargetValue(final Object newValue) 
	{
		Runnable runnable = new Runnable() 
		{
			public void run()
			{
				if ((sourceValue==null && newValue==null) || DefaultTypeTransformation.compareEqual(sourceValue, newValue))
				{
					// not a change, don't fire it
					return;
				}
			}
		};
	}
	*/

	void PressAltTab() throws Exception
	{
		robot.keyPress(KeyEvent.VK_ALT);
		Thread.sleep(100);
		robot.keyPress(KeyEvent.VK_TAB);
		Thread.sleep(100);
		robot.keyRelease(KeyEvent.VK_TAB);
		Thread.sleep(10);
		robot.keyRelease(KeyEvent.VK_ALT);
		Thread.sleep(500);
	}
	
	void TypeKey(int key) throws Exception
	{
		robot.keyPress(key);
		Thread.sleep(100);
		robot.keyRelease(key);
		Thread.sleep(100);		
	}
	
	public static void copy(String text)
    {
        Clipboard clipboard = getSystemClipboard();

        clipboard.setContents(new StringSelection(text), null);
    }

    public static void paste() throws AWTException
    {
        Robot robot = new Robot();

        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_V);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.keyRelease(KeyEvent.VK_V);
    }

    private static Clipboard getSystemClipboard()
    {
        Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
        Clipboard systemClipboard = defaultToolkit.getSystemClipboard();

        return systemClipboard;
    }	
	
	void AddWordToOutputStr(String word)
	{
		if(!displayAsItIs && isEnglishChar(word.charAt(0)))
		{
			String teluguWord = database.get(word);
			
			if(teluguWord == null)
			{
				//System.out.println(word+" added");
				teluguWord = ConvertToTeluguWord(word);
				database.put(word, teluguWord);
			}
			
			outputString += teluguWord;
		}
		else
		{
			displayAsItIs = false;
			
			if( word.charAt(word.length()-1) == '-')
			{
				displayAsItIs = true;
				word = word.substring(0, word.length()-1);
			}
			
			outputString += word;
		}
	}
	
	String ConvertToTeluguWord(String word)
	{
        word = word.replaceAll("aH", "#");
        word = word.replaceAll("rHu", "%");
        word = word.replaceAll("rH", "*");
        word = word.replaceAll("aa", "A");
        word = word.replaceAll("ee", "E");
        word = word.replaceAll("ii", "I");
        word = word.replaceAll("oo", "O");
        word = word.replaceAll("uu", "U");
        word = word.replaceAll("au", "@");
        word = word.replaceAll("ai", "&");
        
		Vector<String> tokensVec = getTokens(word);

		String teluguStr = "";
		
		for(int i=0; i<tokensVec.size(); i++)
		{
			String token = tokensVec.get(i);
			
			if(map.containsKey(token))
			{
				teluguStr += map.get(token);
			}
			else
			{
				if(token.length() == 1)
				{
					if(i == tokensVec.size()-1 && token.charAt(0) == 'm')
						teluguStr += sunna;
					else
						teluguStr += oneMap.get(token);
				}
				else
				{
					//System.out.println("<Token><"+token+">");
                    
					/*
                    token = token.replaceAll("ksh", "x");
                    token = token.replaceAll("ch", "1");
                    token = token.replaceAll("CH", "2");
                    token = token.replaceAll("th", "3");
                    token = token.replaceAll("TH", "4");
                    token = token.replaceAll("dh", "5");
                    token = token.replaceAll("DH", "6");
                    token = token.replaceAll("ph", "7");
                    token = token.replaceAll("sh", "8");
                    token = token.replaceAll("nH", "9");
                    token = token.replaceAll("mH", "0");
					*/    
					
                    int tokenLen = token.length();
                    
                    StringBuilder finalToken = new StringBuilder(""+token.charAt(0));
                    
                    for(int ti=1; ti<token.length(); ti++)
                    {
                        char ch = token.charAt(ti);
                        
                        boolean replaced = false;
                        
                        if(ch == 'h' || ch == 'H')
                        {
                            char ch0 = token.charAt(ti-1);
                            
                            if(ch0 == 'c' && ch == 'h')         {   finalToken.setCharAt(finalToken.length()-1 , '1'); replaced = true; }
                            else if(ch0 == 'C' && ch == 'H')    {   finalToken.setCharAt(finalToken.length()-1 , '2'); replaced = true; }
                            else if(ch0 == 't' && ch == 'h')    {   finalToken.setCharAt(finalToken.length()-1 , '3'); replaced = true; }
                            else if(ch0 == 'T' && ch == 'H')    {   finalToken.setCharAt(finalToken.length()-1 , '4'); replaced = true; }
                            else if(ch0 == 'd' && ch == 'h')    {   finalToken.setCharAt(finalToken.length()-1 , '5'); replaced = true; }
                            else if(ch0 == 'D' && ch == 'H')    {   finalToken.setCharAt(finalToken.length()-1 , '6'); replaced = true; }
                            else if(ch0 == 'p' && ch == 'h')    {   finalToken.setCharAt(finalToken.length()-1 , '7'); replaced = true; }
                            else if(ch0 == 's' && ch == 'h')    {   finalToken.setCharAt(finalToken.length()-1 , '8'); replaced = true; }
                            else if(ch0 == 'n' && ch == 'H')    {   finalToken.setCharAt(finalToken.length()-1 , '9'); replaced = true; }
                            else if(ch0 == 'm' && ch == 'H')    {   finalToken.setCharAt(finalToken.length()-1 , '0'); replaced = true; }
                        }
                        
                        if(!replaced)
                        {
                            finalToken.append(ch);
                        }
                    }
                    
                    token = finalToken.toString();
                    
					if(token.length() == 1)
					{
						teluguStr += oneMap.get(token);
					}
					
					for(int pos=1; pos<token.length(); pos++)
					{
						char ch1 = token.charAt(pos-1);
						char ch2 = token.charAt(pos);
						
						//System.out.println("<"+ch1+">");
						
						if( !isVowelPlus(ch1) && !isVowelPlus(ch2) )
						{
							if(pos == 1 && ch1 == 'n' && ch2 != 'n' && ch2 != 'l' && ch2 != 'm' && ch2 != 'r' && ch2 != 's' && ch2 != 'v' && ch2 != 'w' && ch2 != 'y')
							{
								teluguStr += sunna;
							}
                            else if(pos == 1 && ch1 == 'm' && ch2 != 'm' && ch2 != 'y' && ch2 != 'n' && ch2 != 'h' && ch2 != 'r')
                            {
                                teluguStr += sunna;
                            }
							else
							{
								
								teluguStr += oneMap.get(""+ch1);
							}
						}
						else if( !isVowelPlus(ch1) && isVowelPlus(ch2) )
						{
							if(ch2 == 'a')
							{
								teluguStr += map.get(ch1+"a");
							}
							else
							{
								teluguStr += map.get(ch1+"a")+""+signMap.get(""+ch2);
							}
							
							
							for(pos=pos+1 ;pos<token.length(); pos++)
							{
								teluguStr += map.get(""+token.charAt(pos));
							}
							
							break;
						}
						else if( isVowelPlus(ch1) )
						{
							for(pos=0 ;pos<token.length(); pos++)
							{
								teluguStr += map.get(""+token.charAt(pos));
							}
							
							break;
						}
						
						if(pos == token.length()-1 && !isVowel(ch2))
						{
							teluguStr += oneMap.get(""+ch2);
						}
					}
					
					//teluguStr += "_"+token
				}
			}	
		}
		
		return teluguStr;
	}
    
	Vector<String> getTokens(String str)
	{
		Vector<String> tokensVec = new Vector<String>();
		
		int strLen = str.length();
		
		if(strLen == 0)
			return tokensVec;
			
		if(strLen == 1)
		{
			tokensVec.add(""+str.charAt(0));
			return tokensVec;
		}
		
		int startI =0;
		
		for(int i=1; i<strLen; i++)
		{
			char ch = str.charAt(i);			
			
			if(isEnglishChar(ch))
			{
				char beforeCH = str.charAt(i-1);
				
				if( isVowelPlus(beforeCH) && !isVowelPlus(ch))
				{
					String subStr = str.substring(startI, i);
					tokensVec.add(subStr);
					startI = i;
				}
			}
		}
		
		String subStr = str.substring(startI, strLen);
		tokensVec.add(subStr);
		
		return tokensVec;
	}
	
	boolean isEnglishChar(char ch)
	{
		boolean isSmallLetter = (int)ch >= (int)'a' && (int)ch <= (int)'z';
		boolean isCapitalLetter = (int)ch >= (int)'A' && (int)ch <= (int)'Z';
		
		return (isSmallLetter || isCapitalLetter);
	}
	
	boolean isVowel(char ch)
	{
		boolean isSmallVowel = (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u');
		boolean isCapitalVowel = (ch == 'A' || ch == 'E' || ch == 'I' || ch == 'O' || ch == 'U');
		
		return ( isSmallVowel || isCapitalVowel );
	}

	boolean isVowelPlus(char ch)
	{
		boolean isSmallVowel = (ch == 'a' || ch == 'e' || ch == 'i' || ch == 'o' || ch == 'u');
		boolean isCapitalVowel = (ch == 'A' || ch == 'E' || ch == 'I' || ch == 'O' || ch == 'U' || ch == '@' || ch == '&' || ch == '#' || ch == '*' || ch == '%');
		
		return ( isSmallVowel || isCapitalVowel );
	}
	
	void FillMaps()
	{
        signMap.put("aa",   "\u0C3E");
        signMap.put("A",   "\u0C3E");
        signMap.put("i",    "\u0C3F");
        signMap.put("ii",   "\u0C40");
        signMap.put("I",   "\u0C40");
        signMap.put("u",    "\u0C41");
        signMap.put("uu",   "\u0C42");
        signMap.put("U",   "\u0C42");
        signMap.put("e",    "\u0C46");
        signMap.put("ee",   "\u0C47");
        signMap.put("E",   "\u0C47");
        signMap.put("o",    "\u0C4A");
        signMap.put("oo",   "\u0C4B");
        signMap.put("O",   "\u0C4B");
        signMap.put("au",   "\u0C4C");
        signMap.put("@",   "\u0C4C");
        signMap.put("ai",   "\u0C48");
        signMap.put("&",   "\u0C48");
        signMap.put("aH", 	"\u0C03");
        signMap.put("#", 	"\u0C03");
        signMap.put("rH", 	"\u0C43");
        signMap.put("*", 	"\u0C43");
        signMap.put("rHu", 	"\u0C44");
        signMap.put("%", 	"\u0C44");
        
        
        
        map.put("a", 	"\u0C05");
        map.put("aa", 	"\u0C06");
        map.put("A",	"\u0C06");
        map.put("i", 	"\u0C07");
        map.put("ii", 	"\u0C08");
        map.put("I", 	"\u0C08");
        map.put("u", 	"\u0C09");
        map.put("uu", 	"\u0C0A");
        map.put("U", 	"\u0C0A");
        map.put("e", 	"\u0C0E");
        map.put("ee", 	"\u0C0F");
        map.put("E",	"\u0C0F");
        map.put("o", 	"\u0C12");
        map.put("oo", 	"\u0C13");
        map.put("O", 	"\u0C13");
        map.put("au", 	"\u0C14");
        map.put("@", 	"\u0C14");
        map.put("ai", 	"\u0C10");
        map.put("&", 	"\u0C10");
        map.put("aH", 	"\u0C05\u0C03");
        map.put("#", 	"\u0C05\u0C03");
        map.put("rH", 	"\u0C0B");
        map.put("*", 	"\u0C0B");
        map.put("rHu", 	"\u0C60");
        map.put("%", 	"\u0C60");
        
        
        map.put("ka", 	"\u0C15");
        map.put("Ka",	"\u0C16");
        map.put("qa",	"\u0C15\u0C4D\u0C35");		//alternate for kwa
        
        map.put("ga", 	"\u0C17");
        map.put("Ga",	"\u0C18");
        
        map.put("cha", 	"\u0C1A");
        map.put("CHa",	"\u0C1B");
        
        map.put("ja",	"\u0C1C");
        map.put("Ja",	"\u0C1D");
        map.put("za",	"\u0C1C");
        
        map.put("ta",	"\u0C1F");
        map.put("Ta",	"\u0C20");
        
        map.put("da",	"\u0C21");
        map.put("Da",	"\u0C22");
        
        map.put("tha",	"\u0C24");
        map.put("THa",	"\u0C25");
        
        map.put("dha",	"\u0C26");
        map.put("DHa",	"\u0C27");
        
        map.put("na",	"\u0C28");
        map.put("Na",	"\u0C23");
        map.put("nha",	"\u0C1E");
        
        map.put("pa",	"\u0C2A");
        map.put("fa",	"\u0C2B");
        map.put("Pa",	"\u0C2B");
        map.put("pha",	"\u0C2B");
        
        map.put("ba",	"\u0C2C");
        map.put("Ba",	"\u0C2D");
        
        map.put("ma",	"\u0C2E");
        
        map.put("ya",	"\u0C2F");
        
        map.put("ra",	"\u0C30");
        map.put("Ra",	"\u0C31");
        
        map.put("la",	"\u0C32");
        map.put("La",	"\u0C33");
        
        map.put("va",	"\u0C35");
        map.put("wa",	"\u0C35");
        
        map.put("sa",	"\u0C38");
        map.put("Sa",	"\u0C37");
        
        map.put("sha",	"\u0C36");
        
        map.put("ha",	"\u0C39");
        
        map.put("xa",	"\u0C15\u0C4D\u0C37");		//alternate for ksha
        
        map.put("1a", 	"\u0C1A");  //ch
        map.put("2a",	"\u0C1B");  //CH
        map.put("3a",	"\u0C24");  //th
        map.put("4a",	"\u0C25");  //TH
        map.put("5a",	"\u0C26");  //dh
        map.put("6a",	"\u0C27");  //DH
        map.put("7a",	"\u0C2B");  //ph
        map.put("8a",	"\u0C36");  //sh
        map.put("9a",	"\u0C1E");  //nH
        map.put("0a",	"\u0C19");  //mH
        
        
        /////
        
        
        oneMap.put("k", "\u0C15");
        oneMap.put("K",	"\u0C16");
        oneMap.put("q",	"\u0C15\u0C4D\u0C35");		//alternate for kwa
        oneMap.put("g", "\u0C17");
        oneMap.put("G",	"\u0C18");
        oneMap.put("j",	"\u0C1C");
        oneMap.put("J",	"\u0C1D");
        oneMap.put("z",	"\u0C1C");
        oneMap.put("t",	"\u0C1F");
        oneMap.put("T",	"\u0C20");
        oneMap.put("d",	"\u0C21");
        oneMap.put("D",	"\u0C22");
        oneMap.put("N",	"\u0C23");
        oneMap.put("n",	"\u0C28");
        oneMap.put("p",	"\u0C2A");
        oneMap.put("f",	"\u0C2B");
        oneMap.put("P",	"\u0C2B");
        oneMap.put("b",	"\u0C2C");
        oneMap.put("B",	"\u0C2D");
        oneMap.put("m",	"\u0C2E");
        oneMap.put("y",	"\u0C2F");
        oneMap.put("r",	"\u0C30");
        oneMap.put("R", "\u0C31");
        oneMap.put("l",	"\u0C32");
        oneMap.put("L",	"\u0C33");
        oneMap.put("v",	"\u0C35");
        oneMap.put("w",	"\u0C35");
        oneMap.put("s",	"\u0C38");
        oneMap.put("S",	"\u0C37");
        oneMap.put("h",	"\u0C39");
        oneMap.put("x",	"\u0C15\u0C4D\u0C37");		//alternate for ksha
        oneMap.put("1", "\u0C1A");  //ch
        oneMap.put("2",	"\u0C1B");  //CH
        oneMap.put("3",	"\u0C24");  //th
        oneMap.put("4",	"\u0C25");  //TH
        oneMap.put("5",	"\u0C26");  //dh
        oneMap.put("6",	"\u0C27");  //DH
        oneMap.put("7",	"\u0C2B");  //ph
        oneMap.put("8",	"\u0C36");  //sh
        oneMap.put("9",	"\u0C1E");  //nH
        oneMap.put("0",	"\u0C19");  //mH
        
		
		java.util.Set<String> keySet = oneMap.keySet();
		Object[] keysArr = keySet.toArray();
		
		for(int i=0; i<keysArr.length; i++)
		{
			String key = (String)keysArr[i];
			String value = oneMap.get(key);
			oneMap.put(key, value+""+pollu);
		}		
	}
}
