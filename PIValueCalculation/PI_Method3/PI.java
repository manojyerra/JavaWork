import java.math.*;
import java.util.*;

public class PI
{
	long totTimeToMultiply = 0;
	long totTimeToDivide = 0;
	
	long spigotStartTime = 0;
	
	public static void main(String[] args)
	{
 		long startTime = System.currentTimeMillis();
		
		new PI();

		long timeTaken = System.currentTimeMillis() - startTime;
		
		System.out.println("\n\nTime taken : "+timeTaken+" milli seconds\n\n");		
    }
	
	
	PI()
	{
		Spigot();
	}
	
	
	String Gregory_Leibniz_Series()
	{
		BigDecimal result = new BigDecimal(0.0);
		
		boolean positiveSign = true;
		
		for(long i=1; i<100000; i+=2)
		{
			if(positiveSign)
			{
				result = result.add( BigDecimal.ONE.divide( new BigDecimal(i), 1000, RoundingMode.HALF_UP ) );
			}
			else
			{
				result = result.subtract( BigDecimal.ONE.divide( new BigDecimal(i), 1000, RoundingMode.HALF_UP ) );			
			}			
										
			positiveSign = !positiveSign;
		}
		
		return ""+result.multiply( new BigDecimal(4.0) );
	}


	String Nilakantha_Series()
	{
		BigDecimal result = new BigDecimal(0.0);
		
		boolean positiveSign = true;
		
		for(long i=2; i<100000; i+=2)
		{
			BigDecimal numar = (new BigDecimal(i)).multiply(new BigDecimal(i+1)).multiply(new BigDecimal(i+2));
			
			if(positiveSign)
			{
				result = result.add( BigDecimal.ONE.divide( numar, 1000, RoundingMode.HALF_UP ) );
			}
			else
			{
				result = result.subtract( BigDecimal.ONE.divide( numar, 1000, RoundingMode.HALF_UP ) );			
			}			
										
			positiveSign = !positiveSign;
		}
		
		return ""+result.multiply( new BigDecimal(4.0) ).add(new BigDecimal(3.0));
	}
	
	
	String WithSquares()
	{
		BigDecimal result = new BigDecimal(0.0);
		
		for(long i=1; i<100000; i++)
		{
			BigDecimal square = new BigDecimal(i).multiply(new BigDecimal(i));
			
			result = result.add( BigDecimal.ONE.divide(square, 1000, RoundingMode.HALF_UP ) );
		}
		
		return ""+sqrtBigDecimal ( result.multiply( new BigDecimal(6.0) ), 1024 );
	}
	
	
	String RamanujanPIFormula()
	{
		//double val = 9801.0 / ( 2206.0 * Math.sqrt(2.0) );
		//return ""+val;

		double mulFactor = 2.0 * Math.sqrt(2.0) / 9801.0;
		
		BigDecimal mulFactorBigDecimal = new BigDecimal( mulFactor );
		
		BigDecimal _4 = new BigDecimal(4);
		BigDecimal _1103 = new BigDecimal(1103);
		BigDecimal _26390 = new BigDecimal(26390);
		BigDecimal _396 = new BigDecimal(396);

		BigDecimal sum = new BigDecimal(0.0);
				
		for(int k=0; k<100; k++)
		{			
			BigDecimal numer = Factorial( 4*k ).multiply( _1103.add( _26390.multiply( new BigDecimal(k) ) ) ) ;
			
			BigDecimal denom = Factorial(k).pow(4).multiply( _396.pow( 4*k ) );
			
			sum = sum.add ( numer.divide( denom, 1000, RoundingMode.HALF_UP ) );
		}
		
		sum = sum.multiply(mulFactorBigDecimal);
		
		sum = BigDecimal.ONE.divide( sum, 1000, RoundingMode.HALF_UP );
		
		return ""+sum;
	}
	
	
	String ChudnovskyFormula()
	{
		BigDecimal _13591409 = new BigDecimal( 13591409 );
		BigDecimal _545140134 = new BigDecimal( 545140134 );
		BigDecimal _640320 = new BigDecimal( 640320 );
		
		BigDecimal result = new BigDecimal(0.0);
		
		for(int k=0; k<1; k++)
		{
			BigDecimal numar = Factorial( 6*k ).multiply( _13591409.add( _545140134.multiply( new BigDecimal(k) ) ) );
			
			BigDecimal denom = Factorial(k).pow(3).multiply( Factorial( 3*k ) ).multiply( _640320.pow(3*k) );
		
			if(k % 2 == 0)
			{
				result = result.add( numar.divide( denom, 10000, RoundingMode.HALF_UP ) );
			}
			else
			{
				result = result.subtract( numar.divide( denom, 10000, RoundingMode.HALF_UP ) );
			}
		}
		
		BigDecimal sqrtVal = sqrtBigDecimal(new BigDecimal(10005), 100000);
		
		result = result.multiply ( sqrtVal ). divide( new BigDecimal(4270934400.0), 10000, RoundingMode.HALF_UP );
		
		result = BigDecimal.ONE.divide( result, 10000, RoundingMode.HALF_UP);
			
		return ""+result;
	}
	
	
	String ABTP()
	{
		BigDecimal a = new BigDecimal( 1.0 );
    	BigDecimal b = new BigDecimal( 1.0 / Math.sqrt(2.0) );
    	BigDecimal t = new BigDecimal( 1.0 / 4.0 );
    	BigDecimal p = new BigDecimal( 1.0 );

		for(int i=0; i<2000; i++)
		{
			BigDecimal a1 = a.add(b).divide( new BigDecimal(2.0) );
			
			BigDecimal b1 = sqrtBigDecimal ( a.multiply(b), 1024 );

			BigDecimal aSubtract = a.subtract(a1);

			BigDecimal t1 = t.subtract( p.multiply( aSubtract.multiply(aSubtract) ) );
			
			BigDecimal p1 = p.multiply( new BigDecimal(2.0) );
			
			a = a1;
			b = b1;
			t = t1;
			p = p1;
		}
		
		BigDecimal aPlusB = a.add(b);
		BigDecimal aPlusBSquare = aPlusB.multiply( aPlusB );
		
		BigDecimal denom = t.multiply( new BigDecimal(4.0) );
				
		BigDecimal result = aPlusBSquare.divide(denom, 1000, RoundingMode.HALF_UP);

		return ""+result;		
	}
	
	
	BigDecimal Factorial(int val)
	{
		BigDecimal result = new BigDecimal(1.0);
		
		for(int i=1; i<= val; i++)
			result = result.multiply( new BigDecimal(i) );
		
		return result;
	}
	
	
	public static BigDecimal sqrtBigDecimal(BigDecimal in, int scale)
	{
	    BigDecimal sqrt = new BigDecimal(1);
    	sqrt.setScale(scale + 3, RoundingMode.FLOOR);
	    BigDecimal store = new BigDecimal(in.toString());
    
    	boolean first = true;
    
	    do
    	{
        	if (!first)
	        {
    	        store = new BigDecimal(sqrt.toString());
        	}
	        else
    	    {
        		first = false; 
	        }
        
    	    store.setScale(scale + 3, RoundingMode.FLOOR);
        
        	sqrt = in.divide(store, scale + 3, RoundingMode.FLOOR).add(store).divide( BigDecimal.valueOf(2), scale + 3, RoundingMode.FLOOR);
	    }
    	while (!store.equals(sqrt));
    
	    return sqrt.setScale(scale, RoundingMode.FLOOR);
	}
	
	
	void Spigot()
	{		
		//Time taken for 1 Million ( 1000,000 ) is 100 secnods.
		
		//Set increment as 2000 when max value is 100000		
		//increment/10, 10 as parameters for Spigot_Level_3
		//Time taken ~ 4300 milli secnods		
		
		//Set increment as 20000 when max value is 1000,000 
		//increment/10, 10 as parameters for Spigot_Level_3
		//Time taken ~ 100000 milli seconds
		
		
		//int increment 	= 1000000;
		int max 		= 1000000;
		
		spigotStartTime = System.currentTimeMillis();
	
		BigDecimal result = Spigot_Level_6(0, max, 1, max);

		System.out.println("\n\n"+result+"\n\n");
	}
	
	
	BigDecimal Spigot_Level_6(int startVal, int increment, int numTimes, int scale)
	{
		BigDecimal totalNumar = new BigDecimal(0.0);
		BigDecimal totalDenom = new BigDecimal(1.0);

		BigDecimal sixTeen = new  BigDecimal(16);
		BigDecimal sixTeenPow = new  BigDecimal(1.0);
		
		BigDecimal sixTeenPowIncrement = new  BigDecimal(16).pow(increment);
		
		int endVal = startVal + increment * numTimes;
		
		for(int k=endVal ; k>startVal; k -= increment)
		{
			NumarAndDenom nd = Spigot_Level_4(k-increment, increment/10, 10);
			
			long startTime = System.currentTimeMillis();
			
			totalNumar = totalNumar.multiply( nd.denom ).add( totalDenom.multiply( nd.numar ) );
			totalDenom = totalDenom.multiply( nd.denom );
			
			if(k-increment > 0)
				totalDenom = totalDenom.multiply( sixTeenPowIncrement );
			
			totTimeToMultiply += System.currentTimeMillis() - startTime;
		}
	
		long divideStartTime = System.currentTimeMillis();
		
		BigDecimal result = totalNumar.divide( totalDenom, scale, RoundingMode.HALF_UP );
		
		totTimeToDivide += System.currentTimeMillis() - divideStartTime;
		
		return result;
	}		

	NumarAndDenom Spigot_Level_5(int startVal, int increment, int numTimes)
	{
		BigDecimal totalNumar = new BigDecimal(0.0);
		BigDecimal totalDenom = new BigDecimal(1.0);

		BigDecimal sixTeen = new  BigDecimal(16);
		BigDecimal sixTeenPow = new  BigDecimal(1.0);
		
		BigDecimal sixTeenPowIncrement = new  BigDecimal(16).pow(increment);
		
		int endVal = startVal + increment * numTimes;
		
		for(int k=endVal ; k>startVal; k -= increment)
		{
			NumarAndDenom nd = Spigot_Level_4(k-increment, increment/10, 10);
			
			long startTime = System.currentTimeMillis();
			
			totalNumar = totalNumar.multiply( nd.denom ).add( totalDenom.multiply( nd.numar ) );
			totalDenom = totalDenom.multiply( nd.denom );
			
			if(k-increment > 0)
				totalDenom = totalDenom.multiply( sixTeenPowIncrement );
			
			totTimeToMultiply += System.currentTimeMillis() - startTime;
		}
		
		NumarAndDenom numarAndDenom = new NumarAndDenom();
		
		numarAndDenom.numar = totalNumar;
		numarAndDenom.denom = totalDenom;
		
		return numarAndDenom;		
	}		
	
	NumarAndDenom Spigot_Level_4(int startVal, int increment, int numTimes)
	{
		System.out.println(startVal+" : "+(System.currentTimeMillis() - spigotStartTime));
		
		BigDecimal totalNumar = new BigDecimal(0.0);
		BigDecimal totalDenom = new BigDecimal(1.0);

		BigDecimal sixTeen = new  BigDecimal(16);
		BigDecimal sixTeenPow = new  BigDecimal(1.0);
		
		BigDecimal sixTeenPowIncrement = new  BigDecimal(16).pow(increment);
		
		int endVal = startVal + increment * numTimes;
		
		for(int k=endVal ; k>startVal; k -= increment)
		{
			NumarAndDenom nd = Spigot_Level_3(k-increment, increment/10, 10);
			
			long startTime = System.currentTimeMillis();
			
			totalNumar = totalNumar.multiply( nd.denom ).add( totalDenom.multiply( nd.numar ) );
			totalDenom = totalDenom.multiply( nd.denom );
			
			if(k-increment > 0)
				totalDenom = totalDenom.multiply( sixTeenPowIncrement );
			
			totTimeToMultiply += System.currentTimeMillis() - startTime;
		}
		
		NumarAndDenom numarAndDenom = new NumarAndDenom();
		
		numarAndDenom.numar = totalNumar;
		numarAndDenom.denom = totalDenom;
		
		return numarAndDenom;		
	}		
		
	NumarAndDenom Spigot_Level_3(int startVal, int increment, int numTimes)
	{	
		System.out.println(startVal+" : "+(System.currentTimeMillis() - spigotStartTime));
	
		BigDecimal totalNumar = new BigDecimal(0.0);
		BigDecimal totalDenom = new BigDecimal(1.0);

		BigDecimal sixTeen = new  BigDecimal(16);
		BigDecimal sixTeenPow = new  BigDecimal(1.0);
		
		BigDecimal sixTeenPowIncrement = new  BigDecimal(16).pow(increment);
		
		int endVal = startVal + increment * numTimes;
		
		for(int k=endVal ; k>startVal; k -= increment)
		{
			NumarAndDenom nd = Spigot_Level_2(k-increment, increment/10, 10);
			
			long startTime = System.currentTimeMillis();
			
			totalNumar = totalNumar.multiply( nd.denom ).add( totalDenom.multiply( nd.numar ) );
			totalDenom = totalDenom.multiply( nd.denom );
			
			if(k-increment > 0)
				totalDenom = totalDenom.multiply( sixTeenPowIncrement );
			
			totTimeToMultiply += System.currentTimeMillis() - startTime;
		}
		
		NumarAndDenom numarAndDenom = new NumarAndDenom();
		
		numarAndDenom.numar = totalNumar;
		numarAndDenom.denom = totalDenom;
		
		return numarAndDenom;
	}		
	
	NumarAndDenom Spigot_Level_2(int startVal, int increment, int numTimes)
	{
		System.out.println("L2 : "+(startVal)+" : "+(System.currentTimeMillis() - spigotStartTime));
		
		BigDecimal totalNumar = new BigDecimal(0.0);
		BigDecimal totalDenom = new BigDecimal(1.0);

		BigDecimal sixTeen = new  BigDecimal(16);
		BigDecimal sixTeenPow = new  BigDecimal(1.0);
		
		BigDecimal sixTeenPowIncrement = new  BigDecimal(16).pow(increment);
		
		int endVal = startVal + increment * numTimes;
		
		for(int k=endVal ; k>startVal; k -= increment)
		{
			NumarAndDenom nd = Spigot_Level_1(k-increment, k);
			
			long startTime = System.currentTimeMillis();
			
			totalNumar = totalNumar.multiply( nd.denom ).add( totalDenom.multiply( nd.numar ) );
			totalDenom = totalDenom.multiply( nd.denom );
			
			if(k-increment > 0)
				totalDenom = totalDenom.multiply( sixTeenPowIncrement );
			
			totTimeToMultiply += System.currentTimeMillis() - startTime;
		}
		
		NumarAndDenom numerAndDenom = new NumarAndDenom();
		
		numerAndDenom.numar = totalNumar;
		numerAndDenom.denom = totalDenom;		
		
		return numerAndDenom;		
	}	
	
	NumarAndDenom Spigot_Level_1(int startVal, int endVal)
	{
		//System.out.println("L1 : "+(endVal - startVal)+" : "+(System.currentTimeMillis() - spigotStartTime));
		
		BigDecimal totalNumar = new BigDecimal(0.0);
		BigDecimal totalDenom = new BigDecimal(1.0);
		
		BigDecimal sixTeen = new BigDecimal(16);
		
		for(int k=endVal-1 ; k>=startVal; k--)
		{
			NumarAndDenom elementND = Element( k );
						
			totalNumar = totalNumar.multiply( elementND.denom ).add( totalDenom.multiply( elementND.numar ) );
			totalDenom = totalDenom.multiply( elementND.denom );
		
			if(k != startVal)
				totalDenom = totalDenom.multiply( sixTeen );
		}
		
		NumarAndDenom numerAndDenom = new NumarAndDenom();
		
		numerAndDenom.numar = totalNumar;
		numerAndDenom.denom = totalDenom;		
		
		return numerAndDenom;
	}

	
	NumarAndDenom Element(int k)
	{
		BigDecimal two = new BigDecimal(2.0);
		BigDecimal four = new BigDecimal(4.0);
		
		BigDecimal a = new BigDecimal( 8 * k + 1 );
		BigDecimal b = new BigDecimal( 8 * k + 4 );
		BigDecimal c = new BigDecimal( 8 * k + 5 );
		BigDecimal d = new BigDecimal( 8 * k + 6 );

		BigDecimal const1 = four.multiply(b).multiply(c).multiply(d);
		BigDecimal const2 = two.multiply(a).multiply(c).multiply(d);
		BigDecimal const3 = a.multiply(b).multiply(d);
		BigDecimal const4 = a.multiply(b).multiply(c);
			
		BigDecimal numar = const1.subtract( const2.add(const3.add(const4)));
		BigDecimal denom = a.multiply(b).multiply(c).multiply(d);
		
		NumarAndDenom numerAndDenom = new NumarAndDenom();
		
		numerAndDenom.numar = numar;
		numerAndDenom.denom = denom;	
		
		return numerAndDenom;
	}

	
	// NumarAndDenom Element2(int k)
	// {
		// final BigDecimal two = new BigDecimal(2.0);
		// final BigDecimal four = new BigDecimal(4.0);
		// final BigDecimal sixTeen = new BigDecimal(16.0);
		
		// long _8k = 8 * k;
		
		// final BigDecimal a0 = new BigDecimal( _8k + 1 );
		// final BigDecimal b0 = new BigDecimal( _8k + 4 );
		// final BigDecimal c0 = new BigDecimal( _8k + 5 );
		// final BigDecimal d0 = new BigDecimal( _8k + 6 );

		// BigDecimal const0_1 = four.multiply(b0).multiply(c0).multiply(d0);
		// BigDecimal const0_2 = two.multiply(a0).multiply(c0).multiply(d0);
		// BigDecimal const0_3 = a0.multiply(b0).multiply(d0);
		// BigDecimal const0_4 = a0.multiply(b0).multiply(c0);
			
		// BigDecimal x0 = const0_1.subtract( const0_2.add(const0_3.add(const0_4)));
		// BigDecimal y0 = a0.multiply(b0).multiply(c0).multiply(d0);
		

		// final BigDecimal a1 = new BigDecimal( _8k + 9 );
		// final BigDecimal b1 = new BigDecimal( _8k + 12 );
		// final BigDecimal c1 = new BigDecimal( _8k + 13 );
		// final BigDecimal d1 = new BigDecimal( _8k + 14 );

		// BigDecimal const1_1 = four.multiply(b1).multiply(c1).multiply(d1);
		// BigDecimal const1_2 = two.multiply(a1).multiply(c1).multiply(d1);
		// BigDecimal const1_3 = a1.multiply(b1).multiply(d1);
		// BigDecimal const1_4 = a1.multiply(b1).multiply(c1);
			
		// BigDecimal x1 = const1_1.subtract( const1_2.add(const1_3.add(const1_4)));
		// BigDecimal y1 = a1.multiply(b1).multiply(c1).multiply(d1);

		// BigDecimal sixTeenY1 = sixTeen.multiply( y1 );
		
		// BigDecimal numar = sixTeenY1.multiply( x0 ).add( x1.multiply(y0) );
		// BigDecimal denom = sixTeenY1.multiply( y0 );
		
		// NumarAndDenom numerAndDenom = new NumarAndDenom();
		
		// numerAndDenom.numar = numar;
		// numerAndDenom.denom = denom;	
		
		// return numerAndDenom;
	// }
	
	// NumarAndDenom Spigot_Level_1(int startVal, int endVal)
	// {		
		// BigDecimal two = new BigDecimal(2.0);
		// BigDecimal four = new BigDecimal(4.0);
		
		// BigDecimal sixTeen = new BigDecimal(16);
		// BigDecimal sixTeenPow = new BigDecimal(16).pow( 0 );
		
		// BigDecimal totalNumar = new BigDecimal(0.0);
		// BigDecimal totalDenom = new BigDecimal(1.0);
		
		// for (int k = startVal; k < endVal; k++)
		// {	
			// BigDecimal a = new BigDecimal( 8 * k + 1 );
			// BigDecimal b = new BigDecimal( 8 * k + 4 );
			// BigDecimal c = new BigDecimal( 8 * k + 5 );
			// BigDecimal d = new BigDecimal( 8 * k + 6 );

			// BigDecimal const1 = four.multiply(b).multiply(c).multiply(d);
			// BigDecimal const2 = two.multiply(a).multiply(c).multiply(d);
			// BigDecimal const3 = a.multiply(b).multiply(d);
			// BigDecimal const4 = a.multiply(b).multiply(c);
				
			// BigDecimal numar = const1.subtract( const2.add(const3.add(const4)));
			// BigDecimal denom = a.multiply(b).multiply(c).multiply(d).multiply(sixTeenPow) ;
			
			// totalNumar = totalNumar.multiply( denom ).add( totalDenom.multiply( numar ) );
			// totalDenom = totalDenom.multiply( denom );
			
			// sixTeenPow = sixTeenPow.multiply( sixTeen );
		// }
		
		// NumarAndDenom numerAndDenom = new NumarAndDenom();
		
		// numerAndDenom.numar = totalNumar;
		// numerAndDenom.denom = totalDenom;		
		
		// return numerAndDenom;
	// }
	
	
	// BigDecimal SpigotUsingThreads(int startVal, int increment, int numThreads, int scale)
	// {
		// final int incrementConst = increment;
		// final int scaleConst = scale;
	
		// Vector<Thread> vec = new Vector<Thread>();
		
		// for(int i=0; i<numThreads; i++)
		// {
			// final int start = startVal;
			// final int end = startVal + incrementConst;
				
			// vec.add( new Thread(new Runnable() { @Override public void run() 
			// {
				
				// vecBigDeci.add( Spigot1( start, end, scaleConst) );
				
			// }}));
			
			// startVal += incrementConst;
		// }
		
		// System.out.println("\n\n Going to invoke all threads... \n\n");

		// for(int i=0; i<numThreads; i++)
		// {
			// vec.get(i).start();
		// }

		// System.out.println("\n\n All threads invoked... \n\n");

		// for(int i=0; i<numThreads; i++)
		// {
			// try { vec.get(i).join(); } catch(Exception e){ e.printStackTrace(); }			
		// }

		// System.out.println("\n\n All threads job done ... \n\n");
		
		// BigDecimal result = new BigDecimal(0);
		
		// for(int i=0; i<numThreads; i++)
		// {
			// result = result.add( vecBigDeci.get(i) );
		// }
		
		// System.out.println( result );
		
		// return result;
	// }
}

class NumarAndDenom
{
	public BigDecimal numar;
	public BigDecimal denom;
}
