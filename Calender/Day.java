import java.io.*;

class  Day
{
    public static void main(String[] args) throws Exception
    {
		new Day();
	}
	
	Day() throws Exception
	{
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

		System.out.print("\n\n ENTER   YEAR  ");
		int year = Integer.parseInt(br.readLine().trim());
	
		System.out.print("\n ENTER   MONTH   ");
		int month = Integer.parseInt(br.readLine().trim());
		
		PrintCalender(year, month);
    }
	
	void PrintCalender(int year, int month)
	{
		int dayID = GetDayID(year, month, 1);
		int daysOfMonth  = GetNumDaysOfMonth(year, month);

		System.out.print("\n\n S    M    T    W    T    F    S\n\n");
		
		for(int i=1;i<dayID+1;i++)System.out.print("     ");
		for(int i=1;i<=daysOfMonth;i++)
		{
			if(i>=10){System.out.print(" "+i+"  ");}
			if(i<10){System.out.print(" "+i+"   ");}dayID++;
			if(dayID==7){System.out.print("\n");dayID=0;}
		}
	}
	
	int GetDayID(int year, int month, int date)
	{
		int totDays = GetTotalNumDaysTillMonth(year, month);
		int y = year-1;
		int dayID = (date+totDays+y+(y/4)+(y/400)-(y/100)) % 7;
		
		return dayID;
	}
	
	int GetNumDaysOfMonth(int year, int month)
	{
		int m = month;
		int d = 0;
		int e = 0;
		
		if(m==1 ){d=0  ;e=31;}   if(m==2 ){d=31 ;e=28;}  if(m==3 ){d=59 ;e=31;}
		if(m==4 ){d=90 ;e=30;}   if(m==5 ){d=120;e=31;}  if(m==6 ){d=151;e=30;}
		if(m==7 ){d=181;e=31;}   if(m==8 ){d=212;e=31;}  if(m==9 ){d=243;e=30;}
		if(m==10){d=273;e=31;}   if(m==11){d=304;e=30;}  if(m==12){d=334;e=31;}

		boolean isLeapYear = IsLeapYear(year);		

		if(isLeapYear && m==2) { e=29; }
		if(isLeapYear && m>=3) { d++; }
		
		return e;
	}

	int GetTotalNumDaysTillMonth(int year, int month)
	{
		int m = month;
		int d = 0;
		int e = 0;
		
		if(m==1 ){d=0  ;e=31;}   if(m==2 ){d=31 ;e=28;}  if(m==3 ){d=59 ;e=31;}
		if(m==4 ){d=90 ;e=30;}   if(m==5 ){d=120;e=31;}  if(m==6 ){d=151;e=30;}
		if(m==7 ){d=181;e=31;}   if(m==8 ){d=212;e=31;}  if(m==9 ){d=243;e=30;}
		if(m==10){d=273;e=31;}   if(m==11){d=304;e=30;}  if(m==12){d=334;e=31;}

		boolean isLeapYear = IsLeapYear(year);		

		if(isLeapYear && m==2) { e=29; }
		if(isLeapYear && m>=3) { d++; }
		
		return d;
	}
	
	boolean IsLeapYear(int year)
	{
		return ( year%4==0 && (year%100!=0||year%400==0) );
	}
}
