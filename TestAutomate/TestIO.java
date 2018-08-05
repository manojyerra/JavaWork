import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

class TestIO
{
	public static void main(String args[]) throws IOException
	{
		new TestIO(args[0]);
	}

	TestIO(String projPath) throws IOException
	{
		startDockerDB("manoj");
		startDestorySyncDB(projPath);

		System.out.println("Going to exit...");
	}

	boolean startDestorySyncDB(String projPath) throws IOException
	{
		ProcessBuilder builder = new ProcessBuilder(projPath+"/destroy-and-sync-db");

		Process proc = builder.start();

		BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(proc.getOutputStream()));

		setOutput(bw, "y");

		String line = "";
		boolean status = false;

		while( (line = br.readLine()) != null)
		{
			System.out.println(line);

			if(line.startsWith("BUILD SUCCESSFUL in"))
			{
				status = true;
			}
		}

		br.close();
		bw.close();

		proc.destroy();

		System.out.println("destroy-and-sync-db status : "+status);

		return status;
	}

	void setOutput(BufferedWriter bw, String outputStr) throws IOException
	{
		outputStr += "\n";
		bw.write(outputStr, 0, outputStr.length());
		bw.flush();
	}

	boolean startDockerDB(String userName) throws IOException
	{
		String dbName = userName+"-database";

		ProcessBuilder builder = new ProcessBuilder("docker", "start", dbName);
		Process proc = builder.start();

		BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		String line = "";
		boolean status = false;

		while( (line = br.readLine()) != null)
		{
			System.out.println(line);
			
			if(line.equals(dbName))
			{
				System.out.println("docker stated db successfully.");
				status = true;
			}
		}

		br.close();
		proc.destroy();

		System.out.println("docker start status : "+status);

		return status;
	}
}


		//pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
		//pb.redirectError(ProcessBuilder.Redirect.INHERIT);

		//builder.redirectErrorStream(true);
