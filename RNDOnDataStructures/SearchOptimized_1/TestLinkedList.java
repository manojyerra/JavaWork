import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;


class TestLinkedList
{
	static void Perform(HashMap<Integer, Integer> map, LinkedList<Integer> obj)
	{		
		for(Map.Entry m: map.entrySet())
		{  
			int action = ((Integer)m.getValue()).intValue();
			
			Integer value = (Integer)m.getKey();			
			
			if(action == 0)
			{
				obj.addLast(value);
			}
			else if(action == 1)
			{			
				if(obj.size() > 0)				
					obj.removeLast();
			}
			else if(action == 2)
			{				
				obj.addFirst(value);
			}
			else if(action == 3)
			{				
				if(obj.size() > 0)
					obj.removeFirst();
			}
 			else
			{
				obj.addLast(value);			
			}
		}

		int searchCount = 0;
		long searchIndexCount = 0;
		
		long startTime = System.currentTimeMillis();
		
		for(Map.Entry m: map.entrySet())
		{  		
			int action = ((Integer)m.getValue()).intValue();
			
			Integer value = (Integer)m.getKey();

			int intValue = 0;
			int size = obj.size();
			
			if(size > 0)
				intValue = value.intValue() % (int)size;
			
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
			else  if(action == 6)
			{				
				if(size > 0)
				{
					//obj.remove(intValue);
					searchIndexCount += obj.indexOf(value);
					searchCount++;					
				}
			}
			else if(action == 7)
			{				
				obj.add(intValue, value);
			}
		}
		
		System.out.println("\nLinkedList, Num Searches = "+searchCount+" Time = "+(System.currentTimeMillis()-startTime)+" SIC: "+searchIndexCount);
	}
}