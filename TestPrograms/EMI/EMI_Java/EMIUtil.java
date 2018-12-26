
public class EMIUtil {

    public static double CalcEMI(double loanAmount, double interestRate, int TimeInMonths)
    {
        double L = loanAmount;
        double IP = interestRate;
        int T = TimeInMonths;

        double I = IP / 1200;
        double E = I + 1;

        double C1 = L * I * Epsilon(E, 0, T - 1);
        double C2 = I * SpecEpsilon(E, 0, T - 2, T - 1);

        double emiVal = (C1 + L) / (T + C2);

        System.out.println("\nCalcEMI : C1 : "+C1+" C2 : "+C2+" emiVal : "+emiVal);

        //double totInterest = C1 - emiVal * C2;

        return emiVal;
    }

    //1101086134

    public static double CalcTotInterest(double loanAmount, double interestRate, int TimeInMonths)
    {
        double L = loanAmount;
        double IP = interestRate;
        int T = TimeInMonths;

        double I = IP / 1200;
        double E = I + 1;

        double C1 = L * I * Epsilon(E, 0, T - 1);
        double C2 = I * SpecEpsilon(E, 0, T - 2, T - 1);

        double emiVal = (C1 + L) / (T + C2);

        double totInterest = (emiVal * (double)TimeInMonths) - loanAmount; //C1 - emiVal * C2;

        System.out.println("\nCalcTotInterest : C1 : "+C1+" C2 : "+C2+" emiVal : "+emiVal+" totInterest : "+totInterest);

        return totInterest;
    }

    public static double CalcLoanAmount(double emi, double interestRate, int TimeInMonths)
    {
        double IP = interestRate;
        int T = TimeInMonths;

        double I = IP / 1200;
        double E = I + 1;

        double EP1 = Epsilon(E, 0, T - 1);
        double EP2 = SpecEpsilon(E, 0, T - 2, T - 1);

        double C2 = I * EP2;

        double loanAmount = emi * (T + C2) / ( I*EP1 + 1 );

        return loanAmount;
    }
/*
    public static double CalcLoanPeriod(double loanAmount, double emi, double interestRate)
    {
        double firstMonthInterest = loanAmount * interestRate / 1200;

        if(emi <= firstMonthInterest)
        {
            return -1.0;
        }

        double deduct = emi - firstMonthInterest;

        double maxMonths = loanAmount / deduct;
        double minMonths = 0;
        double currMonths = (minMonths + maxMonths) / 2;

        double prevCurrMonths = currMonths;

        double emi_months = 0;
        double loan_interest = 0;

        while(true)
        {
            emi_months = emi * currMonths;
            loan_interest = loanAmount + CalcTotInterest(loanAmount, interestRate, (int)(currMonths));

            System.out.println("\n\n emi_months = " + emi_months);
            System.out.println("loan_interest = " + loan_interest);

            if(emi_months < loan_interest)
            {
                minMonths = currMonths;
                currMonths = (minMonths + maxMonths) / 2;
            }
            else if(emi_months > loan_interest)
            {
                maxMonths = currMonths;
                currMonths = (minMonths + maxMonths) / 2;
            }

            if((long)prevCurrMonths == (long)currMonths)
                break;

            prevCurrMonths = currMonths;
        }

        //System.out.println("\nLoan Time : "+currMonths+" months ");

        return currMonths;
    }
*/

    public static double CalcLoanPeriod(double loanAmount, double emi, double interestRate)
    {
        double firstMonthInterest = loanAmount * interestRate / 1200;

        if(emi <= firstMonthInterest)
        {
            return -1.0;
        }

        double deduct = emi - firstMonthInterest;

        double maxMonths = 200*12; //loanAmount / deduct;
        double minMonths = 0;
        double currMonths = (minMonths + maxMonths) / 2;

        while(true)
        {
            double emiChk = loanAmount;

            if((int)currMonths > 0)
                emiChk = CalcEMI(loanAmount, interestRate, (int)currMonths);

            double emiChkNextMonth = CalcEMI(loanAmount, interestRate, (int)currMonths + 1);

            if(emi >= emiChkNextMonth && emi <= emiChk)
            {
                break;
            }

            if(emiChk > emi)
            {
                System.out.println("emiChk > emi "+emiChk+" > "+emi+" currMonths"+currMonths);

                minMonths = currMonths;
                currMonths = (minMonths + maxMonths) / 2;
            }
            else if(emiChk < emi)
            {
                System.out.println("emiChk < emi "+emiChk+" < "+emi+" currMonths"+currMonths);

                maxMonths = currMonths;
                currMonths = (minMonths + maxMonths) / 2;
            }
        }

        return currMonths;
    }

    private static double Epsilon(double base, int startPow, int endPow)
    {
        double sum = 0;

        for(int i=startPow; i<=endPow; i++)
        {
            sum += Math.pow(base, i);
        }

        return sum;
    }

    private static double SpecEpsilon(double base, int startPow, int endPow, int factor)
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
