import java.io.*;
import java.net.*;
import java.util.*;

class Utils
{
	public static int[] readInfo(String link) throws Exception
	{
		URL url = new URL(link);
		BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
		
		String line = br.readLine();
		
		int[] arr = new int[2];
		
		for(int i=0; i<arr.length; i++)
			arr[i] = 1;
		
		if(line != null)
		{
			arr[0] = Utils.tagValue("shStart", "shEnd", line);
			arr[1] = Utils.tagValue("chStart", "chEnd", line);
		}

		br.close();
		return arr;
	}
	
	private static int tagValue(String beginStr, String endStr, String line)
	{
		int beginI = line.indexOf(beginStr);
		int endI = line.indexOf(endStr);
		
		if(beginI != -1 && endI != -1)
		{
			String data = line.substring(beginI+beginStr.length(), endI);
			return Integer.parseInt(data.trim());
		}
		
		return 1;
	}
	
	public static boolean hasNetworkConnection()
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
	
	public static void shutdownSystem() throws Exception
	{
		String shutdownCommand = "shutdown.exe -s -f -t 1";
		Runtime.getRuntime().exec(shutdownCommand);					
	}
	
	public static void closeChrome() throws Exception
	{
		Runtime.getRuntime().exec("taskkill /F /IM chrome.exe /T");		
	}
	
	public static boolean isChromeRunning()
	{
		List<String> listProcesses = listRunningProcesses();
		
		for(int i=0; i<listProcesses.size(); i++)
		{
			if(listProcesses.get(i).equals("chrome.exe"))
				return true;
		}
		
		return false;
	}
	
	private static List<String> listRunningProcesses() {
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
	
	public static List<File> getFileListByExtension(String dir, String ext)
	{
		File[] files = (new File(dir)).listFiles();
		
		List<File> list = new ArrayList<File>();
		
		String chkEndsWith = "."+ext;
		
		for(int i=0; i<files.length; i++)
		{
			if(files[i].getPath().endsWith(chkEndsWith))
			{
				list.add(files[i]);
			}
		}
		
		return list;
	}
	
	public static void printList(List<?> list)
	{
		for(int i=0; i<list.size(); i++)
		{
			System.out.println(list.get(i).toString());
		}
	}
}


	// public static void renameFiles(String dir)
	// {
		// File[] files = (new File(dir)).listFiles();
		
		// for(int i=0; i<files.length; i++)
		// {
			// String name = files[i].getPath();
			// name = name.replace("1_0", "0");
			
			// //name = name.substring(2, name.length());
			
			// files[i].renameTo(new File(name));
		// }
	// }	