import java.util.ArrayList;
import java.io.*;

class FileAndLineNum
{
	public File file = null;
	public int numLines = 0;

	FileAndLineNum()
	{
	}
	
	FileAndLineNum(File file, int numLines)
	{
		this.file = file;
		this.numLines = numLines;
	}
}


class NumberOfLines
{
	public static void main(String[] args)
	{
		new NumberOfLines();
	}
	
	NumberOfLines()
	{
		try
		{
			File srcDir = new File("C:/all/work/ESS/Repos/GLRenderer/src/App");
			ArrayList<File> listOfFiles = GetFilesList(srcDir);
			
			ArrayList<FileAndLineNum> list = new ArrayList<FileAndLineNum>();
			
			for(int i=0; i<listOfFiles.size(); i++)
			{
				File file = listOfFiles.get(i);
				int numLines = GetNumLines(file);
				list.add(new FileAndLineNum(file, numLines));
			}
			
			SortByLineNums(list);
			
			for(int i=0; i<100 && i < list.size(); i++)
			{
				System.out.println(list.get(i).numLines+" : "+list.get(i).file.getName());
			}
		}
		catch(Exception exce)
		{
			exce.printStackTrace();
		}
	}
	
	void SortByLineNums(ArrayList<FileAndLineNum> list)
	{
		int listSize = list.size();
		
		for(int i=0; i<listSize; i++)
		{
			for(int j=i+1; j<listSize; j++)
			{
				if(list.get(j).numLines > list.get(i).numLines)
				{
					FileAndLineNum obj = list.get(i);
					list.set(i, list.get(j));
					list.set(j, obj);
				}
			}
		}
	}
	
	int GetNumLines(File file)
	{
		int numLines = 0;
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			
			while( (line = br.readLine()) != null)
			{
				numLines++;
			}
		}
		catch(Exception exce)
		{
			exce.printStackTrace();
		}
		
		return numLines;
	}
	
	ArrayList<File> GetFilesList(File dir)
	{
		ArrayList<File> filesList = new ArrayList<File>();
		GetFilesListRecursively(dir, filesList);
		return filesList;
	}
	
	void GetFilesListRecursively(File dir, ArrayList<File> filesList)
	{
		File[] listFiles = dir.listFiles();
		
		for(int i=0; i<listFiles.length; i++)
		{
			File file = listFiles[i];
			String fileName = file.getName();
			
			if(file.isDirectory())
			{
				//if(!file.getName().endsWith("3rdParty") &&  !file.getName().endsWith("SUI"))
					GetFilesListRecursively(file, filesList);
			}			
			else if(fileName.endsWith(".h") || fileName.endsWith(".cpp"))
			{
				filesList.add(file);
			}
		}
	}
}

