package scroll;

import java.util.EventObject;


public class ScrollEvent extends EventObject
{
	private float _scrollPos = 0.0f;
	
	
	public ScrollEvent(Object source, float scrollPos)
	{
		super(source);
		_scrollPos = scrollPos;	
	}
	
	public float getScrollPos()
	{
		return _scrollPos;
	}
}
