

class EMI
{
	public static void main(String args[])
	{
		new EMI();
	}
	
	EMI()
	{	
			double tenPow7 = 10000000;
			
            double loanAmount = 1000000;//tenPow7*tenPow7;
            double interestPercent = 13;
            double emi = 20000;//tenPow7 * 100000 + 43;

            if(loanAmount > tenPow7*tenPow7)
            {
                System.out.println("Loan amount exceeded the limit");
                return;
            }

            if(interestPercent > 200)
            {
                System.out.println("Interest percent should not be more than 200");
                return;
            }

            if(emi >= loanAmount)
            {
                System.out.println("EMI should be less than Loan Amount");
                return;
            }

            long emiFor200Yrs = (long)EMIUtil.CalcEMI(loanAmount, interestPercent, 200*12);

            if(emi < emiFor200Yrs)
            {
                System.out.println("To calculate, EMI should be more than or equal to ₹ "+ (emiFor200Yrs+1));
                return;
            }

            double months = EMIUtil.CalcLoanPeriod(loanAmount, emi, interestPercent);

            if((long)months < 0)
            {
                long val = (long)(loanAmount * interestPercent / 1200.0);
                System.out.println("EMI should be more than or equals to ₹ "+ (val+1));
            }
            else
            {
                if((long)months >= 1)
                {
	                System.out.println("Time Period "+(months));
	                
    	            System.out.println(MonthsToYears((int)months)+" to "+MonthsToYears((int)months+1));
                }
            }
    }
    
    String MonthsToYears(int months)
    {
	    if(months > 12)
	    {
	    int years = (int)months/(int)12;
	    int mon = (int)months - years*12;
	                	
	    if(mon >= 1)
	    {
	        return (years+" years and "+mon+" months");
	    }
	    else
	    {
	    	return (years+" years");
	    }
	    }
	    
	    return months+" months";
    }
}