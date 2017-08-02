import java.math.*;
import java.io.*;
import java.util.*;

class BigPrimeFactor
{
	HashMap<String, BigDecimal> map = new HashMap<String, BigDecimal>();
	
	BigPrimeFactor()
	{
		map.put("1", BigDecimal.ONE);
	}
	
	BigPrimeFactor(String str)
	{
		Init( new BigDecimal(str) );
	}
	
	BigPrimeFactor(BigDecimal n)
	{
		Init( n );
	}
	
	void Init(BigDecimal n)
	{
		Iterator<String> iterator = PrimList.vec.iterator();
		boolean run = true;
	
		while(iterator.hasNext())
		{
			String keyStr = iterator.next();
			BigDecimal keyBI = new BigDecimal(keyStr);
			
			while(keyBI.compareTo(n) <= 0)
			{
				if( n.remainder(keyBI).compareTo(BigDecimal.ZERO) == 0)
				{
					if(map.containsKey(keyStr))
					{
						map.put(keyStr, map.get(keyStr).add(BigDecimal.ONE));
					}
					else
					{
						map.put(keyStr, BigDecimal.ONE);
					}
					
					n = n.divide(keyBI);
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
		
		if( n.compareTo(BigDecimal.ONE) != 0)
		{
			//System.out.println(n);
			
			map.put(n.toString(), BigDecimal.ONE);
		}
		else if(map.size() == 0)
		{			
			map.put("1", BigDecimal.ONE);
		}
		
		//System.out.println(this);
	}
	
	public String toString()
	{
		return map.toString();
	}
	
	void put(String key, BigDecimal value)
	{
		map.put(key, value);
	}
	
	boolean containsKey(String key)
	{
		return map.containsKey(key);
	}
	
	BigDecimal getValue(String key)
	{
		return map.get(key);
	}
	
	Iterator<String> getIterator()
	{
		return map.keySet().iterator();
	}
	
	BigPrimeFactor multiply(BigPrimeFactor bigPrime)
	{
		BigPrimeFactor result = new BigPrimeFactor();
		
		Iterator<String> iterator = getIterator();
	
		while(iterator.hasNext())
		{
			String key = iterator.next();
			
			if(bigPrime.containsKey(key))
			{
				BigDecimal value = new BigDecimal("0");
				
				value = value.add(getValue(key)).add(bigPrime.getValue(key));
				
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
			String key = iterator.next();
			
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
		
		Iterator<String> iterator = getIterator();
	
		while(iterator.hasNext())
		{
			String key = iterator.next();
			
			if(bigPrime.containsKey(key))
			{
				BigDecimal value = new BigDecimal("0");
				
				if(getValue(key).longValue() > bigPrime.getValue(key).longValue())
				{
					result.put( key, bigPrime.getValue(key) );
					
					value = getValue(key).subtract(bigPrime.getValue(key));
					reminder1.put(key, value);
					
				}
				else
				{
					result.put( key, getValue(key) );
					
					value = bigPrime.getValue(key).subtract(getValue(key));
					if(value.longValue() != 0)
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
			String key = iterator.next();
			
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
	
	BigPrimeFactor subtract(BigPrimeFactor bigPrime)
	{
		BigPrimeFactor result = new BigPrimeFactor();
		BigPrimeFactor reminder1 = new BigPrimeFactor();
		BigPrimeFactor reminder2 = new BigPrimeFactor();
		
		Iterator<String> iterator = getIterator();
	
		while(iterator.hasNext())
		{
			String key = iterator.next();
			
			if(bigPrime.containsKey(key))
			{
				BigDecimal value = new BigDecimal("0");
				
				if(getValue(key).longValue() > bigPrime.getValue(key).longValue())
				{
					result.put( key, bigPrime.getValue(key) );
					
					value = getValue(key).subtract(bigPrime.getValue(key));
					reminder1.put(key, value);
					
				}
				else
				{
					result.put( key, getValue(key) );
					
					value = bigPrime.getValue(key).subtract(getValue(key));
					if(value.longValue() != 0)
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
			String key = iterator.next();
			
			if(!containsKey(key))
			{
				//System.out.println("key22 : "+key+" : value : "+bigPrime.getValue(key));
				reminder2.put( key, bigPrime.getValue(key) );			
			}
		}
		
		BigDecimal reminder = reminder1.GetBigDecimal().subtract(reminder2.GetBigDecimal());
		
		BigPrimeFactor remnderFactor=new BigPrimeFactor(reminder);
		
		return result.multiply(remnderFactor);
	}

	
	static NumarDenomPrime SimplifyNumarDenom(BigPrimeFactor numar, BigPrimeFactor denom)
	{
		NumarDenomPrime result = new NumarDenomPrime();
		
		Iterator<String> iterator = numar.getIterator();
	
		while(iterator.hasNext())
		{
			String key = iterator.next();
			
			if(denom.containsKey(key))
			{
				BigDecimal value = new BigDecimal("0");
				
				if(numar.getValue(key).longValue() > denom.getValue(key).longValue())
				{
					value = numar.getValue(key).subtract(denom.getValue(key));
					
					result.numar.put( key, value );
				}
				else
				{
					value = denom.getValue(key).subtract(numar.getValue(key));
					
					if(value.longValue() != 0)
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
			String key = iterator.next();
			
			if(!numar.containsKey(key))
			{
				//System.out.println("key22 : "+key+" : value : "+bigPrime.getValue(key));
				result.denom.put( key, denom.getValue(key) );				
			}
		}
		
		return result;
	}
	

	BigDecimal GetBigDecimal()
	{
		BigDecimal result = new BigDecimal("1");
		
		Iterator<String> keys = getIterator();
		
		while(keys.hasNext())
		{
			String key = keys.next();
			
			BigDecimal val = new BigDecimal(key).pow( getValue(key).intValue() );
			result = result.multiply( val );
		}
		
		return result;
	}
}

class PrimList implements Serializable
{
	static Vector<String> vec = null;
	
	static void Compose()
	{
		vec = new Vector<String>();
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
							
							vec.add(tokens[tokenIndex]);
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
