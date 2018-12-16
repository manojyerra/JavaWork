import java.util.*;
import java.io.*;

class JarCreator
{
	Vector<String> folders = new Vector<String>();
	String currDirAbsolutePath = "";
	String currDir = "";
	String osName = "";
	String jarCreatorFileName = "";
	String compileFileName = "";
	
	public static void main(String[] args)
	{
		new JarCreator();
	}
	
	JarCreator()
	{
		currDirAbsolutePath = System.getProperty("user.dir");
		currDir = ".";
		osName = System.getProperty("os.name");
		
		if(osName.equals("Mac OS X"))
		{
			jarCreatorFileName = "JarCreator.command";
			compileFileName = "Compile.command";
		}
		else
		{
			jarCreatorFileName = "JarCreator.bat";
			compileFileName = "Compile.bat";
		}
		
		
		//Step 1) Get all the folder paths.
		folders.add(currDir);
		AddAllFolders(new File(currDir));
		//PrintVector(folders, "Folders...");		
		
		
		//Step 2) Filter folders which has java files.
		Vector<String> filterDir = new Vector<String>();
		for(int i=0;i<folders.size();i++)
			if(HasType(folders.get(i), "java"))
				filterDir.add(folders.get(i));
		PrintVector(filterDir, "Java Folders...");

		
		//Step 3) Find java file which has main function.
		String mainJavaFile = "";
		for(int i=0;i<filterDir.size();i++)
		{
			File f = new File(filterDir.get(i));
			File[] files = f.listFiles();
			
			for(int j=0;j<files.length;j++)
			{
				if(files[j].getPath().toLowerCase().endsWith(".java"))
				{
					if(HasMainFunction(files[j].getPath()))// && files[j].getPath().indexOf("JarCreator") == -1)
					{
						System.out.println("\n\nMain function file : "+files[j].getPath()+"\n\n");
						mainJavaFile = files[j].getName();
						mainJavaFile = mainJavaFile.replace(".java", "");
					}
				}
			}
		}
		
		
		//Step 4) Create Compile.bat file.
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(compileFileName));
			String comLine = "cd "+currDirAbsolutePath;
			bw.write(comLine, 0, comLine.length());
			bw.newLine();
			bw.flush();
			
			String line = "javac ";
			
			for(int i=0;i<filterDir.size();i++)
				line += " "+filterDir.get(i)+"/*.java";
			
			bw.write(line, 0, line.length());
			bw.flush();
			bw.close();
		}catch(Exception e){e.printStackTrace();}		
		
		
		//Step 5) Create Manifest file
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter("MANIFEST.MF"));
			String maniFestStr3 = "Main-Class: "+mainJavaFile;			
			bw.write(maniFestStr3, 0, maniFestStr3.length());
			bw.newLine();
			bw.flush();
			bw.close();
		}catch(Exception e){e.printStackTrace();}
		
		
		//Step 6) Create JarCreator.bat file.
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(jarCreatorFileName));
			String comLine = "cd "+currDirAbsolutePath;
			bw.write(comLine, 0, comLine.length());
			bw.newLine();
			bw.flush();
						
			String line = "jar cfm "+mainJavaFile+".jar MANIFEST.MF";
			
			for(int i=0;i<filterDir.size();i++)
				line += " "+filterDir.get(i)+"/*.class";
			
			bw.write(line, 0, line.length());
			bw.close();
		}catch(Exception e){e.printStackTrace();}
		
		
		//Step 7) Run Compile.bat file after that JarCreator.bat file.
		RunFile(compileFileName);
		
		Int javaFileCount = new Int(0);
		GetCountOfType(new File("."), "java", javaFileCount);
		System.out.println("Java file count : "+javaFileCount.val);

		Int classFileCount = new Int(0);
		while(classFileCount.val < javaFileCount.val)
		{
			GetCountOfType(new File("."), "class", classFileCount);
			try{Thread.sleep(200);}catch(Exception e){e.printStackTrace();}
		}
		
		System.out.println("First class file count : "+classFileCount.val);
		try{Thread.sleep(1000);}catch(Exception e){e.printStackTrace();}
		classFileCount.val = 0;
		GetCountOfType(new File("."), "class", classFileCount);
		System.out.println("class file count : "+classFileCount.val);
		
		RunFile(jarCreatorFileName);
		
		//Step 8) Wait for jar file to be created...
		while(true)
		{
			File jarFile = new File(mainJavaFile+".jar");
			if(jarFile.exists())
				break;
			try{Thread.sleep(100);}catch(Exception e){e.printStackTrace();}
		}
		
		
		//Step 9) Delete Manifest.txt, Compile.bat and JarCreator.bat and class files.
		boolean deleteFiles = true;
		if(deleteFiles)
		{
			File manifestFile = new File("MANIFEST.MF");
			manifestFile.delete();
			File compileFile = new File(compileFileName);
			compileFile.delete();
			File jarCreatorFile = new File(jarCreatorFileName);
			jarCreatorFile.delete();
		
			DeleteType(new File(currDir), "class");
		}
	}
	
	void RunFile(String filePath)
	{
		try{
		
		Runtime runTime = Runtime.getRuntime();
		
		if(osName.equals("Mac OS X"))
		{
			Process p = runTime.exec("chmod +x "+filePath);
			p.waitFor();
			p.getOutputStream().close();
			
			p = runTime.exec("open "+filePath);
			p.waitFor();
			p.getOutputStream().close();
			
			//p.destroy();
		}
		else
		{
			Process p = runTime.exec(filePath);
			p.waitFor();
			p.getOutputStream().close();
			//p.destroy();	
		}
		
		}catch(Exception e){e.printStackTrace();}
	}
	
	boolean HasMainFunction(String filePath)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			
			String line = "";
			while((line = br.readLine()) != null)
			{
				if(line.indexOf("public") != -1 && line.indexOf("static") != -1 && line.indexOf("void") != -1 && line.indexOf("main") != -1 && line.indexOf("String") != -1)
					return true;
			}
		}catch(Exception e){e.printStackTrace();}
		
		return false;
	}
	
	void PrintVector(Vector<String> vec, String heading)
	{
		System.out.println("\n\n"+heading);
		for(int i=0;i<vec.size();i++)
			System.out.println(vec.get(i));
	}
	
	void AddAllFolders(File f)
	{
		File files[]=f.listFiles();

		for(int i=0;i<files.length;i++)
		{
			if(files[i].isDirectory())
			{
				folders.add(files[i].getPath());
				AddAllFolders(files[i]);
			}
		}	
	}
	
	void DeleteType(File f, String type)
	{	
		File files[]=f.listFiles();

		for(int i=0;i<files.length;i++)
		{
			if(files[i].isDirectory())
			{
				DeleteType(files[i], type);
			}
			
			if(files[i].getPath().toLowerCase().endsWith("."+type))
			{
				File typeFile = new File(files[i].getPath());
				typeFile.delete();
			}
		}
	}	
	
	boolean HasType(String folderPath, String typeName)
	{
		typeName = typeName.toLowerCase();
		File f = new File(folderPath);
		File[] files = f.listFiles();
		
		for(int i=0;i<files.length;i++)
		{
			if(files[i].getPath().toLowerCase().endsWith("."+typeName))
			{
				return true;
			}
		}
		
		return false;
	}
	
	void GetCountOfType(File f, String type, Int intObj)
	{
		type = type.toLowerCase();
		
		File files[] = f.listFiles();

		for(int i=0;i<files.length;i++)
		{
			if(files[i].isDirectory())
			{
				GetCountOfType(files[i], type, intObj);
			}
			
			if(files[i].getPath().toLowerCase().endsWith("."+type))
			{
				intObj.Set(intObj.Get()+1);
			}
		}		
	}
}

class Int
{
	public int val;
	
	public Int(int val)
	{
		this.val = val;
	}
	
	public void Set(int val)
	{
		this.val = val;
	}
	
	public int Get()
	{
		return val;
	}
}