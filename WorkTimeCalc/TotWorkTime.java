import java.sql.Timestamp;    
import java.util.Date; 
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;


class TotWorkTime
{
	public static void main(String args[]) throws IOException
	{
		new TotWorkTime(args[0]);
	}
	
	TotWorkTime(String filePath) throws IOException
	{
		File file = new File(filePath);
		ArrayList<String> list = new ArrayList<String>();
		
		if(file.isDirectory())
		{
			File[] listOfFile = file.listFiles();
			
			for(int i=0; i<listOfFile.length; i++)
			{
				String dayFile = listOfFile[i].getName();
				
				if(dayFile.startsWith("bg_"))
				{
					int totTime = calcTime(listOfFile[i].getPath());
					String printStr = "Total time on "+dateByFilePath(dayFile)+" : "+formatMinutes(totTime);
					list.add(printStr);
				}
			}
			
			Collections.sort(list);
			
			for(int i=0; i<list.size(); i++)
			{
				System.out.println(list.get(i));
			}
		}
		else
		{
			int totTime = calcTime(filePath);
			System.out.println("Total time on "+dateByFilePath(filePath)+" : "+formatMinutes(totTime));
		}
	}
	
	
	int calcTime(String filePath) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		
		String line = "";
		int totTime = 0;
		int prevTime = 0;
		int time = 0;
		
		while( (line = br.readLine()) != null)
		{
			String tokens[] = line.split(" ");
			time = Integer.parseInt(tokens[1]);
			
			if(time < prevTime)
			{
				totTime += prevTime;
			}
			
			prevTime = time;
		}
		
		totTime += time;
		
		return totTime;
	}
	
	
	String dateByFilePath(String filePath)
	{
		File file = new File(filePath);
		String fileName = file.getName();
		String date = fileName.substring(3, fileName.length()-4);
		return date;
	}
	
	
	String formatMinutes(int timeInMinutes)
	{
		int hours = timeInMinutes / 60;
		int mins = timeInMinutes - hours * 60;
		
		return hours+" hrs "+mins+" minutes";
	}
}
