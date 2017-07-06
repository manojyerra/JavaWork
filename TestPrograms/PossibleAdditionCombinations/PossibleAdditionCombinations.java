import java.io.*;
import java.util.*;

class PossibleAdditionCombinations
{	
	Hashtable<String, Object> hashTable = new Hashtable<String, Object>();
	
	Object tempObject = new Object();
	
	public static void main(String args[]) throws Exception
	{
		new PossibleAdditionCombinations();
	}
	
	PossibleAdditionCombinations() throws Exception
	{
		System.out.println();

		for(int i=5; i<=5; i++)
		{
			GetNumCombi(i);
		}
	}
	
	int GetNumCombi(int n)
	{	
		hashTable.clear();
		
		int L[] = new int[n];
		
		for(int i=0; i<n; i++)
		{
			L[i] = 0;
		}
		
		int arr[] = new int[n];
		
		for(int i=0; i<n; i++)
		{
			arr[i] = i+1;
		}
		
		int loopNum = 0;
	
		while(true)
		{
			if(L[0] < n)
			{
				int sum = 0;
				boolean printCombi = false;

				for(int i=n-1; i>=0; i--)
				{
					sum += arr[ L[i] ];
					
					if(sum == n)
					{
						printCombi = true;
						break;
					}
				}
				
				if(printCombi)
				{
					sum = 0;
					String key = "";
					
					for(int i=n-1; i>=0; i--)
					{
						sum += arr[ L[i] ];
						key += ""+arr[ L[i] ];
						
						if(sum == n)
						{
							hashTable.put(key, tempObject);
							break;
						}
					}
				}
				
				L[0]++;
			}
			else
			{		
				boolean end = false;
						
				for(int i=0; i<n; i++)
				{
					if(L[n-1] >= n)
					{
						end = true;
						break;
					}
					
					if(L[i] >= n)
					{
						L[i] = 0;
						L[i+1]++;
					}
					else
					{
						break;
					}
				}
				
				if(end)
					break;
			}
		}
		
		Object[] keys = hashTable.keySet().toArray();

		System.out.println("Total Combinations for "+n+" is : "+(keys.length)+"\n");
	
		for(int i=0; i<keys.length; i++)
		{
			System.out.println((String)keys[i]);
		}	

		return keys.length;
	}
}


	// double RamanujanFormula(double n)
	// {
		// double denom = Math.sqrt( n - (1.0/24.0) );
		
		// double pi1 = 1.0 / ( 2 * Math.PI * Math.sqrt(2) );
		
		// double pi2 = ( 2 * Math.PI ) / Math.sqrt(6);
		
		// double finalVal = pi1 * (1.0 / n ) * Math.exp( pi2 * denom ) / denom;
		
		// return finalVal;
	// }
	
	// double ApproximationFormula(double n)
	// {
		// double val1 = 1.0 / ( 4 * n * Math.sqrt(3) );
		// double val2 = Math.PI * Math.sqrt(2 * n / 3);
		
		// double finalVal = val1 * Math.exp(val2);
		
		// return finalVal;
	// }