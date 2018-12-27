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
import java.util.Comparator;


class TotWorkTime
{
	public static void main(String args[]) throws IOException
	{
		new TotWorkTime(args[0]);
	}
	
	TotWorkTime(String basePath) throws IOException
	{
		File file = new File(basePath);
		ArrayList<String> list = new ArrayList<String>();
		
		if(file.isDirectory())
		{
			File[] listOfFile = file.listFiles();
			
			for(int i=0; i<listOfFile.length; i++)
			{
				String dayFile = listOfFile[i].getName();
				
				if(dayFile.startsWith("bg_"))
				{
					String filePath = listOfFile[i].getPath();
					
					int totTime = calcTime(filePath);
					String dayShortName = getDayShortName(filePath);
					String printStr = "Total time on "+dateByFilePath(filePath)+" ["+dayShortName+"] : "+formatMinutes(totTime);
					
					//System.out.println(printStr);
					list.add(printStr);
				}
			}
			
			sortList(list);
			
			for(int i=0; i<list.size(); i++)
			{
				System.out.println(list.get(i));
			}
		}
		else
		{
			int totTime = calcTime(basePath);
			String dayShortName = getDayShortName(basePath);
			
			System.out.println("Total time on "+dateByFilePath(basePath)+" ["+dayShortName+"] : "+formatMinutes(totTime));
		}
	}
	
	private static void sortList(ArrayList<String> arrayList)
	{
		Collections.sort(arrayList, new Comparator<String>() {
			@Override
			public int compare(String str2, String str1) 
			{				
				//Total time on 2018_12_2 [SUN] : 2 hrs 12 minutes
				
				//C:\all\work\JavaWork\WorkTimeCalc\data\bg_2018_11_18.txt
				//C:\all\work\JavaWork\WorkTimeCalc\data\bg_2018_11_19.txt

				String preStr = "Total time on ";
				String postStr = " [";
				
				String dateStr1 = str1.substring(str1.indexOf(preStr)+preStr.length(), str1.lastIndexOf(postStr));
				String[] date1 = dateStr1.split("_");
				
				String dateStr2 = str2.substring(str2.indexOf(preStr)+preStr.length(), str2.lastIndexOf(postStr));
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
		
		br.close();
		
		return totTime;
	}
	
	
	String dateByFilePath(String filePath)
	{
		File file = new File(filePath);
		String fileName = file.getName();
		String date = fileName.substring(3, fileName.length()-4);
		return date;
	}
	
	
	String getDayShortName(String filePath)
	{
		String dateWithUnderscore = dateByFilePath(filePath);
		String[] dateTokens = dateWithUnderscore.split("_");
		
		int year = Integer.parseInt(dateTokens[0]);
		int month = Integer.parseInt(dateTokens[1]);
		int day = Integer.parseInt(dateTokens[2]);
		
		return Day.getDayShortName(year, month, day);
	}
	
	
	String formatMinutes(int timeInMinutes)
	{
		int hours = timeInMinutes / 60;
		int mins = timeInMinutes - hours * 60;
		
		return hours+" hrs "+mins+" minutes";
	}
}


class Day
{
	static String getDayShortName(int year, int month, int date)
	{
		int dayID = GetDayID(year, month, date);
		
		String dayShortName = "SUN";
		
		if(dayID == 0) dayShortName = "SUN";
		else if(dayID == 1) dayShortName = "MON";
		else if(dayID == 2) dayShortName = "TUE";
		else if(dayID == 3) dayShortName = "WED";
		else if(dayID == 4) dayShortName = "THU";
		else if(dayID == 5) dayShortName = "FRI";
		else if(dayID == 6) dayShortName = "SAT";
		
		return dayShortName;
	}
	
	static int GetDayID(int year, int month, int date)
	{
		int totDays = GetTotalNumDaysTillMonth(year, month);
		int y = year-1;
		int dayID = (date+totDays+y+(y/4)+(y/400)-(y/100)) % 7;
		
		return dayID;
	}
	
	static int GetNumDaysOfMonth(int year, int month)
	{
		int m = month;
		int d = 0;
		int e = 0;
		
		if(m==1 ){d=0  ;e=31;}   if(m==2 ){d=31 ;e=28;}  if(m==3 ){d=59 ;e=31;}
		if(m==4 ){d=90 ;e=30;}   if(m==5 ){d=120;e=31;}  if(m==6 ){d=151;e=30;}
		if(m==7 ){d=181;e=31;}   if(m==8 ){d=212;e=31;}  if(m==9 ){d=243;e=30;}
		if(m==10){d=273;e=31;}   if(m==11){d=304;e=30;}  if(m==12){d=334;e=31;}

		boolean isLeapYear = IsLeapYear(year);		

		if(isLeapYear && m==2) { e=29; }
		if(isLeapYear && m>=3) { d++; }
		
		return e;
	}

	private static int GetTotalNumDaysTillMonth(int year, int month)
	{
		int m = month;
		int d = 0;
		int e = 0;
		
		if(m==1 ){d=0  ;e=31;}   if(m==2 ){d=31 ;e=28;}  if(m==3 ){d=59 ;e=31;}
		if(m==4 ){d=90 ;e=30;}   if(m==5 ){d=120;e=31;}  if(m==6 ){d=151;e=30;}
		if(m==7 ){d=181;e=31;}   if(m==8 ){d=212;e=31;}  if(m==9 ){d=243;e=30;}
		if(m==10){d=273;e=31;}   if(m==11){d=304;e=30;}  if(m==12){d=334;e=31;}

		boolean isLeapYear = IsLeapYear(year);		

		if(isLeapYear && m==2) { e=29; }
		if(isLeapYear && m>=3) { d++; }
		
		return d;
	}
	
	static boolean IsLeapYear(int year)
	{
		return ( year%4==0 && (year%100!=0||year%400==0) );
	}	
}