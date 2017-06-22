import javax.swing.*;
import java.awt.*;
import java.io.*;

class So
{
	public static void p(String str)
	{
		System.out.print(str);
	}

	public static void pln(String str)
	{
		System.out.println(str);
	}
}


public class UtilFuncs
{

	public static float GetPercent(float base, float newNum)
	{
		return (newNum * 100.0f / base);
	}

	public static boolean Is2Power(int value)
	{
		long power = 2;
		for(int i=1;i<=20;i++)
		{
			if(value == power)
				return true;
			power = 2*power;
		}
		return false;
	}

	public static int GetLessAndNearest2Power(int value)
	{
		if(value <=  0)
			return 0;
			
		int power = 2;
		for(int i=1;i<=20;i++)
		{
			if(value == power)
				return value;
			
			if(value < power)
			{
				return power/2;
			}
			power = 2*power;
		}
		return -1;
	}

	public static int GetNextAndNearest2Power(int value)
	{
		if(value <=  0)
			return 0;
			
		int power = 2;
		for(int i=1;i<=20;i++)
		{
			if(value == power)
				return value;
			
			if(value < power)
			{
				return power;
			}
			power = 2*power;
		}
		return -1;
	}
	
	public static int GetPositiveIntWODialog(TextField textField)
	{
		try 
		{
			return Integer.parseInt(textField.getText().trim());
			
		}catch(Exception e)
		{
			textField.setBackground(Color.RED);
			textField.setBackground(Color.WHITE);
		}
		
		return -1;
	}
	
	public static int GetPositiveInt(TextField textField, String errorMsg, Frame frame)
	{
		try 
		{
			int value = Integer.parseInt(textField.getText().trim());
			
			if(value < 0)
			{
				textField.setBackground(Color.RED);
				JOptionPane.showMessageDialog(frame, errorMsg+" Number should be positive integer.", "Inane error", JOptionPane.ERROR_MESSAGE);
				textField.setBackground(Color.WHITE);
			}
			else
				return value;
			
		}catch(Exception e)
		{
			textField.setBackground(Color.RED);
			JOptionPane.showMessageDialog(frame, errorMsg, "Inane error", JOptionPane.ERROR_MESSAGE);
			textField.setBackground(Color.WHITE);
		}
		return -1;
	}
	
	public static boolean IsValidFolderPath(TextField textField, Frame frame)
	{
		String folderPath = textField.getText().trim();

		if(folderPath.length() == 0)
		{
			textField.setBackground(Color.RED);
			JOptionPane.showMessageDialog(frame, "Folder Path is missing","Inane error", JOptionPane.ERROR_MESSAGE);
			textField.setBackground(Color.WHITE);
			return false;
		}
		else
		{
			File file = new File(folderPath);
			if(file.exists() == false || file.isDirectory() == false)
			{
				textField.setBackground(Color.RED);
				JOptionPane.showMessageDialog(frame, "Folder does not exist","Inane error", JOptionPane.ERROR_MESSAGE);
				textField.setBackground(Color.WHITE);
				return false;
			}
		}

		return true;
	}	
}