

class MagicSquares_Repeated
{
	public static void main(String args[])
	{
		new MagicSquares_Repeated();
	}
	
	MagicSquares_Repeated()
	{
		int a = 1;
		int b = 1;
		int c = 1;
		int d = 1;
		int e = 1;
		int f = 1;
		int g = 1;
		int h = 1;
		int i = 1;
		
		int cCount = 0;
		
		int max = 100;
		
		for(a = 1; a<max; a++)
		{
			int aPow2 = a*a;
		
			for(b = 1; b<max; b++)
			{
				int bPow2 = b*b;
										
				for(c = 1; c<max; c++)
				{
					cCount++;
					
					int cPow2 = c*c;
					
					int n1 = aPow2 + bPow2 + cPow2;
					
					for(d = 1; d<max; d++)
					{						
						int dPow2 = d*d;
						
						for(e = 1; e<max; e++)
						{			
							int ePow2 = e*e;
							
							if(aPow2 + dPow2 != ePow2 + cPow2) continue;
								
							for(f = 1; f<max; f++)
							{
								int fPow2 = f*f;
																	
								int n2 = dPow2 + ePow2 + fPow2;
								
								if( n2 != n1)
									continue;
									
								if( cPow2 + fPow2 != aPow2 + ePow2)
									continue;
									
								for(g = 1; g<max; g++)
								{
									int gPow2 = g*g;
									
									int n4 = aPow2 + dPow2 + gPow2;
									if( n4 != n1)
										continue;
										
									int n8 = cPow2 + ePow2 + gPow2;
									
									if( n8 != n1)
										continue;
										
									if(bPow2 + cPow2 != dPow2 + gPow2) continue;
									if(aPow2 + bPow2 != gPow2 + ePow2) continue;
									if(aPow2 + gPow2 != ePow2 + fPow2) continue;
									if(dPow2 + fPow2 != gPow2 + cPow2) continue;
									if(aPow2 + dPow2 != ePow2 + cPow2) continue;
									if(cPow2 + fPow2 != aPow2 + ePow2) continue;
										
									for(h = 1; h<max; h++)
									{						
										int hPow2 = h*h;
										
										int n5 = bPow2 + ePow2 + hPow2;
										if( n5 != n1)
											continue;
											
										for(i = 1; i<max; i++)
										{
											if(i == a && i == b && i == c && i == d && i == e && i == f && i == g && i == h) continue;
											
											int iPow2 = i*i;
											
											int n3 = gPow2 + hPow2 + iPow2;
											if( n3 != n1)
												continue;
											
											int n6 = cPow2 + fPow2 + iPow2;
											if( n6 != n1)
												continue;

											int n7 = aPow2 + ePow2 + iPow2;
											if( n7 != n1)
												continue;
											
											System.out.println(a+","+b+","+c+","+d+","+e+","+f+","+g+","+h+","+i);
											
// 											a = max + 1;
// 											b = max + 1;
// 											c = max + 1;
// 											d = max + 1;
// 											e = max + 1;
// 											f = max + 1;
// 											g = max + 1;
// 											h = max + 1;
// 											i = max + 1;
// 											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		
		
	}
	
	
}