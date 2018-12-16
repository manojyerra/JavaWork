import java.awt.Frame;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Cursor;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.awt.event.WindowListener;
import java.awt.event.ComponentListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.ComponentEvent;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.DropTargetEvent;

import java.awt.dnd.DropTarget;
import java.awt.dnd.DnDConstants;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import java.util.Vector;
import java.util.List;
import java.io.File;
import javax.imageio.ImageIO;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.BufferedInputStream;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.net.URL;
import java.io.*;
import java.nio.*;

import javax.swing.JOptionPane;

//Sorting need to be fix
//Need to support .jpg images.
//Need to fix "fit in screen" bug.
//Need to support alpha channel.
//Add Tooltips for buttons with description.

class ImageDiffViewer implements WindowListener, ComponentListener, ActionListener, KeyListener, DropTargetListener, MouseWheelListener, MouseListener
{
	Frame frame = null;
	ImageCanvas imgCanvas = null;
	Button decreseSize = null;
	Button increseSize = null;
	Button fitInScreen = null;
	Button originalSize = null;
	Button nextImg = null;
	Button prevImg = null;
	Button goToNextNonBaseLineImg = null;
	Button goToNextType = null;
	Button bgColor = null;
	int _hudHeight = 0;
	boolean _keyPressed = false;
	boolean _showInOriginalSize = false;

	File selectedDir;
	long lastImgChangeTime = 0;
	boolean shiftKeyPressed = false;
	int bgColorChangeCount = 0;
	
	Vector<String> imgNamesVec = new Vector<String>();
	int index = 0;
	
	Button makeRunAsBase = null;	
	String baseLineFolder = "Re_BaseLinedImages";
	
    public static void main(String[] argS)
	{
		new ImageDiffViewer();  
    }

    public ImageDiffViewer()
	{
		selectedDir = new File("Selected");
		AddFilesOfFolder(System.getProperty("user.dir"), null);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame = new Frame("Image Viewer");
		frame.setBounds(0,0,(int)screenSize.getWidth(), (int)screenSize.getHeight() - GetScreenInsets().bottom);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setLayout(null);
		frame.setBackground(Color.black);
		frame.addComponentListener(this);
		frame.addWindowListener(this);
		frame.addMouseWheelListener(this);
		frame.addKeyListener(this);		
		new DropTarget(frame, this);
		frame.setBackground(Color.white);
		frame.validate();
		
		if(imgNamesVec.size() == 0)
			frame.setTitle("No images found - Image Viewer");

		if(imgNamesVec.size() > 0)
		{
			File f = new File(baseLineFolder);
			f.mkdir();
			CreateCanvas( imgNamesVec.get(0) );
		}
		
		Insets in = frame.getInsets();

		int totBtns = 10;

		int btnW = frame.getWidth() / totBtns;
		int btnH = _hudHeight;
		int btnY = frame.getHeight()-btnH-in.bottom+2;
		
		int totBtnsWidth = totBtns * btnW;
		int btnsStartX = (imgCanvas.getWidth() - totBtnsWidth)/2;
		
		increseSize = new Button(" Increse Size ");
		increseSize.setBounds(btnsStartX + btnW*0,btnY, btnW,btnH);
		increseSize.addActionListener(this);

		decreseSize = new Button(" Decrese Size ");
		decreseSize.setBounds(btnsStartX + btnW*1,btnY, btnW,btnH);
		decreseSize.addActionListener(this);
		
		prevImg = new Button(" <- ");
		prevImg.setBounds(btnsStartX + btnW*2,btnY, btnW,btnH);
		prevImg.addActionListener(this);
        prevImg.addMouseListener(this);

		nextImg = new Button(" -> ");
		nextImg.setBounds(btnsStartX + btnW*3,btnY, btnW,btnH);
		nextImg.addActionListener(this);
		nextImg.addMouseListener(this);
		
		goToNextNonBaseLineImg = new Button("Goto Next Non-BaseLine");
		goToNextNonBaseLineImg.setBounds(btnsStartX + btnW*4,btnY, btnW,btnH);
		goToNextNonBaseLineImg.addActionListener(this);
		
		goToNextType = new Button("Goto Next Type");
		goToNextType.setBounds(btnsStartX + btnW*5,btnY, btnW,btnH);
		goToNextType.addActionListener(this);
        
		makeRunAsBase = new Button("Make Run Img As Base");
		makeRunAsBase.setBounds(btnsStartX + btnW*6,btnY, btnW,btnH);
		makeRunAsBase.addActionListener(this);
		
		fitInScreen = new Button("Fit In Screen");
		fitInScreen.setBounds(btnsStartX + btnW*7,btnY, btnW,btnH);
		fitInScreen.addActionListener(this);

		originalSize = new Button("Set to Original Size");
		originalSize.setBounds(btnsStartX + btnW*8,btnY, btnW,btnH);
		originalSize.addActionListener(this);
		
		bgColor = new Button("Bg Color");
		bgColor.setBounds(btnsStartX + btnW*9,btnY, btnW,btnH);
		bgColor.addActionListener(this);
		
		frame.add(increseSize);
		frame.add(decreseSize);
		frame.add(prevImg);
		frame.add(nextImg);
		frame.add(fitInScreen);
		frame.add(originalSize);
		frame.add(bgColor);
		frame.add(imgCanvas);
		frame.add(makeRunAsBase);
		frame.add(goToNextNonBaseLineImg);
		frame.add(goToNextType);

		increseSize.setFocusable(false);
		decreseSize.setFocusable(false);
		prevImg.setFocusable(false);
		nextImg.setFocusable(false);
		fitInScreen.setFocusable(false);
		originalSize.setFocusable(false);
		bgColor.setFocusable(false);
		imgCanvas.setFocusable(false);
		makeRunAsBase.setFocusable(false);
		goToNextNonBaseLineImg.setFocusable(false);
		goToNextType.setFocusable(false);
	}
	
	public void AddFilesOfFolder(String folderPath, File indexFile)
	{
		File[] files = (new File(folderPath)).listFiles();
		String indexFilePath = "";
		
		if(indexFile != null)
			indexFilePath = indexFile.getAbsolutePath();
		
		imgNamesVec.removeAllElements();
		index = 0;

		for(int i=0;i<files.length;i++)
		{
			String filePath = files[i].getAbsolutePath();
			
			if(filePath.toLowerCase().endsWith("_diff.png") || filePath.toLowerCase().endsWith("_diff.tga"))
			{
				imgNamesVec.add(filePath);
				
				if(indexFile != null && indexFilePath.equals(filePath))
					index = imgNamesVec.size()-1;
			}
		}
		
		/*
		int size = imgNamesVec.size();
		
		for(int i=0;i<size;i++)
		{
			for(int j=i+1;j<size;j++)
			{
				String t1 = imgNamesVec.get(i);
				String t2 = imgNamesVec.get(j);
				
				String s1 = t1.replace("0","");
				String s2 = t2.replace("0","");
				
				if(s1.compareTo(s2) >= 0)
				{
					imgNamesVec.set(i, t2);
					imgNamesVec.set(j, t1);					
				}
			}
		}

		for(int i=0;i<size;i++)
			System.out.println(imgNamesVec.get(i));
		*/
	}	
	
	public void CreateCanvas(String imgPath)
	{
		if(imgPath != null)
		{
			Insets in = frame.getInsets();
			_hudHeight = (int)(frame.getHeight()*0.05f);
			
			if(imgCanvas != null)
			{
				frame.remove(imgCanvas);
				imgCanvas = null;
			}
			
			imgCanvas = new ImageCanvas(imgPath, in.left, in.top, frame.getWidth()-in.left-in.right, frame.getHeight()- in.top- in.bottom - _hudHeight);
			SetBgColorToCanvas();
			imgCanvas.setFocusable(false);
			frame.add(imgCanvas);
			imgCanvas.repaint();
			_showInOriginalSize = false;
            
			imgPath = (new File(imgPath)).getName();
			
            if(IsBaseLined(imgPath) == false)
				frame.setTitle(imgPath+" - Image Viewer");
			else
                frame.setTitle("******* ( Base Lined ) *******   "+imgPath+" - Image Viewer");
		}
	}
	
	boolean IsBaseLined(String imgPath)
	{
		String runImgPath = imgPath.replace("_diff", "");
		String name = (new File(runImgPath)).getName();
		
		return (new File(baseLineFolder+"/"+name)).exists();
	}
	
	String GetNextImagePath()
	{
		if(imgNamesVec.size() > 0)
		{
			index++;		
			if(index >= imgNamesVec.size())
			{
				JOptionPane.showMessageDialog(null, "Reached end of files");
				index = 0;
			}
			else if(index < 0)
				index = imgNamesVec.size()-1;

			return imgNamesVec.get(index);
		}
		return null;
	}
	
	String GetPrevImagePath()
	{
		if(imgNamesVec.size() > 0)
		{
			index--;		
			if(index >= imgNamesVec.size())
				index = 0;
			else if(index < 0)
				index = imgNamesVec.size()-1;

			return imgNamesVec.get(index);
		}
		return null;
	}
	
	public Insets GetScreenInsets()
	{
		return Toolkit.getDefaultToolkit().getScreenInsets(frame.getGraphicsConfiguration());
	}
	
	public void actionPerformed(ActionEvent e)
	{
		try
		{	
			Object obj = e.getSource();
			
			if(obj == decreseSize)
			{
				imgCanvas.DecreaseSize();
				imgCanvas.repaint();
			}
			else if(obj == increseSize)
			{
				imgCanvas.IncreaseSize();
				imgCanvas.repaint();
			}
			else if(obj == fitInScreen)
			{
				imgCanvas.FitImageInCanvas();
				imgCanvas.repaint();
			}
			else if(obj == originalSize)
			{
				imgCanvas.SetToOriginalSize();
				imgCanvas.repaint();
			}
			else if(obj == nextImg)
			{
				CreateCanvas(GetNextImagePath());
				imgCanvas.repaint();
			}
			else if(obj == goToNextNonBaseLineImg)
			{
				String nextImg = "";
				
				int totImgs = imgNamesVec.size();
				int count = 0;
				
				do
				{
					count++;
					nextImg = GetNextImagePath();
				}while(IsBaseLined(nextImg) && count <= totImgs);
				
				if(count <= totImgs)
				{
					CreateCanvas(nextImg);
					imgCanvas.repaint();
				}
			}
			else if(obj == goToNextType)
			{
				String currPathNoDigits = RemoveDigits( imgNamesVec.get(index) );
				
				String nextImg = GetNextImagePath();
				int totImgsCount = imgNamesVec.size();
				int count = 0;
				
				while(RemoveDigits(nextImg).equals(currPathNoDigits) && count <= totImgsCount)
				{
					count++;
					nextImg = GetNextImagePath();
				}
				
				if(count <= totImgsCount)
				{
					CreateCanvas(nextImg);
					imgCanvas.repaint();
				}				
			}
			else if(obj == prevImg)
			{
				CreateCanvas(GetPrevImagePath());
				imgCanvas.repaint();
			}		
			else if(obj == makeRunAsBase)
			{
                MakeCurrentAsBaseImg();
			}
			else if(obj == bgColor)
			{
				ChangeBgColor();
			}
			
		}catch(Exception exce){exce.printStackTrace();}
	}
    
	
	public String RemoveDigits(String str)
	{
		int len = str.length();
		String returnStr = "";
		
		for(int i=0;i<len;i++)
		{
			int ascii = (int)(str.charAt(i));
			
			if( (ascii >= (int)'0' && ascii <= (int)'9') == false)
				returnStr += ""+(char)ascii;
		}
		
		return returnStr;
	}
	
    public void MakeCurrentAsBaseImg()
    {
        String currImgPath = imgNamesVec.get(index);
        
        if(IsBaseLined(currImgPath) == false)
        {
            String diffStr = "_diff";
            int diffIndex = currImgPath.lastIndexOf(diffStr);
            
            String runImgPath = currImgPath.substring(0,diffIndex) +""+currImgPath.substring(diffIndex+diffStr.length(), currImgPath.length());
            
            //String runImgPath = currImgPath.replace("_diff", "");
            PasteFile(runImgPath, baseLineFolder+"/"+ (new File(runImgPath)).getName());
			
            if(IsBaseLined(currImgPath))
            {
                String fileName = (new File(currImgPath)).getName();
                frame.setTitle("******* ( Base Lined ) *******   "+fileName+" - Image Viewer");
                System.out.println(runImgPath+" has Base lined...");
            }
        }
    }
    
	public void ChangeBgColor()
	{
		IncrementColor();	
		SetBgColorToCanvas();
	}	

	public void SetBgColorToCanvas()
	{
		if(bgColorChangeCount == 0)
		{
			frame.setBackground(new Color(128, 128, 128));
			imgCanvas.setBackground(new Color(128, 128, 128));
		}
		else if(bgColorChangeCount == 1)
		{
			frame.setBackground(new Color(240, 240, 240));
			imgCanvas.setBackground(new Color(240, 240, 240));
		}
		else if(bgColorChangeCount == 2)
		{
			frame.setBackground(new Color(243, 248, 255));
			imgCanvas.setBackground(new Color(243, 248, 255));
		}		
		else if(bgColorChangeCount == 3)
		{
			frame.setBackground(Color.black);
			imgCanvas.setBackground(Color.black);		
		}
		if(bgColorChangeCount == 4)
		{
			frame.setBackground(Color.white);
			imgCanvas.setBackground(Color.white);
		}		
	}
	
	public void IncrementColor()
	{
		bgColorChangeCount++;
		if(bgColorChangeCount == 5)
			bgColorChangeCount = 0;		
	}
	
    public void mouseClicked(MouseEvent e){}
    public void mousePressed(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseReleased(MouseEvent e)
    {
        Object obj = e.getSource();
        
        if(e.getButton() == e.BUTTON3)
        {
            if(obj == nextImg)
            {
                MakeCurrentAsBaseImg();
				//CreateCanvas(GetNextImagePath());
				//imgCanvas.repaint();
            }
            else if(obj == prevImg)
            {
                MakeCurrentAsBaseImg();
				//CreateCanvas(GetPrevImagePath());
				//imgCanvas.repaint();
            }
        }
    }
    
    
	public boolean PasteFile(String source, String destin)
	{
		try
		{
			File file = new File(source);
			int fileLen = (int) file.length();
			
			byte[] fileBytes =new byte[fileLen];
			DataInputStream dis = new DataInputStream(new FileInputStream(source));
			dis.readFully(fileBytes);
			dis.close();
			
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(destin));
			dos.write(fileBytes, 0, fileBytes.length);
			dos.close();
			return true;
		}
		catch(Exception e){e.printStackTrace();}
		return false;
	}
	
	public void keyTyped(java.awt.event.KeyEvent e){}
	public void keyPressed(java.awt.event.KeyEvent e)
	{
		_keyPressed = true;
		
		if(e.getKeyCode() == e.VK_SHIFT)
			shiftKeyPressed = true;
		
		if(shiftKeyPressed == true && ( lastImgChangeTime ==0 || (System.currentTimeMillis() - lastImgChangeTime) > 150 ))
		{
			if(e.getKeyCode() == e.VK_RIGHT || e.getKeyCode() == e.VK_DOWN)
			{
				CreateCanvas(GetNextImagePath());
				imgCanvas.repaint();			
			}
			else if(e.getKeyCode() == e.VK_LEFT || e.getKeyCode() == e.VK_UP)
			{
				CreateCanvas(GetPrevImagePath());
				imgCanvas.repaint();
			}
			
			lastImgChangeTime = System.currentTimeMillis();
		}
	}

	public void keyReleased(java.awt.event.KeyEvent e)
	{
		lastImgChangeTime = 0;
		shiftKeyPressed = false;
		
		if(_keyPressed == true)
		{
			_keyPressed = false;

			if(e.getKeyCode() == e.VK_D)
			{
				index += 5;
				CreateCanvas(GetNextImagePath());
				imgCanvas.repaint();				
			}
			else if(e.getKeyCode() == e.VK_A)
			{
				index -= 5;
				CreateCanvas(GetPrevImagePath());
				imgCanvas.repaint();				
			}
			else if(e.getKeyCode() == e.VK_RIGHT || e.getKeyCode() == e.VK_DOWN)
			{
				CreateCanvas(GetNextImagePath());
				imgCanvas.repaint();			
			}
			else if(e.getKeyCode() == e.VK_LEFT || e.getKeyCode() == e.VK_UP)
			{
				CreateCanvas(GetPrevImagePath());
				imgCanvas.repaint();
			}
			else if(e.getKeyCode() == e.VK_ENTER)
			{
				_showInOriginalSize = !_showInOriginalSize;

				if(_showInOriginalSize)
					imgCanvas.SetToOriginalSize();
				else
					imgCanvas.FitImageInCanvas();

				imgCanvas.repaint();
			}
			else if(e.getKeyCode() == e.VK_CONTROL)
			{
				MakeCurrentAsBaseImg();
				CreateCanvas(GetNextImagePath());
				imgCanvas.repaint();
			}			
			else if(e.getKeyCode() == e.VK_SHIFT)
			{
                MakeCurrentAsBaseImg();
			}
			else if(e.getKeyCode() == e.VK_S)
			{
                if(selectedDir.exists() == false)
				{
					selectedDir.mkdir();
				}
				
				String currImgPath = imgNamesVec.get(index);
				File currFile = new File(currImgPath);
				File desinFile = new File("Selected/"+currFile.getName());
				boolean success = PasteFile(currImgPath, "Selected/"+desinFile.getName());
				System.out.println("Pasted : "+success+", From "+currImgPath+" to Selected/"+desinFile.getName());
			}			
		}
	}
	
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int scrollRotation = e.getWheelRotation();
		
		if(scrollRotation < 0)
		{
			imgCanvas.IncreaseSize();
			imgCanvas.repaint();			
		}
		else
		{
			imgCanvas.DecreaseSize();
			imgCanvas.repaint();		
		}
	}	
	
	public void dragEnter(DropTargetDragEvent e){}
	public void dragOver(DropTargetDragEvent e){}
	public void dropActionChanged(DropTargetDragEvent e){}
	public void dragExit(DropTargetEvent e){}
	public void drop(DropTargetDropEvent event)
	{
		event.acceptDrop(DnDConstants.ACTION_COPY);
		Transferable transferable = event.getTransferable();
		DataFlavor[] flavors = transferable.getTransferDataFlavors();

		//if(flavors.length == 1)
		{
			for(int i=0;i<flavors.length;i++)
			{
				DataFlavor flavor = flavors[i];
				
				try
				{
					if(flavor.isFlavorJavaFileListType())
					{
						List<File> filesList = (List<File>) transferable.getTransferData(flavor);
						if(filesList.size() == 1)
						{
							String filePath = filesList.get(0).getPath();
							filePath = filePath.toLowerCase();
							if(filePath.endsWith("_diff.png"))
							{
								AddFilesOfFolder(filesList.get(0).getParentFile().getPath(), filesList.get(0));
								CreateCanvas(imgNamesVec.get(index));
                                break;
							}
						}
					}
				}
				catch(Exception e){e.printStackTrace();}
			}
		}
		
		event.dropComplete(true);		
	}
	
    public void componentMoved(ComponentEvent e){}
    public void componentResized(ComponentEvent e){}
	public void componentHidden(ComponentEvent e){}
    public void componentShown(ComponentEvent e){}	

	public void windowOpened(java.awt.event.WindowEvent e){}
	public void windowClosing(java.awt.event.WindowEvent e)
	{
		/*
		int reply = JOptionPane.showConfirmDialog(null, "Do you want to paste Re_BaseLinedImages to \\\\10.20.19.38\\ReplaceBaseImages ", "Quit", JOptionPane.YES_NO_OPTION);
		
		if(reply == JOptionPane.YES_OPTION)
		{
			File f = new File(baseLineFolder);
			File[] allFiles = f.listFiles();
			
			for(int i=0;i<allFiles.length;i++)
			{
				if(allFiles[i].getPath().endsWith(".png") || allFiles[i].getPath().endsWith(".PNG"))
				{
					PasteFile(allFiles[i].getPath(),  "\\\\10.20.19.38\\ReplaceBaseImages\\"+allFiles[i].getName());
					//PasteFile(allFiles[i].getPath(),  "temp\\"+allFiles[i].getName());
				}
			}
			
			JOptionPane.showMessageDialog (null, "Done", "", JOptionPane.INFORMATION_MESSAGE);
		}
		*/

		System.exit(0);
	}
	public void windowClosed(java.awt.event.WindowEvent e){}
	public void windowIconified(java.awt.event.WindowEvent e){}
	public void windowDeiconified(java.awt.event.WindowEvent e){}
	public void windowActivated(java.awt.event.WindowEvent e){}
	public void windowDeactivated(java.awt.event.WindowEvent e){}
}


class ImageCanvas extends Canvas implements MouseListener, MouseMotionListener
{
	private BufferedImage _buffImg = null;
	private Image _scaledImg = null;
	
	private int _x = 0;
	private int _y = 0;
	private int _drawW = 0;
	private int _drawH = 0;
	
	private int _prevMX = 0;
	private int _prevMY = 0;
	
	private int _currMX = 0;
	private int _currMY = 0;
	
	private int _typeOfScale = 0;
	
	public static final int FIT_IN_SCREEN = 1;
	public static final int ORIGINAL_SIZE = 2;
	
	private boolean _isWindows = false;
	
	public ImageCanvas(String imagePath, int x, int y, int w, int h)
	{
		String osName = System.getProperty("os.name");
		if(osName.startsWith("Windows"))
			_isWindows = true;
		
		try
		{
			if(imagePath.endsWith(".tga") == true)
			{
				try{
				_buffImg = TGALoader.loadTGA(imagePath);
				}catch(Exception e){e.printStackTrace();}
			}
			else
				_buffImg = ImageIO.read(new File(imagePath));

			setBounds(x, y, w, h);
			FitImageInCanvas();
			addMouseListener(this);
			addMouseMotionListener(this);
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	public void SetBounds(int x, int y, int w, int h)
	{
		setBounds(x,y,w,h);
	}

	public void FitImageInCanvas()
	{
		int canvasW = getWidth();
		int canvasH = getHeight();
		
		int imgW = _buffImg.getWidth(null);
		int imgH = _buffImg.getHeight(null);
		
		if(imgW > canvasW)
		{
			int backUpImgW = imgW;
			imgW = canvasW;
			imgH = imgW * imgH / backUpImgW;
		}
		
		if(imgH > canvasH)
		{
			int backUpImgH = imgH;	
			imgH = canvasH;
			imgW = imgH * imgW / backUpImgH;
		}
		
		_drawW = imgW;
		_drawH = imgH;
		_x = 0;
		_y = 0;
	}
	
	public void DecreaseSize()
	{
		_drawW -= (int)(_drawW*0.05);
		_drawH -= (int)(_drawH*0.05);		
	}

	public void IncreaseSize()
	{
		_drawW += (int)(_drawW*0.05);
		_drawH += (int)(_drawH*0.05);
	}
	
	public void SetToOriginalSize()
	{
		_x = 0;
		_y = 0;
		_drawW = _buffImg.getWidth(null);
		_drawH = _buffImg.getHeight(null);
	}
	
	public void paint(Graphics g)
	{
		_x += _currMX - _prevMX;
		_y += _currMY - _prevMY;
		
		int canvasW = getWidth();
		int canvasH = getHeight();
		
		if(_x + _drawW < canvasW)	_x = canvasW - _drawW;
		if(_y + _drawH < canvasH)	_y = canvasH - _drawH;
		if(_x > 0)					_x = 0;
		if(_y > 0)					_y = 0;

		if(_isWindows)
		{
			if(_scaledImg == null || _scaledImg.getWidth(null) != _drawW || _scaledImg.getHeight(null) != _drawH)
			{
				g.drawImage(_buffImg, _x, _y, _drawW, _drawH, this);
				_scaledImg = _buffImg.getScaledInstance(_drawW, _drawH, Image.SCALE_SMOOTH);			
			}
			
			g.drawImage(_scaledImg, _x, _y,  this);
		}
		else
		{
			if(_scaledImg == null || _scaledImg.getWidth(null) != _drawW || _scaledImg.getHeight(null) != _drawH)
				_scaledImg = _buffImg.getScaledInstance(_drawW, _drawH, Image.SCALE_SMOOTH);

			g.drawImage(_scaledImg, _x, _y,  this);
		}
	}
	
	public void mouseClicked(java.awt.event.MouseEvent e){}
	public void mousePressed(java.awt.event.MouseEvent e)
	{
		_prevMX = e.getX();
		_prevMY = e.getY();
		
		_currMX = e.getX();
		_currMY = e.getY();
	}
	
	public void mouseReleased(java.awt.event.MouseEvent e)
	{
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	public void mouseEntered(java.awt.event.MouseEvent e){}
	public void mouseExited(java.awt.event.MouseEvent e){}
	public void mouseMoved(java.awt.event.MouseEvent e){}
	public void mouseDragged(java.awt.event.MouseEvent e)
	{
		setCursor(new Cursor(Cursor.MOVE_CURSOR));
		
		_prevMX = _currMX;
		_prevMY = _currMY;
		
		_currMX = e.getX();
		_currMY = e.getY();
		
		repaint();
	}
}



class TGALoader {
  private static final byte[] uTGAcompare = new byte[]{0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0};	// Uncompressed TGA Header
  private static final byte[] cTGAcompare = new byte[]{0, 0, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0};	// Compressed TGA Header

  public static BufferedImage loadTGA(String filename) throws IOException{				// Load a TGA file
   ClassLoader  fileLoader  = TGALoader.class.getClassLoader();
   InputStream  in          = fileLoader.getResourceAsStream( filename);
    byte[]      header = new byte[12];
 
    if(in == null)
      in = new FileInputStream(filename);

     readBuffer(in, header);

     if(Arrays.equals(uTGAcompare, header)){// See if header matches the predefined header of
       return loadUncompressedTGA(in);			    // If so, jump to Uncompressed TGA loading code
     }
     else if (Arrays.equals(cTGAcompare, header)){		// See if header matches the predefined header of
       return loadCompressedTGA( in);					// If so, jump to Compressed TGA loading code
     }
     else{												// If header matches neither type
       in.close();
       throw new IOException("TGA file be type 2 or type 10 ");	// Display an error
     }
  }

  private static void readBuffer(InputStream in, byte[] buffer) throws IOException {
    int bytesRead = 0;
    int bytesToRead = buffer.length;
    while (bytesToRead > 0) {
      int read = in.read(buffer, bytesRead, bytesToRead);
      bytesRead  += read;
      bytesToRead -= read;
    }
  }

  private static BufferedImage loadUncompressedTGA(InputStream in) throws IOException{ // Load an uncompressed TGA (note, much of this code is based on NeHe's
                                                                                       // TGA Loading code nehe.gamedev.net)
    byte[] header = new byte[6];
    readBuffer(in, header);

    int  imageHeight = (unsignedByteToInt(header[3]) << 8) + unsignedByteToInt(header[2]), // Determine The TGA height	(highbyte*256+lowbyte)
         imageWidth  = (unsignedByteToInt(header[1]) << 8) + unsignedByteToInt(header[0]), // Determine The TGA width	(highbyte*256+lowbyte)
         bpp         = unsignedByteToInt(header[4]);                                       // Determine the bits per pixel

    if ((imageWidth  <= 0) ||
        (imageHeight <= 0) ||
        ((bpp != 24) && (bpp!= 32))){ // Make sure all information is valid
      throw new IOException("Invalid texture information");	// Display Error
    }
    int  bytesPerPixel = (bpp / 8),                                   // Compute the number of BYTES per pixel
         imageSize     = (bytesPerPixel * imageWidth * imageHeight);  // Compute the total amout ofmemory needed to store data
    byte imageData[]   = new byte[imageSize];                         // Allocate that much memory

    readBuffer(in, imageData);

    BufferedImage  bufferedImage  = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

    for(int j = 0; j < imageHeight; j++)
      for(int i = 0; i < imageWidth; i++) {
        int  index = ( (imageHeight - 1 -j)* imageWidth + i) * bytesPerPixel,
             value = (    255              & 0xFF) << 24|
                     (imageData[index + 0] & 0xFF) << 16|
                     (imageData[index + 1] & 0xFF) <<  8|
                     (imageData[index + 2] & 0xFF) ;
        bufferedImage.setRGB(i, j,value);
      }
    return bufferedImage;
  }

  private static BufferedImage loadCompressedTGA(InputStream fTGA) throws IOException		// Load COMPRESSED TGAs
  {

    byte[] header = new byte[6];
    readBuffer(fTGA, header);

    int  imageHeight = (unsignedByteToInt(header[3]) << 8) + unsignedByteToInt(header[2]), // Determine The TGA height	(highbyte*256+lowbyte)
         imageWidth  = (unsignedByteToInt(header[1]) << 8) + unsignedByteToInt(header[0]), // Determine The TGA width	(highbyte*256+lowbyte)
         bpp         =  unsignedByteToInt(header[4]);                                       // Determine the bits per pixel

    if ((imageWidth  <= 0) ||
        (imageHeight <= 0) ||
        ((bpp != 24) && (bpp!= 32))){ // Make sure all information is valid
      throw new IOException("Invalid texture information");	// Display Error
    }
    int  bytesPerPixel = (bpp / 8),                                   // Compute the number of BYTES per pixel
         imageSize     = (bytesPerPixel * imageWidth * imageHeight);  // Compute the total amout ofmemory needed to store data
    byte imageData[]   = new byte[imageSize];                         // Allocate that much memory

    int pixelcount     = imageHeight * imageWidth,                    // Nuber of pixels in the image
        currentbyte    = 0,                                           // Current byte
        currentpixel   = 0;                                           // Current pixel being read


	System.out.println("bytesPerPixel = "+bytesPerPixel);

    byte[] colorbuffer = new byte[bytesPerPixel];                     // Storage for 1 pixel

    do {
      int chunkheader = 0;											// Storage for "chunk" header
      try{
        chunkheader = unsignedByteToInt((byte) fTGA.read());
      }
      catch (IOException e) {
        throw new IOException("Could not read RLE header");	// Display Error
      }

      if(chunkheader < 128){                                            // If the ehader is < 128, it means the that is the number of RAW color packets minus 1
        chunkheader++;                                                  // add 1 to get number of following color values
        for(short counter = 0; counter < chunkheader; counter++){       // Read RAW color values
          readBuffer(fTGA, colorbuffer);
          // write to memory
          imageData[currentbyte   ]  = colorbuffer[2];                  // Flip R and B vcolor values around in the process
          imageData[currentbyte + 1] = colorbuffer[1];
          imageData[currentbyte + 2] = colorbuffer[0];

          if(bytesPerPixel == 4)                                        // if its a 32 bpp image
             imageData[currentbyte + 3] = colorbuffer[3];               // copy the 4th byte

          currentbyte += bytesPerPixel;                                 // Increase thecurrent byte by the number of bytes per pixel
          currentpixel++;                                               // Increase current pixel by 1

          if(currentpixel > pixelcount){                                // Make sure we havent read too many pixels
            throw new IOException("Too many pixels read");              // if there is too many... Display an error!
          }
        }
      }
      else{                                                             // chunkheader > 128 RLE data, next color reapeated chunkheader - 127 times
        chunkheader -= 127;                                             // Subteact 127 to get rid of the ID bit
        readBuffer(fTGA, colorbuffer);
        for(short counter = 0; counter < chunkheader; counter++){       // copy the color into the image data as many times as dictated
          imageData[currentbyte    ] = colorbuffer[2];                  // switch R and B bytes areound while copying
          imageData[currentbyte + 1] = colorbuffer[1];
          imageData[currentbyte + 2] = colorbuffer[0];

          if(bytesPerPixel == 4)                                        // If TGA images is 32 bpp
            imageData[currentbyte + 3] = colorbuffer[3];                // Copy 4th byte
            currentbyte +=  bytesPerPixel;                              // Increase current byte by the number of bytes per pixel
            currentpixel++;                                             // Increase pixel count by 1
          if(currentpixel > pixelcount){                                // Make sure we havent written too many pixels
            throw new IOException("Too many pixels read");              // if there is too many... Display an error!
          }
        }
      }
    } while (currentpixel < pixelcount);                                // Loop while there are still pixels left


    BufferedImage  bufferedImage  = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

	if(bytesPerPixel == 4)
	{
    	for(int j = 0; j < imageHeight; j++)
      	for(int i = 0; i < imageWidth; i++) 
      	{
        	int index = ( (imageHeight - 1 -j)* imageWidth + i) * bytesPerPixel;
        
	        int value = (imageData[index + 3] & 0xFF) << 24 | (imageData[index + 0] & 0xFF) << 16| 
        			(imageData[index + 1] & 0xFF) <<  8 | (imageData[index + 2] & 0xFF) ;
                                 
    	    bufferedImage.setRGB(i, j, value);
      }
    }
    else
    {
    	for(int j = 0; j < imageHeight; j++)
      	for(int i = 0; i < imageWidth; i++) 
      	{
        	int index = ( (imageHeight - 1 -j)* imageWidth + i) * bytesPerPixel;
        
	        int value = (255 & 0xFF) << 24 | (imageData[index + 0] & 0xFF) << 16| 
        			(imageData[index + 1] & 0xFF) <<  8 | (imageData[index + 2] & 0xFF) ;
                                 
    	    bufferedImage.setRGB(i, j, value);
      }    
    }
    
      
/*      
IntBuffer intBuf = ByteBuffer.wrap(imageData).order(ByteOrder.LITTLE_ENDIAN).asIntBuffer();
int[] array = new int[intBuf.remaining()];
intBuf.get(array);            
bufferedImage.setRGB(0, 0, imageWidth, imageHeight, array, 0, imageWidth);
*/      
      
    return bufferedImage;
  }

  private static int unsignedByteToInt(byte b) {
    return (int) b & 0xFF;
  }
}