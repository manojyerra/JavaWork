package lib_utils;

import java.io.*;
import java.util.*;

public class Utils_File
{
	public static Vector<String> GetAllLines(String filePath) throws Exception
	{	
		Vector<String> jsonVec = new Vector<String>();
		
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		
		String line = "";
		
		while((line = br.readLine()) != null)
		{
			line = line.trim();
			
			if(line.length()==0)
				continue;
				
			jsonVec.add(line);
		}
		
		br.close();
		
		return jsonVec;
	}	
	
	public static String GetText(String filePath) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String jsonStr = "";

		String line = "";
		while((line = br.readLine()) != null)
			jsonStr += line;

		br.close();
		return jsonStr;
	}
}