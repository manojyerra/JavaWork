package utils;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
	
public class UIUtils
{
	public static void wrapLabelText(JLabel label, String text)
	{
		FontMetrics fm = label.getFontMetrics(label.getFont());
		int textWidth = fm.stringWidth(text);
		int labelW = label.getWidth();
		
		if(labelW > textWidth)
		{
			label.setText(text);
		}
		else
		{
			int lineStartI = 0;
			int wordStartI = 0;
			
			StringBuffer sb = new StringBuffer("<html>");
			
			int txtLen = text.length();
			
			for(int i=0; i<txtLen; i++)
			{
				char ch = text.charAt(i);
				
				if(ch == ' ' || i == txtLen-1)
				{
					String subStr = text.substring(lineStartI, i+1);
					int subStrWidth = fm.stringWidth(subStr);
					
					if(subStrWidth > labelW)
					{
						//System.out.println("subString:"+subStrWidth);
						sb.append(text.substring(lineStartI, wordStartI));
						sb.append("<br/>");
						i = wordStartI;
						lineStartI = wordStartI;
					}
					else
					{
						if(i == txtLen-1)
						{
							sb.append(text.substring(lineStartI, txtLen));
						}
						else
						{
							wordStartI = i;
						}
					}
				}
			}
			
			sb.append("</html>");
			label.setText(sb.toString());
			//System.out.println("*** Label width is less than text width. ***");
		}
	}
	
	/*
	public static void wrapLabelText(JLabel label, String text)
	{
		FontMetrics fm = label.getFontMetrics(label.getFont());
		
		PlainDocument doc = new PlainDocument();
		Segment segment = new Segment();
		try {
			doc.insertString(0, text, null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		StringBuffer sb = new StringBuffer("<html>");
		int noOfLine = 0;
		for (int i = 0; i < text.length();) {
			try {
				doc.getText(i, text.length() - i, segment);
			} catch (BadLocationException e) {
				//throw new Error("Can't get line text");
				e.printStackTrace();
				return;
			}
			
			//int breakpoint = Utilities.getBreakLocation(segment, fm, 0, this.width - pointerSignWidth - insets.left - insets.right, null, 0);
			
			int breakpoint = Utilities.getBreakLocation(segment, fm, 0, label.getWidth(), null, 0);
			
			sb.append(text.substring(i, i + breakpoint));
			sb.append("<br/>");
			i += breakpoint;

			noOfLine++;
		}
		sb.append("</html>");
		label.setText(sb.toString());

		//labelHeight = noOfLine * fm.getHeight();
		//setSize();
	}
	*/
}