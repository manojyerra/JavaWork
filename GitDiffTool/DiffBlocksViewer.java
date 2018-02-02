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

import javax.swing.JFrame;


class DiffBlocksViewer extends Panel
{
	ArrayList<DiffBlock> _diffBlocks = null;
	
	ListGrid _tableLeft = null;
	ListGrid _tableRight = null;
	
	ArrayList<String> _leftTableData = new ArrayList<String>();
	ArrayList<String> _rightTableData = new ArrayList<String>();
	
	
	DiffBlocksViewer(ArrayList diffData, int x, int y, int w, int h)
	{
		setBounds(x, y, w, h);
		setLayout(null);
		setBackground(Color.WHITE);

		_diffBlocks = Util.getDiffBlocks(diffData);
		
		retriveTablesData( _diffBlocks );
		
		_tableLeft 	= new ListGrid(0, 	0, w/2, h, 23, _leftTableData);
		_tableRight = new ListGrid(w/2, 0, w/2, h, 23, _rightTableData);
		
		_tableLeft.setLinkedListGrid( _tableRight );
		_tableRight.setLinkedListGrid( _tableLeft );
		
		add(_tableLeft);
		add(_tableRight);
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
}
