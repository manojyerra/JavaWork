import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.datatransfer.*;
//import javax.activation.DataHandler;
//import javax.activation.FileDataSource;

class ClipboardUtils extends JFrame implements ActionListener
{
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

		if(com == download && uploadFilePath != null && downloadLoc != null)
		{
			try
			{
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				Transferable transferable = clipboard.getContents( null );
				//Data dataObj = (Data)transferable;
				//DataFlavor[] dataFlavors = transferable.getTransferDataFlavors();

				//if(dataFlavors != null && dataFlavors.length > 0)
				{
					//String mimeType = dataFlavors[0].getMimeType();
					//System.out.println("MimeType:"+mimeType);

 					//byte[] data = (byte[]) clipboard.getData(dataFlavors[0]);

					transferable.getTransferData(null);
					byte[] data = globalData;

					int nameLen=0;
					for(; nameLen<NAME_MAX_LEN; nameLen++)
					{
						if(data[nameLen] == '\0')
							break;
					}

					byte[] nameBytes = new byte[nameLen];
					for(int i=0; i<nameLen; i++)
						nameBytes[i] = data[i];

					String name = new String(nameBytes); 
					String saveLoc = downloadLoc+"/"+name;
					FileOutputStream outStream = new FileOutputStream(saveLoc);
					outStream.write(data, NAME_MAX_LEN, data.length-NAME_MAX_LEN);
					outStream.flush();
					outStream.close();

					DisplayMessage("Downloaded at: "+saveLoc);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				DisplayMessage("Error: "+e.getMessage());
			}
		}
	}

	void CopyFileToClipboard(String filePath)
	{
		try
		{
			setTitle("Reading file contents...");
			DisplayMessage("Reading file contents...");

			File file = new File(filePath);
			BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));

			byte[] nameBytes = new byte[NAME_MAX_LEN];
			byte[] fileBytes = new byte[(int)file.length()];
			byte[] finalBytes = new byte[nameBytes.length + fileBytes.length];

			inputStream.read(fileBytes, 0, (int)file.length());

			byte[] strCharArr = file.getName().getBytes();

			for(int i=0; i<nameBytes.length; i++)
			{
				nameBytes[i] = (i < strCharArr.length) ? strCharArr[i] : (byte)'\0';
				finalBytes[i] = nameBytes[i];
			}

			for(int i=0; i<fileBytes.length; i++)
			{
				finalBytes[nameBytes.length + i] = fileBytes[i];
			}

			setTitle("Copying file contents to clipboard...");

			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			//DataHandler dataHandler = new DataHandler(finalBytes, "application/octet-stream");

			clipboard.setContents(new Data(finalBytes), null);

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

class Data implements Transferable 
{
	public byte[] data;

	Data(byte[] data) 
	{
		this.data = data;
	}

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException 
	{
		System.out.println("getTransferData called:"+data.length);
		ClipboardUtils.globalData = data;
		return null;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() 
	{
		return new DataFlavor[] { };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return true;
	}
}

/*
			//Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			//DataHandler dataHandler = new DataHandler(new FileDataSource(filePath));
			//clipboard.setContents(dataHandler, null);

			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

			File file = new File(filePath);
			byte[] fileBytes = new byte[(int)file.length()];
			BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
			inputStream.read(fileBytes, 0, (int)file.length());
			DataHandler dataHandler = new DataHandler(fileBytes, "application/octet-stream");
			clipboard.setContents(dataHandler, null);


			//writting...
			DataHandler dataHandler = (DataHandler)Toolkit.getDefaultToolkit().getSystemClipboard().getContents( null );
			String saveLoc = downloadLoc+"/temp.txt";
			FileOutputStream outStream = new FileOutputStream(saveLoc);
			dataHandler.writeTo(outStream);

			FileInputStream inStream = (FileInputStream)transferable.getTransferData(dataFlavors[0]);
			String saveLoc = downloadLoc+"/temp.txt";
			FileOutputStream outStream = new FileOutputStream(saveLoc);

			byte[] buffer = new byte[1024*1024];
			int bytesRead = -1;

			while ((bytesRead = inStream.read(buffer)) != -1) 
			{
				outStream.write(buffer, 0, bytesRead);
			}			
*/