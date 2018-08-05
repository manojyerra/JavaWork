import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class RunTest
{
	public static void main(String args[]) throws IOException
	{
		new RunTest();
	}

	RunTest() throws IOException
	{
		System.out.println("Enter a number2");
		sleep(3000);

		System.out.println("AAA");
		String inputStr = (new BufferedReader(new InputStreamReader(System.in))).readLine();

		System.out.println("inputStr:"+inputStr);
	}

	void sleep(long timeInMillis)
	{
		try{Thread.sleep(timeInMillis);}catch(Exception e){}
	}
}