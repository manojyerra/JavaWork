import java.util.List;
import java.io.IOException;
import java.io.File;
import java.io.FileFilter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.util.Enumeration;


public class Search {

	public static void main(String[] args) throws Exception
	{
		new Search();
	}

	Search()
	{
		//boolean found = searchInJar("test.jar", "BigDataTable.cass");
		//System.out.println("Found : "+found);

		ArrayList<File> foundList = new ArrayList<File>();

		SearchInDir(new File("/home/vagrant/git/dsm/prpc-platform"), "DNodeException.class", foundList);

		for(int i=0; i<foundList.size(); i++)
			System.out.println("Found file at : "+foundList.get(i));
	}

	void SearchInDir(File dir, String searchFileName, ArrayList<File> foundList)
	{
		//if(foundAt[0] != null)
		//	return;

		File[] filesArr = dir.listFiles();

		for(int i=0; i<filesArr.length; i++)
		{
			File file = filesArr[i];

			if(file.isDirectory())
			{
				SearchInDir(file, searchFileName, foundList);
			}
			else
			{
				if(file.getName().endsWith(".jar"))
				{
					if(searchInJar(file.getPath(), searchFileName))
						foundList.add(file);
				}
				//else if(file.getName().equals(searchFileName))
				//{
				//	foundAt[0] = file;
				//	return;
				//}
			}
		}
	}


	boolean searchInJar(String jarPath, String searchFileName)
	{
		try
		{
			JarFile jar = new JarFile(jarPath);
			Enumeration e = jar.entries();

			while (e.hasMoreElements())
			{
				JarEntry entry = (JarEntry)e.nextElement();
				String strEntry = entry.getName();

				String fileName = (new File(strEntry)).getName();

				if(fileName.equals(searchFileName))
					return true;
			}
		}
		catch(Exception e){System.out.println(e.getMessage());}

		return false;
	}
}