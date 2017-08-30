import java.util.Vector;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;


class BenchmarkTest
{
	LinkedArray<Integer> 	linkedArr 	= new LinkedArray<Integer>();
	ArrayList<Integer> 		arrayList 	= new ArrayList<Integer>(4096*10);
	Vector<Integer> 		vector 		= new Vector<Integer>(4096*10, 1024*10);
	LinkedList<Integer> 	linkedList 	= new LinkedList<Integer>();
	
	boolean testWithArrayList 	= true;
	boolean testWithVector 		= false;
	boolean testWithLinkedList 	= false;
	
	Random random = new Random();
	
	public static void main(String args[])
	{
		new BenchmarkTest();		
	}
		
	BenchmarkTest()
	{
		System.out.print("\n\nCreating map with random values..");
	
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		
		int numOperations = 500000;
		
		for(int i=1; i<=numOperations; i++)
		{
			int action = randomVal(8);
			int value = randomVal(i);
			map.put(value, action);
		}

		System.out.print(" : Done\n\n");
		
		
		System.out.println("Performing "+numOperations+" operations...\n");

		long startTime = System.currentTimeMillis();		
		TestLinkedArray.Perform(map, linkedArr);
		System.out.println("Time for LinkedArray : "+(float)(System.currentTimeMillis()-startTime)/1000.0f+" seconds");
		System.out.println("\nLinkedArray size : "+linkedArr.size()+"\n");

		if(testWithArrayList)
		{
			startTime = System.currentTimeMillis();
			TestArrayList.Perform(map, arrayList);
			System.out.println("Time for ArrayList   : "+(float)(System.currentTimeMillis()-startTime)/1000.0f+" seconds");
		}
		
		if(testWithVector)
		{
			startTime = System.currentTimeMillis();
			TestVector.Perform(map, vector);
			System.out.println("Time for Vector      : "+(float)(System.currentTimeMillis()-startTime)/1000.0f+" seconds");
		}
		
		if(testWithLinkedList)
		{
			startTime = System.currentTimeMillis();
			TestLinkedList.Perform(map, linkedList);
			System.out.println("Time for LinkedList  : "+(float)(System.currentTimeMillis()-startTime)/1000.0f+" seconds");
		}
		
		System.out.println("");
		
		if(testWithArrayList)	CompareWithArrayList();
		if(testWithVector)		CompareWithVector();
		if(testWithLinkedList)	CompareWithLinkedList();
		
		System.out.println();
	}
	
	
	void CompareWithArrayList()
	{
		int linkedArrSize = linkedArr.size();
		
		if(linkedArr.size() == arrayList.size())
		{
			boolean isSame = true;
			
			for(int i=0; i<linkedArrSize; i++)
				if(linkedArr.get(i).intValue() != arrayList.get(i).intValue())
				{
					isSame = false; break;
				}
			
			System.out.println("LinkedArray "+(isSame ? "=" : "!")+"= ArrayList");
		}
		else
			System.out.println("LinkedArray != ArrayList (ArrayList size = "+arrayList.size()+")");
	}
	
	
	void CompareWithVector()
	{
		int linkedArrSize = linkedArr.size();
		
		if(linkedArrSize == vector.size())
		{
			boolean isSame = true;
			
			for(int i=0; i<linkedArrSize; i++)
				if(linkedArr.get(i).intValue() != vector.get(i).intValue())
				{
					isSame = false; break;
				}
			
			System.out.println("LinkedArray "+(isSame ? "=" : "!")+"= Vector");
		}
		else
			System.out.println("LinkedArray != Vector (Vector size = "+vector.size()+")");		
	}
	
	
	void CompareWithLinkedList()
	{
		int linkedArrSize = linkedArr.size();
		
		if(linkedArrSize == linkedList.size())
		{
			boolean isSame = true;
			
			for(int i=0; i<linkedArrSize; i++)
				if(linkedArr.get(i).intValue() != linkedList.get(i).intValue())
				{
					isSame = false; break;
				}
			
			System.out.println("LinkedArray "+(isSame ? "=" : "!")+"= LinkedList");
		}
		else
			System.out.println("LinkedArray != LinkedList (LinkedList size = "+linkedList.size()+")");		
	}
	
	
	int randomVal(int limit)
	{
		return Math.abs(random.nextInt()) % limit;
	}
}


		// int size = linkedArr.size();
		// int capacity = linkedArr.capacity();
		// int optMemSize = linkedArr.optimizeForMemory();
		
		// System.out.println("\nsize:"+size);
		// System.out.println("capacity:"+capacity);
		

		// size = linkedArr.size();
		// capacity = linkedArr.capacity();

		// System.out.println("optMemSize:"+optMemSize);
		// System.out.println("size:"+size);
		// System.out.println("capacity:"+capacity);
		
		
 		// System.out.println("\nLinkedArray mainListSize: "+linkedArr.chunkVector.size()+"\n");
// // 		System.out.println("LinkedArray size = "+linkedArr.size()+", capacity = "+linkedArr.capacity());
// // 		linkedArr.trimToSize();
// // 		System.out.println("LinkedArray size = "+linkedArr.size()+", capacity = "+linkedArr.capacity());