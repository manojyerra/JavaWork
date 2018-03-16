import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Random;

class FasterDelete
{
	ArrayList<DeleteFileByThread> threadsArr = new ArrayList<DeleteFileByThread>();
	int numThreads = 5;	
	int filesCount = 0;
	
	public static void main(String[] args) throws Exception
	{
		File src = new File("/home/vagrant/git/dsm/merge_rules");

		if(args.length > 0)
		{
			new FasterDelete(new File(args[0]));
		}
		else
		{			
			new FasterDelete(src);
		}
	}
	
	FasterDelete(File src) throws Exception
	{
		long startTime = System.currentTimeMillis();
				
		deleteFolderUsingThreads(src);
		
		long timeTaken = System.currentTimeMillis() - startTime;
		
		System.out.println("Time taken : "+timeTaken);
	}
	
	void deleteFolderUsingThreads(File src) throws Exception	
	{
		System.out.println("\n\nStarted Deleting files...");

		for(int i=0; i<numThreads; i++)
			threadsArr.add(new DeleteFileByThread());
		
		for(int i=0; i<numThreads; i++)
			threadsArr.get(i).start();

		System.out.println("\n\nCreated "+numThreads+" threads...");
				
		deleteFolder(src);

		System.out.println("\n\nDistibuted files list to all the threads for deletion...");

		System.out.println("\n\nTotal files inside folder : "+filesCount);

		for(int i=0; i<numThreads; i++)
			threadsArr.get(i).endOfAddingFiles();		

		for(int i=0; i<numThreads; i++)
			threadsArr.get(i).join();

		System.out.println("All Files are deleted...\n\nDeleting folder...");

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
	Vector<File> list = new Vector<File>();
	boolean isEndOfAddingFiles = false;
	int index = 0;

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
		try{
			while(isEndOfAddingFiles == false || index < list.size())
			{
				if(index < list.size())
				{
					list.get(index).delete();
					index++;
					
					if(index % 5000 == 0)
						System.out.println(index+" files deleted using thread : "+Thread.currentThread().getId());
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}	
}

