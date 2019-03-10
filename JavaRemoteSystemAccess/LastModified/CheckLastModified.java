import java.io.*;
import java.net.*;
import java.util.*;

class CheckLastModified
{
	public static void main(String[] args) throws Exception
	{
		new CheckLastModified();
	}

	CheckLastModified() throws Exception
	{
		File sh = new File("C:/Users/HI/Google Drive/SystemInfo.gdoc");
		
		while(true)
		{
			try 
			{
				//System.out.println("LastModified : "+sh.lastModified());				
				System.out.println("LastModified : "+hasNetworkConnection());
				Thread.sleep(10*1000);				
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}			
		}
	}
	
	void FolderBase() throws Exception
	{
		File sh = new File("C:/Users/HI/Google Drive/Sh");
		File ch = new File("C:/Users/HI/Google Drive/Ch");
		
		while(true)
		{
			try 
			{
				Thread.sleep(5*1000);
				
				if(sh.exists())
				{
					shutdownSystem();
				}
				else if(ch.exists() && isChromeRunning())
				{
					shutdownChrome();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}		
	}
	
	static boolean hasNetworkConnection()
	{
		try{
			String link = "https://docs.google.com/document/d/1xy06l2XjzObkMcNr7XwRNVKv6a-b-0TVit7zDGTSviI/edit?usp=sharing";
			BufferedReader br = new BufferedReader(new InputStreamReader((new URL(link)).openStream(), "UTF-8"));
			System.out.println(br.readLine());
			br.close();
			return true;
		}
		catch(Exception e) {
		}
		
		return false;		
	}
	
	static void shutdownSystem() throws Exception
	{
		String shutdownCommand = "shutdown.exe -s -f -t 1";
		Runtime.getRuntime().exec(shutdownCommand);					
	}
	
	static void shutdownChrome() throws Exception
	{
		Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");		
	}
	
	static boolean isChromeRunning()
	{
		List<String> listProcesses = listRunningProcesses();
		
		for(int i=0; i<listProcesses.size(); i++)
		{
			if(listProcesses.get(i).equals("chrome.exe"))
				return true;
		}
		
		return false;
	}
	
	static List<String> listRunningProcesses() {
		List<String> processes = new ArrayList<String>();
		
		try {
			String line;
			Process p = Runtime.getRuntime().exec("tasklist.exe /fo csv /nh");
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			
			while ((line = input.readLine()) != null){
				if (!line.trim().equals("")) {
					// keep only the process name
					line = line.substring(1);
					processes.add(line.substring(0, line.indexOf("\"")));
				}
			}
			input.close();
		}
		catch (Exception err) {
			err.printStackTrace();
		}
		return processes;
	}		
}
