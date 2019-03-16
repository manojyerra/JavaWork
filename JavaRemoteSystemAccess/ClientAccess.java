import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.*;


class ClientAccess
{
	static final int SLEEP_SECONDS = 5*60; //5 mins
	
	public static void main(String[] args) throws Exception
	{
		long startTime = System.currentTimeMillis();
		boolean isConnected = isInternetConnected_1();
		System.out.println(isConnected+" Time1: "+(System.currentTimeMillis() - startTime));
		
		startTime = System.currentTimeMillis();
		isInternetConnected_2();
		System.out.println(isConnected+" Time2: "+(System.currentTimeMillis() - startTime));
		
		//new ClientAccess();
	}

	ClientAccess() throws Exception
	{
		copyFiles("C:/Users/HI/Google Drive", "D:/KidsVideos", "mp4");
		
		String link = "https://docs.google.com/document/d/1tj49UC38YEnkZMnQEWLyAjRvg_BpwoHFhjj4BAUw-U4/edit?usp=sharing";

		while(true)
		{
			try 
			{
				Thread.sleep(SLEEP_SECONDS*1000);
				
				int[] arr = Utils.readInfo(link);
				
				if(arr[0] == 0)
				{
					Utils.shutdownSystem();
				}
				
				if(arr[1] == 0 && Utils.isChromeRunning())
				{
					Utils.closeChrome();
					
					for(int i=0; i<300000; i+=5000)
					{
						if(Utils.isChromeRunning())
						{
							Utils.closeChrome();
						}
						
						Thread.sleep(5000);
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	static boolean isInternetConnected_1() throws Exception
	{
		Process process = Runtime.getRuntime().exec("ping www.geeksforgeeks.org");
        return (process.waitFor() == 0);
	}
	
	static boolean isInternetConnected_2()
	{
		try
		{ 
            URL url = new URL("https://www.geeksforgeeks.org/");
            URLConnection connection = url.openConnection(); 
            connection.connect(); 
        } 
        catch (Exception e) { 
            return false;
        } 
		
		return true;		
	}
	
	void copyFiles(String srcDir, String desDir, String ext)
	{
		//System.out.println("copyFiles called...");
		List<File> listFiles = Utils.getFileListByExtension(srcDir, ext);
		
		for(int i=0; i<listFiles.size(); i++)
		{
			File srcFile = listFiles.get(i);
			File desFile = new File(desDir+"/"+srcFile.getName());
			
			try{
				if(desFile.exists() == false)
				{
					//System.out.println("Going to copy "+srcFile.getName());
					Files.copy(srcFile.toPath(), desFile.toPath());
					//System.out.println(" : Done");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
