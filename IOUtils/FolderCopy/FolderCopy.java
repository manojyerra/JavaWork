import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Random;
import java.io.BufferedWriter;
import java.io.FileWriter;

class FolderCopy
{
	public static void main(String[] args) throws Exception
	{
		int numJVMs = 4;

		File src = null;
		File dest = null;

		if(args.length != 2)
		{
			System.out.println("Error: Invalid input.");
			loopInfinity();	
		}


		if(args.length == 2)
		{
			if(!validateFolderPath(args[0]))
				loopInfinity();	

			if(!validateFolderPath(args[1]))
				loopInfinity();	


			src = new File(args[0]);
			dest = new File(args[1]);
		}

		new FolderCopy(src, dest, numJVMs);
	}


	static void loopInfinity()
	{
		while(true)
		{
			try
			{
				Thread.sleep(200);
			}
			catch(Exception e){}
		}
	}
	

	FolderCopy(File src, File dest, int numJVMs) throws Exception
	{
		System.out.println("Creating folder structure...");
		
		dest = new File(dest.getAbsolutePath()+"/"+src.getName());

		long startTime = System.currentTimeMillis();

		ArrayList<String> list = FolderStructureCreator.create(src, dest);
		
		System.out.println("Time for create folders and generate list : "+(System.currentTimeMillis() - startTime));

		startTime = System.currentTimeMillis();
		
		writeInFiles(list, "list", src.getAbsolutePath(), numJVMs);

		System.out.println("Time for writing list into files : "+(System.currentTimeMillis() - startTime));
		
		for(int i=0; i<numJVMs; i++)
		{
			System.out.println("Starting list : "+i);
		
			ProcessBuilder pb = new ProcessBuilder("java", "FilesBatchCopy", "list_"+i+".txt", src.getAbsolutePath(), dest.getAbsolutePath());
			pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
			pb.redirectError(ProcessBuilder.Redirect.INHERIT);
			Process p = pb.start();

			//Runtime.getRuntime().exec("java CopyPaste list_"+i+".txt");
		}
		
		System.out.println("FolderCopy : end of constructor...");
	}


	static boolean validateFolderPath(String folderPath)
	{
		if(folderPath == null || folderPath.trim().length() == 0)
		{
			System.out.println("Error : Invalid folder path.");
			return false;
		}

		File dir = new File(folderPath);

		if(dir.exists() == false)
		{
			System.out.println("Error : "+folderPath+" Folder not found.");
			return false;
		}

		if(dir.isDirectory() == false)
		{
			System.out.println("Error : "+folderPath+" is not a directory.");
			return false;
		}

		return true;
	}

	
	void writeInFiles(ArrayList<String> list, String filePathPreFix, String removePreFix, int numJVMs) throws Exception
	{
		int rmLen = removePreFix.length();
		int size = list.size();
		
		BufferedWriter[] bwArr = new BufferedWriter[numJVMs];
		
		for(int i=0; i<numJVMs; i++)
		{
			bwArr[i] = new BufferedWriter(new FileWriter(filePathPreFix+"_"+i+".txt"));
		}
		
		for(int i=0; i<size; i++)
		{
			String line = list.get(i);
			
			bwArr[i%numJVMs].write(line, rmLen, line.length()-rmLen);
			bwArr[i%numJVMs].newLine();
		}
		
		for(int i=0; i<numJVMs; i++)
		{
			bwArr[i].flush();
			bwArr[i].close();
		}
	}
	

	// void writeSourceAndDestin(String filePath, File src, File destin) throws Exception
	// {
	// 	BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
		
	// 	String srcPath = src.getAbsolutePath();
	// 	String destinPath = destin.getAbsolutePath();
		
	// 	bw.write(srcPath, 0, srcPath.length());
	// 	bw.newLine();
	// 	bw.write(destinPath, 0, destinPath.length());
		
	// 	bw.flush();
	// 	bw.close();
	// }
}
