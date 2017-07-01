import java.io.*;

class ExcelColumnNumber
{
	public static void main(String[] args) throws Exception
	{
		new ExcelColumnNumber();
	}
	
	ExcelColumnNumber() throws Exception
	{
		String str = "ABC";
		
		int maxDigit = 26;
		
		int strLen = str.length();
		int sum = 0;
		
		for(int i=0; i<strLen; i++)
		{
			char ch = str.charAt(i);
			int val = GetNum(ch);
			int powVal = (int)(Math.pow(maxDigit, strLen-1-i));
			
			sum += val * powVal;
		}
		
		System.out.println("Excel column number of \""+str+"\" = "+sum);
	}
	
	int GetNum(char ch) throws Exception
	{
		int val = 0;
		
		if(ch >='A' && ch <= 'Z')
		{
			val = ch - 'A' + 1;
		}
		else
		{
			throw new Exception(ch+" is not valid for input.");
		}
		
		return val;
	}
}
