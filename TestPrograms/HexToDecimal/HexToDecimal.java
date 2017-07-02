import java.io.*;

class HexToDecimal
{
	public static void main(String[] args) throws Exception
	{
		new HexToDecimal();
	}
	
	ExcelColumnNumber() throws Exception
	{
		long val = 0x2300E8FF;
		
		String str = "2300E8FF";
		
		int maxDigit = 16;
		
		int strLen = str.length();
		int sum = 0;
		
		for(int i=0; i<strLen; i++)
		{
			char ch = str.charAt(i);
			int val = GetNum(ch);
			int powVal = (int)(Math.pow(maxDigit, strLen-1-i));
			
			sum += val * powVal;
		}
		
		System.out.println("value \""+str+"\" = "+sum);
		
		System.out.println("hex value "+val);
		
	}
	
	int GetNum(char ch) throws Exception
	{
		int val = 0;
		
		if(ch >='0' && ch <= '9')
		{
			val = ch - '0';
		}
		else if(ch >='A' && ch <= 'F')
		{
			val = ch - 'A' + 10;
		}		
		else
		{
			throw new Exception(ch+" is not valid for input.");
		}
		
		return val;
	}
}
