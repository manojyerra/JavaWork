import org.json.JSONObject;
import org.json.JSONArray;

import utils.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;


class BatchCommands
{
	boolean runAll = false;

	public static void main(String args[]) throws Exception
	{
		new BatchCommands();
	}

	BatchCommands() throws Exception
	{
		JSONObject jsonObj = new JSONObject(FileUtils.getFileDataAsStr("config.json"));

		//System.out.println("\n\nJSONObj:"+jsonObj);

		runAll = jsonObj.getBoolean("runAllTasks");

		JSONArray taskArr = (JSONArray)jsonObj.get("tasks");
		

		for(int i=0; i<taskArr.length(); i++)
		{
			executeTask((JSONObject)taskArr.get(i));
		}
	}


	void executeTask(JSONObject taskMap) throws Exception
	{
		if(!runAll && !taskMap.getBoolean("run")) {
			return;
		}

		System.out.println("\n\nExecuting task : "+taskMap.getString("taskName")+"\n");

		if(taskMap.has("command"))
		{
			executeCommand(taskMap.getString("command"));
		}
		else if(taskMap.has("batchCommands"))
		{
			executeBatchCommands(taskMap.getJSONArray("batchCommands"));
		}
	}


	void executeBatchCommands(JSONArray jsonArr) throws Exception
	{
		String fileContent = "";

		for(int i=0; i<jsonArr.length(); i++)
		{
			if(i!= 0)
			{
				fileContent += "\n";
			}

			fileContent += jsonArr.getString(i);
		}

		FileUtils.write(new File("script.sh"), fileContent);
		
		executeCommand("sh script.sh");
	}


	void executeCommand(String command) throws Exception
	{
		//System.out.println("\n\n	Running Command:"+command+"\n");

		String[] cmdTokens = command.split("\\s+");
		
		Process process = (new ProcessBuilder(cmdTokens)).start();

		BufferedReader br = new BufferedReader( new InputStreamReader(process.getInputStream()) ) ;
		
		String line = null;

		while( (line = br.readLine()) != null)
		{
			System.out.println(line);
		}

		process.getInputStream().close();
		process.getOutputStream().close();
		process.getErrorStream().close();
		
		br.close();
		
		process.waitFor();

		//System.out.println("Process exited with "+process.exitValue()+"\n\n");
	}
}









