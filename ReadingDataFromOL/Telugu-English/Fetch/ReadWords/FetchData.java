import java.io.*;
import java.net.*;
import java.util.*;

/*
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Store;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
*/

public class FetchData
{
	Vector<String> linesVec = new Vector<String>();
	//Transport transport;
	
	public static void main(String[] args) throws Exception
	{
		new FetchData();
	}
		
	FetchData() throws Exception
	{	
		try
		{
			//Login("gamequeengamequeen@gmail.com", "tingting");
		
			BufferedReader br = new BufferedReader(new FileReader("Links_all.snv"));
			
			int skipNumLinks = Integer.parseInt((new BufferedReader(new FileReader("LinkCount.snv"))).readLine());
			
			for(int i=0;i<skipNumLinks; i++)
				br.readLine();
			
			int linkCount = skipNumLinks;
			String line = "";
			while( (line = br.readLine()) != null)
			{
				ReadLink(line);
				
				Vector<String> linkVec = ReadWords(linesVec, "href=\"/store/apps/details?id=", "\" aria-hidden=", 100);		//https://play.google.com/store/apps/details?id=com.icenta.sudoku.ui
				Vector<String> peopleVec = ReadWords(linesVec, "href=\"/store/people/details?id=", "\"", 50);				//https://play.google.com/store/people/details?id=104064383023300887873
				Vector<String> mailVec = ReadWords(linesVec, "href=\"mailto:", "\"", 0);

				///////////////
				/*
				String gPlusLink = "https://play.google.com/store/people/details?id="+peopleVec.get(0);
				
				ReadLink(gPlusLink);
				
				BufferedWriter bwTemp = new BufferedWriter(new FileWriter("Temp.js"));

				for(int i=0;i<linesVec.size();i++)
				{
					bwTemp.write(linesVec.get(i), 0, linesVec.get(i).length());
					bwTemp.newLine();
					bwTemp.flush();
				}
				
				bwTemp.close();
				*/
				////////////////////////

				AppendIfNotExist(linkVec, "Links_all.snv", "https://play.google.com/store/apps/details?id=", "");
				AppendIfNotExist(peopleVec, "People.snv", "https://play.google.com/store/people/details?id=", "");
				AppendIfNotExist(mailVec, "MailID.snv", "", "");
				
				linkCount++;
				
				BufferedWriter bw = new BufferedWriter(new FileWriter("LinkCount.snv"));
				String linkCountStr = ""+linkCount;
				bw.write(linkCountStr, 0, linkCountStr.length());
				bw.flush();
				bw.close();
				
				System.out.print(linkCount+" ) ");
			}
		}
		catch(Exception e){e.printStackTrace();}
		
		//Logout();
	}
	
	void ReadLink(String link) throws Exception
	{
		try
		{
			URL url = new URL(link);
			
			System.out.print("Going to open link -> ");
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
			System.out.print(" Done.  ");

			linesVec.clear();
			
			System.out.print(" Going to read -> ");

			String lineData = "";
			while((lineData = br.readLine()) != null)
				linesVec.add(lineData);

			System.out.print(" Done. \n\n");
			
			br.close();
		}
		catch(Exception e)
		{
			System.out.println("ERRORRRRRRRRRRRRRR...........");
			e.printStackTrace();
		}
	}
	
	Vector<String> ReadWords(Vector<String> linesVec, String startStr, String endStr, int maxLen) throws Exception
	{	
		Vector<String> wordsVec = new Vector<String>();
		
		for(int i = 0; i < linesVec.size(); i++)
        {
			String line = linesVec.get(i);
			        	
        	do
			{
	    		int startIndex = line.indexOf(startStr);
				
				if(startIndex == -1)
					break;
	    		
				line = line.substring(startIndex + startStr.length(), line.length());
				int endIndex = line.indexOf(endStr);
				
				if(endIndex == -1)
					break;
				
				String word = line.substring(0, endIndex);

				boolean alreadyExist = false;
				for(int loop=0; loop<wordsVec.size(); loop++)
				{
					if(wordsVec.get(loop).equals(word))
					{
						alreadyExist = true;
						break;
					}
				}
				
				if(alreadyExist == false)
				{
					if( (maxLen > 0 && word.length() < maxLen) || maxLen <= 0)
					{			
						wordsVec.add( word );
						line = line.substring(endIndex + endStr.length(), line.length());
					}
				}
        	}
			while(true);
        }
		
		//for(int i=0;i<wordsVec.size();i++)
		//{
		//	System.out.println("Word : <"+wordsVec.get(i)+">");
			
		//	output.write(wordsVec.get(i), 0, wordsVec.get(i).length());
		//	output.newLine();
		//	output.flush();
		//}
		
		return wordsVec;
	}
	
	void AppendIfNotExist(Vector<String> vec, String filePath, String prefix, String suffix) throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		
		String line = "";
		
		while( (line = br.readLine()) != null )
		{
			for(int i=0;i<vec.size();i++)
			{
				String oriStr = prefix +""+ vec.get(i) +""+ suffix;
				if(oriStr.equals(line))
					vec.remove(i);
			}
		}
		
		br.close();
		
		if(vec.size() > 0)
		{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));
			for(int i=0;i<vec.size();i++)
			{
				String oriStr = prefix +""+ vec.get(i) +""+ suffix;
				bw.write(oriStr, 0, oriStr.length());
				bw.newLine();
				bw.flush();
			}
			bw.close();
		}
	}
	
	/*
	void Login(String mailID, String password)
	{
		try
		{
			String host = "smtp.gmail.com";
			String from = mailID;				//your gmail account
			String pass = password;				//your password of gmail account
			Properties props = System.getProperties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.user", from);
			props.put("mail.smtp.password", pass);
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");

			Session session = Session.getDefaultInstance(props, null);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));

			// receiver mail address can be other then gmail
			//String[] to = {"manuyerra@gmail.com"};			
			//InternetAddress[] toAddress = new InternetAddress[to.length];

			//for( int i=0; i < to.length; i++ )
			//	toAddress[i] = new InternetAddress(to[i]);

			//System.out.println(Message.RecipientType.TO);

			//for( int i=0; i < toAddress.length; i++)
			//	message.addRecipient(Message.RecipientType.TO, toAddress[i]);

			//message.setSubject("Java Test Mail!!!");//set subject of mail
			//message.setText("This is mail from Java Program");//set mail content
			
			transport = session.getTransport("smtp");
			transport.connect(host, from, pass);
			//transport.sendMessage(message, message.getAllRecipients());
			
			System.out.println("Logged in ...");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	void Logout()
	{
		try{
			transport.close();
		}catch(Exception e){e.printStackTrace();}
	}
	*/
}



