import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Map;

import java.awt.Component;
import java.awt.Panel;
import java.awt.Label;
import java.awt.Color;
import java.awt.Point;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.RenderingHints;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import scroll.ScrollBar;
import scroll.ScrollListener;
import scroll.ScrollEvent;

import javax.swing.JLabel;


class ListGrid extends Panel implements ScrollListener, MouseWheelListener, MouseListener, KeyListener
{
	private ArrayList<RowUI> _labels = new ArrayList<RowUI>();
	private ArrayList<String> _rows;
	private int _topIndex = 0;
	
	private ScrollBar _vScrollBar = null;
	private ScrollBar _hScrollBar = null;
	
	private Font _font = null;
	private int _maxWidth = 0;
	
	private static final int SCROLL_BAR_W = 15;
	
	private ListGrid _linkedListGrid = null;
	
	private Color red   = new Color(220, 120, 120);
	private Color green = new Color(120, 220, 120);
	private Color gray  = new Color(150, 150, 150);
	
	private Color default_label_bg = new Color(32, 32, 32);
	private Color default_label_fg = new Color(200, 200, 200);
		
	ListGrid(int x, int y, int w, int h, int rowH, ArrayList<String> rows)
	{
		init(x, y, w, h, rowH, (ArrayList<String>)rows.clone());
	}


	ListGrid(int x, int y, int w, int h, int rowH, String filePath)
	{
		init(x, y, w, h, rowH, Util.getFileData(filePath));
	}
	

	private void init(int x, int y, int w, int h, int rowH, ArrayList<String> rows)
	{
		super.setBounds(x, y, w, h);
		super.setLayout(null);
		super.setBackground(Color.GRAY);
		
		_rows = rows;

		_font = new Font("Serif", Font.PLAIN, rowH-5);

		Label tempLabel = new Label();	
		FontMetrics fontMatrics = tempLabel.getFontMetrics(_font);
		
		_maxWidth = Util.getMaxWidth(rows, fontMatrics);
		
		if(_maxWidth < w-SCROLL_BAR_W)
			_maxWidth = w-SCROLL_BAR_W;

		createLabels(_maxWidth, h-SCROLL_BAR_W, rowH);
		setTextOnLabels(0);

		int verScrollBarX = getWidth()-SCROLL_BAR_W;
		int verScrollBarY = 0;
		int verScrollBarW = SCROLL_BAR_W;
		int verScrollBarH = h-SCROLL_BAR_W;
		
		_vScrollBar = new ScrollBar(ScrollBar.VERTICAL, verScrollBarX, verScrollBarY, verScrollBarW, verScrollBarH);
		_vScrollBar.setPointerLen( (float)_labels.size() / (float)_rows.size());
		_vScrollBar.addScrollListener(this);

		_hScrollBar = new ScrollBar(ScrollBar.HORIZONTAL, 0, getHeight()-SCROLL_BAR_W, getWidth()-SCROLL_BAR_W, SCROLL_BAR_W);
		_hScrollBar.setPointerLen( (float)(w-SCROLL_BAR_W) / (float)_maxWidth);
		_hScrollBar.addScrollListener(this);

        super.add( _vScrollBar.getPointer() );
        super.add( _vScrollBar.getBg() );

        for(int i=0; i<_labels.size(); i++)
            super.add(_labels.get(i));
			
        super.add( _hScrollBar.getPointer() );
        super.add( _hScrollBar.getBg() 		);
		//super.add(_changesColors);
    }


    ArrayList<String> getData()
    {
    	return _rows;
    }
	
	
	int getScrollBarWidth()
	{
		return SCROLL_BAR_W;
	}
	

	private void createLabels(int w, int h, int rowH)
	{
		int numLabels = (int)((float)(h) / (float)rowH);
				
		for(int i=0; i<numLabels; i++)
		{
			RowUI label = new RowUI();

			label.setBounds(0, i*rowH, w, rowH);
			label.setForeground(default_label_fg);
			label.setOpaque(true);
			label.setBackground(default_label_bg);			
			label.setFont(_font);
			label.addMouseWheelListener(this);
			label.addMouseListener(this);
			
			_labels.add( label );
		}	
	}

	
	void setLinkedListGrid(ListGrid listGrid)
	{
		_linkedListGrid = listGrid;
	}

	
	void updateTopIndex(int topIndex)
	{		
		if(topIndex >= 0)
		{
			_topIndex = topIndex;
			borderCheck();
			setTextOnLabels( _topIndex );
		}				
	}
	

	void updateHorMove(float horScrollMove)
	{
		if(_maxWidth > getWidth())
		{
			float scrollableWidth = (_maxWidth - (getWidth() - SCROLL_BAR_W));
			
			Iterator<RowUI> iter = _labels.iterator();
		
			int move = (int)(scrollableWidth * horScrollMove);
		
			if(move > scrollableWidth)
				move = (int)scrollableWidth;
		
			while(iter.hasNext())
			{
				RowUI label = iter.next();
				label.setLocation(-move, label.getY());
			}
		}	
	}


	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int notches = e.getWheelRotation();
		moveAndUpdateScroll(notches);
	}
	
	public void moveAndUpdateScroll(int notches)
	{
		move(notches);

		if(_linkedListGrid != null)
			_linkedListGrid.move( notches );


		if(_rows.size() > _labels.size())
		{
			float moveableRows = (float)(_rows.size() - _labels.size());
			
			float scrollAmount =  (float)_topIndex / (float)moveableRows;

			_vScrollBar.setPointerPos(scrollAmount);

			if(_linkedListGrid != null)
				_linkedListGrid._vScrollBar.setPointerPos(scrollAmount);
		}	
	}
	

	public void scrollBarMoved( ScrollEvent scrollEvent )	
	{
		if(scrollEvent.getSource() == _hScrollBar)
		{
			float horScrollMove = scrollEvent.getScrollPos();
			
			updateHorMove(horScrollMove);

			if(_linkedListGrid != null)
			{
				_linkedListGrid.updateHorMove(horScrollMove);
				_linkedListGrid._hScrollBar.setPointerPos(horScrollMove);
			}	
		}
		else if(scrollEvent.getSource() == _vScrollBar)
		{
			float verScrollMove = scrollEvent.getScrollPos();

			if(_linkedListGrid != null)
				_linkedListGrid._vScrollBar.setPointerPos(verScrollMove);
			
			if(_rows.size() > _labels.size())
			{
				float moveableRows = (float)(_rows.size() - _labels.size());
				
				int topIndex = (int)(verScrollMove * moveableRows);
				
				updateTopIndex( topIndex );
				
				if(_linkedListGrid != null)
					_linkedListGrid.updateTopIndex( topIndex );
			}
		}		
	}
	

	private void move(int moveRows)
	{
		_topIndex += moveRows;
	
		borderCheck();
		
		setTextOnLabels( _topIndex );
	}
	

	private void borderCheck()
	{
		if(_topIndex > _rows.size() - _labels.size())
		{
			_topIndex = _rows.size() - _labels.size();
		}
		
		if(_topIndex < 0 || _rows.size() <= _labels.size())					
		{
			_topIndex = 0;
		}
		else if(_topIndex >= _rows.size())
		{
			_topIndex = _rows.size() -1;
		}		
	}
	

	private void setTextOnLabels(int rowIndex)
	{
		if(rowIndex < 0)
			rowIndex = 0;
		
		int numRows = _rows.size();
		int numLabels = _labels.size();
		
		for(int i=0; i<numLabels; i++)
		{
			RowUI label = _labels.get(i);
			
			if(rowIndex + i < numRows)
			{
				String line = _rows.get(rowIndex + i);

				if(line == null)
					label.setText( "" );
				else
					label.setText( line.substring(1,line.length()) );
				
				if(line == null)
				{
					label.setBackground(gray);
				}
				else if(line.startsWith("-"))
				{
					label.setBackground(red);
					label.setForeground(Color.BLACK);
				}
				else if(line.startsWith("+"))
				{
					label.setBackground(green);
					label.setForeground(Color.BLACK);
				}
				else
				{
					label.setForeground(default_label_fg);
					label.setBackground(default_label_bg);
				}
			}
			else
			{
				label.setBackground(default_label_bg);
				label.setText("");
			}
		}
	}


	void undoChange(int rowIndex)
	{
		if(_linkedListGrid == null || rowIndex < 0 || rowIndex >= _rows.size())
			return;

		ArrayList<String> lRows = _rows;
		ArrayList<String> rRows = _linkedListGrid._rows;

		String lRow = lRows.get(rowIndex);
		String rRow = rRows.get(rowIndex);

		if(lRow != null && rRow != null && lRow.charAt(0) == '+' && rRow.charAt(0) == '-')
		{
			String line = " " + rRow.substring(1, rRow.length());
			lRows.set(rowIndex, line);
			rRows.set(rowIndex, line);
		}
		else if(lRow == null && rRow != null && rRow.charAt(0) == '-')
		{
			String line = " " + rRow.substring(1, rRow.length());
			lRows.set(rowIndex, line);
			rRows.set(rowIndex, line);
		}
		else if(lRow != null && rRow == null)
		{
			lRows.remove(rowIndex);
			rRows.remove(rowIndex);
		}
	}


	int canUndo(int rowIndex)
	{
		if(_linkedListGrid == null || rowIndex < 0 || rowIndex >= _rows.size())
			return 0;

		String lRow = _rows.get(rowIndex);
		String rRow = _linkedListGrid._rows.get(rowIndex);

		if(lRow != null && rRow != null && lRow.charAt(0) == '+' && rRow.charAt(0) == '-')
		{
			return 1;
		}
		else if(lRow == null && rRow != null && rRow.charAt(0) == '-')
		{
			return 1;
		}
		else if(lRow != null && rRow == null)
		{
			return 2;
		}

		return 0;		
	}


	public void mouseReleased(java.awt.event.MouseEvent e)
	{
		int rowIndex = -1;

		int numLabels = _labels.size();
		
		for(int i=0; i<numLabels; i++)
		{
			if(_labels.get(i) == e.getSource())
			{
				rowIndex = _topIndex + i;

				break;
			}
		}

		if(rowIndex >= 0 && rowIndex < _rows.size())
		{
			if(e.getButton() == MouseEvent.BUTTON2)
			{
				undoChange(rowIndex);
				
				setTextOnLabels(_topIndex);
				
				if(_linkedListGrid != null)
					_linkedListGrid.setTextOnLabels(_topIndex);
			}
			else if(e.getButton() == MouseEvent.BUTTON2)
			{
				undoChange(rowIndex);
				
				setTextOnLabels(_topIndex);
				
				if(_linkedListGrid != null)
					_linkedListGrid.setTextOnLabels(_topIndex);
			}
			else if(e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
			{
				int startIndex = rowIndex;

				if(canUndo(startIndex) != 0)
				{
					while(canUndo(startIndex) != 0)
						startIndex--;

					startIndex++;

					int retVal = 0;

					while( (retVal = canUndo(startIndex)) != 0)
					{
						undoChange(startIndex);

						if(retVal == 1)
							startIndex++;
					}

					setTextOnLabels(_topIndex);
					
					if(_linkedListGrid != null)
						_linkedListGrid.setTextOnLabels(_topIndex);
				}
			}
			else if(e.getButton() == MouseEvent.BUTTON3)
			{
				int middleIndex = _topIndex + _labels.size() / 2;
				int startIndex = middleIndex;

				while(canUndo(startIndex++) != 0);
				while(canUndo(startIndex++) == 0);
				
				int diff = startIndex - middleIndex;

				moveAndUpdateScroll(diff);
			}
		}
	}

	public void keyReleased(java.awt.event.KeyEvent e)
	{		
		int keyCode = e.getKeyCode();
	
		System.out.println("Key Released "+keyCode);

		if(keyCode == KeyEvent.VK_HOME)
		{
			moveAndUpdateScroll(-_topIndex-1);
		}
		else if(keyCode == KeyEvent.VK_END)
		{
			moveAndUpdateScroll(_rows.size()+1);
		}
	}
	
	public void keyTyped(java.awt.event.KeyEvent e){}
	public void keyPressed(java.awt.event.KeyEvent e){}

	public void mouseClicked(java.awt.event.MouseEvent e){}
	public void mousePressed(java.awt.event.MouseEvent e){}
	public void mouseEntered(java.awt.event.MouseEvent e){}
	public void mouseExited (java.awt.event.MouseEvent e){}
}


	    //     Label label = new Label() 
	    //     {
	    //         @Override
	    //         public void paint(Graphics g) 
	    //         {
	    //             super.paint(g);

					// Graphics2D gr = (Graphics2D) g;
					
					// if (props instanceof Map)
			  // 			gr.addRenderingHints ((Map) props);

					// gr.setRenderingHint (RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					// gr.setRenderingHint (RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	    //         }
	    //     };


	/*
	private Point getMousePos(MouseEvent e)
	{
		Component source=(Component)e.getSource();
		
		Point p = new Point(e.getX(), e.getY());
		
		if(source instanceof Label)
		{
			p.x += source.getX();
			p.y += source.getY();			
		}
			
		return p;
	}
	*/

		//export _JAVA_OPTIONS="-Dawt.useSystemAAFontSettings=on -Dswing.aatext=true -Dsun.java2d.xrender=true"
		//export _JAVA_OPTIONS="-Dawt.useSystemAAFontSettings=lcd -Dswing.aatext=true -Dsun.java2d.xrender=true -Dhidpi=true"

/*
export _JAVA_OPTIONS = "-Xms128m \
-Xmx750m \
-XX:MaxPermSize=350m \
-XX:ReservedCodeCacheSize=225m \
-XX:+UseConcMarkSweepGC \
-XX:SoftRefLRUPolicyMSPerMB=50 \
-ea \
-Dsun.io.useCanonCaches=false \
-Djava.net.preferIPv4Stack=true \
-Dawt.useSystemAAFontSettings=on \
-Dswing.aatext=true \
-Dswing.defaultlaf=com.sun.java.swing.plaf.gtk.GTKLookAndFeel"

*/

		//GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
		//Font[] allFonts = e.getAllFonts()

		//_font = new Font("Sans Mono", Font.PLAIN, rowH-5);
		//new Font(rowH-5);

		//_font = new Font("TimesRoman", Font.PLAIN, rowH-5);
		//_font = new Font("Serif", Font.PLAIN, rowH-5);
		//_font = new Font("Helvetica", Font.PLAIN, rowH-5);
		//_font = new Font("SansSerif", Font.PLAIN, rowH-5);
		//_font = new Font("Courier", Font.PLAIN, rowH-5);
		//_font = new Font("Monospaced", Font.PLAIN, rowH-5);
		
		
		// try
		// {
		// 	float fontH = (float)(rowH-5);
		// 	_font = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/InaiMathi.ttf")).deriveFont(fontH);
		// }
		// catch(Exception e)
		// {
		// 	e.printStackTrace();
		// }