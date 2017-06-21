import java.io.*;

class SubStringMatch
{
	public static void main(String[] args) throws Exception
	{
		new SubStringMatch();
	}
	
	
	SubStringMatch() throws Exception
	{
		String str1 = "Hi this is manoj, working in konylabs and working thet company";
		String str2 = "aaking that compn indianking theiljldj";
		
		int str1Len = str1.length();
		int str2Len = str2.length();
		
		int maxCharsMatch = 0;
		int startIndex = 0;
		
		for(int i=0; i<str2Len; i++)
		{
			char ch = str2.charAt(i);
			
			int index = -1;
			int fromIndex = 0;
		
			do
			{
				index = str1.indexOf(ch, fromIndex);
				
				if(index != -1)
				{
					int matchCharsCount = numCharsMatch(str1, str2, index, i);
					
					if(matchCharsCount > maxCharsMatch)
					{
						maxCharsMatch = matchCharsCount;
						startIndex = i;
					}
					
					index++;
					fromIndex = index;
				}
				
			}while(index != -1 && index < str1.length());
		}
		
		String matchStr = str2.substring(startIndex, startIndex+maxCharsMatch);
		System.out.println("match string :"+matchStr);
	}
	 
	 
	int numCharsMatch(String str1, String str2, int str1Index, int str2Index)
	{
		int charsMatchCount = 0;
		
		for(; str1Index<str1.length() && str2Index<str2.length(); )
		{
			if(str1.charAt(str1Index) != str2.charAt(str2Index))
				break;
			
			charsMatchCount++;
			str1Index++;
			str2Index++;
		}
		
		return charsMatchCount;
	}
}
