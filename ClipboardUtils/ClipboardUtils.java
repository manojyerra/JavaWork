import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.datatransfer.*;

class ClipboardUtils extends JFrame implements ActionListener
{
	DataFlavor fileDataFlavor = new DataFlavor(FileData.class, "File_Data_Flavot");
	ArrayList<JComponent> comList = new ArrayList<JComponent>();
	JButton dragDropBtn = new JButton("Drag & Drop");
	JButton download = new JButton("Download");
	JButton update = new JButton("Update");
	JLabel msg = new JLabel("Status...");

	String uploadFilePath = null;
	String downloadLoc = null;
	static final int NAME_MAX_LEN = 256;
	static byte[] globalData = null;

	public static void main(String[] args)
	{
		new ClipboardUtils();
	}

	ClipboardUtils()
	{
		setTitle("File transfer");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setBounds(200,200,500,500);
		setVisible(true);
		AddComponents();
		update.addActionListener(this);
		download.addActionListener(this);

		ListenUpload();
		ListenDownloadDir();
	}

	public void actionPerformed(ActionEvent ae)
	{
		Component com = (Component)ae.getSource();

		if(com == download && downloadLoc != null)
		{
			try
			{
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				DataWrap dataWrap = (DataWrap)clipboard.getData(fileDataFlavor);

				String saveLoc = downloadLoc+"/"+dataWrap.fileName;
				FileOutputStream outStream = new FileOutputStream(saveLoc);
				outStream.write(dataWrap.fileBytes, 0, dataWrap.fileBytes.length);
				outStream.flush();
				outStream.close();

				DisplayMessage("Downloaded at: "+saveLoc);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				DisplayMessage("Errorrr: "+e.getMessage());
			}
		}
	}

	void CopyFileToClipboard(String filePath)
	{
		try
		{
			setTitle("Reading file contents...");
			DisplayMessage("Reading file contents...");

			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(new FileData(filePath), null);

			DisplayMessage("End: Copied file contents to clipboard...");
			setTitle("File transfer");
		}
		catch(Exception e)
		{
			DisplayMessage("Error: "+e.getMessage());
		}
	}

	void DisplayMessage(String str)
	{
		msg.setText(str);
		System.out.println(str);
	}

	void ListenUpload()
	{
		new FileDrop(dragDropBtn, new FileDrop.Listener()
		{
			public void  filesDropped(java.io.File[] files)
			{
				if(files != null && files.length == 1)
				{
					if(!files[0].isDirectory())
					{
						uploadFilePath = files[0].getPath();
						DisplayMessage("Upload File Path: "+uploadFilePath);
						CopyFileToClipboard(uploadFilePath);
					}
				}
			}
		});
	}

	void ListenDownloadDir()
	{
		new FileDrop(download, new FileDrop.Listener()
		{
			public void filesDropped(java.io.File[] files)
			{
				if(files != null && files.length == 1)
				{
					downloadLoc = files[0].isDirectory() ? files[0].getPath() : files[0].getParent();
					DisplayMessage("Download Location: "+downloadLoc);
				}
			}
		});
	}

	void AddComponents()
	{
		comList.add(dragDropBtn);
		comList.add(download);
		comList.add(update);
		comList.add(msg);

		int x = 0;
		int y = 0;
		int w = getWidth();
		int h = getHeight() / comList.size();

		for(int i=0; i<comList.size(); i++)
		{
			Component com = comList.get(i);
			com.setBounds(x, y, w, h);
			add(com);
			y += h;
		}
	}	
}
