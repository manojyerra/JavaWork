import java.io.*;
import java.net.*;

public class Main {

	BufferedReader br = null;
	BufferedWriter bw = null;
	
	public static void main(String[] args) throws Exception
	{
		new Main();
	}
	
	//http://www.telugudictionary.org/english_telugu_dictionary.php?alpha=b&pn=62
	//http://telugudictionary.telugupedia.com/search.php?q=Abuse
	
	
	Main() throws Exception
	{
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Temp.txt"), "UTF-8"));
		
		/*
		int end = 25863;
		
		for(int i=1;i<=end; i++)
		{
			try{
			String link = "http://www.telugudictionary.org/english_telugu.php?id="+i;		
			boolean success = ReadWords(link, "Meaning of '", "' in Telugu", bw);
			if(success)
			{
				bw.newLine();
				ReadWords(link, "<li>", "</li>", bw);
				bw.newLine();
				bw.flush();
			}
			}catch(Exception e)
			{
				e.printStackTrace();
				Thread.sleep(10000);
				bw.write(e.getMessage(), 0, e.getMessage().length());
				bw.newLine();
				bw.flush();
				Thread.sleep(10000);
			}
		}
		
		bw.close();
		*/

		br = new BufferedReader(new FileReader("production/AllWords.txt"));
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Temp.txt"), "UTF-8"));
		
		String line = null;
		int count = 0;
		
		while((line = br.readLine()) != null)
		{
			try{
			count++;
			
			System.out.println(count+") "+line);
			line = line.trim();
			
			bw.write(line, 0, line.length());
			bw.newLine();
			
			line = line.replaceAll(" ", "+");
			
			String link = "http://www.telugudictionary.org/search.php?q=";
			link += line;
			
			ReadWords(link, "<li>", "</li>", bw);
			}catch(Exception e){e.printStackTrace(); bw.write(e.getMessage(), 0, e.getMessage().length());}
		}
		
		bw.close();
		br.close();	
		
		
		/*
		br = new BufferedReader(new FileReader("LettersLimits.txt"));
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("Temp.txt"), "UTF-8"));

        String startStr = "- English to Telugu Dictionary\">";
        String endStr = "</a></td>";
		
		String line = null;
		while((line = br.readLine()) != null)
		{
			line = line.trim();
			String[] tokens = line.split("-");
			int val = Integer.parseInt(tokens[1].trim());
			
			for(int i=0;i<val; i++)
			{
				String link = "http://www.telugudictionary.org/english_telugu_dictionary.php?alpha=";
				
				if(i == 0)
					link += tokens[0];
				else
					link += tokens[0]+"&pn="+i;
				
				ReadWords(link, startStr, endStr, bw);
			}
		}

		bw.close();
		br.close();
		*/
		
	}
	
	boolean ReadWords(String link, String startStr, String endStr, BufferedWriter bw) throws Exception
	{
		//System.out.println(link);
		
        URL url = new URL(link);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
                
        String line = null;
        int count = 0;
        
        while((line = br.readLine()) != null)
        {
        	boolean loopAgain = false;
        	
        	do{
        		loopAgain = false;
	    		int startIndex = line.indexOf(startStr);
	    		int endIndex = line.indexOf(endStr);
	    		
	        	if(startIndex != -1 && endIndex != -1 && startIndex < endIndex)
	        	{
	        		String word = line.substring(startIndex+startStr.length(), endIndex);
	        		loopAgain = true;
	        		line = line.substring(endIndex+endStr.length(), line.length());
	        		
	        		int ascii = (int)(word.charAt(0));

	        		//if( (ascii >= (int)'A' && ascii <= (int)'Z') ||  (ascii >= (int)'a' && ascii <= (int)'z'))
	        		{
		        		System.out.println(line);

		        		if(bw != null)
		        		{
		        			if(count == 0)
		        				bw.write(word, 0, word.length());
		        			else
		        				bw.write(", "+word, 0, word.length()+2);
		        			
			        		count++;
		        		}
	        		}
	        	}
        	}while(loopAgain);
        }

    	if(count > 0)
    		bw.newLine();
    	
    	bw.flush();

        br.close();

        return (count > 0);
	}
}
