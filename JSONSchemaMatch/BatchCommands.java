import org.json.JSONObject;
import org.json.JSONArray;

import utils.FileUtils;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Iterator;


class BatchCommands
{

	public static void main(String args[]) throws Exception
	{
		new BatchCommands();
	}

	BatchCommands() throws Exception
	{
		JSONObject jsonObj1 = new JSONObject(FileUtils.getFileDataAsStr("sample1.json"));
		JSONObject jsonObj2 = new JSONObject(FileUtils.getFileDataAsStr("sample2.json"));
		
		
		//System.out.println("\n\nJSONObj:"+jsonObj);
		//runAll = jsonObj.getBoolean("runAllTasks");
		//JSONArray taskArr = (JSONArray)jsonObj.get("tasks");
		
		
		//if(taskMap.has("command"))
		//{
		//	taskMap.getString("command"));
		//}
		//else if(taskMap.has("batchCommands"))
		//{
		//	executeBatchCommands(taskMap.getJSONArray("batchCommands"));
		//}
		
		//System.out.println("JSON1 "+jsonObj1);
		
		/*
		Integer obj = new Integer(10);
		Integer fl = new Integer(22);
		
		if( obj.getClass().equals(fl.getClass()) )
		{
            System.out.println("aaa");
		}
		*/
		
		boolean matching = isSchemaMatching(jsonObj1, jsonObj2);
		System.out.println("match : "+matching);
	}

	boolean isSchemaMatching(JSONObject map1, JSONObject map2) throws Exception
	{
        boolean result[] = {true};
        
        isSchemaMatching(map1, map2, result);
        return result[0];
	}
	
	void isSchemaMatching(JSONObject map1, JSONObject map2, boolean[] result) throws Exception
	{
        if(result[0] == false)
        {
            return;
        }
	
        if(map1.length() != map2.length())
        {
            result[0] = false;
            return;
        }

        Iterator map1Iter = map1.keys();
	
        while(map1Iter.hasNext())
        {
            String key = (String)map1Iter.next();
            Object val1 = map1.get(key);
            
            if(!map2.has(key) || !val1.getClass().equals(map2.get(key).getClass()))
            {
                result[0] = false;
                return;
            }
            
            if(val1 instanceof JSONObject)
            {
                isSchemaMatching((JSONObject)val1, (JSONObject)map2.get(key), result);
            }
            else if(val1 instanceof JSONArray)
            {
                isSchemaMatching((JSONArray)val1, (JSONArray)map2.get(key), result);
            }
        }
	}
	
	
    void isSchemaMatching(JSONArray arr1, JSONArray arr2, boolean[] result) throws Exception
    {
        if(result[0] == false)
        {
            return;
        }
    
        if(arr1.length() != arr2.length())
        {
            result[0] = false;
            return;                    
        }
        
        for(int i=0; i<arr1.length(); i++)
        {
            Object val1 = arr1.get(i);
            Object val2 = arr2.get(i);
            
            if(!val1.getClass().equals(val2.getClass()))
            {
                result[0] = false;
                return;
            }
            
            if(val1 instanceof JSONObject)
            {
                isSchemaMatching((JSONObject)val1, (JSONObject)val2, result);
            }
            else if(val2 instanceof JSONArray)
            {
                isSchemaMatching((JSONArray)val1, (JSONArray)val2, result);
            }
        }
    }

}









