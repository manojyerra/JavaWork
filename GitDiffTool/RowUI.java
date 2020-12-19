import javax.swing.JLabel;
import java.awt.Label;
import java.awt.Button;
import java.awt.Color;


class RowUI extends Label
{
	RowUI()
	{
		//setOpaque(true);
	}

	@Override
	public void setText(String text)
	{
		text = text.replace("\t", "    ");
		super.setText(text);
	}


	// @Override
	// public void setBackground(Color color)
	// {
	// 	System.out.println(color);

	// 	super.setBackground(color);
	// }
}