
class Test
{
	public static void main(String args[])
	{
		new Test();
	}
	
	Test()
	{		
	    long startTime = System.currentTimeMillis();
		
    	BigInteger bigDecimal1 = new BigInteger("7797279247");
    
	    for(int i=0; i<10; i++)
		    bigDecimal1 = bigDecimal1.multiply(bigDecimal1);

	    BigInteger bigDecimal2 = new BigInteger("123987456789");

	    for(int i=0; i<10; i++)
		    bigDecimal2 = bigDecimal2.multiply(bigDecimal2);
    
	    BigInteger result = bigDecimal1.multiply( bigDecimal2 );
    
    	long timeTaken = System.currentTimeMillis() - startTime;
	    
		System.out.println("\nResult :"+result.toString(16)+" \n");
		
		System.out.println("\nTime taken :"+timeTaken+" \n");
	}
}
