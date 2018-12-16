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
import java.util.Arrays;
import java.util.Comparator;


class TotWorkTime
{
	public static void main(String args[]) throws IOException
	{
		File[] allFiles = (new File("data")).listFiles();
		ArrayList<String> fileNamesArr = new ArrayList<String>();
		
		for(int i=0; i<allFiles.length; i++)
		{
			fileNamesArr.add(allFiles[i].getAbsolutePath());
		}
				
		sortList(fileNamesArr);
	
		for(int i=0; i<fileNamesArr.size(); i++)
		{
			new TotWorkTime(fileNamesArr.get(i));
		}
	}
	
	private static void sortList(ArrayList<String> arrayList)
	{
		Collections.sort(arrayList, new Comparator<String>() {
			@Override
			public int compare(String str2, String str1) 
			{				
				//C:\all\work\JavaWork\WorkTimeCalc\data\bg_2018_11_18.txt
				//C:\all\work\JavaWork\WorkTimeCalc\data\bg_2018_11_19.txt

				String dateStr1 = str1.substring(str1.indexOf("bg_")+3, str1.lastIndexOf(".txt"));
				String[] date1 = dateStr1.split("_");
				
				String dateStr2 = str2.substring(str2.indexOf("bg_")+3, str2.lastIndexOf(".txt"));
				String[] date2 = dateStr2.split("_");
				
				String ymd1 = date1[0];
				String ymd2 = date2[0];
				
				if(Integer.parseInt(ymd1) >= Integer.parseInt(ymd2))
				{
					if(Integer.parseInt(ymd1) > Integer.parseInt(ymd2))return 1;
					if(Integer.parseInt(ymd1) == Integer.parseInt(ymd2))
					{

						ymd1 = date1[1];
						ymd2 = date2[1];
						if(Integer.parseInt(ymd1) >= Integer.parseInt(ymd2))
						{
							if(Integer.parseInt(ymd1) > Integer.parseInt(ymd2))return 1;
							if(Integer.parseInt(ymd1) == Integer.parseInt(ymd2))
							{
								
								ymd1 = date1[2];
								ymd2 = date2[2];
								if(Integer.parseInt(ymd1) >= Integer.parseInt(ymd2))
								{
									if(Integer.parseInt(ymd1) > Integer.parseInt(ymd2))return 1;
									if(Integer.parseInt(ymd1) == Integer.parseInt(ymd2))
									{
										return 0;
									}
								}									
								
							}
						}
						
					}
				}
				
				return -1;
			}
		});		
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
