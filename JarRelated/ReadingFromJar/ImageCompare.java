import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class ImageCompare
{
	//BufferedWriter bw = new BufferedWriter(new FileWriter("Output.txt"));

	public static void main(String args[])throws Exception
	{
		new ImageCompare(args);
	}

	public ImageCompare(String args[])throws Exception
	{
		PasteFile(getClass().getResourceAsStream("0.png"), "1.png");
		//bw.close();
	}

	public void Writeln(String line)
	{
		//try{
		//	bw.write(line,0,line.length());
		//	bw.newLine();
		//	bw.flush();
		//}
		//catch(Exception e) { Writeln(e.getMessage()); }
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
			Writeln(e.getMessage());
		}
	}	
}