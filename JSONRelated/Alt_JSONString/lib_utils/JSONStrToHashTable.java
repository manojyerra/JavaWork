package lib_utils;

import lib_json.*;

import java.util.*;
import java.io.*;

class JSONStrToHashTable
{
	Vector<String> keyVec = new Vector<String>();
	Vector<Object> valueVec = new Vector<Object>();
	Vector<Integer> parentIndexVec = new Vector<Integer>();
	
	Hashtable hashTable = new Hashtable();
	
	JSONStrToHashTable(File file, boolean sortByKeys)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file.getPath()));
			String jsonStr = "";
			
			String line = "";
			while((line = br.readLine()) != null)
				jsonStr += line;
				
			ConvertJSONToHashTable(jsonStr, sortByKeys);
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	JSONStrToHashTable(String jsonStr)
	{
		ConvertJSONToHashTable(jsonStr, true);
	}
	
	JSONStrToHashTable(JSONObject jsonObj)
	{
		ConvertJSONToHashTable(jsonObj, true);
	}	

	private void ConvertJSONToHashTable(String jsonStr, boolean sortByKeys)
	{
		ConvertJSONToHashTable(new JSONObject(jsonStr), true);
	}

	private void ConvertJSONToHashTable(JSONObject jsonObj, boolean sortByKeys)
	{
		keyVec.add("");
		valueVec.add(jsonObj);
		parentIndexVec.add(-1);
		
		for(int i=0;i<keyVec.size();i++)
		{
			Object value = valueVec.get(i);
			
			if(value instanceof JSONObject)
				AddKeyValues((JSONObject)value, i);
		}
		
		Vector<String> finalKeyVec = new Vector<String>();
		Vector<Object> finalValueVec = new Vector<Object>();
		
		for(int i=0;i<keyVec.size();i++)
		{
			Object value = valueVec.get(i);
			
			if( (value instanceof JSONObject) == false)
			{
				String completeKey = keyVec.get(i);
				int count = 0;
				int parentIndex = parentIndexVec.get(i);

				while(parentIndex > 0)
				{
					completeKey = keyVec.get(parentIndex) +"."+ completeKey;
					parentIndex = parentIndexVec.get(parentIndex);
				}
				
				finalKeyVec.add(completeKey);
				finalValueVec.add(value);
				
				hashTable.put(completeKey, value);
			}
		}
		
		if(sortByKeys)
			SortByKeys(finalKeyVec, finalValueVec);
		
		keyVec = finalKeyVec;
		valueVec = finalValueVec;	
	}
	
	void AddKeyValues(JSONObject map, int index)
	{
		String[] keys = JSONObject.getNames(map);
		
		for(int i=0;i<keys.length;i++)
		{
			keyVec.add(keys[i]);
			valueVec.add( map.get(keys[i]) );
			parentIndexVec.add(index);
		}
	}
	
	void SortByKeys(Vector<String> finalKeyVec, Vector<Object> finalValueVec)
	{
		int size = finalKeyVec.size();
		
		for(int j=0; j<size;j++)
		{
			for(int i=j+1 ; i<size; i++)
			{
				if(finalKeyVec.get(i).compareTo(finalKeyVec.get(j))<0)
				{
					String tempStr = finalKeyVec.get(j);
					finalKeyVec.set(j, finalKeyVec.get(i));
					finalKeyVec.set(i, tempStr);
					
					Object tempObj = finalValueVec.get(j);
					finalValueVec.set(j, finalValueVec.get(i));
					finalValueVec.set(i, tempObj);					
				}
			}
		}	
	}
	
	Vector<String> GetAllKeys()	{ return keyVec; }
	Vector<Object> GetAllValues() { return valueVec; }
	
	String GetKey(int index) { return keyVec.get(index); }
	Object GetValue(int index) { return valueVec.get(index); }
	
	Object GetValue(String key) { return hashTable.get(key); }
	

	void PrintAllKeysAndValues()
	{
		Vector<String> keys = GetAllKeys();
		Vector<Object> values = GetAllValues();
		
		String lastParentKey = "";
		
		for(int i=0;i<keys.size();i++)
		{
			String key = keys.get(i);
			
			String currParentKey = "";
			
			if(key.indexOf(".") != -1)
				currParentKey = key.substring(0,key.indexOf("."));
			
			if(currParentKey.equals(lastParentKey) == false)
				System.out.println();
				
			System.out.println(key+" : "+values.get(i));
			
			lastParentKey = currParentKey;
		}
	}
}