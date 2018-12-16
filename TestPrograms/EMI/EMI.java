import java.io.*;
import java.util.*;

class EMI
{
	public static void main(String[] args)
	{
		double L = 10;		//Loan Amount
		double IP = 12;			//Intrest Rate
		int T = 1;				//Tim in months
		
		double I = IP / 1200;
		double E = I+1;
		
		double C1 = L * I *( Epsilon(E, 0, T-1) ); 
		double C2 = I * SpecEpsilon(E, 0, T-2, T-1);
		
		double emiVal = (C1 + L) / (T + C2);
		
		double totInterest = C1 - emiVal*C2;
		
		System.out.println("EMI : "+emiVal);
		System.out.println("TOT Interest : "+totInterest);
		System.out.println("TOT payable(L+TI) : "+ (totInterest + L));
		System.out.println("TOT payable(EMI*T): "+ (emiVal * T));
	}

	static double Epsilon(double base, int startPow, int endPow)
	{
		double sum = 0;
		
		for(int i=startPow; i<=endPow; i++)
		{
			sum += Math.pow(base, i);
		}
		
		return sum;
	}
	
	static double SpecEpsilon(double base, int startPow, int endPow, int factor)
	{
		double sum = 0;
		
		for(int i=startPow; i<=endPow; i++)
		{
			sum += factor * Math.pow(base, i);
			factor--;
		}
		
		return sum;
	}
}



//

