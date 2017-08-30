import java.util.Arrays;

class Sort
{
	public static void merge(int[] a, int s, int m, int e, int[] r)
	{
		int i=s, j=m, k=s;
		
		while(true)
		{
			if(a[i] < a[j])		{ r[k++] = a[i++]; if(i >= m) break; }
			else				{ r[k++] = a[j++]; if(j >= e) break; }
		}
		
		if (i < m)		System.arraycopy(a, i, r, k, m - i);
		else if(j < e )	System.arraycopy(a, j, r, k, e - j);
	}
	
	
	public static void mergeSort(int[] a, int s, int e, int[] r)
	{		
		if(e-s > 1)
		{
			int m = (s+e)/2;
			
			mergeSort(a, s, m, r);
			mergeSort(a, m, e, r);
			merge(a, s, m, e, r);
			
			System.arraycopy(r, s, a, s, e-s);
		}
	}
	
	
	public static void QuickSort(int a[])
	{
		QuickSort(a, null, 0, a.length - 1);
	}
	
	public static void QuickSort(int a[], int b[], int lo0, int hi0)
    {
		int lo = lo0;
		int hi = hi0;
		int mid;

		if ( hi0 > lo0)
		{
			mid = a[ ( lo0 + hi0 ) / 2 ];

			while( lo <= hi )
			{
				while(lo < hi0 && a[lo] < mid)
					++lo;

				while(hi > lo0 && a[hi] > mid)
					--hi;

				if( lo <= hi ) 
				{
					int T = a[lo]; 
					a[lo] = a[hi];
					a[hi] = T;			
					
					// T = b[lo]; 
					// b[lo] = b[hi];
					// b[hi] = T;
		
					++lo;
					--hi;
				}
			}

			if( lo0 < hi )
				QuickSort( a, b, lo0, hi );

			if( lo < hi0 )
				QuickSort( a, b, lo, hi0 );
		}
    }
	
	
	public static int search(int[] a, int v)
	{
		int s = 0;
		int e = a.length - 1;
		
		if(v >= a[e] || v <= a[s])
		{
			if(v == a[e]) return e;
			if(v == a[s]) return s;
			return -1;
		}
		
		int m = 0;
		
		while(s <= e)
		{
			m = (s+e)/2;
			
			if(v < a[m])		e = m;
			else if(v > a[m])	s = m;
			else return m;
		}
	
		return -1;
	}
	
	
	public static int[] searchAll(int[] a, int[] indixArr, int v)
	{
		int foundAt = search(a, v);
		
		if(foundAt != -1)
		{
			int sPos = foundAt;
			int ePos = foundAt;
			int aLen = a.length;
			
			for(int i=foundAt-1; i>=0; i--)
			{
				if(v != a[i])
				{
					sPos = i+1;
					break;
				}
			}
			
			for(int i=foundAt+1; i<aLen; i++)
			{
				if(v != a[i])
				{
					ePos = i-1;
					break;
				}
			}
			
			int[] retArr = new int[ePos-sPos+1];
			
			for(int i=0; i<retArr.length; i++)
				retArr[i] = indixArr[sPos + i];
			
			return retArr;
		}
		
		return null;
	}	
}


// public static int search(int[] a, int v)
	// {
		// int s = 0;
		// int e = a.length - 1;
		// int m = (s+e)/2;
 
		// while (s <= e)
		// {
			// if (a[m] < v)			s = m + 1;
			// else if (a[m] == v)		return m+1;
			// else					e = m - 1;
 
			// m = (s + e)/2;
		// }
		
		// return -1;
	// }
	
	
	// public static void mergeSortMain(int[] a, int s, int e, int[] r)
	// {
		// if(e-s <= 2)
		// {
			// int len = e-s;
			
			// if(len == 1)
			// {
				// r[s] = a[s];
			// }
			// else if(len == 2)
			// {
				// if(a[s] < a[s+1])
				// {
					// r[s] = a[s];
					// r[s+1] = a[s+1];
				// }
				// else
				// {
					// r[s] = a[s+1];
					// r[s+1] = a[s];					
				// }
			// }
			
			// return;
		// }
		
		// mergeSort(a, s, e, r);
	// }
	
	
	// public static void mergeSort(int[] a, int s, int e, int[] r)
	// {
		// int aLen = e-s;
	
		// int maxTwoPow = 1;
		
		// while(maxTwoPow < aLen)
			// maxTwoPow <<= 1;
	
		// if(maxTwoPow > aLen)
			// maxTwoPow = (maxTwoPow >> 1);
		
		// int twoPow = 2;
		
		// while(twoPow <= maxTwoPow)
		// {
			// for(int i=s; i<e; )
			// {
				// int j = i+twoPow;
				// merge(a, i, (i+j)/2, j, r);
				// i = j;
			// }
		
			// copyCount++;
			
			// if(e-s < 1024)
				// System.arraycopy(r, s, a, s, maxTwoPow);
			// else
			// {
				// for(int i=s; i<e; i+=1024)
					// System.arraycopy(r, s, a, s, 1024);
			// }
			
			// twoPow *= 2;
		// }
	// }
	
	
			// int s = 0;
		// int e = a.length - 1;
		
		// if(v >= a[e] || v <= a[s])
		// {
			// if(v == a[e])
			// {
				// int[] retArr = new int[1];
				// retArr[0] = e;
				// return retArr;
			// }
			
			// if(v == a[s])
			// {
				// int[] retArr = new int[1];
				// retArr[0] = s;
				// return retArr;
			// }
			
			// return null;
		// }
		
		// int foundAt = -1;
		
		// while(e - s > 1)
		// {
			// int m = (s + e)/2;
			// int x = a[m];
			
			// if(v < x)			e = m;
			// else if(v > x)		s = m;
			// else if(v == x)
			// {
				// foundAt = m;
				// break;
			// }
		// }
		