import java.util.*;
import java.io.*;

class SearchMaxOccuredString
{
	HashMap<String, Integer> hashMap = new HashMap<String, Integer>(10000000);
	
	public static void main(String[] args) throws Exception
	{
		new SearchMaxOccuredString();
	}
	
	SearchMaxOccuredString() throws Exception
	{
		BufferedReader br = new BufferedReader( new FileReader( "PIValue_100000.txt") );
	
		String sourceStr = br.readLine();
		
		br.close();
	
		long startTime = System.currentTimeMillis();
		
		MaxOccuredStr maxOccured = GetMaxOccurance(sourceStr, 5);
		
		long timeTaken = System.currentTimeMillis() - startTime;
				
		System.out.println(maxOccured);
		
		System.out.println("Time taken : "+timeTaken+" milli seconds.");
	}
	
	MaxOccuredStr GetMaxOccurance(String sourceStr, int searchStrLen) throws Exception
	{
		int sourceStrLen = sourceStr.length();
		
		int max = 1;
		String maxOccuredStr = sourceStr.substring(0, searchStrLen);
		
		hashMap.put(maxOccuredStr, 1);
		
		int loopUpto = sourceStrLen - searchStrLen;
				
		for(int i=1; i<loopUpto+1; i++)
		{
			String key = sourceStr.substring(i, i+searchStrLen);

			//System.out.println("key"+key);
						
			if(hashMap.containsKey(key))
			{
				int val = hashMap.get(key);
				
				if(val + 1 > max)
				{
					maxOccuredStr = key;
					max = val+1;
				}
				
				//if(val+1 == 2)
				//{
				//	System.out.println(key);
				//}
				
				hashMap.put(key, val+1);
			}
			else
			{
				hashMap.put(key, 1);	
			}
		}
		
		return new MaxOccuredStr(maxOccuredStr, max);
	}
}


class MaxOccuredStr
{
	String str;
	int numTimes;
	
	MaxOccuredStr(String str, int numTimes)
	{
		this.str = str;
		this.numTimes = numTimes;
	}
	
	public String toString()
	{
		return "{"+numTimes+" number of times "+str+" occured }";
	}
}