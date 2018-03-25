import java.io.File;
import java.util.ArrayList;

public class FolderStructureCreator
{
	public static ArrayList<String> create(File src, File dest)
	{
		ArrayList<String> list = new ArrayList<String>();
		
		createDirRecursive(src, dest, list);
		
		return list;
	}
	
	private static void createDirRecursive(File src, File dest, ArrayList<String> list)
	{
    	if(src.isDirectory())
		{
    		if(!dest.exists())
    		   dest.mkdir();

    		String files[] = src.list();

    		for (String file : files)
			{
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);

				createDirRecursive(srcFile,destFile, list);
    		}
		}
		else
		{
			list.add(src.getAbsolutePath());
		}
	}
}
