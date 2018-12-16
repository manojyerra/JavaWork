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

		long startTime = System.currentTimeMillis();

		ArrayList<File> modifiedFiles = new ArrayList<File>();

		File snapshotFile = new File(projDir+"/"+snapshotPath);

		//filter.setTimeStamp(Long.parseLong("1532770544719"));
		filter.setTimeStamp(snapshotFile.lastModified());

		SearchInDir(new File(projDir), modifiedFiles);

		printListFiles(modifiedFiles, ".java");

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
		String fileName = file.getName();

		if(file.isDirectory())
		{
			return (!fileName.equals("rules") && !fileName.equals("build") && !fileName.startsWith("."));
		}

		//String absolutePath = file.getAbsolutePath();

		//if(absolutePath.endsWith(".class") && absolutePath.indexOf("com/pega/") == -1)
		//{
		//	System.out.println("No com/pega : "+absolutePath);
		//}

		return ( (fileName.endsWith(".java") || fileName.endsWith(".class")) && file.lastModified() > timeStamp );
	}

	void setTimeStamp(long timeStamp)
	{
		this.timeStamp = timeStamp;
	}
}



/*
Java Files found false, Class files found : false  Dir Name : /home/manoj/all/work/master/prpc-platform/archives
Java Files found false, Class files found : false  Dir Name : /home/manoj/all/work/master/prpc-platform/db
Java Files found false, Class files found : false  Dir Name : /home/manoj/all/work/master/prpc-platform/dist
Java Files found false, Class files found : false  Dir Name : /home/manoj/all/work/master/prpc-platform/gradle
Java Files found false, Class files found : false  Dir Name : /home/manoj/all/work/master/prpc-platform/pipeline


Java Files found true, Class files found : false  Dir Name : /home/manoj/all/work/master/prpc-platform/addons
Java Files found false, Class files found : true  Dir Name : /home/manoj/all/work/master/prpc-platform/buildSrc
Java Files found true, Class files found : true  Dir Name : /home/manoj/all/work/master/prpc-platform/core
Java Files found true, Class files found : false  Dir Name : /home/manoj/all/work/master/prpc-platform/doc
Java Files found true, Class files found : true  Dir Name : /home/manoj/all/work/master/prpc-platform/dsm
Java Files found true, Class files found : true  Dir Name : /home/manoj/all/work/master/prpc-platform/etc
Java Files found false, Class files found : true  Dir Name : /home/manoj/all/work/master/prpc-platform/infrastructure
Java Files found true, Class files found : true  Dir Name : /home/manoj/all/work/master/prpc-platform/platform
Java Files found true, Class files found : true  Dir Name : /home/manoj/all/work/master/prpc-platform/test
Java Files found true, Class files found : true  Dir Name : /home/manoj/all/work/master/prpc-platform/tools



		SearchInDir(new File("/home/manoj/all/work/master/prpc-platform/addons"), modifiedFiles);
		SearchInDir(new File("/home/manoj/all/work/master/prpc-platform/buildSrc"), modifiedFiles);
		SearchInDir(new File("/home/manoj/all/work/master/prpc-platform/core"), modifiedFiles);
		SearchInDir(new File("/home/manoj/all/work/master/prpc-platform/doc"), modifiedFiles);
		SearchInDir(new File("/home/manoj/all/work/master/prpc-platform/dsm"), modifiedFiles);
		SearchInDir(new File("/home/manoj/all/work/master/prpc-platform/etc"), modifiedFiles);
		SearchInDir(new File("/home/manoj/all/work/master/prpc-platform/infrastructure"), modifiedFiles);
		SearchInDir(new File("/home/manoj/all/work/master/prpc-platform/platform"), modifiedFiles);
		SearchInDir(new File("/home/manoj/all/work/master/prpc-platform/test"), modifiedFiles);
		SearchInDir(new File("/home/manoj/all/work/master/prpc-platform/tools"), modifiedFiles);


		
		ArrayList<String> listOfDir = new ArrayList<String>();

		listOfDir.add("/home/manoj/all/work/master/prpc-platform/addons");
		listOfDir.add("/home/manoj/all/work/master/prpc-platform/archives");
		listOfDir.add("/home/manoj/all/work/master/prpc-platform/buildSrc");
		listOfDir.add("/home/manoj/all/work/master/prpc-platform/core");
		listOfDir.add("/home/manoj/all/work/master/prpc-platform/db");
		listOfDir.add("/home/manoj/all/work/master/prpc-platform/dist");
		listOfDir.add("/home/manoj/all/work/master/prpc-platform/doc");
		listOfDir.add("/home/manoj/all/work/master/prpc-platform/dsm");
		listOfDir.add("/home/manoj/all/work/master/prpc-platform/etc");
		listOfDir.add("/home/manoj/all/work/master/prpc-platform/gradle");
		listOfDir.add("/home/manoj/all/work/master/prpc-platform/infrastructure");
		listOfDir.add("/home/manoj/all/work/master/prpc-platform/pipeline");
		listOfDir.add("/home/manoj/all/work/master/prpc-platform/platform");
		listOfDir.add("/home/manoj/all/work/master/prpc-platform/test");
		listOfDir.add("/home/manoj/all/work/master/prpc-platform/tools");

		for(int i=0; i<listOfDir.size(); i++)
		{
			ArrayList<File> filesList = new ArrayList<File>();

			SearchInDir(new File(listOfDir.get(i)), filesList);

			boolean foundJavaFile = false;
			boolean foundClassFile = false;

			for(int j=0; j<filesList.size(); j++)
			{
				if(filesList.get(j).getName().endsWith(".java"))
					foundJavaFile = true;

				if(filesList.get(j).getName().endsWith(".class"))
					foundClassFile = true;
			}

			System.out.println("Java Files found "+foundJavaFile+", Class files found : "+foundClassFile+"  Dir Name : "+listOfDir.get(i));
		}
		
*/
