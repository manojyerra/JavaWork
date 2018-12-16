package lib_utils;

import lib_json.*;

import java.util.*;

public class Utils_JSON
{
	public static boolean SetKeyValue(JSONObject map, String key, Object value)
	{
		String keyArr[] = key.split("\\.");

		for(int i=0;i<keyArr.length-1;i++)
		{
			Object obj = map.get(keyArr[i]);
			if(obj instanceof JSONObject)
				map = (JSONObject)obj;
			else
				return false;
		}
		
		map.put(keyArr[keyArr.length-1], value);
		return true;
	}
	
	public static Hashtable Compare(JSONObject base, JSONObject curr)
	{
		Hashtable table = new Hashtable();
		
		JSONStrToHashTable baseTable = new JSONStrToHashTable(base);
		JSONStrToHashTable currTable = new JSONStrToHashTable(curr);
		
		Vector<String> currKeys = currTable.GetAllKeys();
		Vector<Object> currValues = currTable.GetAllValues();
		
		for(int i=0;i<currKeys.size();i++)
		{
			String currKey = currKeys.get(i);
			Object currVal = currValues.get(i);
			
			Object baseVal = baseTable.GetValue(currKey);
			
			if(baseVal == null)
			{
				//System.out.println("\n"+currKey+" key not found in base");
				
				table.put(currKey, currVal);
			}
			else if(baseVal.toString().equals(currVal.toString()) == false)
			{
				//System.out.println("\nCurr : "+currKey+" : "+currVal);
				//System.out.println("Base : "+currKey+" : "+baseVal);
				
				table.put(currKey, currVal);
			}
		}
		
		return table;
	}
	
	public static String GetFormattedJSONStr(String jsonStr)
	{
		return (new JSONFormatter(jsonStr)).GetFormattedString();
	}

	public static Vector<String> GetFormattedJSONStrVector(String jsonStr)
	{
		return (new JSONFormatter(jsonStr)).GetFormattedStringVector();
	}
}

class JSONFormatter
{
	Vector<String> strVec = new Vector<String>();
	int numTabs = 0;
	String currLine = "";
	
	JSONFormatter(String str)
	{
		int ascii = 0;
		boolean arrayOn = false;
		int len = str.length();
		
		for(int i=0;i<len;i++)
		{
			char ch = str.charAt(i);
			
			if(ch == '\n')
				continue;
			
			if(ch == '[')		arrayOn = true;
			else if(ch == ']')	arrayOn = false;
			
			if(ch == ',')
			{
				WriteChar(ch);
				
				if(arrayOn == false)
				{
					NewLine();
					WriteTabs(numTabs);
				}
			}
			else if(ch == '{' || ch == '}')
			{
				NewLine();
				
				if(ch == '{')
				{
					WriteTabs(numTabs);					
					WriteChar(ch);
					
					NewLine();
					TabsCalc(ch);
					WriteTabs(numTabs);
				}
				else
				{
					WriteTabs(numTabs-1);
					WriteChar(ch);
					
					TabsCalc(ch);
				}					
			}
			else
			{
				WriteChar(ch);
			}
		}
		
		NewLine();
	}

	Vector<String> GetFormattedStringVector()
	{
		return strVec;
	}
	
	String GetFormattedString()
	{
		String altStr = "";
		
		for(int i=0;i<strVec.size();i++)
			altStr += strVec.get(i)+"\n";
		
		return altStr;
	}
	
	void WriteChar(char ch)
	{
		if(ch == ' ')
			if(currLine.charAt(currLine.length()-1) == '\t')
				return;
		
		currLine += ch;
	}

	void WriteTabs(int numTabs)
	{
		for(int i=0;i<numTabs;i++)
			currLine += '\t';
	}
	
	void NewLine()
	{
		if(currLine.trim().length() > 0)
			strVec.add(currLine);

		currLine = "";
	}

	void TabsCalc(char ch)
	{
		if(ch == '{')	numTabs++;
		if(ch == '}')	numTabs--;
		
		if(numTabs < 0)
			numTabs = 0;
	}
}