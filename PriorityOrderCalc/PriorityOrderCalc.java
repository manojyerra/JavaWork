import json.*;
import utils.*;
import java.util.*;

class Task
{
	String taskName;
	double priority;
	double taskTimeInHours;
	boolean isLongTermTask;
	double deadLineInHours;
	
	Task(JSONObject map)
	{
		taskName = map.getString("taskName");
		priority = map.getDouble("priority");
		taskTimeInHours = map.getDouble("taskTimeInHours");
		isLongTermTask = map.getBoolean("isLongTermTask");
		deadLineInHours = map.getDouble("deadLineInHours");
	}
	
	double calcPriorityByDeadLine()
	{
		if(deadLineInHours > 0)
		{
			double deadLineWOTaskTime = deadLineInHours - taskTimeInHours;
			
			if(deadLineInHours <= 0)
				return Double.MAX_VALUE;
			
			double coff = 24.0 * 10.0 / deadLineWOTaskTime;
			return priority * coff;
		}
		
		return priority;
	}
	
	double priorityAfterDeadLineAndTerm()
	{
		double prior = calcPriorityByDeadLine();
		return isLongTermTask ? prior / 2.0 : prior;
	}
}

class SortByTask implements Comparator<Task>
{
	public int compare(Task t1, Task t2) 
    {
		double t1Priority = t1.priorityAfterDeadLineAndTerm();
		double t2Priority = t2.priorityAfterDeadLineAndTerm();
	
		//double t1Priority = t1.calcPriorityByDeadLine();
		//double t2Priority = t2.calcPriorityByDeadLine();
		
		//t1Priority = t1.isLongTermTask ? t1Priority / 2.0 : t1Priority;
		//t2Priority = t2.isLongTermTask ? t2Priority / 2.0 : t2Priority;
		
		if(t1Priority > t2Priority)
		{
			double coff = t1Priority * 3.0 / t2Priority;			
			return (t2.taskTimeInHours * coff < t1.taskTimeInHours) ? 1 : -1;
		}
		else	
		{
			double coff = t2Priority * 3.0 / t1Priority;			
			return (t1.taskTimeInHours * coff < t2.taskTimeInHours) ? -1 : 1;			
		}
    } 	
}

class PriorityOrderCalc
{
	public static void main(String args[]) throws Exception 
	{
		new PriorityOrderCalc();
	}
	
	PriorityOrderCalc() throws Exception
	{
		String filePath = "tasks.json";
		
		JSONArray arr = new JSONArray(FileUtils.getFileDataAsStr(filePath));
		
		List<Task> list = new ArrayList<Task>();
		
		for(int i=0; i<arr.length(); i++)
		{
			JSONObject map = (JSONObject)arr.get(i);
			list.add(new Task(map));
		}
		
		Collections.sort(list, new SortByTask());
		
		System.out.println("\nTasks priority order :\n ");
		
        for (int i=0; i<list.size(); i++) 
		{
            System.out.println((i+1)+") "+list.get(i).taskName);
		}
	}
}

// System.out.println("taskName:"+map.get("taskName"));
// System.out.println("priority:"+map.get("priority"));
// System.out.println("taskTimeInHours:"+map.get("taskTimeInHours"));
// System.out.println("isLongTermTask:"+map.get("isLongTermTask"));
// System.out.println("deadLineInHours:"+map.get("deadLineInHours"));
