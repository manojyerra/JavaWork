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


class FolderDelete
{
	ArrayList<DeleteFileByThread> threadsArr = new ArrayList<DeleteFileByThread>();

	int numThreads = 3;

	int filesCount = 0;
	static int processedFilesCount = 0;

	
	public static void main(String[] args) throws Exception
	{
		String folderPath = null;

		if(args.length != 1)
		{
			System.out.println("Enter folder path to delete.");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			folderPath = br.readLine();

			br.close();
		}
		else if(args.length == 1)
		{
			folderPath = args[0];
		}

		new FolderDelete(folderPath);

		while(true)
		{
			try{Thread.sleep(100);}catch(Exception e){}		
		}		
	}

	
	FolderDelete(String folderPath) throws Exception
	{
		long startTime = System.currentTimeMillis();

		if(validateFolderPath(folderPath) == false)
			System.exit(0);

		deleteFolderUsingThreads(new File(folderPath));
		
		long timeTaken = System.currentTimeMillis() - startTime;
		
		System.out.println("Time taken : "+timeTaken);
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


	void deleteFolderUsingThreads(File src) throws Exception	
	{
		System.out.println("\n\nStarted Deleting  "+src.getPath()+"  folder.");

		for(int i=0; i<numThreads; i++)
			threadsArr.add(new DeleteFileByThread());
		
		for(int i=0; i<numThreads; i++)
			threadsArr.get(i).start();

		//System.out.println("\n\nCreated "+numThreads+" threads...");
				
		deleteFolder(src);

		//System.out.println("\n\nDistibuted files list to all the threads for deletion...");

		for(int i=0; i<numThreads; i++)
			threadsArr.get(i).endOfAddingFiles();		

		for(int i=0; i<numThreads; i++)
			threadsArr.get(i).join();

		System.out.println("\n\nTotal files inside folder : "+filesCount);

		System.out.println("All Files are deleted...\n\nDeleting folders...");

		deleteEmptyFolders(src);

		System.out.println("End of Deleting folder...");		
	}
	

	public void deleteFolder(File src) throws Exception
	{
    	if(src.isDirectory())
		{
    		String files[] = src.list();

    		for (String file : files)
			{
				File srcFile = new File(src, file);

				deleteFolder(srcFile);
    		}
    	}
		else
		{
			threadsArr.get( filesCount%numThreads ).add(src);
			filesCount++;			
    	}
    }


	public void deleteEmptyFolders(File src) throws Exception
	{
    	if(src.isDirectory())
		{
    		String files[] = src.list();

			for (String file : files)
			{
				File srcFile = new File(src, file);
				deleteEmptyFolders(srcFile);
			}
			
			src.delete();
    	}
		
		return;
    }
}


class DeleteFileByThread extends Thread
{
	private Vector<File> list = new Vector<File>();
	private boolean isEndOfAddingFiles = false;
	private int index = 0;


	DeleteFileByThread()
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


	public void run() 
	{
		try
		{
			while(isEndOfAddingFiles == false || index < list.size())
			{
				if(index < list.size())
				{
					list.get(index).delete();
					index++;
					FolderDelete.processedFilesCount++;

					if(FolderDelete.processedFilesCount % 5000 == 0)
						System.out.println(FolderDelete.processedFilesCount+" files deleted.");
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	
}

