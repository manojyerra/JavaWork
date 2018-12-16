// http://www.computerhope.com/windows7.htm

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
import java.io.DataInputStream;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;
import java.awt.Window;
import java.awt.Frame;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.BufferedWriter;
import java.io.FileWriter;

import java.io.*;
import java.awt.*;
import javax.imageio.*;
import java.awt.image.*;

class VideoMaker
{
	Robot robot = null;
	int screenW = 0;
	int screenH = 0;
	BufferedWriter reportWriter;
	Rectangle rect;
	
	BufferedImage cursor = null;

	RectWindow rectWin;
	
	public static void main(String args[]) throws Exception
	{
		new VideoMaker();
	}

	public VideoMaker() throws Exception
	{
		rectWin = new RectWindow(0,0,0,0);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();	
		screenW = (int)screenSize.getWidth();
		screenH = (int)screenSize.getHeight();
		
		System.out.println("Width : "+screenW+", Height : "+screenH);
		//System.exit(0);
		
		rect = new Rectangle(0,0,screenW,screenH);

		robot = new Robot();
		System.out.println("Enter folder name to save jpeg images ");
		String folderName = (new DataInputStream(System.in)).readLine();
		File folder = new File(folderName);
		folder.mkdirs();
		
		cursor = ImageIO.read(new File("Cursor.png"));
		
		long captureStartTime = System.currentTimeMillis();
		
		int i = 0;
		boolean run = true;
		while(run) 
		{
			i++;
			int x = screenW/2; //MouseInfo.getPointerInfo().getLocation().x;
			int y = screenH/2; //MouseInfo.getPointerInfo().getLocation().y;

			int rectW = screenW;
			int rectH = screenH;
			int rectX = 0;
			int rectY = 0;
			
			if(rectX < 0)
				rectX = 0;
			if(rectY < 0)
				rectY = 0;
			if(rectX+rectW > screenW)
				rectX = screenW - rectW;
			if(rectY+rectH > screenH)
				rectY = screenH - rectH;
				
			rect.setBounds(rectX, rectY, rectW, rectH);
			rectWin.setBounds(rectX, rectY, rectW, rectH);
			
			rectWin.setVisible(false);
			BufferedImage buffImg = robot.createScreenCapture(rect);
			rectWin.setVisible(true);
			buffImg.getGraphics().drawImage(cursor, x-cursor.getWidth()/2 - rectX, y-cursor.getHeight()/2 - rectY, cursor.getWidth(), cursor.getHeight(), null);
			
			buffImg = SetSize(buffImg, 854,480);
			ImageIO.write(buffImg, "jpg", new File(folderName+"/"+i+".jpg"));
			robot.delay(1000);
			
			long timeInMillis = System.currentTimeMillis() - captureStartTime;
			//if(timeInMillis > 60*1000)
			//	break;
		}
		
		long timeTaken = System.currentTimeMillis() - captureStartTime;
		
		System.out.println("Total Time Captured : "+(timeTaken)/1000+" Seconds...");
	}
	
	public BufferedImage SetSize(BufferedImage bufImg, int width, int height) throws Exception
	{
		int type = BufferedImage.TYPE_INT_ARGB;

		if(HasTransparency(bufImg) == false)
			type = BufferedImage.TYPE_INT_RGB;
	
		Image image = bufImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage localTexture = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		localTexture.getGraphics().drawImage(image, 0, 0, null);

		return localTexture;
	}
	
	public boolean HasTransparency(BufferedImage bufImg)
	{
		return (bufImg.getTransparency() != Transparency.OPAQUE);
	}	
}

class RectWindow
{
	Window lwin;
	Window rwin;
	Window twin;
	Window bwin;
	
	int x;
	int y;
	int w;
	int h;
	
	RectWindow(int X, int Y, int W, int H)
	{
		this.x = X;
		this.y = Y;
		this.w = W;
		this.h = H;

		lwin = new Window(new Frame());
		lwin.setBackground(Color.red);
		lwin.setAlwaysOnTop(true);
		
		twin = new Window(new Frame());
		twin.setBackground(Color.red);
		twin.setAlwaysOnTop(true);

		rwin = new Window(new Frame());
		rwin.setBackground(Color.red);
		rwin.setAlwaysOnTop(true);

		bwin = new Window(new Frame());
		bwin.setBackground(Color.red);
		bwin.setAlwaysOnTop(true);

		setVisible(true);
		setBounds(x,y,w,h);
	}
				
	public void setVisible(boolean val)
	{
		lwin.setVisible(val);
		rwin.setVisible(val);
		twin.setVisible(val);
		bwin.setVisible(val);
		
		lwin.validate();
		rwin.validate();
		twin.validate();
		bwin.validate();

		lwin.invalidate();
		rwin.invalidate();
		twin.invalidate();
		bwin.invalidate();
	}
	
	public void setBounds(int X, int Y, int W, int H)
	{
		if(X == x && Y == y && W == w && H == h)
			return;
			
		this.x = X;
		this.y = Y;
		this.w = W;
		this.h = H;
		
		int lineLen = 1;
		
		lwin.setBounds(x-lineLen, y, lineLen, h);
		twin.setBounds(x-lineLen,y-lineLen,w+2*lineLen,lineLen);
		rwin.setBounds(x+w,y,lineLen,h);
		bwin.setBounds(x-lineLen,y+h,w+2*lineLen,lineLen);
	}
}