import java.io.*;
import java.util.*;
import utils.*;

class Sentence
{
	String telugu;
	String english;
	String conversion;
}

class Page
{
	ArrayList<String> teluguArr = null;
	ArrayList<String> englishArr = null;
	ArrayList<String> convertionArr = null;
	
	CurrentPage currentPage = null;
	int arrIndex;
	
	Page(CurrentPage currentPage)throws Exception
	{
		this.currentPage = currentPage;
				
		arrIndex = this.currentPage.lineNumber-2;
		
		teluguArr = readTeluguPage("Pages/"+currentPage.pageNumber+"/Telugu.txt");
		englishArr = readPageAndCreateIfNotExist("Pages/"+currentPage.pageNumber+"/English.txt");
		convertionArr = readPageAndCreateIfNotExist("Pages/"+currentPage.pageNumber+"/Convertion.txt");
	}
	
	Sentence nextSentence()
	{
		arrIndex++;
		currentPage.lineNumber = arrIndex+1;
		
		Sentence sentence = new Sentence();
		sentence.telugu = teluguArr.get(arrIndex);
		sentence.english = englishArr.get(arrIndex);
		sentence.conversion = convertionArr.get(arrIndex);
		
		return sentence;
	}
	
	Sentence sentenceByIndex(int index)
	{
		if(index >= 0 && index < teluguArr.size())
		{
			arrIndex = index-1;
			return nextSentence();
		}
		
		return null;
	}
	
	Sentence firstSentence()
	{
		arrIndex = -1;
		return nextSentence();
	}
	
	Sentence lastSentence()
	{
		arrIndex = teluguArr.size()-2;
		return nextSentence();
	}

	boolean hasNextSentence()
	{
		return (arrIndex+1 < teluguArr.size());
	}
	
	int getTotalLines()
	{
		return teluguArr.size();
	}
	
	Sentence prevSentence()
	{
		arrIndex--;
		currentPage.lineNumber = arrIndex+1;

		Sentence sentence = new Sentence();
		sentence.telugu = teluguArr.get(arrIndex);
		sentence.english = englishArr.get(arrIndex);
		sentence.conversion = convertionArr.get(arrIndex);
		
		return sentence;
	}
	
	boolean hasPrevSentence()
	{
		return (arrIndex-1 >= 0);
	}
	
	void setConversion(String text)
	{
		convertionArr.set(arrIndex, text);
	}
	
	int unAnsweredIndex()
	{
		for(int i=0; i<convertionArr.size(); i++)
		{
			if(convertionArr.get(i).trim().length() == 0)
				return i;
		}
		
		return -1;
	}
	
	void saveConversion()
	{
		try{
			FileUtils.writeList("Pages/"+currentPage.pageNumber+"/Convertion.txt", convertionArr);
			//FileUtils.writeList("Pages/"+currentPage.pageNumber+"/Telugu.txt", teluguArr);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	ArrayList<String> readTeluguPage(String filePath) throws Exception
	{
		ArrayList<String> list = new ArrayList<String>();
		
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
		
		while( (line = br.readLine()) != null)
		{
			list.add(line);
		}
		
		return list;
	}
	
	ArrayList<String> readPageAndCreateIfNotExist(String filePath) throws Exception
	{
		File file = new File(filePath);
		
		if(file.exists())
		{			
			return FileUtils.getFileData(filePath);
		}
		else
		{
			ArrayList<String> list = new ArrayList<String>();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			
			for(int i=0; i<teluguArr.size(); i++)
			{
				list.add(" ");
				
				if(i != 0)
					bw.newLine();

				bw.write(" ", 0, 1);
				bw.flush();
			}
			
			bw.close();
			
			return list;
		}
	}
}




/*

	ArrayList<String> readPage(String filePath) throws Exception
	{
		ArrayList<String> list = new ArrayList<String>();
		
		String line = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
		
		ArrayList<String> topicData = new ArrayList<String>();

		while( (line = br.readLine()) != null)
		{
			line = line.trim();
			
			if(line.length() > 0)
			{
				if(line.equals("topicEnd"))
				{
					list.addAll( readTopic(topicData) );
					topicData.clear();
				}
				else
				{
					topicData.add(line);
				}
			}
		}
		
		return list;
	}
	
	ArrayList<String> readTopic(ArrayList<String> topicData)
	{
		ArrayList<String> list = new ArrayList<String>();
		
		for(int i=0; i<topicData.size();i++)
		{
			if(i%2 == 0)
			{
				if(i == 0)
					list.add("Topic Name : "+topicData.get(i));
				else
					list.add("Sub Topic Name : "+topicData.get(i));
			}
			else
			{
				list.addAll(getAllSentences(topicData.get(i)));
			}
		}
		
		return list;
	}
	
	ArrayList<String> getAllSentences(String str)
	{
		ArrayList<String> list = new ArrayList<String>();
		
		if(str.length() <= 1)
		{
			list.add(str);
			return list;
		}
		
		StringBuffer sentence = new StringBuffer();
		sentence.append(str.charAt(0));
		
		for(int i=1; i<str.length(); i++)
		{
			char ch = str.charAt(i);
			sentence.append(ch);
			
			if(ch == '.')
			{
				if(i == str.length()-1)
				{
					list.add(sentence.toString().trim());
					break;
				}
				
				char chNext = str.charAt(i+1);
				char chPrev = str.charAt(i-1);
				
				if(chNext == ' ' && chPrev != '.')
				{
					list.add(sentence.toString().trim());
					sentence = new StringBuffer();
				}
			}
		}
		
		return list;
	}	

*/
