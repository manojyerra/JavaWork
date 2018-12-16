//javax.mail
// this code sends mail using gmail smtp
//you may need javax.mail api to implement this code

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
 
import java.io.*;
 
class SendEMail
{
	public static void main(String[] args)
	{
		try
		{
			//BufferedReader br = new BufferedReader(new FileReader("List.txt"));
		
			String host = "smtp.gmail.com";
			String from = "pandagameshub@gmail.com";		//your gmail account
			String pass = "arjun@123";				//your password of gmail account
			Properties props = System.getProperties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.user", from);
			props.put("mail.smtp.password", pass);
			props.put("mail.smtp.port", "587");
			props.put("mail.smtp.auth", "true");

			String line = "";
			int count = 0;
			int failCount = 0;
			int successCount = 0;
			
			//while( (line = br.readLine()) != null )
			{								
				line = "manojyerra@gmail.com";
				
				String[] to = {line};
				
				System.out.print("\n"+(count+1)+") "+line+" ");

				try
				{
					Session session = Session.getDefaultInstance(props, null);
					MimeMessage message = new MimeMessage(session);
					message.setFrom(new InternetAddress(from));

					InternetAddress[] toAddress = new InternetAddress[to.length];

					for( int i=0; i < to.length; i++ ) {
						toAddress[i] = new InternetAddress(to[i]);
					}
					//System.out.println(Message.RecipientType.TO);

					for( int i=0; i < toAddress.length; i++) {
						message.addRecipient(Message.RecipientType.TO, toAddress[i]);
					}
					message.setSubject("This is subject2");//set subject of mail
					message.setText("This is text");//set mail content
					Transport transport = session.getTransport("smtp");
					transport.connect(host, from, pass);
					transport.sendMessage(message, message.getAllRecipients());
					transport.close();
					
					successCount++;
					System.out.print(" ...Sent, Success count = "+successCount);

					Thread.sleep(3000);
				}
				catch(Exception e)
				{
					System.out.print(" ...Failed");
					e.printStackTrace();
					Thread.sleep(7000);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}   
	}        
}



/*

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

class SendEMail
{
    public static void main(String [] args)
    {
        // Recipient's email ID needs to be mentioned.
        String to = "manuyerra@gmail.com";
        
        // Sender's email ID needs to be mentioned
        String from = "manojyerra@gmail.com";
        
        // Assuming you are sending email from localhost
        String host = "localhost";
        
        // Get system properties
        Properties properties = System.getProperties();
        
        // Setup mail server
        properties.setProperty("mail.smtp.host", host);
        
        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);
        
        try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);
            
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));
            
            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                                 new InternetAddress(to));
            
            // Set Subject: header field
            message.setSubject("This is the Subject Line!");
            
            // Now set the actual message
            message.setText("This is actual message");
            
            // Send message
            Transport.send(message);
            System.out.println("Sent message successfully....");
        }catch (Exception mex) {
            mex.printStackTrace();
        }
    }
}
*/