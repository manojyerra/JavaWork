

public class Combi
{
	String str = "123";

	
	public static void main(String asdf[])throws Exception
	{
		new Combi();
	}

	public Combi()
	{
		int len = str.length();
		int loop[] = new int[len];

		for(int i=0;i<len;i++)
			loop[i] = 0;

		while(true)
		{
			System.out.println();
			for(int i=0;i<len;i++)
				System.out.print( str.charAt(loop[i]) );

			loop[len-1]++;
			
			for(int i=len-1;i>0;i--)
			{
				if((loop[i] < len) == false)
				{
					loop[i-1]++;
					loop[i] = 0;

					if(i-1 == 0 && (loop[i-1] < len) == false)
						System.exit(0);
				}
			}
		
		}

	}
}
