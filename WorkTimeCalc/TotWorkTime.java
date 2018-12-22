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
		File[] allFiles = (new File("data")).listFiles();
		
		for(int i=0; i<allFiles.length; i++)
		{
			new TotWorkTime(allFiles[i].getAbsolutePath());
			//new TotWorkTime(args[0]);
		}
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
			String dayShortName = getDayShortName(filePath);
			
			System.out.println("Total time on "+dateByFilePath(filePath)+" ["+dayShortName+"] : "+formatMinutes(totTime));
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