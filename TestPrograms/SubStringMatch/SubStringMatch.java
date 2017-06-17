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

/*
1. Containers in c++
2. Design patterns in c++
3. Copy constructor in c++  http://www.geeksforgeeks.org/copy-constructor-in-cpp/
4. deep copy shallow copy in c++
5. What is class, object, encapsulation, inheritance, Runtime polymorphism.
6. Virtual keywords
7. Private virtual functions behavior in run time polymorphism
8. virtual destructors
9. dynamic casting and static casting and interprete casting
10. Data Structures
11. Multi Threading, Semaphores and Metaphores
12. Map, Vectors Usage and Advantages and dis-advantages
13. Sorted Map and UnSorted Map usage
14. Mutable array and unMutable array
15. Volatile and extern Keywords Usage
16. Smart pointers
17. Deletion of double dimension array
*/