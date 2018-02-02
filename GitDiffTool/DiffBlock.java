import java.util.ArrayList;


class DiffBlock
{
	private ArrayList<String> leftBlock = new ArrayList<String>();
	private ArrayList<String> rightBlock = new ArrayList<String>();
	
	static final int NO_CHANGE = 0;
	static final int ADDITION = 1;
	static final int DELETION = 2;
	static final int MODIFIED = 3;
	
	int type = NO_CHANGE;
	
	DiffBlock(ArrayList<String> diffData, int startIndex, int endIndex)
	{
		type = findType(diffData, startIndex, endIndex);
		
		//System.out.println("Type : "+type);
		
		if(type == NO_CHANGE)
		{
			for(int i=startIndex; i<endIndex; i++)
			{
				leftBlock.add(diffData.get(i));
				rightBlock.add(diffData.get(i));				
			}
		}
		else if(type == ADDITION)
		{
			for(int i=startIndex; i<endIndex; i++)
			{
				leftBlock.add(diffData.get(i));
				rightBlock.add(null);				
			}
		}
		else if(type == DELETION)
		{
			for(int i=startIndex; i<endIndex; i++)
			{
				leftBlock.add(null);
				rightBlock.add(diffData.get(i));				
			}
		}
		else if(type == MODIFIED)
		{
			for(int i=startIndex; i<endIndex; i++)
			{
				if(diffData.get(i).charAt(0) == '-')
					rightBlock.add(diffData.get(i));
				else if(diffData.get(i).charAt(0) == '+')
					leftBlock.add(diffData.get(i));					
			}
			
			if(rightBlock.size() > leftBlock.size())
			{
				int numEmptyLines = rightBlock.size() - leftBlock.size();
				
				for(int i=0; i<numEmptyLines; i++)
					leftBlock.add(null);
			}
			else if(leftBlock.size() > rightBlock.size())
			{
				int numEmptyLines = leftBlock.size() - rightBlock.size();
				
				for(int i=0; i<numEmptyLines; i++)
					rightBlock.add(null);
			}	
		}
	}
	
	private int findType(ArrayList<String> diffData, int startIndex, int endIndex)
	{
		if(diffData.get(startIndex).charAt(0) == ' ')
			return NO_CHANGE;
			
		boolean addition = false;
		boolean deletion = false;
		
		for(int i=startIndex; i<endIndex; i++)
		{
			if(diffData.get(i).charAt(0) == '+')
				addition = true;

			else if(diffData.get(i).charAt(0) == '-')
				deletion = true;
		}
		
		if(addition && !deletion)
			return ADDITION;

		if(!addition && deletion)
			return DELETION;

		return MODIFIED;
	}
	
	ArrayList<String> getLeftBlock()
	{
		return leftBlock;
	}
	
	ArrayList<String> getRightBlock()
	{
		return rightBlock;
	}	
	
	void print()
	{
		System.out.println("\nBlock : Begin ********************\n");

		for(int i=0; i<leftBlock.size(); i++)
			System.out.println(leftBlock.get(i));		

		System.out.println("\nBlock : End   ********************\n");
	}
	
	void add(String leftStr, String rightStr)
	{
		leftBlock.add(leftStr);
		rightBlock.add(rightStr);
	}
}