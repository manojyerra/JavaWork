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

class FindImgOnScreen
{
    Robot robo = new Robot();
	byte[] imgBytes = null;
	
	int x = 142;
	int y = 46;
	int w = 69;
	int h = 14;
	
	String binFilePath = "img.bin";
   
	public static void main(String[] args) throws Exception
	{
		new FindImgOnScreen();
	}
	
	FindImgOnScreen() throws Exception
	{
		//BufferedImage screenImg = robo.createScreenCapture(new Rectangle(x,y,w,h));
		//SaveBufferedImgAsBinary(screenImg, binFilePath);

		imgBytes = new byte[w * h * 3];
		FileInputStream fis = new FileInputStream(binFilePath);
		fis.read(imgBytes);

        try
        {
			while(true)
			{
				if(IsSame(x,y,w,h))
				{
					//System.out.println("Image matched...");
					Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");
				}
				else
				{
					//System.out.println("Image not matched...");	
				}
				
				Thread.sleep(10000);
			}
        }
        catch(Exception e){e.printStackTrace();}
    }
	
	boolean IsSame(int xx, int yy, int ww, int hh)
	{
		BufferedImage img = robo.createScreenCapture(new Rectangle(xx,yy,ww,hh));
		
		int imgW = img.getWidth();
		int imgH = img.getHeight();
		
		int idx = 0;
		
		for(int y=0;y<imgH;y++)
		{
			for(int x=0;x<imgW;x++)
			{
				int pixel = img.getRGB(x, y);
				
				byte r = (byte)((pixel >> 16) & 0xff);
				byte g = (byte)((pixel >> 8) & 0xff);
				byte b = (byte)((pixel) & 0xff);
	
				if(imgBytes[idx++] != r)
					return false;

				if(imgBytes[idx++] != g)
					return false;

				if(imgBytes[idx++] != b)
					return false;
				
			}
		}
		
		return true;
	}
	
	boolean IsSame(BufferedImage img1, BufferedImage img2) throws Exception
	{
		int img1W = img1.getWidth();
		int img1H = img1.getHeight();
		
		int img2W = img2.getWidth();
		int img2H = img2.getHeight();
		
		if(img1W != img2W || img1H != img2H)
			return false;
		
		for(int y=0;y<img1H;y++)
		{
			for(int x=0;x<img1W;x++)
			{
				int img1Pixel = img1.getRGB(x, y);
				int img2Pixel = img2.getRGB(x, y);
							
				if(img1Pixel != img2Pixel)
					return false;
			}
		}
		
		return true;
	}
	
	void SaveBufferedImgAsBinary(BufferedImage img, String savePath) throws Exception
	{
		int imgW = img.getWidth();
		int imgH = img.getHeight();
		
		byte[] arr = new byte[imgW * imgH * 3];
		int index = 0;
		
		for(int y=0;y<imgH;y++)
		{
			for(int x=0;x<imgW;x++)
			{
				int pixel = img.getRGB(x, y);
				
				arr[index] = (byte)((pixel >> 16) & 0xff); 	index++;
				arr[index] = (byte)((pixel >> 8) & 0xff); 	index++;
				arr[index] = (byte)((pixel) & 0xff); 		index++;
			}
		}
		
		FileOutputStream fos = new FileOutputStream(savePath);
		fos.write(arr);
		fos.close();
	}
}
