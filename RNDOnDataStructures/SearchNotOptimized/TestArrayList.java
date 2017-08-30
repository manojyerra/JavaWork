import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


class TestArrayList
{
	static void Add(HashMap<Integer, Integer> map, ArrayList<Integer> obj)
	{		
		for(Map.Entry m: map.entrySet())
		{  
			int action = ((Integer)m.getValue()).intValue();
			
			Integer value = (Integer)m.getKey();			
			
			if(action == 0)
			{
				obj.add(value);
			}
			else if(action == 1)
			{			
				if(obj.size() > 0)				
					obj.remove(obj.size() - 1);
			}
			else if(action == 2)
			{				
				obj.add(0 , value);
			}
			else if(action == 3)
			{				
				if(obj.size() > 0)
					obj.remove(0);
			}
			else
			{
				obj.add(value);			
			}
		}
		
		for(Map.Entry m: map.entrySet())
		{  		
			int action = ((Integer)m.getValue()).intValue();
			
			Integer value = (Integer)m.getKey();

			int intValue = 0;
			int size = obj.size();
			
			if(size > 0)
			{
				intValue = value.intValue() % (int)size;
			}			
			
			if(action == 4)
			{
				if(size > 0)
					obj.get(intValue);
			}
			else if(action == 5)
			{
				if(size > 0)	
					obj.set(intValue, value);
			}
			else if(action == 6)
			{				
				if(size > 0)
					obj.remove(intValue);
			}
			else if(action == 7)
			{				
				obj.add(intValue, value);
			}
		}
	}
}