import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.awt.GraphicsEnvironment;
import java.io.Console;

class FolderSize
{
	ArrayList<CalcFileSizeByThread> threadsArr = new ArrayList<CalcFileSizeByThread>();

	int numThreads = 3;
	int filesCount = 0;
	int foldersCount = 0;
	long folderSize = 0;
	static int totProcessedFiles = 0;


	public static void main(String[] args) throws Exception
	{
		String folderPath = null;

		if(args.length != 1)
		{
			System.out.println("Enter folder path to calculate size.");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			folderPath = br.readLine();

			br.close();
		}
		else if(args.length == 1)
		{
			folderPath = args[0];
		}

		new FolderSize(folderPath);

		boolean run = true;

		while(run)
		{
			try{Thread.sleep(1000);}catch(Exception e){}	
			//System.out.println("count : "+count);	
		}

		//System.out.println("End of constructor...");
	}
	

	FolderSize(String folderPath) throws Exception
	{
		long startTime = System.currentTimeMillis();

		if(validateFolderPath(folderPath) == false)
			System.exit(0);

		calcFolderSizeUsingThreads(new File(folderPath));
		
		long timeTaken = System.currentTimeMillis() - startTime;
		
		System.out.println("\nTime taken    : "+timeTaken+" milliseconds.");
	}


	boolean validateFolderPath(String folderPath)
	{
		if(folderPath == null || folderPath.trim().length() == 0)
		{
			System.out.println("Error : Invalid folder path.");
			return false;
		}

		File dir = new File(folderPath);

		if(dir.exists() == false)
		{
			System.out.println("Error : Folder not found.");
			return false;
		}

		if(dir.isDirectory() == false)
		{
			System.out.println("Error : "+folderPath+" is not a directory.");
			return false;
		}

		return true;
	}


	void calcFolderSizeUsingThreads(File src) throws Exception	
	{
		System.out.println("\n\nStarted calculating  "+src.getPath()+"  folder size...");

		for(int i=0; i<numThreads; i++)
			threadsArr.add(new CalcFileSizeByThread());
		
		for(int i=0; i<numThreads; i++)
			threadsArr.get(i).start();

		//System.out.println("Number of threads active : "+Thread.activeCount());

		calcFolderSize(src);

		for(int i=0; i<numThreads; i++)
			threadsArr.get(i).endOfAddingFiles();
		
		for(int i=0; i<numThreads; i++)
			threadsArr.get(i).join();

		for(int i=0; i<numThreads; i++)
			folderSize += threadsArr.get(i).getTotalSize();

		printFolderSize(folderSize);
		
		System.out.println("\nTotal folders : "+foldersCount);
		System.out.println("\nTotal files   : "+filesCount);
		System.out.println("\nTotal files and folders   : "+(foldersCount+filesCount));		
	}

	
	// public void printCurrentStatus()
	// {
		// int totProcessed = 0;

		// for(int i=0; i<numThreads; i++)
			// totProcessed += threadsArr.get(i).processedElementsCount();		

		// System.out.println("Calculated size for "+totProcessed+" files.");		
	// }


	public void printFolderSize(long folderSize)
	{
		long KB = 1024;
		long MB = 1024 * KB;
		long GB = 1024 * MB;

		if(folderSize < KB)
		{
			System.out.println("\nFolder size   : "+folderSize+" bytes");
		}	
		else if(folderSize < MB)
		{
			System.out.printf("\nFolder size   : %.2f KB ( %d bytes )\n",((double)folderSize/(double)KB), folderSize);
		}
		else if(folderSize < GB)
		{
			System.out.printf("\nFolder size   : %.2f MB ( %d bytes )\n",((double)folderSize/(double)MB), folderSize);
		}
		else
		{
			System.out.printf("\nFolder size   : %.2f GB ( %d bytes )\n",((double)folderSize/(double)GB), folderSize);
		}		
	}
	

	public void calcFolderSize(File src) throws Exception
	{
    	if(src.isDirectory())
		{
			folderSize += src.length();
			foldersCount++;

    		String files[] = src.list();

    		for (String file : files)
			{
				File srcFile = new File(src, file);
				calcFolderSize(srcFile);				
    		}
    	}
		else
		{
			threadsArr.get( filesCount%numThreads ).add(src);
			filesCount++;

			//if(filesCount % 20000 == 0)
			//	printCurrentStatus();
    	}
    }
}


class CalcFileSizeByThread extends Thread
{
	private Vector<File> list = new Vector<File>();
	private boolean isEndOfAddingFiles = false;
	private int index = 0;
	private long totalSize = 0;


	CalcFileSizeByThread()
	{
	}

		
	void endOfAddingFiles()
	{
		isEndOfAddingFiles = true;
	}
	

	void add(File src)
	{
		list.add(src);
	}


	long getTotalSize()
	{
		return totalSize;
	}


	int processedElementsCount()
	{
		return index;
	}


	public void run() 
	{
		try
		{
			while(isEndOfAddingFiles == false || index < list.size())
			{
				if(index < list.size())
				{
					totalSize += list.get(index).length();

					index++;
					FolderSize.totProcessedFiles++;

					if(FolderSize.totProcessedFiles % 5000 == 0)
					{
						System.out.println("Calculated size for "+FolderSize.totProcessedFiles+" files.");
						try{Thread.sleep(100);}catch(Exception e){}
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	
}
