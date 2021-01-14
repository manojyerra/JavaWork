import java.awt.Component;
import java.awt.Container;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.util.*;

class FileData implements Serializable
{
    public byte[] data = null;
    public String name = "";
}

class FileTransfer implements Transferable
{
    public FileData fileData = new FileData();
    public DataFlavor fileDataFlavor = new DataFlavor(FileData.class, "File_Data_Flavot");

    FileData(String filePath)
    {
        try
        {
            File file = new File(filePath);
            fileData.data = new byte[(int)file.length()];

            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
            inputStream.read(fileData.data, 0, (int)file.length());
            fileData.name = file.getName();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

	@Override
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException 
	{
		return fileData;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() 
	{
		return new DataFlavor[] { fileDataFlavor };
	}

	@Override
    public boolean isDataFlavorSupported(DataFlavor flavor) 
    {
		return true;
	}
}

/*

public static class MyObjectSelection implements Transferable, ClipboardOwner {

    private static DataFlavor dmselFlavor = new DataFlavor(MyObject.class, "Test data flavor");
    private MyObject selection;



    public MyObjectSelection(MyObject selection){
       this.selection = selection;
    }


    // Transferable implementation

    @Override
    public DataFlavor[] getTransferDataFlavors(){
       System.out.println("getTransferDataFlavors");
       DataFlavor[] ret = {dmselFlavor};
       return ret;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor){
       return dmselFlavor.equals(flavor);
    }

    @Override
    public synchronized Object getTransferData (DataFlavor flavor)
       throws UnsupportedFlavorException 
    {
       if (isDataFlavorSupported(flavor)){
          return this.selection;
       } else {
          throw new UnsupportedFlavorException(dmselFlavor);
       }
    }



    // ClipboardOwner implementation

    @Override
    public void lostOwnership(Clipboard clipboard, Transferable transferable){
       System.out.println("MyObjectSelection: Lost ownership");
    }

 }
 */