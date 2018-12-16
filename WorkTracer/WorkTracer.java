import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.awt.MouseInfo;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.BufferedWriter;
import java.io.FileWriter;

class WorkTracer
{
	Robot robot = null;
	int screenW = 0;
	int screenH = 0;
	BufferedWriter reportWriter;


	public static void main(String args[]) throws Exception
	{
		new WorkTracer();
	}

	public WorkTracer() throws Exception
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();	
		screenW = (int)screenSize.getWidth();
		screenH = (int)screenSize.getHeight();

		robot = new Robot();
       	
		Calendar cal = Calendar.getInstance();

		DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
		DateFormat timeFormat = new SimpleDateFormat("HH_mm_ss");

		String dateInStr = "_"+dateFormat.format(cal.getTime());
		
		(new File(dateInStr)).mkdir();
		reportWriter = new BufferedWriter(new FileWriter(dateInStr+"/Report.txt", true));

		int lastMX = GetMX();
		int lastMY = GetMY();
		long lastClickTime = System.currentTimeMillis();

		String lastTimeInStr = timeFormat.format(Calendar.getInstance().getTime());

		Writeln("Application Start Time  "+lastTimeInStr.replace("_", ":"));
		reportWriter.newLine();
		reportWriter.flush();

		Runtime.getRuntime().addShutdownHook(new Thread( new ExitCallback(reportWriter, robot) ));

		int countDown = 0;
		boolean isFirstTime = true;
		
		long totalIdleTime = 0;
		long idleTime = 0;
		
		Font font = new Font("Arial", Font.BOLD, 50);

		while(true)
		{
			String currDate = "_"+dateFormat.format(Calendar.getInstance().getTime());
			
			if(currDate.equals(dateInStr) == false)
			{
				dateInStr = currDate;
				(new File(dateInStr)).mkdir();
				reportWriter.close();
				reportWriter = new BufferedWriter(new FileWriter(dateInStr+"/Report.txt", true));
			}

			String timeInStr = timeFormat.format(Calendar.getInstance().getTime());

			if(lastMX != GetMX() || lastMY != GetMY())
			{
				idleTime = System.currentTimeMillis() - lastClickTime;

				if(idleTime > 60*1000)
				{
					totalIdleTime += idleTime;

					long totSeconds = idleTime/1000;

					long hours = totSeconds / (60*60);
					long mins = (totSeconds-(hours*60*60)) / (60);
					long secs = totSeconds % (60);


					long totIdleSeconds = totalIdleTime/1000;

					long totIdleHours = totIdleSeconds / (60*60);
					long totIdleMins = (totIdleSeconds-(totIdleHours*60*60)) / (60);
					long totIdleSecs = totIdleSeconds % (60);

					Writeln("Idle Time : "+hours+":"+mins+":"+secs+" Total Idle Time : "+totIdleHours+":"+totIdleMins+":"+totIdleSecs+" From  "+lastTimeInStr.replace("_",":")+" To  "+timeInStr.replace("_",":"));
				}

				lastMX = GetMX();
				lastMY = GetMY();
				lastClickTime = System.currentTimeMillis();
				lastTimeInStr = timeFormat.format(Calendar.getInstance().getTime());
			}

			countDown++;

			if(isFirstTime || ( countDown > 60 && (System.currentTimeMillis() - lastClickTime) < 60*1000) )
			{
				if(isFirstTime == false)
					countDown = 0;
				
				BufferedImage buffImg = robot.createScreenCapture(new Rectangle(0,0,screenW,screenH));

				long idleSeconds = idleTime/1000;
				long idleHours = idleSeconds / (60*60);
				long idleMins = (idleSeconds-(idleHours*60*60)) / (60);

				if(isFirstTime == true)
				{
					Graphics g = buffImg.getGraphics();
					g.setFont(font);
					g.setColor(Color.red);
					g.drawString("Application started... ", 100, 100);
					g.dispose();					
				}
				else if(idleMins > 1)
				{
					Graphics g = buffImg.getGraphics();
					g.setFont(font);
					g.setColor(Color.red);
					g.drawString(idleHours+" hours and "+idleMins+" mins Idle before this ", 100, 100);
					g.dispose();
				}
				
				
				ImageIO.write(buffImg, "jpg", new File(dateInStr+"/"+timeInStr+".jpg"));
				//System.out.println(dateInStr+"/"+timeInStr+".jpg");
				
				isFirstTime = false;
			}

			robot.delay(1*1000);
		}
	}


	public void Writeln(String str)
	{
		try{
		reportWriter.write(str, 0, str.length());
		reportWriter.newLine();
		reportWriter.flush();
		}catch(Exception e){}
	}

	int GetMX()
	{
		int mx = (int) MouseInfo.getPointerInfo().getLocation().getX();
		return mx;
	}

	int GetMY()
	{
		int my = (int) MouseInfo.getPointerInfo().getLocation().getY();
		return my;
	}

}



class ExitCallback implements Runnable
{
	BufferedWriter bw;
	Robot robot;
	
	ExitCallback(BufferedWriter bw, Robot robot)
	{
		this.bw = bw;
		this.robot = robot;
	}

	void SetWriter(BufferedWriter bw)
	{
		this.bw = bw;
	}

	public void run()
	{
		try{
		DateFormat timeFormatTemp = new SimpleDateFormat("HH_mm_ss");
		String exitTime = timeFormatTemp.format(Calendar.getInstance().getTime());

		bw.newLine();

		String line = "Application End Time  "+ exitTime.replace("_",":");
		bw.write(line, 0, line.length());
		bw.newLine();
		bw.newLine();
		bw.newLine();
		bw.newLine();

		bw.flush();
		bw.close();
		}
		catch(Exception e){}
	}
}


/*
import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import java.awt.MouseInfo;

class ClickPlz
{
    public static void main(String args[]) throws Exception
    {
        System.out.println("ClickPlz has started....");
		
        //int loopCount = 0;
        //while(true)
        //{
        //    loopCount++;
        //    if(loopCount > 20)
        //        break;
        //    
        //    Thread.sleep(1000);
		//
        //    int x = (int) MouseInfo.getPointerInfo().getLocation().getX();
        //    int y = (int) MouseInfo.getPointerInfo().getLocation().getY();
        //
        //    System.out.println(x+","+y);
        //}
        
        
        Robot robot = new Robot();
        
        robot.delay(5000);
        
        while(true)
        {
            robot.mouseMove(1420,8);
            robot.mousePress(MouseEvent.BUTTON1_MASK);
            robot.mouseRelease(MouseEvent.BUTTON1_MASK);
            
            robot.delay(3000);
            robot.mouseMove(1594,324);
            robot.mousePress(MouseEvent.BUTTON1_MASK);
            robot.mouseRelease(MouseEvent.BUTTON1_MASK);
            
            robot.delay(3000);
            robot.mouseMove(1420,34);
            robot.mousePress(MouseEvent.BUTTON1_MASK);
            robot.mouseRelease(MouseEvent.BUTTON1_MASK);

            robot.delay(15*60*1000);
        }
    }
}

*/