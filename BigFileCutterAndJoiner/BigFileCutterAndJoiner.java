import java.io.*;


class BigFileCutterAndJoiner
{

	public static void main(String args[]) throws Exception
	{
		new BigFileCutterAndJoiner();
	}
	
	
	BigFileCutterAndJoiner() throws Exception
	{
		BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
		
		System.out.println("Enter the option ( Cutter for C and Joiner for J ");
		
		String bigFilePath = br.readLine();

		if(bigFilePath.equals("C"))
		{
			Cutter();
		}
		else if(bigFilePath.equals("J"))
		{
			Joiner();
		}
		else
		{
			System.out.println("Wrong option entered.");
		}
	}

	void Joiner() throws Exception
	{
		BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
		
		System.out.println("Enter file part preFix path ");
		String bigFilePath = br.readLine().trim();
		
		System.out.println("Enter number of parts to be devided ");
		int numParts = Integer.parseInt(br.readLine().trim());
			
		//int numParts = 4;
		//String bigFilePath = "bigSizeFile.zip";

		DataOutputStream dos = new DataOutputStream(new FileOutputStream(bigFilePath));
		
		for(int i=0; i<numParts; i++)
		{
			String partFilePath = bigFilePath+"_"+(i+1);
			int partFileLen = (int)(new File(partFilePath)).length();
			
			byte[] memBytes = new byte[(int)partFileLen];
			
			DataInputStream dis = new DataInputStream(new FileInputStream(partFilePath));
			dis.read(memBytes, 0, partFileLen);
			
			dos.write(memBytes, 0, partFileLen);
			dos.flush();
		}

		dos.close();
	}
	
	void Cutter() throws Exception
	{
		BufferedReader br = new BufferedReader( new InputStreamReader(System.in));
		
		System.out.println("Enter the BIG size file path ");
		String bigFilePath = br.readLine().trim();
		
		System.out.println("Enter number of parts to be joined ");
		int numParts = Integer.parseInt(br.readLine().trim());
		
		long bigFileLength = (new File(bigFilePath)).length();
		
		long eachPartLength = bigFileLength / numParts;
		long lastPartLength = eachPartLength + ( bigFileLength % numParts );
		
		//System.out.println("Length : "+bigFileLength);
		//System.out.println("eachPartLength : "+eachPartLength+", lastPartLength"+lastPartLength);
		
		DataInputStream dis = new DataInputStream(new FileInputStream(bigFilePath));
		
		byte[] memBytes = new byte[(int)lastPartLength];
		
		for(int i=0; i<numParts; i++)
		{
			int readSize = (int)eachPartLength;
			
			if( i == numParts-1)
				readSize = (int)lastPartLength;
			
			dis.read(memBytes, 0, readSize);

			String partFilePath = bigFilePath+"_"+(i+1);
				
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(partFilePath));
			dos.write(memBytes, 0, readSize);
			dos.flush();
			dos.close();
		}	
	}
}
