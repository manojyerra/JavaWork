import java.sql.Timestamp;    
import java.util.Date; 
import java.awt.Robot;
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.FileReader;

class WorkTimeCalc
{
	Robot robo = new Robot();
	Frame frame = new Frame();

// 	NO_PROCESS=$(echo ps -aef | grep RunWorkTimeCalc | wc -l)
// 
// 	if [ $NO_PROCESS -le 0 ]; then
// 		sh "/home/manoj/all/work/other/JavaWork/WorkTimeCalc/RunWorkTimeCalc.sh" &>/dev/null & 
// 	fi
	
	public static void main(String args[]) throws AWTException, IOException
	{
		new WorkTimeCalc("bg");
	}
	
	WorkTimeCalc(String filePreFix) throws AWTException, IOException
	{
		File data = new File("data");
		data.mkdir();
	
		long continuesTime = 0;
		long sleepTimeInMin = 2;
		
		Point prevPos = MouseInfo.getPointerInfo().getLocation();
		
		frame.setTitle("0:0");
		frame.setLayout(null);
		frame.setBounds(500,300,300,50);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		
		updateTimeOnFrameFirstTime(data.getPath(), "bg", System.currentTimeMillis());
		
		while(true)
		{
			try{
				long sleepTimeMillis = sleepTimeInMin * 60L * 1000L;
				Thread.sleep(sleepTimeMillis);
			}catch(Exception e){}
			
			Point currPos = MouseInfo.getPointerInfo().getLocation();
			
			if(isEqual(currPos, prevPos) == false)
			{
				continuesTime += sleepTimeInMin;
				writeTime(filePreFix, continuesTime, System.currentTimeMillis(), data.getPath(), currPos);
				prevPos = currPos;
			}
			else
			{
				continuesTime = 0;
			}
		}
	}

	boolean isEqual(Point p1, Point p2)
	{
		int x1 = (int)p1.getX();
		int y1 = (int)p1.getY();
		
		int x2 = (int)p2.getX();
		int y2 = (int)p2.getY();
	
		return ( x1 == x2 && y1 == y2 );
	}
	
	void writeTime(String filePreFix, long continuesTime, long currentTimeMillis, String folderPath, Point mousePos) throws IOException
	{
		TimestampToDate currDate = new TimestampToDate(currentTimeMillis);
		
		String fileName = folderPath+"/"+filePreFix+"_"+currDate.getYear()+"_"+currDate.getMonth()+"_"+currDate.getDate()+".txt";

		frame.setTitle(formatMinutes(calcTime(fileName)));
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
		
		String text = currentTimeMillis+" "+continuesTime+" "+mousePos+" "+currDate;
		
		System.out.println(text);
		
		bw.write(text, 0, text.length());
		bw.newLine();
		bw.flush();
		
		bw.close();
	}

	void updateTimeOnFrameFirstTime(String folderPath, String filePreFix, long currTime) throws IOException
	{
		TimestampToDate currDate = new TimestampToDate(currTime);
		
		String fileName = folderPath+"/"+filePreFix+"_"+currDate.getYear()+"_"+currDate.getMonth()+"_"+currDate.getDate()+".txt";
		
		File fileObj = new File(fileName);
		
		if(fileObj.exists())
		{
			frame.setTitle(formatMinutes(calcTime(fileName)));
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
		
		br.close();
		
		return totTime;
	}
	
	String formatMinutes(int timeInMinutes)
	{
		int hours = timeInMinutes / 60;
		int mins = timeInMinutes - hours * 60;
		
		return hours+":"+mins;
	}	
}


class TimestampToDate
{
	int yy = 0;
	int mm = 0;
	int dd = 0;
	String dateStr = "";
	
	TimestampToDate(long timeInMillis)
	{
		Date date = dateFromTimeStamp(timeInMillis);
		
		dateStr = date.toString();
		String tokens[] = dateStr.split(" ");
		yy = Integer.parseInt(tokens[5]);
		mm = getMonthNumber(tokens[1]);
		dd = Integer.parseInt(tokens[2]);
	}
	
	public String toString()
	{
		return dateStr;
	}
	
	public int getYear()
	{
		return yy;
	}

	public int getMonth()
	{
		return mm;
	}

	public int getDate()
	{
		return dd;
	}
	
	private int getMonthNumber(String monthName)
	{
		int monthVal = 0;
		
		if(monthName.equals("Jan")) monthVal = 1;
		else if(monthName.equals("Feb")) monthVal = 2;
		else if(monthName.equals("Mar")) monthVal = 3;
		else if(monthName.equals("Apr")) monthVal = 4;
		else if(monthName.equals("May")) monthVal = 5;
		else if(monthName.equals("Jun")) monthVal = 6;
		else if(monthName.equals("Jul")) monthVal = 7;
		else if(monthName.equals("Aug")) monthVal = 8;
		else if(monthName.equals("Sep")) monthVal = 9;
		else if(monthName.equals("Oct")) monthVal = 10;
		else if(monthName.equals("Nov")) monthVal = 11;
		else if(monthName.equals("Dec")) monthVal = 12;

		return monthVal;
	}
	
	Date dateFromTimeStamp(long currentTimeMillis)
	{
		Timestamp ts=new Timestamp(currentTimeMillis);
		Date date=new Date(ts.getTime());  
		return date;
	}	
}




/*
Mon Jan 29 14:06:30 IST 2018
Sun Feb 18 07:09:17 IST 2018
Sat Mar 10 00:12:04 IST 2018
Wed Apr 18 10:17:39 IST 2018
Tue May 08 03:20:26 IST 2018
Sat Jun 16 13:26:01 IST 2018
Fri Jul 06 06:28:48 IST 2018
Tue Aug 14 16:34:23 IST 2018
Mon Sep 03 09:37:10 IST 2018
Fri Oct 12 19:42:45 IST 2018
Sat Nov 11 17:55:21 IST 2017
Fri Dec 01 10:58:08 IST 2017
*/


/*
work tracer

1) how to put some executable in the starup programs.
2) print correct time
3) Write every day data in separate file.
4) parser of the file

time 3
time 6
time 9
if mouse position is same : make continuesTime = 0
 
*/
