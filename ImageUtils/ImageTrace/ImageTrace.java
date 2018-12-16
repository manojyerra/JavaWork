import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.File;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

class ImageTrace
{
	Robot robo = new Robot();

	public static void main(String[] args) throws Exception
	{
		new ImageTrace();
	}
	
	ImageTrace() throws Exception
	{
		String subImgName = "x64rcproc.png";
		
		//BufferedImage subImgBuf = robo.createScreenCapture(new Rectangle(133,36,140,15));
		//ImageIO.write(subImgBuf, "png", new FileOutputStream(subImgName));		

		BufferedImage subImg = ImageIO.read(getClass().getResourceAsStream("x64rcproc.png"));
		
		while(true)
		{
			Thread.sleep(3*40*1000);
			
			if(IsProcessRunning("VirtualRouterClient.exe"))
				KillProcess("VirtualRouterClient.exe");
				
			if(IsProcessRunning("firefox.exe"))
				KillProcess("firefox.exe");
				
			if(IsProcessRunning("iexplore.exe"))
				KillProcess("iexplore.exe");

			if(IsProcessRunning("chrome.exe"))
			{
				//System.out.println("Chrome is running...");
				//if(IsImageMatch(subImg))
					KillProcess("chrome.exe");
			}
		}
				
	}
	
	boolean IsImageMatch(BufferedImage subImg) throws Exception
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double screenHeight = screenSize.getHeight();
		
		BufferedImage screenImg = robo.createScreenCapture(new Rectangle(0,0,(int)screenWidth,(int)screenHeight));

		return(Contains(screenImg, subImg));	
	}
	
	boolean IsProcessRunning(String processName) throws Exception
	{
		Runtime runtime = Runtime.getRuntime();
		String cmds[] = {"cmd", "/c", "tasklist"};
		Process proc = runtime.exec(cmds);
		BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		
		String line;
		while((line = br.readLine()) != null)
		{
			if(line.indexOf(processName) != -1)
			{
				br.close();
				return true;
			}
		}
		
		return false;
	}
	
	void KillProcess(String processName) throws Exception
	{
		Runtime.getRuntime().exec("taskkill /F /IM "+processName);	
	}
	
	boolean Contains(BufferedImage mainImg, BufferedImage subImg) throws Exception
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
						return true;
				}
			}
		}
		
		return false;
	}
	
}