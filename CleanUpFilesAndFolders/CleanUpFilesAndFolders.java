import java.util.*;
import java.io.*;

class CleanUpFilesAndFolders
{
	public static void main(String args[]) throws Exception
	{
		new CleanUpFilesAndFolders();
	}

	CleanUpFilesAndFolders() throws Exception
	{
		BufferedReader br = new BufferedReader( new InputStreamReader( System.in ) );
		
		System.out.println("Enter folder path : ");
		String folderPath = br.readLine();
		
		System.out.println("Enter list of endsWith strings to delete files");
		String fileEndsWithList = br.readLine();
		
		System.out.println("Enter list of endsWith strings to delete folders");
		String folderEndsWithList = br.readLine();
		
		String[] fileEndsWithArr = fileEndsWithList.split(",");
		String[] folderEndsWithArr = folderEndsWithList.split(",");
		
		//for(int i=0; i<fileEndsWithArr.length; i++)
		//	System.out.println(fileEndsWithArr[i]);
		
		CleanUp(folderPath, fileEndsWithArr, folderEndsWithArr);
	}
	
	void CleanUp(String folderPath, String[] fileEndsWithArr, String[] folderEndsWithArr)
	{
		Vector<File> vec = new Vector<File>();
		vec.add(new File(folderPath));
		
		for(int i=0; i<vec.size(); i++)
		{
			File fp = vec.get(i);
			
			if(fp.isDirectory())
			{
				boolean dirDeleted = false;
				
				for(int dirArrI = 0; dirArrI < folderEndsWithArr.length; dirArrI++)
				{
					if(fp.getName().endsWith(folderEndsWithArr[dirArrI]))
					{
						DeleteDir(fp);
						dirDeleted = true;
						System.out.print("\nDeleting folder : "+fp.getName()+" -- Done");
						break;
					}
				}
				
				if(!dirDeleted)
				{
					File[] listFiles = fp.listFiles();
					
					for(int listFileI = 0; listFileI < listFiles.length; listFileI++)
						vec.add(listFiles[listFileI]);
				}
			}
			else
			{
				for(int j = 0; j < fileEndsWithArr.length; j++)
				{
					if(fp.getName().endsWith(fileEndsWithArr[j]))
					{
						System.out.print("\nDeleting file : "+fp.getName());
						
						if(fp.delete())
							System.out.print(" -- Done");
						else
							System.out.print(" -- Failed");
					
						break;
					}
				}
			}
		}
	}
	
	void DeleteDir(File file) 
	{
		File[] contents = file.listFiles();
		
		if (contents != null) 
		{
			for (File f : contents) 
			{
				DeleteDir(f);
			}
		}
		file.delete();
	}
}





