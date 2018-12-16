import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;

import java.awt.Font; 
import javax.swing.*;

public class TeluguEnglishPaper extends JFrame implements ActionListener
{
	//3072-3199
	String defaultStr = ""; //"The term yeoman nowadays suggests someone upright, sturdy, honest and trustworthy, qualities attributed to the Yeomen of the Crown; and in the 13th century the Yeomen of the Chamber were described as virtuous, cunning, skillful, courteous, and experts in archery chosen out of every great noble's house in England. The King's Yeoman or King's Valectus (Valetti) is the earliest usage in a recognisable form such as King's Yeman or King's Yoman. Possibly the concept is derived from King's Geneatas, meaning either companion or a follower of a king. In ancient times before the establishments of feudalism and manorialism, a yeoman was a follower of a district (gau) chief or judice.";

	JFrame frame = new JFrame();
	JTextArea textArea = new JTextArea(defaultStr);
	JScrollPane scrollPane = new JScrollPane(textArea);
	
	JButton button = new JButton("Insert Telugu Meanings");
	JButton removeBtn = new JButton("Remove");
	JButton addBtn = new JButton("Add");
	JButton rectifyBtn = new JButton("Rectify");
	JButton printBtn = new JButton("Print");

	Vector<String> removeVec = new Vector<String>();
	Vector<String> addVec = new Vector<String>();
	Vector<String> rectifyVec = new Vector<String>();

	BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("DataOneToOne.txt"), "UTF-8"));
	HashMap map = new HashMap();
	String totTelugu = "";
	String finalStr = "";
	
 	public static void main(String[] args)  throws Exception
	{
		new TeluguEnglishPaper();
	}
 
	public TeluguEnglishPaper() throws Exception
	{
	/*
		//3072-3199
		
		for(int i=3072;i<=3199;i++)
		{
			finalStr += "("+i+"="+(char)i+") "; i++; if(i>3199) break;
			finalStr += "("+i+"="+(char)i+") "; i++; if(i>3199) break;
			finalStr += "("+i+"="+(char)i+") "; i++; if(i>3199) break;
			finalStr += "("+i+"="+(char)i+") "; i++; if(i>3199) break;
			finalStr += "("+i+"="+(char)i+") ";	i++; if(i>3199) break;
			finalStr += "("+i+"="+(char)i+") ";	i++; if(i>3199) break;
			finalStr += "("+i+"="+(char)i+") ";	i++; if(i>3199) break;
			finalStr += "("+i+"="+(char)i+") ";	i++; if(i>3199) break;
			finalStr += "("+i+"="+(char)i+") ";	i++; if(i>3199) break;
			finalStr += "("+i+"="+(char)i+") ";	i++; if(i>3199) break;
			finalStr += "("+i+"="+(char)i+") ";	i++; if(i>3199) break;

			finalStr += "\n";
		}
		finalStr += "\n\n\n";
	
		finalStr += "";
	*/
		frame.setSize(1200, 640);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		textArea.setFont(new Font("gautami", Font.PLAIN, 15));	
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		frame.getContentPane().setLayout(null);
		
		//textArea.setBounds(0,0,frame.getWidth(),500);
		//frame.getContentPane().add(textArea);

		scrollPane.setBounds(0,0,frame.getWidth(),500);
		frame.getContentPane().add(scrollPane);

		button.setBounds(0,520,220,25);
		button.addActionListener(this);
		frame.getContentPane().add(button);
		
		removeBtn.setBounds(220,520,90,25);
		removeBtn.addActionListener(this);
		frame.getContentPane().add(removeBtn);
		
		addBtn.setBounds(320,520,90,25);
		addBtn.addActionListener(this);
		frame.getContentPane().add(addBtn);

		rectifyBtn.setBounds(420,520,90,25);
		rectifyBtn.addActionListener(this);
		frame.getContentPane().add(rectifyBtn);
		
		printBtn.setBounds(520,520,90,25);
		printBtn.addActionListener(this);
		frame.getContentPane().add(printBtn);

		ReadData();
		//textArea.setText(finalStr);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		String command = ae.getActionCommand();

		if(command.equals("Insert Telugu Meanings"))
			InsertTeluguMeanings();
		else if(command.equals("Remove"))
		{
			map.remove(textArea.getSelectedText().toUpperCase());
			//removeVec.add(textArea.getSelectedText());
		}
		else if(command.equals("Add"))
			addVec.add(textArea.getSelectedText());
		else if(command.equals("Rectify"))
			rectifyVec.add(textArea.getSelectedText());

		else if(command.equals("Print"))
		{
			try{
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("DataOneToOne.txt"), "UTF-8"));
			Object[] keys = map.keySet().toArray();
			for(int i=0;i<keys.length; i++)
			{
				String keyName = (String)keys[i];
				String val = (String) map.get(keyName);
				
				if(val.trim().length() == 0)
					continue;
					
				bw.write(keyName, 0, keyName.length());
				bw.newLine();
				
				bw.write(val, 0, val.length());
				bw.newLine();
				
				bw.flush();
			}
			
			bw.close();
			}catch(Exception e){e.printStackTrace();}
			System.out.println("Romove Vec");
			for(int i=0;i<removeVec.size();i++)
				System.out.println(removeVec.get(i));
				
			System.out.println("Add Vec");
			for(int i=0;i<addVec.size();i++)
				System.out.println(addVec.get(i));

			System.out.println("Rectify Vec");
			for(int i=0;i<rectifyVec.size();i++)
				System.out.println(rectifyVec.get(i));
		}
	}
	
	void InsertTeluguMeanings()
	{
		String tokens[] = textArea.getText().split(" ");
		
		for(int i=0;i<tokens.length;i++)
		{
			finalStr += tokens[i];
			
			if(tokens[i].length()>3)
			{				
				String upToken = tokens[i].toUpperCase();
				
				if(upToken.endsWith(","))
				{
					upToken = upToken.substring(0,upToken.length()-1);
				}
				
				String teluguMeaning = (String)map.get(upToken);
								
				if(teluguMeaning != null)
				{
					finalStr += "("+teluguMeaning+"";
					
					//for(int k=0;k<teluguMeaning.length();k++)
					//	finalStr += teluguMeaning.charAt(k)+" ";
						
					finalStr += ")";
				}

			}
			
			finalStr += " ";
		}
		
		textArea.setText(finalStr);
	}
	
	public void ReadData() throws Exception
	{
		String line = "";
		int count = 0;
		while( (line = br.readLine()) != null )
		{
			String englishWord = line;
			String teluguWord = br.readLine();
			map.put(englishWord.toUpperCase(), teluguWord);
		}	
	}
}










	/*
	public void MakeOneToOne(String fileName)  throws Exception
	{
		Vector<String> englishWords = new Vector<String>();
		Vector<String> teluguWords = new Vector<String>();	
	
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
		
		String line = "";
		
		while( (line = br.readLine()) != null )
		{
			if(AllLessThan3000(line))
			{
				englishWords.add(line);
			}
			else
			{
				if(englishWords.size() > teluguWords.size())
					teluguWords.add(line);
			}
		}
		
		int size = englishWords.size();
		
		for(int i=0;i<size;i++)
		{
			bw.write(englishWords.get(i), 0, englishWords.get(i).length());
			bw.newLine();
			bw.flush();

			bw.write(teluguWords.get(i), 0, teluguWords.get(i).length());
			bw.newLine();
			bw.flush();
		}	
	}
	
	boolean AllLessThan3000(String line) throws Exception
	{
		for(int i=0;i<line.length();i++)
		{
			if((int)line.charAt(i) > 3000)
				return false;
		}
		return true;
	}
	*/