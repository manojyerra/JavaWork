import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;

class CopyPaste
{
	ArrayList<CopyFileByThread> threadsArr = new ArrayList<CopyFileByThread>();
	int numThreads = 5;
	
	static int totalCopied = 0;
	static String listName = "";
	
	Random rand = new Random();
	boolean copyUsingThreads = true;
	
	int numFiles = 0;
	
	public static void main(String[] args) throws Exception
	{
		listName = args[0];
		new CopyPaste(args[0], args[1], args[2]);
	}
	
	CopyPaste(String listFilePath, String srcPath, String destinPath) throws Exception
	{
		FileReader fr = new FileReader(listFilePath);

		long startTime = System.currentTimeMillis();

		for(int i=0; i<numThreads; i++)
			threadsArr.add(new CopyFileByThread());
		
		for(int i=0; i<numThreads; i++)
			threadsArr.get(i).start();
		
		copyFiles(srcPath, destinPath, listFilePath);
		
		System.out.println(listFilePath+" : number of files to copy : "+numFiles);
	
		for(int i=0; i<numThreads; i++)
			threadsArr.get(i).endOfAddingFiles();
		
		for(int i=0; i<numThreads; i++)
			threadsArr.get(i).join();	

		System.out.println("All threads are joined.");

		System.out.println("End of "+listFilePath+", Time taken : "+(System.currentTimeMillis()-startTime));
		
		fr.close();

		File delListFile = new File(listFilePath);
		delListFile.delete();
		
		System.exit(0);
	}
	
	public void copyFiles(String srcPath, String destPath, String listFilePath) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(listFilePath));
		
		String line = null;
		
		while( (line = br.readLine()) != null)
		{
			if(line.trim().length() > 0)
			{
				SrcDest srcDest = new SrcDest(new File(srcPath+line),  new File(destPath+line));
				
				threadsArr.get( numFiles%numThreads ).add(srcDest);
				
				numFiles++;
			}
		}
		
		br.close();
    }
}

class CopyFileByThread extends Thread
{
	Vector<SrcDest> list = new Vector<SrcDest>();
	boolean isEndOfAddingFiles = false;
	int index = 0;
	
	CopyFileByThread()
	{
	}
	
	void endOfAddingFiles()
	{
		isEndOfAddingFiles = true;
	}
	
	void add(SrcDest srcDest)
	{
		list.add(srcDest);
	}

	public void run() 
	{
		try{
			while(isEndOfAddingFiles == false || index < list.size())
			{
				if(index < list.size())
				{
					SrcDest srcDest = list.get(index);
					copyFile(srcDest.src, srcDest.dest);
					index++;
					CopyPaste.totalCopied++;
					
					if(CopyPaste.totalCopied % 5000 == 0)
						System.out.println(CopyPaste.listName+" : copied "+CopyPaste.totalCopied);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	void copyFile(File src, File dest) throws Exception
	{
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dest);

		int xKB = 1024*128;
		int srcLen = (int)src.length();
		int length = 0;
		
		if(srcLen < xKB && srcLen >= 0)
		{
			byte[] buffer = new byte[(int)srcLen];
	
			if((length = in.read(buffer)) > 0){
			   out.write(buffer, 0, length);
			}			
		}
		else
		{
			byte[] buffer = new byte[xKB];

			while ((length = in.read(buffer)) > 0){
			   out.write(buffer, 0, length);
			}			
		}

		in.close();
		out.close();
	}	
}


class SrcDest
{
	File src;
	File dest;
	
	SrcDest(File src, File dest)
	{
		this.src = src;
		this.dest = dest;
	}
}
