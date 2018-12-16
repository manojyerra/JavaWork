import java.io.*;
import java.net.*;

public class Alt {

	BufferedReader br = null;
	BufferedWriter bw = null;
	
	String filePath = "DataOneToOne.txt";
	
	public static void main(String[] args) throws Exception
	{
		new Alt();
	}
		
	Alt() throws Exception
	{
		bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath+"_new.txt"), "UTF-8"));
		br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "UTF-8"));
	

        String line1 = null;
        String line2 = null;
		
		boolean failed = false;
		
        while(true)
        {
			if(failed)
			{
				line1 = line2;
			}
			else
			{
				line1 = br.readLine();
			}
			
			line2 = br.readLine();
			
			if(line1 == null || line2 == null)
				break;
				
			if(line1.trim().length() == 0 || line2.trim().length() == 0)
				continue;
			
			if( isChar(line1.charAt(0))== true && isChar(line2.charAt(0)) == false)
			{
				failed = false;
				int index1 = line1.indexOf("<a href");
				int index2 = line2.indexOf("<a href");
				
				if(index1 != -1)
					line1 = line1.substring(0,index1);
				
				if(index2 != -1)
					line2 = line2.substring(0,index2);
				
				bw.write(line1, 0, line1.length());
				bw.newLine();
				bw.write(line2, 0, line2.length());
				bw.newLine();
				
				bw.flush();
			}
			else
			{
				failed = true;

				System.out.println(line1);
				//System.out.println(line2);				
			}
		}
		
		bw.close();
		br.close();
	}
	
	boolean isChar(char ch)
	{
		int ascii = (int)ch;
		return ( (ascii >= (int)'A' && ascii <= (int)'Z') ||  (ascii >= (int)'a' && ascii <= (int)'z') );
	}
}
