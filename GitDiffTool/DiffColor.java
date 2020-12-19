import java.awt.Graphics;
import java.awt.Label;
import java.util.ArrayList;
import java.awt.Color;

public class DiffColor extends Label
{
	private Graphics _g = null;
	private ArrayList<Integer> _yList = new ArrayList<Integer>();
	private Color darkGreen = new Color(0,128,0);
	private int _rectH = 5;
	
    public void paint(Graphics g)
	{
		_g = g;
				
		_g.setColor(Color.WHITE);
		_g.fillRect(0, 0, getWidth(), getHeight());
		
		_g.setColor(darkGreen);
		
		for(int i=0; i<_yList.size(); i++)
		{
			DrawLineAt(_yList.get(i));
		}
    }	
	
	void DrawLineAt(int y)
	{
		if(_g != null)
		{
			_g.fillRect(0, y, getWidth(), _rectH);
		}
	}
	
	void paintDiffColors(ArrayList<Integer> rowNumbersList, int totRows)
	{
		_yList.clear();
		int lableH = getHeight();
		
		for(int i=0; i<rowNumbersList.size(); i++)
		{
			int height = (int)((float)rowNumbersList.get(i) * (float)lableH / (float)totRows);
			_yList.add(height);
		}
		
		_rectH = (int)((float)lableH / (float)totRows) + 1;
		
		repaint();
	}
}
