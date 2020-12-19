package scroll;

import java.awt.Label;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class ScrollBar implements MouseListener, MouseMotionListener, MouseWheelListener
{
	private Label _bg = new Label();
	private Label _ptr = new Label();
    
    private Color _bgColor = Color.LIGHT_GRAY;
    private Color _ptrColor = new Color(96, 96, 164);

	private ScrollListener _scrollListener = null;
	
    private float PTR_MIN_LEN = 0.1f;
    private float PTR_MAX_LEN = 0.9f;
    
    private int _mouseClickPos = 0;
    
    public static final int HORIZONTAL = 1;
    public static final int VERTICAL = 2;
    
    private int _type = VERTICAL;
    
    
	public ScrollBar(int type, int x, int y, int w, int h)
	{
		_type = type;

		_bg.setBounds(x, y, w, h);
		_bg.setBackground(_bgColor);
		_bg.addMouseListener(this);
		_bg.addMouseMotionListener(this);
		_bg.addMouseWheelListener(this);
		
		if(_type == HORIZONTAL)
			_ptr.setBounds(x, y, (int)(w*PTR_MIN_LEN), h);
		else if(_type == VERTICAL)
			_ptr.setBounds(x, y, w, (int)(h*PTR_MIN_LEN));
		
		_ptr.setBackground(_ptrColor);
		_ptr.addMouseListener(this);
		_ptr.addMouseMotionListener(this);
	}
	
	public void addScrollListener(ScrollListener scrollListener)
	{
		_scrollListener = scrollListener;
	}
    
    public Label getBg()
    {
        return _bg;
    }
    
    public Label getPointer()
    {
        return _ptr;
    }

    private void borderCheck()
    {
    	if( _type == HORIZONTAL )
    	{
        	if(_ptr.getWidth() > _bg.getWidth())
            	_ptr.setSize(_bg.getWidth(), _ptr.getHeight());
        
	        if(_ptr.getX() < 0)
    	        _ptr.setLocation(0, _ptr.getY());
        
        	if(_ptr.getX()+_ptr.getWidth() > _bg.getX()+_bg.getWidth())
            	_ptr.setLocation(_bg.getX()+_bg.getWidth()-_ptr.getWidth(), _ptr.getY());
        }
        else if( _type == VERTICAL )
        {
        	if(_ptr.getHeight() > _bg.getHeight())
            	_ptr.setSize(_ptr.getWidth(), _bg.getHeight());
        
	        if(_ptr.getY() < 0)
    	        _ptr.setLocation(_ptr.getX(), 0);
        
        	if(_ptr.getY()+_ptr.getHeight() > _bg.getY()+_bg.getHeight())
            	_ptr.setLocation(_ptr.getX(), _bg.getY()+_bg.getHeight()-_ptr.getHeight());        	
        }
    }

	/*    
    public int getPointerXInPixels()
    {
    	return _ptr.getX();
    }

    public int getPointerYInPixels()
    {
    	return _ptr.getY();
    }
    */
        
    public void setPointerLen(float len)
    {
        if(len < PTR_MIN_LEN)       len = PTR_MIN_LEN;
        else if(len > PTR_MAX_LEN)	len = PTR_MAX_LEN;

		if(_type == HORIZONTAL)
		{
        	_ptr.setSize((int)(len * _bg.getWidth()), _ptr.getHeight());
        }
        else if(_type == VERTICAL)
        {
        	_ptr.setSize(_ptr.getWidth(), (int)(len * _bg.getHeight()));        
        }
        
        borderCheck();
    }
    
    public void setPointerPos(float pos)
    {
        if(pos < 0)       pos = 0;
        else if(pos > 1)  pos = 1;
        
		if(_type == HORIZONTAL)
		{
	        _ptr.setLocation((int)(_bg.getX() + pos * (_bg.getWidth()-_ptr.getWidth()) ), _ptr.getY());
        }
        else if(_type == VERTICAL)
        {
	        _ptr.setLocation(_ptr.getX(), (int)( _bg.getY() + pos * (_bg.getHeight()-_ptr.getHeight()) ));
        }
        
        borderCheck();
    }
    
	private void movePointer(java.awt.event.MouseEvent e)
	{
		float scrollMove = 0.0f;
		
		if(_type == HORIZONTAL)
		{
			int x = e.getX();
		
			if(e.getSource() == _ptr)
				x += _ptr.getX();
		
			_ptr.setLocation( x - _mouseClickPos, _ptr.getY() );
		
			borderCheck();
		
			int totMoveLen = _bg.getWidth() - _ptr.getWidth();
			int movedLen = _ptr.getX();

			scrollMove = (float) movedLen / (float) totMoveLen;		
		}
		else if(_type == VERTICAL)
		{
			int y = e.getY();
		
			if(e.getSource() == _ptr)
				y += _ptr.getY();
		
			_ptr.setLocation( _ptr.getX(), y - _mouseClickPos );
		
			borderCheck();
		
			int totMoveLen = _bg.getHeight() - _ptr.getHeight();
			int movedLen = _ptr.getY();

			scrollMove = (float) movedLen / (float) totMoveLen;
		}
		
		if(_scrollListener != null)
			_scrollListener.scrollBarMoved( new ScrollEvent(this, scrollMove) );		
	}

	public void mousePressed(java.awt.event.MouseEvent e)
	{
		if(_type == HORIZONTAL)
		{
			if(e.getSource() == _ptr)
			{
				_mouseClickPos = e.getX();
			}
			else if(e.getSource() == _bg)
			{
				_mouseClickPos = _ptr.getWidth()/2;
			}
		}
		else if(_type == VERTICAL)
		{
			if(e.getSource() == _ptr)
			{
				_mouseClickPos = e.getY();
			}
			else if(e.getSource() == _bg)
			{
				_mouseClickPos = _ptr.getHeight()/2;
			}		
		}
		
		movePointer(e);
	}

	public void mouseDragged(java.awt.event.MouseEvent e)
	{
		movePointer(e);
	}
	
	public void mouseReleased(java.awt.event.MouseEvent e){}
	public void mouseMoved(java.awt.event.MouseEvent me){}
	public void mouseClicked(java.awt.event.MouseEvent e){}	
	public void mouseEntered(java.awt.event.MouseEvent e){}
	public void mouseExited(java.awt.event.MouseEvent e){}	
	
	public void mouseWheelMoved(java.awt.event.MouseWheelEvent e)
	{
		int scrollType = e.getScrollType();
		int scrollAmount = e.getScrollAmount();
		int wheelRot = e.getWheelRotation();
		
		//System.out.println("scrollType : "+scrollType);
		//System.out.println("scrollAmount : "+scrollAmount);
		
		if(wheelRot < 0)
		{
			//System.out.println("*****  UP  *****");
		}
		else
		{
			//System.out.println("***** DOWN *****");
		}
	}
}
