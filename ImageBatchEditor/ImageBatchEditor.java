import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.imageio.*;
import java.awt.image.*;

public class ImageBatchEditor extends Frame implements ActionListener, ItemListener, WindowListener
{
	int tempOffSetX = 0;
	int tempOffSetY = 0;

	String writeExtension = "png";
	String namePostFix= "";
	Vector readExtensionVec = new Vector();

	int numOptTextures = 0;
	int totalMemSave = 0;

	Frame frame = new Frame("asdf");
	MyLayout myLayout = new MyLayout(frame);

	TextField folderNameTextField;
	String sourceFolderPath;

	Checkbox enableAutoCropChkBox;
        Checkbox autoCropBy1stPixelColor_CB;
	Checkbox autoCropByFirstPixelColorChkBox;
	Checkbox cropImageChkBox;
	TextField cropXTextField, cropYTextField, cropWTextField, cropHTextField;
	int cropX = -1, cropY = -1, cropW = -1, cropH = -1;
	
	Checkbox addCanvasChkBox;
	TextField addCanvasLeftTF;
	TextField addCanvasRightTF;
	TextField addCanvasTopTF;
	TextField addCanvasBottomTF;
	int addCanvasLeftVal;
	int addCanvasRightVal;
	int addCanvasTopVal;
	int addCanvasBottomVal;
	
	Checkbox canvasChkBox;
	TextField canvasHeightTF;
	TextField canvasWidthTF;
	int canvasWidth;
	int canvasHeight;	
	
	Checkbox canvasToNext2PowChkBox;
	
	Checkbox makeBlackAndWhiteChkBox;

	Checkbox addAlphaChkBox;
	Checkbox removeAlphaChkBox;
	Checkbox removeAlphaByForceChkBox;
	
	Checkbox replacePixelsChkBox;

	TextField replaceFromR_TF;
	TextField replaceFromG_TF;
	TextField replaceFromB_TF;
	TextField replaceFromA_TF;
	int replaceFromR;
	int replaceFromG;
	int replaceFromB;
	int replaceFromA;

	TextField replaceToR_TF;
	TextField replaceToG_TF;
	TextField replaceToB_TF;
	TextField replaceToA_TF;
	int replaceToR;
	int replaceToG;
	int replaceToB;
	int replaceToA;

	Checkbox scalePixelsChkBox;
	TextField scaleByPixelsWTextField;
	TextField scaleByPixelsHTextField;
	float widthByPixels = -1;
	float heightByPixels = -1;

	Checkbox scalePercentChkBox;
	TextField scaleByPercentWTextField;
	TextField scaleByPercentHTextField;
	float widthByPercent = -1;
	float heightByPercent = -1;
        
        Checkbox scaleDownTo2PowerChkBox;
        Checkbox scaleUpTo2PowerChkBox;

	Checkbox optimize2ChkBox;
	Label percentLabel;
	TextField percentValueTextField;
	float optPercent = 90.0f;

	Button processBtn;

	public static void main(String asdf[])throws Exception
	{
		new ImageBatchEditor();
	}

	public ImageBatchEditor()throws Exception
	{
		readExtensionVec.add("png");
		readExtensionVec.add("bmp");
		readExtensionVec.add("jpeg");
		readExtensionVec.add("jpg");
	
		frame = new Frame("Image Optimizer Tool By Manoj Yerra");
                frame.addWindowListener(this);
		frame.setBounds(10,10,800,650);
		frame.setLayout(null);
		frame.setVisible(true);

		myLayout = new MyLayout(frame);
		myLayout.setInitPos(50, 50);
                myLayout.setLetterWidth(7);

		processBtn = myLayout.addButton("Process");
		processBtn.addActionListener(this);
		
                folderNameTextField = myLayout.addLabelAndTextField("Enter Folder Name", 20*8);
		myLayout.AddRow();
		
		(enableAutoCropChkBox = myLayout.addCheckbox("Enable AutoCrop")).addItemListener(this);
		myLayout.AddRow();
                
                (autoCropBy1stPixelColor_CB = myLayout.addCheckbox("AutoCrop by first pixel color")).addItemListener(this);
		myLayout.AddRow();                

		(cropImageChkBox = myLayout.addCheckbox("Crop Image")).addItemListener(this);
		cropXTextField = myLayout.addLabelAndTextField("x",30);
		cropYTextField = myLayout.addLabelAndTextField("y",30);
		cropWTextField = myLayout.addLabelAndTextField("width",30);
		cropHTextField = myLayout.addLabelAndTextField("height",30);
		myLayout.AddRow();

		(canvasChkBox = myLayout.addCheckbox("Canvas Size")).addItemListener(this);
		canvasWidthTF = myLayout.addLabelAndTextField("width",30);
		canvasHeightTF = myLayout.addLabelAndTextField("height",30);
		myLayout.AddRow();

		(addCanvasChkBox = myLayout.addCheckbox("Add Canvas Boundry")).addItemListener(this);
		addCanvasLeftTF = myLayout.addLabelAndTextField("Left",40);
		addCanvasRightTF = myLayout.addLabelAndTextField("Right",40);
		addCanvasTopTF = myLayout.addLabelAndTextField("Top",40);
		addCanvasBottomTF = myLayout.addLabelAndTextField("Bottom",40);
		myLayout.AddRow();

		
		(canvasToNext2PowChkBox = myLayout.addCheckbox("Increase Canvas Size To Next 2 Power Value")).addItemListener(this);
		myLayout.AddRow();
		
		(makeBlackAndWhiteChkBox = myLayout.addCheckbox("Make Black And White Texture")).addItemListener(this);
		myLayout.AddRow();		
		
		(replacePixelsChkBox = myLayout.addCheckbox("Replace Pixels")).addItemListener(this);
		myLayout.AddRow();
		
		(myLayout.addLabel("From   ")).setFont(new Font("TimesRoman", 1, 14));
		
		replaceFromR_TF = myLayout.addLabelAndTextField("R ",30);
		replaceFromG_TF = myLayout.addLabelAndTextField("G ",30);
		replaceFromB_TF = myLayout.addLabelAndTextField("B ",30);
		replaceFromA_TF = myLayout.addLabelAndTextField("A ",30);
		
		(myLayout.addLabel("  To   ")).setFont(new Font("TimesRoman", 1, 14));
		replaceToR_TF = myLayout.addLabelAndTextField("R ",30);
		replaceToG_TF = myLayout.addLabelAndTextField("G ",30);
		replaceToB_TF = myLayout.addLabelAndTextField("B ",30);
		replaceToA_TF = myLayout.addLabelAndTextField("A ",30);
		myLayout.AddRow();


		(addAlphaChkBox = myLayout.addCheckbox("Add Alpha Layer")).addItemListener(this);
		myLayout.AddRow();
		
		(removeAlphaByForceChkBox = myLayout.addCheckbox("RemoveAlpha Layer")).addItemListener(this);
		myLayout.AddRow();
		(removeAlphaChkBox = myLayout.addCheckbox("RemoveAlpha Layer if no data lose")).addItemListener(this);
		myLayout.AddRow();

		(scalePixelsChkBox = myLayout.addCheckbox("Scale By Pixels")).addItemListener(this);
		scaleByPixelsWTextField = myLayout.addLabelAndTextField("width",4*8);
		scaleByPixelsHTextField = myLayout.addLabelAndTextField("height",4*8);
		myLayout.AddRow();
		
		(scalePercentChkBox = myLayout.addCheckbox("Scale By Percent")).addItemListener(this);
		scaleByPercentWTextField = myLayout.addLabelAndTextField("width",4*8);
		scaleByPercentHTextField = myLayout.addLabelAndTextField("height",4*8);
		myLayout.AddRow();

                (scaleDownTo2PowerChkBox = myLayout.addCheckbox("Scale Down to nearest 2 Power")).addItemListener(this);
                myLayout.AddRow();
                (scaleUpTo2PowerChkBox = myLayout.addCheckbox("Scale Up to nearest 2 Power")).addItemListener(this);
                myLayout.AddRow();

		optimize2ChkBox = myLayout.addCheckbox("Optimize texture memory by scale down to nearest 2Power value");
		optimize2ChkBox.addItemListener(this);

                (myLayout.addLabel("Percentage  ")).setFont(new Font("TimesRoman", 1, 13));
		percentValueTextField = myLayout.addLabelAndTextField("", 4*8);
		myLayout.AddRow();


	}

	public void itemStateChanged(ItemEvent itemEvent)
	{
		Object obj = itemEvent.getSource();

		if(itemEvent.getStateChange() == ItemEvent.SELECTED)
		{
			if(obj == enableAutoCropChkBox)	
                        {
                            autoCropBy1stPixelColor_CB.setState(false);
                            cropImageChkBox.setState(false);
                        }
                        else if(obj == autoCropBy1stPixelColor_CB)
                        {
                            enableAutoCropChkBox.setState(false);
                            cropImageChkBox.setState(false);
                        }
			else if(obj == cropImageChkBox)
                        {
                            autoCropBy1stPixelColor_CB.setState(false);
                            enableAutoCropChkBox.setState(false);
                        }
                        
                        
			else if(obj == scalePixelsChkBox)
                        {
                            scalePercentChkBox.setState(false);
                            scaleDownTo2PowerChkBox.setState(false);
                            scaleUpTo2PowerChkBox.setState(false);
                            optimize2ChkBox.setState(false);
                        }
			else if(obj == scalePercentChkBox)
                        {
                            scalePixelsChkBox.setState(false);
                            scaleDownTo2PowerChkBox.setState(false);
                            scaleUpTo2PowerChkBox.setState(false);
                            optimize2ChkBox.setState(false);
                        }
                        else if(obj == scaleDownTo2PowerChkBox)
                        {
                            scalePercentChkBox.setState(false);
                            scalePixelsChkBox.setState(false);
                            scaleUpTo2PowerChkBox.setState(false);
                            optimize2ChkBox.setState(false);
                        }
                        else if(obj == scaleUpTo2PowerChkBox)
                        {
                            scalePercentChkBox.setState(false);
                            scalePixelsChkBox.setState(false);
                            scaleDownTo2PowerChkBox.setState(false);
                            optimize2ChkBox.setState(false);
                        } 
                        else if(obj == optimize2ChkBox)
                        {
                            scalePercentChkBox.setState(false);
                            scalePixelsChkBox.setState(false);
                            scaleDownTo2PowerChkBox.setState(false);
                            scaleUpTo2PowerChkBox.setState(false);
                        } 
                         
			else if(obj == canvasChkBox)
			{
				canvasToNext2PowChkBox.setState(false);
				addCanvasChkBox.setState(false);
			}
			else if(obj == addCanvasChkBox)
			{
				canvasToNext2PowChkBox.setState(false);
				canvasChkBox.setState(false);
			}
			else if(obj == canvasToNext2PowChkBox)
			{
				canvasChkBox.setState(false);
				addCanvasChkBox.setState(false);
			}
			
			else if(obj == addAlphaChkBox)
			{
				removeAlphaByForceChkBox.setState(false);
				removeAlphaChkBox.setState(false);
			}
			else if(obj == removeAlphaChkBox)
			{
				addAlphaChkBox.setState(false);
				removeAlphaByForceChkBox.setState(false);
			}
			else if(obj == removeAlphaByForceChkBox)
			{
				addAlphaChkBox.setState(false);
				removeAlphaChkBox.setState(false);
			}
		}
	}

	public void actionPerformed(ActionEvent event)
	{
		Object obj = event.getSource();

		if(obj == processBtn)
		{
			sourceFolderPath = folderNameTextField.getText().trim();

			if(UtilFuncs.IsValidFolderPath(folderNameTextField, frame) == false)
				return;

			if(cropImageChkBox.getState())
			{
				if( (cropX = UtilFuncs.GetPositiveInt(cropXTextField, "CropImage x value is not valid", frame)) == -1 ) return;
				if( (cropY = UtilFuncs.GetPositiveInt(cropYTextField, "CropImage y value is not valid", frame)) == -1 ) return;
				if( (cropW = UtilFuncs.GetPositiveInt(cropWTextField, "CropImage width value is not valid", frame)) == -1 ) return;
				if( (cropH = UtilFuncs.GetPositiveInt(cropHTextField, "CropImage height value is not valid", frame)) == -1 ) return;
			}

			if(canvasChkBox.getState())
			{
				if( (canvasWidth = UtilFuncs.GetPositiveInt(canvasWidthTF, "Canvas width value is not valid", frame)) == -1 ) return;
				if( (canvasHeight = UtilFuncs.GetPositiveInt(canvasHeightTF, "Canvas height value is not valid", frame)) == -1 ) return;
			}

			if(addCanvasChkBox.getState())
			{
				if( (addCanvasLeftVal = UtilFuncs.GetPositiveInt(addCanvasLeftTF, "Canvas left value is not valid", frame)) == -1 ) return;
				if( (addCanvasRightVal = UtilFuncs.GetPositiveInt(addCanvasRightTF, "Canvas right value is not valid", frame)) == -1 ) return;
				if( (addCanvasTopVal = UtilFuncs.GetPositiveInt(addCanvasRightTF, "Canvas top value is not valid", frame)) == -1 ) return;
				if( (addCanvasBottomVal = UtilFuncs.GetPositiveInt(addCanvasRightTF, "Canvas bottom value is not valid", frame)) == -1 ) return;
			}

			if(replacePixelsChkBox.getState())
			{
				if( (replaceFromR = UtilFuncs.GetPositiveInt(replaceFromR_TF, "R value is not valid", frame)) == -1 ) return;
				if( (replaceFromG = UtilFuncs.GetPositiveInt(replaceFromG_TF, "G value is not valid", frame)) == -1 ) return;
				if( (replaceFromB = UtilFuncs.GetPositiveInt(replaceFromB_TF, "B value is not valid", frame)) == -1 ) return;
				if( (replaceFromA = UtilFuncs.GetPositiveInt(replaceFromA_TF, "A value is not valid", frame)) == -1 ) return;

				if( (replaceToR = UtilFuncs.GetPositiveInt(replaceToR_TF, "R value is not valid", frame)) == -1 ) return;
				if( (replaceToG = UtilFuncs.GetPositiveInt(replaceToG_TF, "G value is not valid", frame)) == -1 ) return;
				if( (replaceToB = UtilFuncs.GetPositiveInt(replaceToB_TF, "B value is not valid", frame)) == -1 ) return;
				if( (replaceToA = UtilFuncs.GetPositiveInt(replaceToA_TF, "A value is not valid", frame)) == -1 ) return;
			}

			if(scalePixelsChkBox.getState())
			{
				int ww = UtilFuncs.GetPositiveIntWODialog(scaleByPixelsWTextField);
				int hh = UtilFuncs.GetPositiveIntWODialog(scaleByPercentHTextField);
				
				if(ww > 0 && hh < 0)
				{
					widthByPixels = ww;
					heightByPixels = -1;
				}
				else if(ww < 0 && hh > 0)
				{
					widthByPixels = -1;
					heightByPixels = hh;					
				}
				else
				{
					if(ww < 0)
						UtilFuncs.GetPositiveInt(scaleByPixelsWTextField, "scale by pixels width value is not valid", frame);
					else if(hh < 0)
						UtilFuncs.GetPositiveInt(scaleByPixelsHTextField, "scale by pixels height value is not valid", frame);
				}
			}
			else if(scalePercentChkBox.getState())
			{
				if( (widthByPercent = UtilFuncs.GetPositiveInt(scaleByPercentWTextField, "scale by percentage width value is not valid", frame)) == -1 ) return;
				if( (heightByPercent = UtilFuncs.GetPositiveInt(scaleByPercentHTextField, "scale by percentage height value is not valid", frame)) == -1 ) return;
			}

			if(optimize2ChkBox.getState())
			{
				if( (optPercent = UtilFuncs.GetPositiveInt(percentValueTextField, "percentage value is not valid", frame)) == -1 ) return;
			}

			try{
			Process();
			}catch(Exception exception){exception.printStackTrace();}
		}
	}

	public void Process() throws Exception
	{	
		numOptTextures = 0;
		totalMemSave = 0;

		new File(sourceFolderPath+"_new").mkdir();
		
		CreateFolders(new File(sourceFolderPath));
		Convert(new File(sourceFolderPath));
		
		float TM = (float) totalMemSave;

		So.pln("");
		So.pln("Total Memory Saved "+totalMemSave+" bytes");
		So.pln("Total Memory Saved "+(totalMemSave/1024.0f)+"KB");
		So.pln("Total Memory Saved "+(totalMemSave/(1024.0f*1024.0f))+"MB");
	}


	public void Convert(File f) throws Exception
	{
		File files[]=f.listFiles();

		for(int i=0;i<files.length;i++)
		{
			String filePath = files[i].getPath();

			if(files[i].isDirectory())
				Convert(files[i]);

			for(int loop=0; loop<readExtensionVec.size(); loop++)
			{
				String readExtension = (String)readExtensionVec.get(loop);
				FindReplaceAndSave(filePath, readExtension, writeExtension);
			}
		}
	}
	
	public boolean FindReplaceAndSave(String filePath, String find, String writeExtension) throws Exception
	{
		if(filePath.endsWith("."+find.toLowerCase()) || filePath.endsWith("."+find.toUpperCase()))
		{
			String newPath = filePath.replaceFirst(sourceFolderPath, sourceFolderPath+"_new");
			newPath = newPath.substring(0,newPath.length()-find.length())+writeExtension;
			SaveFile(filePath, newPath, writeExtension);
			return true;
		}
		else if(find.equalsIgnoreCase("jpg") || find.equalsIgnoreCase("jpeg"))
		{
			find = "jpeg";

			if(filePath.endsWith("."+find.toLowerCase()) || filePath.endsWith("."+find.toUpperCase()))
			{
				String newPath = filePath.replaceFirst(sourceFolderPath, sourceFolderPath+"_new");
				newPath = newPath.substring(0,newPath.length()-find.length())+writeExtension;
				SaveFile(filePath, newPath, writeExtension);
				return true;
			}
		}
		
		return false;
	}

	public void SaveFile(String inFile, String outFile, String extension) throws Exception
	{
		System.out.println("Going Save file : "+inFile);
	
		BufferedImagePlus bp = new BufferedImagePlus(inFile);
		int memBeforeOpt = bp.GetHeapMemory();

		String optStr = "";
		Rectangle autoCropRect;

		if(enableAutoCropChkBox.getState())
		{
			autoCropRect = bp.AutoCrop();
			
			tempOffSetX = (int)autoCropRect.getX();		//temporary code...
			tempOffSetY = (int)autoCropRect.getY();		//temporary code...
			
			if(autoCropRect.getWidth() > 0)
				optStr += "[AutoCrop: x = "+autoCropRect.getX()+" y = "+autoCropRect.getY()+" w = "+bp.GetWidth()+", h = "+bp.GetHeight()+"]";
		}
		if(autoCropBy1stPixelColor_CB.getState())
		{
                        bp.AutoCropByFirstPixelColor();
			optStr += "[AutoCropByFirstPixel:"+bp.GetWidth()+","+bp.GetHeight()+"]";
		}
		else if(cropImageChkBox.getState())
		{
			bp.CropImage(cropX, cropY, cropW, cropH);
			optStr += "[AfterCrop:"+bp.GetWidth()+","+bp.GetHeight()+"]";
		}

		if(canvasToNext2PowChkBox.getState())
		{
			int next2PowWidth = UtilFuncs.GetNextAndNearest2Power(bp.GetWidth());
			int next2PowHeight = UtilFuncs.GetNextAndNearest2Power(bp.GetHeight());
			
			bp.SetCanvasSize(next2PowWidth, next2PowHeight);
			optStr += "[After Canvas Size:"+bp.GetWidth()+","+bp.GetHeight()+"]";
		}
		else if(canvasChkBox.getState())
		{
			bp.SetCanvasSize(canvasWidth, canvasHeight);
			optStr += "[After Canvas Size:"+bp.GetWidth()+","+bp.GetHeight()+"]";
		}
		else if(addCanvasChkBox.getState())
		{	
			//bp.AddCanvas(addCanvasLeftVal, addCanvasRightVal, addCanvasTopVal, addCanvasBottomVal);
			//optStr += "[Added Canvas Bounds:"+addCanvasLeftVal+","+addCanvasRightVal+","+addCanvasTopVal+","+addCanvasBottomVal+]";

			int width = (int)bp.GetWidth();
			int height = (int)bp.GetHeight();

			int left = 0;
			int right = 0;
			int top = 0;
			int bottom = 0;

			if(width < height)
			{
				int diff = height - width;
				left = diff/2;
				right = diff/2;
				
				if(width + left + right < height)
				{
					int gap = height - (width+left+right);
					left += gap;
				}
				else if(width + left + right > height)
				{
					int gap = (width+left+right) - height;
					left -= gap;
				}
			}
			else if(height < width)
			{
				int diff =  width - height;
				top = diff/2;
				bottom = diff/2;
				
				if(height + top + bottom < width)
				{
					int gap = width - (height+top+bottom);
					top += gap;
				}
				else if(height + top + bottom > width)
				{
					int gap = (height+top+bottom) - width;
					top -= gap;
				}
			}
			
			bp.AddCanvas(left, right, top, bottom);
			tempOffSetX -= left;
			tempOffSetY -= top;
			
			System.out.println(bp.imageInfo.filePath+","+tempOffSetX+","+tempOffSetY+","+bp.GetWidth()+","+bp.GetHeight());
			
			optStr += "[Added Canvas]";		
		}		
		
		if(makeBlackAndWhiteChkBox.getState())
		{
			bp.MakeBlackAndWhite();
			//bp.Hue();
			optStr += "[BlackAndWhite]";
		}		

		if(replacePixelsChkBox.getState())
		{
			bp.ReplacePixels(replaceFromR, replaceFromG, replaceFromB, replaceFromA, replaceToR, replaceToG, replaceToB, replaceToA, 100);
			optStr += "[ReplacedPixels]";
		}

		if(addAlphaChkBox.getState())
		{
			if(bp.AddAlphaLayer())
				optStr += "[AddedAlpha]";
		}
		else if(removeAlphaByForceChkBox.getState())
		{
			if(bp.RemoveAlphaLayer())
				optStr += "[RemovedAlpha]";
		}
		else if(removeAlphaChkBox.getState())
		{
			if(bp.RemoveAlphaLayerIfNoDataLose())
				optStr += "[RemovedAlpha]";
		}

		if(scalePixelsChkBox.getState())
		{
			if(widthByPixels > 0 && heightByPixels < 0)
			{
				bp.SetSize((int)widthByPixels, (int)((float)bp.GetHeight() * (float)widthByPixels / (float)bp.GetWidth()) );
			}
			else if(widthByPixels < 0 && heightByPixels > 0)
			{
				bp.SetSize((int)((float)bp.GetWidth() * (float)heightByPixels / (float)bp.GetHeight()) , (int)heightByPixels);
			}
			else
			{
				bp.SetSize((int)widthByPixels, (int)heightByPixels);
			}
			
			optStr += "[ReSize:"+bp.GetWidth()+","+bp.GetHeight()+"]";
		}
		else if(scalePercentChkBox.getState())
		{
			float textureWidth = (float)bp.GetWidth();
			float textureHeight = (float)bp.GetHeight();

			textureWidth = (widthByPercent*textureWidth)/100.0f;
			textureHeight = (heightByPercent*textureHeight)/100.0f;

			bp.SetSize((int)textureWidth, (int)textureHeight);
			optStr += "[ReSize:"+bp.GetWidth()+","+bp.GetHeight()+"]";
		}
        else if(scaleDownTo2PowerChkBox.getState())
        {
            int down2Width = UtilFuncs.GetLessAndNearest2Power(bp.GetWidth());
            int down2Height = UtilFuncs.GetNextAndNearest2Power(bp.GetHeight());
            
            bp.SetSize(down2Width, down2Height);
			
			optStr += "[ReSize to before 2 power:"+bp.GetWidth()+","+bp.GetHeight()+"]";
        }
        else if(scaleUpTo2PowerChkBox.getState())
        {
            int up2Width = UtilFuncs.GetNextAndNearest2Power(bp.GetWidth());
            int up2Height = UtilFuncs.GetNextAndNearest2Power(bp.GetHeight());
			
			optStr += "[ReSize to next 2 Power:"+bp.GetWidth()+","+bp.GetHeight()+"]";
			
            bp.SetSize(up2Width, up2Height);
        }
        else if(optimize2ChkBox.getState())
		{
			float tw = (float)bp.GetWidth();
			float th = (float)bp.GetHeight();
			boolean optBy2PowerIsDone = false;
			
			if(tw > 8 && UtilFuncs.Is2Power((int)tw) == false)
			{
				float nearest2Power = (float)UtilFuncs.GetLessAndNearest2Power((int)tw);
				float percent = (nearest2Power * 100.0f)/tw;
				
				if(percent >= optPercent)
				{
					tw = nearest2Power;
					optBy2PowerIsDone = true;
				}
			}
			if(th > 8 && UtilFuncs.Is2Power((int)th) == false)
			{
				float nearest2Power = (float)UtilFuncs.GetLessAndNearest2Power((int)th);
				float percent = (nearest2Power * 100.0f)/th;
				
				if(percent >= optPercent)
				{
					th = nearest2Power;
					optBy2PowerIsDone = true;
				}
			}
			
			if(optBy2PowerIsDone)
			{
				bp.SetSize((int)tw, (int)th);
				optStr += "[OptBy2Pow("+bp.GetWidth()+","+bp.GetHeight()+"]";
			}
		}
		
		if(optStr.equals("") == false)
		{
			int saveMem = memBeforeOpt - bp.GetHeapMemory();
			totalMemSave += saveMem;
			
			numOptTextures++;
			optStr = numOptTextures+")"+inFile+"(SM="+saveMem+"): "+optStr;

			String finalStr = outFile.substring(0, outFile.lastIndexOf("."));
			finalStr += namePostFix+"."+extension;
			
			//So.pln(optStr);
			bp.WriteAs(finalStr, extension);
		}
	}

	public void CreateFolders(File f) throws Exception
	{
		File files[]=f.listFiles();

		for(int i=0;i<files.length;i++)
		{
			String filePath = files[i].getPath();

			if(files[i].isDirectory())
			{
				if(files[i].getPath().endsWith(".svn"))
					continue;

				files[i].mkdir();
				String newPath = filePath.replaceFirst(sourceFolderPath, sourceFolderPath+"_new");
				File newFolder = new File(newPath);
				newFolder.mkdir();

				CreateFolders(files[i]);
			}
		}
	}

	public void windowOpened(WindowEvent we){}
	public void windowDeactivated(WindowEvent we){}
	public void windowActivated(WindowEvent we){}
	public void windowDeiconified(WindowEvent we){}
	public void windowIconified(WindowEvent we){}
	public void windowClosed(WindowEvent we){}
	public void windowClosing(WindowEvent we)
	{
		System.exit(0);
	}
}