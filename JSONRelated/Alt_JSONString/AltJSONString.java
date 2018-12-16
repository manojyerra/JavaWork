import lib_utils.*;

import java.io.*;
import java.util.*;

class AltJSONString
{
	BufferedWriter bw = null;
	String inputFileName = "";

	public static void main(String args[]) throws Exception
	{
		new AltJSONString();
	}

	AltJSONString() throws Exception
	{
		System.out.println("Enter file name : ");
		inputFileName = (new BufferedReader(new InputStreamReader(System.in))).readLine();

		int dotIndex = inputFileName.lastIndexOf(".");
		int len = inputFileName.length();
		String newFileName = inputFileName.substring(0,dotIndex) + "_Alt"+ inputFileName.substring(dotIndex, len);
		bw = new BufferedWriter(new FileWriter(newFileName));
		
		String str = Utils_JSON.GetFormattedJSONStr( Utils_File.GetText(inputFileName) );
		
		bw.write(str, 0, str.length());
		bw.newLine();
		bw.flush();
				
		bw.close();
	}
}
