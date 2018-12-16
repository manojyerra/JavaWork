import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.File;
import java.awt.FontMetrics;
import java.util.Iterator;
import java.io.FileReader;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.GraphicsConfiguration;

class Util
{
	static ArrayList<String> getDiffFile(String gitFolder, String path) throws Exception
	{
		String command = "git -C "+gitFolder+" diff -U99999999 "+path;
		
		Process process = Runtime.getRuntime().exec(command);
		
		BufferedReader br = new BufferedReader( new InputStreamReader(process.getInputStream()) ) ;
		
		String line = null;
		boolean fileContentBegin = false;
		
		ArrayList<String> list = new ArrayList<String>();
		
		int noNewLineCount = 0;

		while( (line = br.readLine()) != null)
		{
			//System.out.println(line);

			if(!fileContentBegin)
			{
				if(line.startsWith("@@") && line.endsWith("@@"))
					fileContentBegin = true;

				continue;
			}
			
			if(line.equals("\\ No newline at end of file"))
			{
				noNewLineCount++;
			}
			else
			{
				list.add(line);
			}
		}

		//System.out.println("noNewLineCount : "+noNewLineCount);

		if(noNewLineCount == 0)
			list.add(" ");
		
		process.getInputStream().close();
		process.getOutputStream().close();
		process.getErrorStream().close();
		
		br.close();
		
		process.destroy();
		
		return list;
	}

	static String getGitBranch(String gitFolder) throws Exception
	{
		String command = "git -C "+gitFolder+" rev-parse --abbrev-ref HEAD";

		Process process = Runtime.getRuntime().exec(command);
		
		BufferedReader br = new BufferedReader( new InputStreamReader(process.getInputStream()) ) ;
		
		String line = br.readLine();
		
		process.getInputStream().close();
		process.getOutputStream().close();
		process.getErrorStream().close();
		
		br.close();
		
		process.destroy();

		return line;	
	}

	static boolean isGitFolder(String projPath) throws Exception
	{
		if(projPath == null || projPath.trim().length() == 0 || !((new File(projPath)).exists()) )
			return false;

		String command = "git -C "+projPath+" rev-parse --is-inside-work-tree";

		Process process = Runtime.getRuntime().exec(command);
		
		BufferedReader br = new BufferedReader( new InputStreamReader(process.getInputStream()) ) ;
		
		String line = br.readLine();

		//System.out.println("isGitFolder:<"+line+">");
		
		process.getInputStream().close();
		process.getOutputStream().close();
		process.getErrorStream().close();
		
		br.close();
		
		process.destroy();

		return (line != null && line.trim().equals("true"));	
	}

	static void checkoutAll(String gitFolder) throws Exception
	{
		String command = "git -C "+gitFolder+" checkout .";
		
		Process process = Runtime.getRuntime().exec(command);
		
		BufferedReader br = new BufferedReader( new InputStreamReader(process.getInputStream()) ) ;
		
		while(br.readLine() != null)
		{
		}
		
		process.getInputStream().close();
		process.getOutputStream().close();
		process.getErrorStream().close();
		
		br.close();
		
		process.destroy();
	}

	static void checkout(String gitFolder, String filePath) throws Exception
	{
		String command = "git -C "+gitFolder+" checkout "+filePath;
		//System.out.println("<"+command+">");
		
		Process process = Runtime.getRuntime().exec(command);
		
		BufferedReader br = new BufferedReader( new InputStreamReader(process.getInputStream()) ) ;
		
		while(br.readLine() != null)
		{
		}
		
		process.getInputStream().close();
		process.getOutputStream().close();
		process.getErrorStream().close();
		
		br.close();
		
		process.destroy();
	}

	static ArrayList<String> getModifiedFilesList(String gitFolder, String ext) throws Exception
	{
		String command = "git -C "+gitFolder+" status -uno *."+ext;
		
		Process process = Runtime.getRuntime().exec(command);
		
		//System.out.println("Command : <"+command+">");
		
		BufferedReader br = new BufferedReader( new InputStreamReader(process.getInputStream()) ) ;
		
		String line = null;
		
		ArrayList<String> list = new ArrayList<String>();
		String find  = "modified:";

		while( (line = br.readLine()) != null)
		{
			line = line.trim();

			if(line.startsWith(find))
			{
				String filePath = line.substring( line.indexOf(find) + find.length(), line.length() ).trim();
				list.add(filePath);
				//System.out.println("FilePath:<"+filePath+">");
			}	
		}
		
		process.getInputStream().close();
		process.getOutputStream().close();
		process.getErrorStream().close();
		
		br.close();
		
		process.destroy();
		
		return list;		
	}
	
	static ArrayList<String> getFileData(String filePath)
	{
		ArrayList<String> rows = new ArrayList<String>();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(filePath));
		
			String line = null;
		
			while( (line = br.readLine()) != null )
			{
				rows.add(line);
			}
		}
		catch(Exception e){e.printStackTrace();}
		
		return rows;
	}
		
	static void printList(ArrayList<String> list)
	{
		Iterator iter = list.iterator();
		
		while(iter.hasNext())
		{
			System.out.print(iter.next());
			
			if(iter.hasNext())
				System.out.print("\n");
		}
	}
	
	static int getMaxWidth(ArrayList<String> rows, FontMetrics fontMatrics)
	{
		int maxWidth = 0;
		
		Iterator<String> iter = rows.iterator();
		
		while(iter.hasNext())
		{
			String str = iter.next();
			
			if(str != null)
			{
				int width = fontMatrics.stringWidth( str );
			
				if(width > maxWidth)
					maxWidth = width;
			}
		}
		
		return maxWidth;
	}	

	public static void write(File file, String content) throws Exception
	{
		BufferedWriter bw = new BufferedWriter(new FileWriter(file) );

		bw.write(content, 0, content.length());
		bw.flush();
		bw.close();
	}

	public static boolean isUnix() 
	{
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	}

	public static boolean isWindows() 
	{
		//return true;
		String OS = System.getProperty("os.name").toLowerCase();
		return (OS.indexOf("win") >= 0);
	}

	public static GraphicsConfiguration getGraphicsConfiguration()
	{
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gs = ge.getDefaultScreenDevice();
        return gs.getDefaultConfiguration();
	}
}
