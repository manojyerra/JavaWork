package utils;

import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.*;


public class FileUtils
{
	public static ArrayList<String> getFileData(String filePath)
	{
		return FileUtils.getFileData(filePath, false, true);
	}

	public static ArrayList<String> getFileData(String filePath, boolean trim, boolean addEmptyLine)
	{
		ArrayList<String> rows = new ArrayList<String>();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(filePath));
		
			String line = null;
		
			while( (line = br.readLine()) != null )
			{
				if(trim)
				{
					line = line.trim();
				}

				if(!addEmptyLine && line.length() == 0)
				{
					continue;
				}

				rows.add(line);
			}
		}
		catch(Exception e){e.printStackTrace();}
		
		return rows;
	}

	public static String getFileDataAsStr(String filePath)
	{
		String data = "";
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(filePath));
		
			String line = null;
		
			while( (line = br.readLine()) != null )
			{
				data += line;
			}
		}
		catch(Exception e){e.printStackTrace();}
		
		return data;
	}

	public static void write(File file, String content) throws Exception
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(file) );

		bw.write(content, 0, content.length());
		bw.flush();
		bw.close();
	}
	
	public static void writeList(String filePath, ArrayList<String> list) throws Exception
	{
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));

		for(int i=0; i<list.size(); i++)
		{			
			if(i != 0)
				bw.newLine();

			String str = list.get(i);
			//System.out.println(str);
			bw.write(str, 0, str.length());
			bw.flush();
		}
			
		bw.close();
	}
}


