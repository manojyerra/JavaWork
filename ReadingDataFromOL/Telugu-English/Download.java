import java.io.*;
import java.net.*;
import java.util.*;

public class Download
{
	int totRead = 0;
	int totReadOfChar = 0;
		
	BufferedWriter saveHere = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Data.rtf"),"UTF-8"));  
	
	public static void main(String args[]) throws Exception
	{
		new Download();
	}
	
	Download() throws Exception
	{
		for(char ch = 'a'; ch <= 'z'; ch++)
		{
			totReadOfChar = 0;
			
			int numPages = GetNumPages(ch);
			
			for(int i=0;i<numPages;i++)
			{
				String str = "http://telugu.indiandictionaries.com/englishdictionary.php?alpha="+ch+"&pn="+i;

				BufferedReader br = new BufferedReader(new InputStreamReader(new URL(str).openStream()));
				
				String line = "";
				
				while((line = br.readLine()) != null)
				{
					String searchStr = "meaning.php?id=";
					int index = line.indexOf(searchStr);
					
					if(index != -1)
					{
						index += searchStr.length();
						
						String id = "";
						do
						{
							id += line.charAt(index); index++;
						}while(Character.isDigit(line.charAt(index)));
						
						String letterStr = "http://telugu.indiandictionaries.com/meaning.php?id="+id;
						
						GrabMeanings(letterStr);
					}
				}
				br.close();							
			}
		}
	}

	void GrabMeanings(String str) throws Exception
	{
		totRead++;
		totReadOfChar++;
		
		BufferedReader br = GetReader(str);

		String line = "";

		while((line = br.readLine()) != null)
		{
			String searchStr = "<div id=\"meaningdivscroll\">";
			int index = line.indexOf(searchStr);
			
			if(index != -1)
			{
				line = br.readLine();
				
				Vector<String> englishWords = GetStrings(line, "<strong>", "</strong>" );
				Vector<String> teluguWords = GetStrings(line, "<li>", "</li>" );
				
				String englishWord = englishWords.get(0);
				WriteLine(englishWord);
				
				System.out.println(englishWord+" TotRead "+totRead+" TotReadOfChar "+totReadOfChar);
				
				for(int i=0;i<teluguWords.size();i++)
					WriteLine(teluguWords.get(i));
			}
		}
	}
	
	void WriteLine(String line) throws Exception
	{
		saveHere.write(line, 0, line.length());
		saveHere.flush();
		saveHere.newLine();
		saveHere.flush();
	}

	BufferedReader GetReader(String str) throws Exception
	{
		URL url = new URL(str);
		InputStream inputStream = url.openStream();
		return (new BufferedReader(new InputStreamReader(inputStream, "UTF-8")));
	}
	
	int GetNumPages(char ch)
	{
		int numPages = 0;
		
		if(ch == 'a') numPages = 28;
		else if(ch == 'b') numPages = 14;
		else if(ch == 'c') numPages = 30;
		else if(ch == 'd') numPages = 16;
		else if(ch == 'e') numPages = 15;
		else if(ch == 'f') numPages = 13;
		else if(ch == 'g') numPages = 10;
		else if(ch == 'h') numPages = 10;
		else if(ch == 'i') numPages = 10;
		else if(ch == 'j') numPages = 2;
		else if(ch == 'k') numPages = 2;
		else if(ch == 'l') numPages = 9;
		else if(ch == 'm') numPages = 11;
		else if(ch == 'n') numPages = 5;
		else if(ch == 'o') numPages = 7;
		else if(ch == 'p') numPages = 15;
		else if(ch == 'q') numPages = 1;
		else if(ch == 'r') numPages = 11;
		else if(ch == 's') numPages = 21;
		else if(ch == 't') numPages = 11;
		else if(ch == 'u') numPages = 5;
		else if(ch == 'v') numPages = 2;
		else if(ch == 'w') numPages = 6;
		else if(ch == 'x') numPages = 1;
		else if(ch == 'y') numPages = 1;
		else if(ch == 'z') numPages = 1;
			
		return numPages;
	}
	
	Vector<String> GetStrings(String line, String startStr, String endStr)
	{
		Vector<String> vec = new Vector<String>();
		int searchFrom = 0;
		
		boolean run = true;
		while(run)
		{
			int startIndex = line.indexOf(startStr, searchFrom) + startStr.length();
			int endIndex = line.indexOf(endStr, searchFrom);
			
			if(startIndex == -1 || endIndex == -1)
				return vec;
			
			vec.addElement(line.substring(startIndex, endIndex));
			searchFrom = endIndex + endStr.length();
			
			if(searchFrom >= line.length())
				return vec;
		}
		
		return vec;
	}
}
