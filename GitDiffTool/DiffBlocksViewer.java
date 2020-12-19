import java.io.*;

import java.util.ArrayList;
import java.util.Iterator;

import java.awt.Frame;
import java.awt.Window;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.Insets;
import java.awt.Graphics;

import javax.swing.JFrame;


public class DiffBlocksViewer extends Panel implements RevertChangeListener
{
	ArrayList<DiffBlock> _diffBlocks = null;
	
	ListGrid _tableLeft = null;
	ListGrid _tableRight = null;
	
	ArrayList<String> _leftTableData = new ArrayList<String>();
	ArrayList<String> _rightTableData = new ArrayList<String>();
	
	private DiffColor _diffColor = new DiffColor();
	private static final int ROW_HEIGHT = 22;
	
	
	DiffBlocksViewer(ArrayList diffData, int x, int y, int w, int h)
	{
		setBounds(x, y, w, h);
		setLayout(null);
		setBackground(Color.WHITE);

		_diffBlocks = Util.getDiffBlocks(diffData);
		
		retriveTablesData( _diffBlocks );
		
		_tableLeft 	= new ListGrid(w/2,	0, w/2, h, ROW_HEIGHT, _leftTableData, this);
		
		int scrollBarW = _tableLeft.getScrollBarWidth();
		
		_tableRight = new ListGrid(0, 0, w/2 - scrollBarW, h, ROW_HEIGHT, _rightTableData, null);
		
		_tableLeft.setLinkedListGrid( _tableRight );
		_tableRight.setLinkedListGrid( _tableLeft );
		
		int diffX = _tableLeft.getX() - _tableRight.getScrollBarWidth();
		int diffY = _tableLeft.getY();
		int diffW = _tableLeft.getScrollBarWidth();
		int diffH = _tableLeft.getHeight();
		
		_diffColor.setBounds(diffX, diffY, diffW, diffH);
		_diffColor.repaint();
		
		add(_tableLeft);
		add(_tableRight);
		add(_diffColor);
		
		_tableLeft.UpdateDiffData();
	}

	
	void retriveTablesData(ArrayList<DiffBlock> diffBlocks)
	{
		for(int i=0; i<_diffBlocks.size(); i++)
		{
			DiffBlock block = _diffBlocks.get(i);
			
			_leftTableData.addAll(block.getLeftBlock());
			_rightTableData.addAll(block.getRightBlock());
		}
	}
	
	
	void print()
	{
		for(int i=0; i<_diffBlocks.size(); i++)
			_diffBlocks.get(i).print();
	}
	

	ArrayList<String> getLeftBlockData()
	{
		return _tableLeft.getData();
	}
	

	ArrayList<String> getRightBlockData()
	{
		return _tableRight.getData();
	}
	
	
	public void changeReverted(ArrayList<Integer> rowNumbersList, int totRows)
	{
		_diffColor.paintDiffColors(rowNumbersList, totRows);
	}
}
