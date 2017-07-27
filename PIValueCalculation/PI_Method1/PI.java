import java.math.*;
import java.util.*;

public class PI
{
	Vector<BigDecimal> vecBigDeci = new Vector<BigDecimal>();
	
	public static void main(String[] args)
	{
 		long startTime = System.currentTimeMillis();
		
		new PI();

		long timeTaken = System.currentTimeMillis() - startTime;
		
		System.out.println("\n\nTime taken : "+timeTaken+" milli seconds\n\n");		
    }
	
	PI()
	{
		BigDecimal result = new BigDecimal(0.0);
		
		//Apporx for 1000,000 is 114 seconds. ( less than 2 minutes ).
		
		int increment 	= 10000;
		int max 		= 1000000;
		
		BigDecimal sixTeenPowStartVal = new BigDecimal(1.0);
		BigDecimal sixTeenPowIncrement = new BigDecimal(16).pow(increment);
		
		long startTime = System.currentTimeMillis();
		
		for(int i=0; i<max; i+=increment)
		{
			result = result.add( Spigot_Level_2(i, increment/10, 10, max, sixTeenPowStartVal) );
			
			long timeTaken = System.currentTimeMillis() - startTime;
			
			System.out.println("Iteration : "+i+" time : "+timeTaken);
			
			sixTeenPowStartVal = sixTeenPowStartVal.multiply( sixTeenPowIncrement );
		}
		
		System.out.println("\n\n"+result+"\n\n");
	}

	
	BigDecimal Spigot_Level_2(int startVal, int increment, int numTimes, int scale, BigDecimal startValSixTeenPow)
	{
		BigDecimal totalNumar = new BigDecimal(0.0);
		BigDecimal totalDenom = new BigDecimal(1.0);

		BigDecimal sixTeen = new  BigDecimal(16);
		BigDecimal sixTeenPow = new  BigDecimal(1.0);
		
		BigDecimal sixTeenPowIncrement = new  BigDecimal(16).pow(increment);
		
		for(int i=0; i<numTimes; i++)
		{
			NumarAndDenom nd = Spigot_Level_1(startVal+ (i*increment), startVal+((i+1)*increment));
			
			nd.denom = nd.denom.multiply( sixTeenPow );
			
			totalNumar = totalNumar.multiply( nd.denom ).add( totalDenom.multiply( nd.numar ) );
			totalDenom = totalDenom.multiply( nd.denom );
			
			sixTeenPow = sixTeenPow.multiply( sixTeenPowIncrement );
		}
		
		BigDecimal result = totalNumar.divide( totalDenom.multiply(startValSixTeenPow), scale, RoundingMode.HALF_UP );
		
		return result;
	}	
		
	NumarAndDenom Spigot_Level_1(int statVal, int endVal)
	{
		BigDecimal totalNumar = new BigDecimal(0.0);
		BigDecimal totalDenom = new BigDecimal(1.0);
		
		BigDecimal sixTeen = new BigDecimal(16);
		
		NumarAndDenom ele = Element( 0 );
		
		for(int k=endVal-1 ; k>=statVal; k--)
		{
			NumarAndDenom elementND = Element( k );
						
			totalNumar = totalNumar.multiply( elementND.denom ).add( totalDenom.multiply( elementND.numar ) );
			totalDenom = totalDenom.multiply( elementND.denom );
		
			if(k != statVal)
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
}

class NumarAndDenom
{
	public BigDecimal numar;
	public BigDecimal denom;
}
