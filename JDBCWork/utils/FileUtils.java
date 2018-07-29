package utils;

import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;


public class FileUtils
{
	public static ArrayList<String> getFileData(String filePath)
	{
		ArrayList<String> rows = new ArrayList<String>();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(filePath));
		
			String line = null;
		
			while( (line = br.readLine()) != null )
			{
				rows.add(line);
			}
		}
		catch(Exception e){e.printStackTrace();}
		
		return rows;
	}

	public static void write(File file, String content) throws Exception
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(file) );

		bw.write(content, 0, content.length());
		bw.flush();
		bw.close();
	}
}
