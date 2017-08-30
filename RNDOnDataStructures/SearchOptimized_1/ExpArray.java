import java.util.Vector;
import java.util.Collection;
import java.util.Arrays;
import java.lang.CloneNotSupportedException;
import java.util.ConcurrentModificationException;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.Objects;
import java.util.Comparator;
import java.util.HashMap;

public class ExpArray<E > extends Vector<E>
{	
	int[] hashInt;
	
    public ExpArray(int initialCapacity, int capacityIncrement)
	{
        super(initialCapacity, capacityIncrement);
		hashInt = new int[initialCapacity];
    }

    public ExpArray(int initialCapacity)
	{
        this(initialCapacity, 0);
    }

    public ExpArray()
	{
        this(10);
    }

    public ExpArray(Collection<? extends E> c)
	{
		super(c);
		
		hashInt = new int[elementCount];
		
		for(int i=0; i<elementCount; i++)
			hashInt[i] = elementData[i].hashCode();		
    }
	
	public synchronized Object[] getElementData()
	{
		return elementData;
	}

    public synchronized void trimToSize()
	{
		modCount++;
        int oldCapacity = elementData.length;
		
        if (elementCount < oldCapacity)
		{
            elementData = Arrays.copyOf(elementData, elementCount);
			hashInt = Arrays.copyOf(hashInt, elementCount);
		}
    }
 
    public synchronized void ensureCapacity(int minCapacity)
	{
        if (minCapacity > 0)
		{
			modCount++;
            ensureCapacityHelper(minCapacity);
        }
    }

    private void ensureCapacityHelper(int minCapacity)
	{
        // overflow-conscious code
        if (minCapacity - elementData.length > 0)
            grow(minCapacity);
    }

	private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    private void grow(int minCapacity)
	{
        // overflow-conscious code
        int oldCapacity = elementData.length;
        int newCapacity = oldCapacity + ((capacityIncrement > 0) ? capacityIncrement : oldCapacity);
		
        if (newCapacity - minCapacity < 0)
            newCapacity = minCapacity;
		
        if (newCapacity - MAX_ARRAY_SIZE > 0)
            newCapacity = hugeCapacity(minCapacity);
		
        elementData = Arrays.copyOf(elementData, newCapacity);
		hashInt = Arrays.copyOf(hashInt, newCapacity);
    }

    private static int hugeCapacity(int minCapacity)
	{
        if (minCapacity < 0) // overflow
            throw new OutOfMemoryError();
			
        return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
    }

    public synchronized void setSize(int newSize)
	{
		modCount++;
		
        if (newSize > elementCount)
		{
            ensureCapacityHelper(newSize);
        } 
		else
		{
            for (int i = newSize ; i < elementCount ; i++)
			{
                elementData[i] = null;
				hashInt[i] = 0;
			}
		}
		
        elementCount = newSize;
    }

    public boolean contains(Object o)
	{
        return indexOf(o, 0) >= 0;
    }

    public int indexOf(Object o)
	{
        return indexOf(o, 0);
    }

    public synchronized int indexOf(Object o, int index) 
	{
        if (o == null)
		{
            for (int i = index ; i < elementCount ; i++)
                if (elementData[i]==null)
                    return i;
        }
		else
		{
			int oHashInt = o.hashCode();
			
            for (int i=index ; i<elementCount; i++)
				if(oHashInt == hashInt[i])
					if (o.equals(elementData[i]))
						return i;
        }
        return -1;
    }

    public synchronized int lastIndexOf(Object o)
	{
        return lastIndexOf(o, elementCount-1);
    }

    public synchronized int lastIndexOf(Object o, int index)
	{
        if (index >= elementCount)
            throw new IndexOutOfBoundsException(index + " >= "+ elementCount);

        if (o == null)
		{
            for (int i = index; i >= 0; i--)
                if (elementData[i]==null)
                    return i;
        }
		else 
		{
			int oHashInt = o.hashCode();
			
            for (int i = index; i >= 0; i--)
			{
				if(oHashInt == hashInt[i])
					if (o.equals(elementData[i]))
						return i;
			}
        }
        return -1;
    }	
	
    public synchronized void setElementAt(E obj, int index)
	{
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index + " >= " +elementCount);
		
        elementData[index] = obj;
		hashInt[index] = obj.hashCode();
	}

    public synchronized void removeElementAt(int index)
	{    
		modCount++;
		
        if (index >= elementCount)
		{
            throw new ArrayIndexOutOfBoundsException(index + " >= " +elementCount);
        }
        else if (index < 0)
		{
            throw new ArrayIndexOutOfBoundsException(index);
        }
		
        int j = elementCount - index - 1;
		
        if (j > 0)
		{
			System.arraycopy(elementData, index + 1, elementData, index, j);
			System.arraycopy(hashInt,     index + 1, hashInt,     index, j);
		}
		
        elementCount--;
        elementData[elementCount] = null; /* to let gc do its work */
		hashInt[elementCount] = 0;
    }

    public synchronized void insertElementAt(E obj, int index)
	{
		modCount++;
		
        if (index > elementCount)
            throw new ArrayIndexOutOfBoundsException(index+" > "+elementCount);
		
        ensureCapacityHelper(elementCount + 1);
        
		System.arraycopy(elementData, index, elementData, index + 1, elementCount - index);
		System.arraycopy(hashInt,     index, hashInt,     index + 1, elementCount - index);
		
        elementData[index] = obj;
		hashInt[index]  = obj.hashCode();
		
        elementCount++;
    }

    public synchronized void addElement(E obj)
	{
		modCount++;
        ensureCapacityHelper(elementCount + 1);
		
        elementData[elementCount] = obj;
		hashInt[elementCount] = obj.hashCode();
		
		elementCount++;
    }

    public synchronized boolean removeElement(Object obj)
	{
		modCount++;
        int i = indexOf(obj);
        if (i >= 0) {
            removeElementAt(i);
            return true;
        }
        return false;
    }

    public synchronized void removeAllElements()
	{
		 modCount++;
		 
        for (int i = 0; i < elementCount; i++)
		{
            elementData[i] = null;
			hashInt[i] = 0;
		}
		
        elementCount = 0;
    }

    public synchronized E set(int index, E element)
	{
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

		@SuppressWarnings("unchecked")
        E oldValue =(E) elementData[index];
		
        elementData[index] = element;
		hashInt[index]  = element.hashCode();
		
        return oldValue;
    }

    public synchronized boolean add(E e)
	{
        modCount++;
        ensureCapacityHelper(elementCount + 1);
		
        elementData[elementCount] = e;
		hashInt[elementCount]  = e.hashCode();
		
		elementCount++;
		
        return true;
    }

    public boolean remove(Object o)
	{
        return removeElement(o);
    }

    public void add(int index, E element)
	{
        insertElementAt(element, index);
    }

    public synchronized E remove(int index)
	{
		modCount++;
        if (index >= elementCount)
            throw new ArrayIndexOutOfBoundsException(index);
		
		@SuppressWarnings("unchecked")
        E oldValue = (E)elementData[index];
		
        int numMoved = elementCount - index - 1;
        if (numMoved > 0)
		{
            System.arraycopy(elementData, index+1, elementData, index, numMoved);
			System.arraycopy(hashInt,     index+1, hashInt,     index, numMoved);
		}
		
		--elementCount;
        elementData[elementCount] = null;
		hashInt[elementCount] = 0;
		
        return oldValue;
    }

    public void clear()
	{
        removeAllElements();
    }

    public synchronized boolean addAll(Collection<? extends E> c)
	{
		modCount++;
        Object[] a = c.toArray();
		
        int numNew = a.length;
        ensureCapacityHelper(elementCount + numNew);
        System.arraycopy(a, 0, elementData, elementCount, numNew);
		
		int elementOldCount = elementCount;
        elementCount += numNew;

		for(int i=elementOldCount; i<elementCount; i++)
			hashInt[i]  = elementData[i].hashCode();
		
        return numNew != 0;
    }

    public synchronized boolean removeAll(Collection<?> c)
	{
        return super.removeAll(c);
    }

    public synchronized boolean retainAll(Collection<?> c)
	{
        return super.retainAll(c);
    }

    public synchronized boolean addAll(int index, Collection<? extends E> c)
	{
		modCount++;
		
        if (index < 0 || index > elementCount)
            throw new ArrayIndexOutOfBoundsException(index);

        Object[] a = c.toArray();

        int numNew = a.length;
				
        ensureCapacityHelper(elementCount + numNew);

        int numMoved = elementCount - index;
		
        if (numMoved > 0)
			System.arraycopy(elementData, index, elementData, index + numNew, numMoved);
		
        System.arraycopy(a, 0, elementData, index, numNew);
		
		for(int i=index; i<numNew; i++)
			hashInt[i] = elementData[i].hashCode();		
		
        elementCount += numNew;
		
        return numNew != 0;
    }
}


	// private synchronized void arraycopy(Object src, int srcIndex, Object des, int desIndex, int len)
	// {
		// int limit = 2048;
		
		// if(len <= limit)
		// {
			// System.arraycopy(src, srcIndex, des, desIndex, len);
		// }
		// else
		// {
			// for(int i=srcIndex,j=desIndex; ;i+=limit,j+=limit)
			// {
				// System.arraycopy(src, i, des, j, limit);
				// len -= limit;
				
				// if(len <= limit)
				// {
					// i+=limit;
					// j+=limit;
					
					// System.arraycopy(src, i, des, j, len);
					// break;
				// }
			// }
		// }		
	// }