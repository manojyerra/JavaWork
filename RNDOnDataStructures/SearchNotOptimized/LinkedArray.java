import java.util.Vector;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.NoSuchElementException;


class LinkedArray<T>
{
	private int CHUNK_LOWER_LIMIT;
	private int CHUNK_UPPER_LIMIT;
	private int CHUNK_INCREMENT;
	
	public ExpArray<ExpArray<T>> chunkVector = new ExpArray<ExpArray<T>>();
	private ExpArray<T> lastVec;
	private int size; 
	
	private int MAX_ARR_SIZE = 1024;
	private int arr[] = new int[MAX_ARR_SIZE];
	private int arrSize = 0;
	
	
	LinkedArray()
	{
		CHUNK_LOWER_LIMIT	= 1024*4;
		CHUNK_INCREMENT		= CHUNK_LOWER_LIMIT / 2;
		CHUNK_UPPER_LIMIT	= CHUNK_LOWER_LIMIT + CHUNK_INCREMENT;
	
		ExpArray<T> vec = new ExpArray<T>(CHUNK_LOWER_LIMIT, CHUNK_INCREMENT);
		chunkVector.add(vec);
		lastVec = vec;
		size = 0;
	}


	public synchronized T get(int index)
	{
		int[] retArr = GetChunkIndex(index);
		return chunkVector.get(retArr[0]).get(retArr[1]);
	}
	
	
	public synchronized void set(int index, T element)
	{		
		int[] retArr = GetChunkIndex(index);
		chunkVector.get(retArr[0]).set(retArr[1], element);
	}

	
	public synchronized void addLast(T ele) //push_back
	{
		if(lastVec.size() >= CHUNK_LOWER_LIMIT)
		{
			ExpArray<T> vec = new ExpArray<T>(CHUNK_LOWER_LIMIT, CHUNK_INCREMENT);
			vec.add(ele);
			
			chunkVector.add(vec);
			lastVec = vec;
			
			UpdateArrSizes();
		}
		else
		{
			lastVec.add(ele);
		}
		
		size++;
	}


	public synchronized void removeLast() //pop_back
	{		
		if(size == 0)
			throw new NoSuchElementException();

		int lastVecSize = lastVec.size();
		lastVec.setSize(lastVecSize-1);
		size--;
			
		if(lastVecSize-1 == 0)
		{
			if(chunkVector.size() > 1)
			{
				chunkVector.remove(chunkVector.size()-1);
				lastVec = chunkVector.lastElement();
				
				UpdateArrSizes();
			}
		}
	}


	public synchronized void addFirst(T element) //push_front
	{
		ExpArray<T> vec = chunkVector.get(0);
		vec.add(0, element);
		size++;
		
		if(vec.size() >= CHUNK_UPPER_LIMIT)
		{
			CreateChunkAt(1, vec);
			UpdateArrSizes();
		}
	}


	public synchronized void removeFirst() 			// pop_front
	{
		if(size == 0)
			throw new NoSuchElementException();
	
		ExpArray<T> vec = chunkVector.get(0);
		
		vec.remove(0);
		size--;
		
		if(vec.size() == 0)
		{
			if(chunkVector.size() > 1)
			{
				chunkVector.remove(0);
				UpdateArrSizes();
			}
		}
	}
	
	
	public synchronized void remove(int index)
	{
		int[] retArr = GetChunkIndex(index);
	
		ExpArray<T> vec = chunkVector.get(retArr[0]);
	
		vec.remove(retArr[1]);
		size--;
				
		if(vec.size() == 0)
		{
			if(chunkVector.size() > 1)
			{
				chunkVector.remove(retArr[0]);
				UpdateArrSizes();
			}
			else
			{
				SubtractOneToChunkSizes(retArr[0]);
			}
		}
		else
		{
			SubtractOneToChunkSizes(retArr[0]);
		}
	}


	public synchronized void add(int index, T element)		//insert
	{
		if(index < size)
		{
			int[] retArr = GetChunkIndex(index);
	
			ExpArray<T> vec = chunkVector.get(retArr[0]);
	
			vec.add(retArr[1], element);
			size++;
						
			if(vec.size() >= CHUNK_UPPER_LIMIT)
			{
				CreateChunkAt( retArr[0]+1, vec );
				UpdateArrSizes();
			}
			else
			{
				AddOneToChunkSizes(retArr[0]);
			}
		}
		else if(index == size)
		{
			addLast(element);
		}	
		else
		{
			throw new IndexOutOfBoundsException();
		}
	}


	public synchronized int size()
	{
		return size;
	}
	
	public synchronized boolean isEmpty()
	{
        return (size == 0);
    }


	public synchronized int capacity()
	{
		int capacity = 0;
		int chunkVecSize = chunkVector.size();
		
		for(int i=0; i<chunkVecSize; i++)
			capacity += chunkVector.get(i).capacity();
		
		return capacity;
	}
	
	
	public synchronized void trimToSize()
	{
		int chunkVecSize = chunkVector.size();
		
		for(int i=0; i<chunkVecSize; i++)
			chunkVector.get(i).trimToSize();
	}
	
	
	public synchronized void removeAllElements()
	{
		int chunkVecSize = chunkVector.size();
		
		for(int i=0; i<chunkVecSize; i++)
			chunkVector.get(i).clear();
		
		chunkVector.clear();
		
		ExpArray<T> vec = new ExpArray<T>(CHUNK_LOWER_LIMIT, CHUNK_INCREMENT);
		chunkVector.add(vec);
		lastVec = vec;
		size = 0;	
	}
	
	
	public synchronized void clear()
	{
		removeAllElements();
	}
	
	
    public synchronized boolean contains(Object o)
    {
		int chunkVecSize = chunkVector.size();
			
		for(int i=0; i<chunkVecSize; i++)
			if(chunkVector.get(i).contains(o))
				return true;
        
        return false;
    }

    
	public synchronized int indexOf(Object o)
	{        	
		int indexCount = 0;
		
		int chunkVecSize = chunkVector.size();
			
		for(int i=0; i<chunkVecSize; i++)
		{
			Vector<T> vec = chunkVector.get(i);
				
			int index = vec.indexOf(o);
			
			if(index != -1)
				return indexCount+index;
			else
				indexCount += vec.size();
		}
		
        return -1;
	}
    
    
    public synchronized int lastIndexOf(Object o)
    {
		int indexCount = 0;
			
		for(int i=chunkVector.size()-1; i>=0; i--)
		{
			Vector<T> vec = chunkVector.get(i);
				
			int index = vec.lastIndexOf(o);
			
			if(index != -1)
				return size - (indexCount+vec.size()-index);
			else
				indexCount += vec.size();
		}
		
        return -1;
    }
    
    
	public synchronized T firstElement()
	{
		if (size == 0)
			throw new NoSuchElementException();

        return chunkVector.get(0).firstElement();
    }

    
    public synchronized T lastElement()
    {
        if(size == 0)
            throw new NoSuchElementException();
        
        return lastVec.lastElement();
    }
    
    
    public synchronized String toString()
    {
    	return chunkVector.toString();
    }
    
    
    public synchronized int hashCode()
    {
        return chunkVector.hashCode();
    }
    
    
    public synchronized int optimizeForMemory()
    {
		int freedUpSize = 0;
		
		int chunkVecSize = chunkVector.size();
			
		for(int i=0; i<chunkVecSize; i++)
		{
			ExpArray<T> vec = chunkVector.get(i);
			
			int vecSize = vec.size();
			
			if(vecSize > 0 && vec.capacity() > vecSize*1.6)
			{
				freedUpSize += vec.capacity() - vecSize;
				
				vec.trimToSize();
			}
    	}
    	
    	return freedUpSize;
    }
    
	
	private void CreateChunkAt(int index, ExpArray<T> prevChunk)
	{
		ExpArray<T> newVec = new ExpArray<T>(CHUNK_LOWER_LIMIT, CHUNK_INCREMENT);						
		
		newVec.addAll( prevChunk.subList(CHUNK_LOWER_LIMIT, prevChunk.size()) );
		
		chunkVector.add(index, newVec);
								
		prevChunk.setSize(CHUNK_LOWER_LIMIT);
						
		lastVec = chunkVector.lastElement();		
	}
	
	
	private void AddOneToChunkSizes(int fromChunkIndex)
	{
		if(fromChunkIndex > 0)
		{
			for(int i=fromChunkIndex-1; i<arrSize; i++)
				arr[i]++;
		}	
	}


	private void SubtractOneToChunkSizes(int fromChunkIndex)
	{
		if(fromChunkIndex > 0)
		{
			for(int i=fromChunkIndex-1; i<arrSize; i++)
				arr[i]--;
		}	
	}
	
	
	private void UpdateArrSizes()
	{
		if(MAX_ARR_SIZE <= chunkVector.size())
		{
			MAX_ARR_SIZE *= 1.5;
			arr = new int[MAX_ARR_SIZE];
		}
	
		arrSize = 0;
		int sum = 0;
		
		Iterator<ExpArray<T>> iterator = chunkVector.iterator();
		iterator.next();
		
		while(iterator.hasNext())
			arr[arrSize++] = (sum += iterator.next().size());
		
		arrSize--;
	}


	private int[] GetChunkIndex(int eleIndex)
	{			
        if(eleIndex >= size)
            throw new ArrayIndexOutOfBoundsException(eleIndex);

		int retArr[] = new int[2];
		int firstChunkSize = chunkVector.get(0).size();
			
		if(eleIndex < firstChunkSize)
		{
			retArr[1] = eleIndex;
			return retArr;
		}
		else if(eleIndex >= (size - chunkVector.lastElement().size()))
		{
			retArr[0] = chunkVector.size()-1;
			retArr[1] = eleIndex - (size - chunkVector.lastElement().size());
			
			return retArr;
		}
		
		if(arrSize == 0)
			throw new IndexOutOfBoundsException("Exception at GetChunkIndex : (arrSize == 0)");
		
		eleIndex -= firstChunkSize;

		if(eleIndex < arr[0])
		{
			retArr[0] = 1;
			retArr[1] = eleIndex;

			return retArr;
		}
		
		int startI = 0;
		int endI = arrSize-1;
		int index = (startI + endI) / 2;
		
		do
		{
			if(eleIndex >= arr[startI]  && eleIndex < arr[index])
				endI = index;
			else
				startI = index;			
			
			index = ( startI + endI ) / 2;
		}
		while(endI - startI > 1);
		
		
		retArr[0] = endI + 1;
		retArr[1] = eleIndex - arr[endI-1];

		return retArr;
	}
}

