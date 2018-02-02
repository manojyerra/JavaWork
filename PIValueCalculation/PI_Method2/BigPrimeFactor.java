import java.math.*;
import java.io.*;
import java.util.*;

class BigPrimeFactor
{
	HashMap<BigDecimal, Integer> map = new HashMap<BigDecimal, Integer>();
	
	BigPrimeFactor()
	{
		map.put(new BigDecimal(1), 1);
	}
	
	BigPrimeFactor(String str)
	{
		Init( new BigDecimal(str) );
	}
	
	BigPrimeFactor(BigDecimal n)
	{
		Init( n );
	}
	
	// BigPrimeFactor(long n)
	// {
		// Iterator<BigDecimal> iterator = PrimList.vec.iterator();
		// boolean run = true;
	
		// while(iterator.hasNext())
		// {
			// BigDecimal key = iterator.next();
			// long keyLong = key.longValue();
			
			// while(keyLong < n)
			// {
				// if( n % keyLong == 0)
				// {
					// if(map.containsKey(key))
					// {
						// map.put(key, map.get(key) + 1);
					// }
					// else
					// {
						// map.put(key, 1);
					// }
					
					// n = n.divide(key);
				// }
				// else
				// {
					// break;
				// }
			// }
			
			// if( n.compareTo(BigDecimal.ONE) == 0)
			// {
				// break;
			// }	
		// }
		
		// if( n.compareTo(BigDecimal.ONE) != 0)
		// {
			// //System.out.println(n);
			
			// map.put(n.add(BigDecimal.ZERO), 1);
		// }
		// else if(map.size() == 0)
		// {			
			// map.put(BigDecimal.ONE, 1);
		// }		
	// }
	
	void Init(BigDecimal n)
	{
		n = new BigDecimal("123456789545477956595656232");
		
		BigDecimal min = new BigDecimal(100);
		boolean loopAgain = false;
		
		do
		{
			loopAgain = false;
			
			Iterator<BigDecimal> iterator = PrimList.vec.iterator();
			boolean run = true;
	
			while(iterator.hasNext())
			{
				BigDecimal key = iterator.next();
				
				while(key.compareTo(n) <= 0)
				{
					//if( n.remainder(key).compareTo(BigDecimal.ZERO) == 0)
					if( isMultipleOf(n, key) )
					{
						if(map.containsKey(key))
						{
							map.put(key, map.get(key) + 1);
						}
						else
						{
							map.put(key, 1);
						}
						
						n = n.divide(key);
					}
					else
					{
						break;
					}
				}
				
				if( n.compareTo(BigDecimal.ONE) == 0)
				{					
					break;
				}
			}
		
			if(n.compareTo(min) > 0)
			{
				// BigDecimal two = new BigDecimal(2);
				// n = n.divide(two, 0, BigDecimal.ROUND_DOWN);
				
				BigDecimal[] remin = n.divideAndRemainder(BigDecimal.TEN);
				
				BigDecimal finalVal = remin[1].divide ( remin[0], n.precision() + 5, BigDecimal.ROUND_HALF_UP );
				finalVal = finalVal.round( new MathContext(10) );
		
				n = remin[0];
				
				if(map.containsKey(finalVal))
				{
					map.put(finalVal, map.get(finalVal) + 1);
				}
				else
				{
					map.put(finalVal, 1);
				}
						
				loopAgain = true;
			}
			
		}while(loopAgain);
		
		
		if( n.compareTo(BigDecimal.ONE) != 0)
		{
			//System.out.println(n);
			
			map.put(n.add(BigDecimal.ZERO), 1);
		}
		else if(map.size() == 0)
		{			
			map.put(BigDecimal.ONE, 1);
		}
		
		System.out.println(this);
	}
	
	public String toString()
	{
		return map.toString();
	}
	
	void put(BigDecimal key, int value)
	{
		map.put(key, value);
	}
	
	boolean containsKey(BigDecimal key)
	{
		return map.containsKey(key);
	}
	
	int getValue(BigDecimal key)
	{
		return map.get(key);
	}
	
	Iterator<BigDecimal> getIterator()
	{
		return map.keySet().iterator();
	}
	
	BigPrimeFactor multiply(BigPrimeFactor bigPrime)
	{
		BigPrimeFactor result = new BigPrimeFactor();
		
		Iterator<BigDecimal> iterator = getIterator();
	
		while(iterator.hasNext())
		{
			BigDecimal key = iterator.next();
			
			if(bigPrime.containsKey(key))
			{
				int value = getValue(key) + bigPrime.getValue(key);
				
				//System.out.println("key1 : "+key+" : value : "+value);

				result.put( key, value );
			}
			else
			{
				//System.out.println("key11 : "+key+" : value : "+getValue(key));
				result.put( key, getValue(key) );				
			}
		}
		
		iterator = bigPrime.getIterator();
	
		while(iterator.hasNext())
		{
			BigDecimal key = iterator.next();
			
			if(!result.containsKey(key))
			{
				//System.out.println("key22 : "+key+" : value : "+bigPrime.getValue(key));
				result.put( key, bigPrime.getValue(key) );				
			}
		}
		
		return result;
	}
	
	BigPrimeFactor add(BigPrimeFactor bigPrime)
	{
		BigPrimeFactor result = new BigPrimeFactor();
		BigPrimeFactor reminder1 = new BigPrimeFactor();
		BigPrimeFactor reminder2 = new BigPrimeFactor();
		
		Iterator<BigDecimal> iterator = getIterator();
	
		while(iterator.hasNext())
		{
			BigDecimal key = iterator.next();
			
			if(bigPrime.containsKey(key))
			{
				if(getValue(key) > bigPrime.getValue(key))
				{
					result.put( key, bigPrime.getValue(key) );
					
					int value = getValue(key) - bigPrime.getValue(key);
					
					reminder1.put(key, value);
				}
				else
				{
					result.put( key, getValue(key) );
					
					int value = getValue(key) - getValue(key);
					
					if(value != 0)
						reminder2.put( key, value );
				}
			}
			else
			{
				//System.out.println("key11 : "+key+" : value : "+getValue(key));
				reminder1.put( key, getValue(key) );			
			}
		}
		
		iterator = bigPrime.getIterator();
	
		while(iterator.hasNext())
		{
			BigDecimal key = iterator.next();
			
			if(!containsKey(key))
			{
				//System.out.println("key22 : "+key+" : value : "+bigPrime.getValue(key));
				reminder2.put( key, bigPrime.getValue(key) );
			}
		}
		
		//System.out.println("reminder1 : "+reminder1+" : reminder2 : "+reminder2);
		//System.out.println("result : "+result);
		
		BigDecimal reminder = reminder1.GetBigDecimal().add(reminder2.GetBigDecimal());
		
		BigPrimeFactor remnderFactor=new BigPrimeFactor(reminder);
		
		return result.multiply(remnderFactor);
	}
	
	// BigPrimeFactor subtract(BigPrimeFactor bigPrime)
	// {
		// BigPrimeFactor result = new BigPrimeFactor();
		// BigPrimeFactor reminder1 = new BigPrimeFactor();
		// BigPrimeFactor reminder2 = new BigPrimeFactor();
		
		// Iterator<String> iterator = getIterator();
	
		// while(iterator.hasNext())
		// {
			// String key = iterator.next();
			
			// if(bigPrime.containsKey(key))
			// {
				// BigDecimal value = new BigDecimal("0");
				
				// if(getValue(key).longValue() > bigPrime.getValue(key).longValue())
				// {
					// result.put( key, bigPrime.getValue(key) );
					
					// value = getValue(key).subtract(bigPrime.getValue(key));
					// reminder1.put(key, value);
					
				// }
				// else
				// {
					// result.put( key, getValue(key) );
					
					// value = bigPrime.getValue(key).subtract(getValue(key));
					// if(value.longValue() != 0)
						// reminder2.put( key, value );
				// }
			// }
			// else
			// {
				// //System.out.println("key11 : "+key+" : value : "+getValue(key));
				// reminder1.put( key, getValue(key) );			
			// }
		// }
		
		
		// iterator = bigPrime.getIterator();
	
		// while(iterator.hasNext())
		// {
			// String key = iterator.next();
			
			// if(!containsKey(key))
			// {
				// //System.out.println("key22 : "+key+" : value : "+bigPrime.getValue(key));
				// reminder2.put( key, bigPrime.getValue(key) );			
			// }
		// }
		
		// BigDecimal reminder = reminder1.GetBigDecimal().subtract(reminder2.GetBigDecimal());
		
		// BigPrimeFactor remnderFactor=new BigPrimeFactor(reminder);
		
		// return result.multiply(remnderFactor);
	// }

	
	static NumarDenomPrime SimplifyNumarDenom(BigPrimeFactor numar, BigPrimeFactor denom)
	{
		NumarDenomPrime result = new NumarDenomPrime();
		
		Iterator<BigDecimal> iterator = numar.getIterator();
	
		while(iterator.hasNext())
		{
			BigDecimal key = iterator.next();
			
			if(denom.containsKey(key))
			{				
				if(numar.getValue(key) > denom.getValue(key))
				{
					int value = numar.getValue(key) - denom.getValue(key);
					
					result.numar.put( key, value );
				}
				else
				{
					int value = denom.getValue(key) - numar.getValue(key);
					
					if(value != 0)
						result.denom.put( key, value );
				}
			}
			else
			{
				//System.out.println("key11 : "+key+" : value : "+getValue(key));
				result.numar.put( key, numar.getValue(key) );				
			}
		}
		
		
		iterator = denom.getIterator();
	
		while(iterator.hasNext())
		{
			BigDecimal key = iterator.next();
			
			if(!numar.containsKey(key))
			{
				//System.out.println("key22 : "+key+" : value : "+bigPrime.getValue(key));
				result.denom.put( key, denom.getValue(key) );				
			}
		}
		
		return result;
	}
	
	
	public static boolean isMultipleOf(final BigDecimal multiple, final BigDecimal base)
	{
		if (multiple.compareTo(base) == 0)
			return true;

		try
		{
			multiple.divide(base, 0, BigDecimal.ROUND_UNNECESSARY);
			return true;
		}
		catch(ArithmeticException e)
		{
			return false;
		}
	}
	
	
	BigDecimal GetBigDecimal()
	{
		BigDecimal result = new BigDecimal(1);
		
		Iterator<BigDecimal> keys = getIterator();
		
		while(keys.hasNext())
		{
			BigDecimal key = keys.next();
			
			if(key.scale() > 0)
			{
				
				result = result.multiply( key.add( BigDecimal.TEN ) );
			}
			else
			{
				BigDecimal val = key.pow( getValue(key) );
			
				result = result.multiply( val );
			}
		}
		
		return result;
	}
}

class PrimList implements Serializable
{
	static Vector<BigDecimal> vec = null;
	
	static
	{
		vec = new Vector<BigDecimal>();
		int count = 0;
		
		try
		{
			String reg = "\\s+";
			
			for(int i=1; i<=1; i++)
			{
				String folderName = "primes"+i;
				
				BufferedReader br = new BufferedReader( new FileReader("PrimNumbers/"+folderName+"/"+folderName+".txt"));
				
				br.readLine();
				br.readLine();
				br.readLine();
				br.readLine();
				
				String line = null;
				
				while( (line = br.readLine()) != null)	
				{
					line = line.trim();
					
					if(line.length() > 0)
					{
						String tokens[] = line.split(reg);
						
						for(int tokenIndex=0; tokenIndex<tokens.length; tokenIndex++)
						{
							//System.out.println(tokens[tokenIndex]);
							
							vec.add(new BigDecimal( tokens[tokenIndex] ));
						}	
						
						count += tokens.length;
					}
					
					if(count >= 100)
						break;
				}
				
				br.close();
			}
			
			
		}
		catch(Exception e){e.printStackTrace();}	
	}
}


class NumarDenomPrime
{
	BigPrimeFactor numar = new BigPrimeFactor();
	BigPrimeFactor denom = new BigPrimeFactor();
	
	NumarDenomPrime()
	{
	}
	
	NumarDenomPrime(BigDecimal numarDecimal, BigDecimal denomDecimal)
	{
		this.numar = new BigPrimeFactor(numarDecimal);
		this.denom = new BigPrimeFactor(denomDecimal);
	}
	
	NumarDenomPrime(BigPrimeFactor numarPrimeFactor, BigPrimeFactor denomPrimeFactor)
	{
		this.numar = numarPrimeFactor;
		this.denom = denomPrimeFactor;
	}
	
	// NumarDenomPrime(BigDecimal numarDecimal, BigDecimal denomDecimal)
	// {
		// this.numar = new BigPrimeFactor(numarDecimal.toBigDecimal());
		// this.denom = new BigPrimeFactor(denomDecimal.toBigDecimal());
	// }
		
	NumarDenomPrime Simplify()
	{
		return BigPrimeFactor.SimplifyNumarDenom(numar, denom);
	}
	
	public String toString()
	{
		return "{ Numar : "+numar.toString()+" , \nDenom : "+denom.toString()+" }";
	}
}
