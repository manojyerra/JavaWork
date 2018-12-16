import java.awt.datatransfer.*;
import java.awt.Toolkit;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.Robot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.InputStream;

import java.io.*;
import java.net.*;
import java.util.*;


class AddFriends
{
    Robot robo = null;
    
    BufferedImage replayImg = null;
    BufferedImage pasteAndGoImg = null;
    BufferedImage addToCircleImg = null;
    BufferedImage friendsImg = null;
    BufferedImage okayGotItImg = null;
    
    Vector<String> linesVec = new Vector<String>();
    
	public static void main(String[] args)
	{
		new AddFriends();
	}
	
	AddFriends()
	{
        try
        {
            robo = new Robot();
            boolean testMode = false;
            
            if(testMode)
            {
                while(true)
                {
                    try{Thread.sleep(5000);}catch(Exception e){}
                    //System.out.println("Mouse At : "+GetMX()+" , "+GetMY());
                    TakeScreenShot();
                }
            }

            replayImg = ImageIO.read(new File("Replay.png"));
            pasteAndGoImg = ImageIO.read(new File("PasteAndGo.png"));
            addToCircleImg = ImageIO.read(new File("AddToCircles.png"));
            friendsImg = ImageIO.read(new File("Friends.png"));
            okayGotItImg = ImageIO.read(new File("OkayGotIt.png"));


            BufferedReader peopleBr = new BufferedReader(new FileReader("People.txt"));
            
			int skipNumLinks = Integer.parseInt((new BufferedReader(new FileReader("PeopleIDCount.txt"))).readLine());
			
			for(int i=0;i<skipNumLinks; i++)
				peopleBr.readLine();
			
			int linkCount = skipNumLinks;
            int newlyAdded = 0;
            
            String line = "";
            
            while( (line = peopleBr.readLine()) != null)
            {
				BufferedWriter bw = new BufferedWriter(new FileWriter("PeopleIDCount.txt"));
				String linkCountStr = ""+linkCount;
				bw.write(linkCountStr, 0, linkCountStr.length());
				bw.flush();
				bw.close();
                
                CopyInClipboard(line);
                
                robo.delay(3000);
                robo.mouseMove(345,77);
                
                robo.mousePress(InputEvent.BUTTON1_MASK);
                robo.mouseRelease(InputEvent.BUTTON1_MASK);
                robo.delay(100);
                
                robo.mousePress(InputEvent.BUTTON1_MASK);
                robo.mouseRelease(InputEvent.BUTTON1_MASK);
                robo.delay(100);
                
                robo.mousePress(InputEvent.BUTTON1_MASK);
                robo.mouseRelease(InputEvent.BUTTON1_MASK);
                robo.delay(500);
                
                robo.mousePress(InputEvent.BUTTON3_DOWN_MASK);
                robo.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                robo.delay(500);
                
                robo.mouseMove(345 + 68, 77 + 72);
                robo.mousePress(InputEvent.BUTTON1_MASK);
                robo.mouseRelease(InputEvent.BUTTON1_MASK);
                
                Point p = new Point();
                
                for(int i=0;i<3;i++)
                {
                    robo.delay(4000);
                    
                    if(IsImageMatch(addToCircleImg, p))
                    {
                        robo.mouseMove(p.x,p.y);
                        robo.mousePress(InputEvent.BUTTON1_MASK);
                        robo.mouseRelease(InputEvent.BUTTON1_MASK);
                        robo.delay(2000);
                        
                        robo.mousePress(InputEvent.BUTTON1_MASK);
                        robo.mouseRelease(InputEvent.BUTTON1_MASK);
                        robo.delay(2000);
                        
                        newlyAdded++;
                        break;
                    }
                    else
                    {
                        System.out.println("Add to circles image not matched...");
                    }
                }
                
                linkCount++;
                System.out.println("Link Count : "+linkCount+" newly added "+newlyAdded);
            }
        }
        catch(Exception e){e.printStackTrace();}
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
	
	void CopyInClipboard(String text)
	{
		StringSelection stringSelection = new StringSelection(text);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents (stringSelection, null);	
	}
    
    void TakeScreenShot()
    {
        try{
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight();
        
        BufferedImage screenImg = robo.createScreenCapture(new Rectangle(0,0,(int)screenWidth,(int)screenHeight));
        
        int count = 1;
        while(true)
        {
            count++;
            File f = new File("/Users/rahulkumar/Desktop/TempScreenShots/"+count+".png");
            if(f.exists() == false)
                break;
        }
        
        ImageIO.write(screenImg, "png", new FileOutputStream("/Users/rahulkumar/Desktop/TempScreenShots/"+count+".png"));
        }catch(Exception e){e.printStackTrace();}
    }
    
	boolean IsImageMatch(BufferedImage subImg, Point point) throws Exception
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();
		
		BufferedImage screenImg = robo.createScreenCapture(new Rectangle(0,0,(int)screenWidth,(int)screenHeight));
        ImageIO.write(screenImg, "png", new FileOutputStream("/Users/rahulkumar/Desktop/TempScreenShots/ScreenShot.png"));
        screenImg = ImageIO.read(new File("/Users/rahulkumar/Desktop/TempScreenShots/ScreenShot.png"));
        
		return(Contains(screenImg, subImg, point));
	}
    
	boolean Contains(BufferedImage mainImg, BufferedImage subImg, Point point) throws Exception
	{
		int mainW = mainImg.getWidth();
		int mainH = mainImg.getHeight();
		
		int firstPixel = subImg.getRGB(0, 0);
		int subW = subImg.getWidth();
		int subH = subImg.getHeight();
		
		for(int j=0;j<mainH;j++)
		{
			for(int i=0;i<mainW;i++)
			{
				if(mainImg.getRGB(i, j) == firstPixel)
				{
					boolean isSame = true;
					
					for(int y=j; y<j+subH && y<mainH; y++)
					{
						for(int x=i; x<i+subW && x<mainW ;x++)
						{
							int mainPixel = mainImg.getRGB(x, y);
							int subPixel = subImg.getRGB(x-i, y-j);
							
							if(mainPixel != subPixel)
							{
								isSame = false;
								break;
							}
						}
						if(isSame == false)
							break;
					}
					
					if(isSame)
                    {
                        point.x = i + subW / 2;
                        point.y = j + subH / 2;
						return true;
                    }
				}
			}
		}
		
		return false;
	}
    
    
    
	void ReadLink(String link) throws Exception
	{
		try
		{
			URL url = new URL(link);
			
			System.out.print("Going to open link -> ");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			System.out.print(" Done.  ");
            
			linesVec.clear();
			
			System.out.print(" Going to read -> ");
            
			String lineData = "";
			while((lineData = br.readLine()) != null)
				linesVec.add(lineData);
            
			System.out.print(" Done. \n\n");
			
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("ERRORRRRRRRRRRRRRR...........");
			e.printStackTrace();
		}
	}
	
	Vector<String> ReadWords(Vector<String> linesVec, String startStr, String endStr, int maxLen) throws Exception
	{
		Vector<String> wordsVec = new Vector<String>();
		
		for(int i = 0; i < linesVec.size(); i++)
        {
			String line = linesVec.get(i);
            
            System.out.println("line <"+line+">");
            
        	do
			{
	    		int startIndex = line.indexOf(startStr);
				
				if(startIndex == -1)
					break;
	    		
				line = line.substring(startIndex + startStr.length(), line.length());
                
				int endIndex = line.indexOf(endStr);
				
				if(endIndex == -1)
					break;
				
				String word = line.substring(0, endIndex);
                
               
                
				boolean alreadyExist = false;
				for(int loop=0; loop<wordsVec.size(); loop++)
				{
					if(wordsVec.get(loop).equals(word))
					{
						alreadyExist = true;
						break;
					}
				}
				
				if(alreadyExist == false)
				{
                    
					if( (maxLen > 0 && word.length() < maxLen) || maxLen <= 0)
					{
						wordsVec.add( word );
						line = line.substring(endIndex + endStr.length(), line.length());
					}
				}
        	}
			while(true);
        }
		
		//for(int i=0;i<wordsVec.size();i++)
		//{
		//	System.out.println("Word : <"+wordsVec.get(i)+">");
        
			//output.write(wordsVec.get(i), 0, wordsVec.get(i).length());
			//output.newLine();
			//output.flush();
		//}
		
		return wordsVec;
	}
}


//                if(IsImageMatch(okayGotItImg, p))
//                {
//                    robo.mouseMove(p.x,p.y);
//                    robo.mousePress(InputEvent.BUTTON1_MASK);
//                    robo.mouseRelease(InputEvent.BUTTON1_MASK);
//                    robo.delay(2000);
//                }
//                else
//                {
//                    System.out.println("Okay got it image not matched...");
//                }

