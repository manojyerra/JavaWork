import java.awt.*;

public class MyLayout
{
	private Frame _frame;

	private int _initY = 50;
	private int _initX = 50;

	private int _currX = _initX;
	private int _currY = _initY;

	private int _gapX = 10;
	private int _gapY = 10;
	
	private int _width = 15;
	private int _height = 20;
	
	private int _letterWidth = 8;

	public MyLayout(Frame frame)
	{
		_frame = frame;
	}

	public void setInitPos(int initX, int initY)
	{
		_initX = initX;
		_initY = initY;
	}
	
	public void setLetterWidth(int letterWidth)
	{
		_letterWidth = letterWidth;
	}
	
	public Button addButton(String name)
	{
		int width = name.length()*_letterWidth;

		Button com = new Button(name);
		com.setBounds(_currX, _currY, width, _height);
		_frame.add(com);
		
		_currX += width + _gapX;
		//com.setBackground(Color.RED);
		return com;
	}

	public TextField addLabelAndTextField(String labelName)
	{
		if(labelName.equals("") == false)
			addLabel(labelName);
	
		TextField com = new TextField();
		com.setBounds(_currX, _currY, _width, _height);
		_frame.add(com);

		_currX += _width + _gapX;
		//com.setBackground(Color.RED);
		return com;
	}

	public TextField addLabelAndTextField(String labelName, int width)
	{
		if(labelName.equals("") == false)
			addLabel(labelName);

		TextField com = new TextField();
		com.setBounds(_currX, _currY, width, _height);
		_frame.add(com);

		_currX += width + _gapX;
		//com.setBackground(Color.RED);
		return com;
	}

	public Label addLabel(String name)
	{
		int width = name.length()*_letterWidth;

		Label com = new Label(name);
		com.setBounds(_currX, _currY, width, _height);
		_frame.add(com);

		_currX += width + _gapX;
		//com.setBackground(Color.RED);
		return com;
	}

	public Checkbox addCheckbox(String name)
	{
		int width = 10 + name.length()*_letterWidth;

		Checkbox com = new Checkbox(name);
		com.setBounds(_currX, _currY, width, _height);
		_frame.add(com);

		_currX += width + _gapX;
		//com.setBackground(Color.RED);
		return com;
	}
	
	public void AddRow()
	{
		_currX = _initX;
		_currY += _height+_gapY;
	}

	public void AddTabSpace()
	{
		_currX += 50;
	}
}
