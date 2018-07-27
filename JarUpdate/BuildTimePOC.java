import java.util.*;
import java.io.File;
import java.io.FileFilter;

//AgentManagementAPI
//RequestorManagementAPI

class BuildTimePOC	
{
	String projDir = "/home/manoj/all/work/master/prpc-platform";
	String snapshotPath = "core/prwebj2ee/scripts/build/appBuild/pegadbinstall-0.0.1-SNAPSHOT.zip";

	LastModifiedFilter filter = new LastModifiedFilter();

	public static void main(String args[]) throws Exception
	{
		new BuildTimePOC();
	}

	BuildTimePOC() throws Exception
	{
		//ArrayList<String> modifiedFiles = Util.getModifiedFilesList(projDir, "java");
		//System.out.println("Modified files list...");
		//Util.printList(modifiedFiles);
		//System.out.println("");

		System.out.println(System.currentTimeMillis()+"\n");
		//File f = new File(projDir+"/sync-readme.md");
		//System.out.println("LastModified Time : "+f.lastModified());

		long startTime = System.currentTimeMillis();

		ArrayList<File> modifiedFiles = new ArrayList<File>();

		File snapshotFile = new File(projDir+"/"+snapshotPath);

		//filter.setTimeStamp(Long.parseLong("1532710354139"));

		filter.setTimeStamp(snapshotFile.lastModified());

		SearchInDir(new File(projDir), modifiedFiles);
		printListFiles(modifiedFiles, ".class");

		System.out.println("\nTime taken : "+(System.currentTimeMillis()-startTime));
		
	}

	void printListFiles(ArrayList<File> list, String ext)
	{
		Iterator<File> iter = list.iterator();
		
		while(iter.hasNext())
		{
			File file = iter.next();

			if(ext == null || file.getName().endsWith(ext))
				System.out.print(file);
			
			if(iter.hasNext())
				System.out.print("\n");
		}
	}

	void SearchInDir(File dir, ArrayList<File> foundList)
	{
		File[] filesArr = dir.listFiles(filter);

		for(int i=0; i<filesArr.length; i++)
		{
			File file = filesArr[i];

			if(file.isDirectory())
			{
				SearchInDir(file, foundList);
			}
			else
			{
				foundList.add(file);
			}
		}
	}
}


class LastModifiedFilter implements FileFilter
{
	private long timeStamp = 0;

	public boolean accept(File file)
	{
		if(file.isDirectory())
			return !file.getName().equals("rules") && !file.getName().equals("build");

		String fileName = file.getName();

		return ( (fileName.endsWith(".java") || fileName.endsWith(".class")) && (file.lastModified() > timeStamp));
	}

	void setTimeStamp(long timeStamp)
	{
		this.timeStamp = timeStamp;
	}
}