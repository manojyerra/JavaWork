import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;

class PathsTest
{
	public static void main(String args[]) throws Exception
	{
		Path folder = Paths.get("/home/manoj/all/work/master/prpc-platform");
		
		long size = Files.walk(folder)
					.filter(p -> p.toFile().isFile())
					.mapToLong(p -> p.toFile().length())
					.sum();
					
		System.out.println("Size : "+size / (1024 * 1024));
	}	
}