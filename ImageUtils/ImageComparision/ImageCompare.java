import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.imageio.*;
import java.awt.image.*;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class ImageCompare
{
	BufferedReader br = null;
	String baseFolder = null;
	String compareWithFolder = null;
	String diffFolder = null;

	BufferedWriter report = null;
	
	String outLine = "";

	public static void main(String args[])throws Exception
	{
		new ImageCompare(args);
	}

	public ImageCompare(String args[])throws Exception
	{
		if(args.length==2)
		{ 
			baseFolder=args[0];
			compareWithFolder=args[1];
		}
		else
		{
			br = new BufferedReader(new InputStreamReader(System.in));
			System.out.println("Enter the baseFolder");
			baseFolder =br.readLine();
			System.out.println("Enter the run Folder");
			compareWithFolder=br.readLine();
		}

		diffFolder =compareWithFolder+"/screen_diff";

		File diff =new File(diffFolder);
		if(diff.exists())
			diff.delete();

		diff.mkdir();
		
		//ImageCompare_Log__2013_10_23__20_18_16.txt
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd");
		DateFormat timeFormat = new SimpleDateFormat("HH_mm_ss");

		String dateInStr = dateFormat.format(Calendar.getInstance().getTime());
		String timeInStr = timeFormat.format(Calendar.getInstance().getTime());
		
		String logFilePath = "ImageCompare_Log__"+dateInStr+"__"+timeInStr+".txt";
		
		report = new BufferedWriter(new FileWriter(logFilePath));
		
		PasteFile(getClass().getResourceAsStream("ImageViewer.txt"), diffFolder+"/ImageViewer-1.1.jar");

		File[] comFolderFiles = (new File(compareWithFolder)).listFiles();

		long startTime = System.currentTimeMillis();

		int totNumPNGImages = 0;
		for(int i=0;i<comFolderFiles.length;i++)
		{
			if(comFolderFiles[i].getName().endsWith(".png") || comFolderFiles[i].getName().endsWith(".tga"))
				totNumPNGImages++;
		}		

		int failCount = 0;
		int passCount = 0;
		int compareCount = 0;

		for(int i=0;i<comFolderFiles.length;i++)
		{
			File comFile = comFolderFiles[i];

			if(comFile.getName().endsWith(".png") || comFile.getName().endsWith(".tga"))
			{
				if(compareCount%10 == 0)
					PrintCompareCount(compareCount, totNumPNGImages, passCount, failCount);

				File baseFolderFile = new File(baseFolder+"/"+comFile.getName());
				
				int result = 0;

				if(baseFolderFile.exists())
					result = CompareAndSave(baseFolderFile.getPath(), comFile.getPath(), diffFolder+"/"+comFile.getName());
				else
					result = CompareAndSave(null, comFile.getPath(), diffFolder+"/"+comFile.getName());

				if(result == 0)
				{
					failCount++;
					compareCount++;
					String line = compareCount +") Img Path : "+comFile.getPath()+" : Failed";
					System.out.println(line);
					WriteInReport(line);
				}
				else if(result == -1)
				{
					failCount++;
					compareCount++;
					String line = compareCount +") Img Path : "+comFile.getPath()+" : Failed (Not found in Base Folder)";
					System.out.println(line);
					WriteInReport(line);
				}
				else if(result == -2)
				{
					failCount++;
					compareCount++;
					String line = compareCount +") Img Path : "+comFile.getPath()+" : Failed (Resolution mismatch)";
					System.out.println(line);
					WriteInReport(line);					
				}
				else if(result == -3)
				{
					failCount++;
					compareCount++;
					String line = compareCount +") Img Path : "+comFile.getPath()+" : Failed (Error : Unable to compare)";
					System.out.println(line);
					WriteInReport(line);					
				}
				else if(result == 1)
				{
					passCount++;
					compareCount++;
					String line = compareCount +") Img Path : "+comFile.getPath()+" : Passed";
					System.out.println(line);
					WriteInReport(line);					
				}
			}
		}

		PrintCompareCount(compareCount, totNumPNGImages, passCount, failCount);
		
		long timeTaken = System.currentTimeMillis() - startTime;
		outLine = "Time Taken for "+compareCount+" images to compare : "+timeTaken+" milliseconds, "+(timeTaken/1000)+" seconds";
		System.out.println("\n\n"+outLine);
		
		WriteInReport("");
		WriteInReport("");
		WriteInReport(outLine);

		report.close();

		//PasteFile("Log__"+dateInStr+"__"+timeInStr+".txt", diffFolder+"/Log__"+dateInStr+"__"+timeInStr+".txt");
		PasteFile(logFilePath, diffFolder+"/Log__"+dateInStr+"__"+timeInStr+".txt");
	}
	
	public void PrintCompareCount(int compareCount, int totNumPNGImages, int passCount, int failCount)
	{
		Runtime.getRuntime().gc();
		outLine = "Total Number of images compared : "+compareCount+"/"+totNumPNGImages+", Passed : "+passCount+" Failed : "+failCount;
		System.out.println("\n"+outLine+"\n");
		
		WriteInReport("");	
		WriteInReport(outLine);	
		WriteInReport("");	
	}
	
	public void WriteInReport(String str)
	{
		try{
		report.write(str, 0, str.length());
		report.newLine();
		report.flush();
		}catch(Exception e){}
	}

	public boolean IsSame(String first, String second)
	{
		boolean returnVal = false;

		if(first == null || second == null)
			return returnVal;

		if( (new File(first)).exists() == false || (new File(second)).exists() == false)
			return returnVal;
			
		try
		{
			File baseFile = new File(first);
			File runFile = new File(second);
			
			int baseFileSize = (int)baseFile.length();
			int runFileSize = (int)runFile.length();
			
			byte[] baseBytes = new byte[baseFileSize];
			byte[] runBytes = new byte[runFileSize];

			DataInputStream base = new DataInputStream(new FileInputStream(first));
			DataInputStream run = new DataInputStream(new FileInputStream(second));
			
			base.read(baseBytes);
			run.read(runBytes);
			base.close();
			run.close();
			
			if(Arrays.equals(baseBytes, runBytes) == true)
				returnVal = true;
			
			baseBytes = null;
			runBytes = null;
		}
		catch(Exception eee){eee.printStackTrace();}
		
		return returnVal;
	}
	
	public int CompareAndSave(String first, String second, String savePath) throws Exception
	{
		if(IsSame(first, second) == true)
		{
			return 1;
		}
		
		int r = 255;
		int g = 0;
		int b = 0;
		int a = 255;

		int redColor = ((a<<24) + (r<<16) + (g<<8) + b);
		int greenColor = ((a<<24) + (0<<16) + (255<<8) + b);		
	
		
		try
		{
			BufferedImagePlus two = new BufferedImagePlus(second);
			BufferedImagePlus one = null;
			
			if(first == null)
				one = new BufferedImagePlus(two.GetWidth(), two.GetHeight(), BufferedImage.TYPE_INT_ARGB);
			else
				one = new BufferedImagePlus(first);
			
			two.SetSize(one.GetWidth(), one.GetHeight());
			
			if(one.GetWidth() != two.GetWidth() || one.GetHeight() != two.GetHeight())
				return -2;
				
			BufferedImagePlus oneClone = one.cloneObject();

			int width = one.GetWidth();
			int height = one.GetHeight();

			BufferedImage aTex = one.imageInfo.texture;
			BufferedImage bTex = two.imageInfo.texture;

			boolean failed = false;

			for(int j=0; (j<height); j++)
			{
				for(int i=0; (i<width); i++)
				{					
					if(IsSameColor(aTex.getRGB(i,j), bTex.getRGB(i,j)) == false)
					{
						//failed = true;
						//aTex.setRGB(i,j, redColor);
						
						
						int bw = 1;	//Box Width
						int maxSubFailCount = 0;
						int subFailCount = 0;
						
						for(int y=j-bw;y<=j+bw;y++)
						{
							if(y < 0 || y >= height)	continue;
							
							for(int x=i-bw;x<=i+bw;x++)
							{
								if(x < 0 || x >= width)	continue;

								if(IsSameColor(aTex.getRGB(x,y), bTex.getRGB(x,y)) == false)
									subFailCount++;
							}
						}
						
						//if(maxSubFailCount < subFailCount)
						//	maxSubFailCount = subFailCount;
						
						if(subFailCount >= 7)
						{
							failed = true;
							
							for(int y=j-bw;y<=j+bw;y++)
							{
								if(y < 0 || y >= height)	continue;
								
								for(int x=i-bw;x<=i+bw;x++)
								{
									if(x < 0 || x >= width)	continue;

									if(IsSameColor(aTex.getRGB(x,y), bTex.getRGB(x,y)) == false)
										aTex.setRGB(x, y, redColor);
								}
							}
						}
						
					}
				}
			}
			
			if(failed)
			{
				int halfW = one.GetWidth();
				int halfH = one.GetHeight();
				
				BufferedImagePlus finalImg = new BufferedImagePlus(halfW*3 + 3, halfH, BufferedImage.TYPE_INT_ARGB);
				
				finalImg.DrawImage(oneClone, halfW*0 + 0, 0, halfW, halfH);
				finalImg.DrawImage(two, halfW*1 + 1, 0, halfW, halfH);
				finalImg.DrawImage(one, halfW*2 + 2, 0, halfW, halfH);
				
				int fontH = 20;
				finalImg.DrawString("Base", halfW*0 + halfW/2, 0, Color.blue, fontH);
				finalImg.DrawString("Run", halfW*1 + halfW/2, 0, Color.blue, fontH);
				finalImg.DrawString("Diff", halfW*2 + halfW/2, 0, Color.blue, fontH);
				
				String finalSavePath = savePath.substring(0,savePath.lastIndexOf(".")) +"_diff.png";
				finalImg.WriteAs(finalSavePath, "png");
				two.WriteAs(savePath, "png");
				return 0;
			}
		}catch(Exception e)
		{
			System.out.println("\n Error : ");
			e.printStackTrace();
			System.out.println("\n");
			
			WriteInReport("\n Error : "+e.getMessage()+"\n");
			return -3;
		}
		return 1;
	}
	
	public boolean IsSameColor(int aColor, int bColor)
	{
		if(aColor == bColor)
			return true; 
					
		int aR = (aColor >> 16) & 255;
		int aG = (aColor >> 8) & 255;
		int aB = (aColor) & 255;
		int aA = (aColor >> 24) & 255;

		int bR = (bColor >> 16) & 255;
		int bG = (bColor >> 8) & 255;
		int bB = (bColor) & 255;
		int bA = (bColor >> 24) & 255;

		int errLimit = 10;
		
		if(Math.abs(aR-bR) > errLimit ||  Math.abs(aG-bG) > errLimit || Math.abs(aB-bB) > errLimit) // || Math.abs(aA-bA) > errLimit)
			return false;
		
		return true;
	}
	
	public void PasteFile(String sourceFile, String destinFile)
	{
		try{
		PasteFile(new FileInputStream(sourceFile), destinFile);
		}catch(Exception e){}
	}
	
	public void PasteFile(InputStream sourceStream, String destinFile)
	{
		try
		{
			DataInputStream dis = new DataInputStream(sourceStream);
			byte[] fileBytes =new byte[dis.available()];
			dis.readFully(fileBytes);
			dis.close();
			
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(destinFile));
			dos.write(fileBytes, 0, fileBytes.length);
			dos.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}

//baseFolder = JOptionPane.showInputDialog("Enter the base folder");
//compareWithFolder = JOptionPane.showInputDialog("Enter the run folder");