import java.io.*;
import java.awt.*;
import javax.imageio.*;
import java.awt.image.*;
import java.util.Arrays;


public class BufferedImagePlus implements Cloneable
{
	public ImageInfo imageInfo;
	
	public BufferedImagePlus(ImageInfo imageInfo)
	{
		this.imageInfo = imageInfo;
	}

	public BufferedImagePlus(String filePath) throws Exception
	{
		if(filePath.endsWith(".tga") || filePath.endsWith(".TGA"))
		{
			imageInfo = new ImageInfo(filePath, TGALoader.loadTGA(filePath));
		}
		else
		{
			imageInfo = new ImageInfo(filePath, ImageIO.read(new File(filePath)));
		}
	}
	
	public BufferedImagePlus(int width, int height, int type)
	{
		BufferedImage bufferedImage = new BufferedImage(width, height, type);
		imageInfo = new ImageInfo("", bufferedImage);
	}
	
	public BufferedImagePlus cloneObject() throws CloneNotSupportedException
	{
		BufferedImagePlus cloneObj = new BufferedImagePlus(GetWidth(), GetHeight(), imageInfo.texture.getType());

		cloneObj.imageInfo.filePath = imageInfo.filePath;
		
		Graphics g = cloneObj.imageInfo.texture.getGraphics();
		g.drawImage(imageInfo.texture, 0, 0, null);
		g.dispose();
		
		return cloneObj;
	}
	
	public void DrawImage(BufferedImagePlus bImgPlus, int x, int y, int w, int h)
	{
		imageInfo.texture.getGraphics().drawImage(bImgPlus.imageInfo.texture, x, y, w, h, null);
	}
	
	public void DrawString(String str, int x, int y, Color color, int fontSize)
	{
		y += fontSize;
		Graphics g = imageInfo.texture.getGraphics();
		g.setColor(color);
		g.setFont(new Font("SansSerif", Font.BOLD, fontSize));
		g.drawString(str, x, y);	
	}

	public void DrawString(String str, int x, int y, Color color, Font font, boolean wrap)
	{
		y = y+font.getSize();
		Graphics g = imageInfo.texture.getGraphics();
		g.setColor(color);
		g.setFont(font);
		
		if(wrap == false)
		{
			g.drawString(str, x, y);
		}
		else
		{
			FontMetrics fontMat = GetGraphics().getFontMetrics(font);
			String strArr[] = StringUtils.wrap(str, fontMat, GetWidth()-x);
			
			for(int i=0;i<strArr.length;i++)
				g.drawString(strArr[i], x,y+(i*font.getSize()));
		}
	}
	
	public Graphics GetGraphics()
	{
		return imageInfo.texture.getGraphics();
	}
	
	public void SetSize(int width, int height) throws Exception
	{
		int type = BufferedImage.TYPE_INT_ARGB;

		if(HasTransparency() == false)
			type = BufferedImage.TYPE_INT_RGB;
	
		Image image = imageInfo.texture.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage localTexture = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		localTexture.getGraphics().drawImage(image, 0, 0, null);

		imageInfo.texture = localTexture;
	}

	public BufferedImagePlus GetScaledImage(int width, int height) throws Exception
	{
		int type = BufferedImage.TYPE_INT_ARGB;

		if(HasTransparency() == false)
			type = BufferedImage.TYPE_INT_RGB;
	
		Image image = imageInfo.texture.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage localTexture = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
		localTexture.getGraphics().drawImage(image, 0, 0, null);

		return new BufferedImagePlus(new ImageInfo(imageInfo.filePath, localTexture));
	}
	
	public void CropImage(int x, int y, int width, int height)
	{
		imageInfo.texture = imageInfo.texture.getSubimage(x, y, width, height);
	}
	
	public BufferedImagePlus GetCroppedImage(int x, int y, int width, int height)
	{
		return new BufferedImagePlus(new ImageInfo(imageInfo.filePath, imageInfo.texture.getSubimage(x, y, width, height)));
	}

	public void SetCanvasSize(int width, int height) throws Exception
	{
		int type = BufferedImage.TYPE_INT_ARGB;

		if(HasTransparency() == false)
			type = BufferedImage.TYPE_INT_RGB;

		BufferedImage localTexture = new BufferedImage(width, height, type);

		int textureWidth = imageInfo.texture.getWidth();
		int textureHeight = imageInfo.texture.getHeight();

		for(int y=0;y<textureHeight;y++)
		{
			for(int x=0;x<textureWidth;x++)
			{
				localTexture.setRGB(x, y, imageInfo.texture.getRGB(x, y));
			}
		}

		imageInfo.texture = localTexture;
	}


	public void AddCanvas(int left, int right , int top, int bottom) throws Exception
	{
		int type = BufferedImage.TYPE_INT_ARGB;

		if(HasTransparency() == false)
			type = BufferedImage.TYPE_INT_RGB;

		int textureWidth = imageInfo.texture.getWidth();
		int textureHeight = imageInfo.texture.getHeight();

		int width = textureWidth + left + right;
		int height = textureHeight + top + bottom;

		BufferedImage localTexture = new BufferedImage(width, height, type);
		
		for(int y=0;y<textureHeight;y++)
		{
			for(int x=0;x<textureWidth;x++)
			{
				localTexture.setRGB(x+left, y+top, imageInfo.texture.getRGB(x, y));
			}
		}
		imageInfo.texture = localTexture;
	}

	public int GetWidth()
	{
		return imageInfo.texture.getWidth();
	}

	public int GetHeight()
	{
		return imageInfo.texture.getHeight();
	}

	public boolean HasTransparency()
	{
		return (imageInfo.texture.getTransparency() != Transparency.OPAQUE);
	}

	public boolean AddAlphaLayer()
	{
		if(HasTransparency())
		{
			return false;
		}
		
		int actualImageWidth = imageInfo.texture.getWidth();
		int actualImageHeight = imageInfo.texture.getHeight();

		BufferedImage localTexture = new BufferedImage(actualImageWidth, actualImageHeight, BufferedImage.TYPE_INT_ARGB);

		for(int y=0;y<actualImageHeight;y++)
		{
			for(int x=0;x<actualImageWidth;x++)
			{
				localTexture.setRGB(x, y, imageInfo.texture.getRGB(x, y));
			}
		}

		imageInfo.texture = localTexture;
		
		return true;
	}	
	
	public boolean RemoveAlphaLayer()
	{
		if(HasTransparency() == false)
		{
			return false;
		}
		
		int actualImageWidth = imageInfo.texture.getWidth();
		int actualImageHeight = imageInfo.texture.getHeight();

		BufferedImage localTexture = new BufferedImage(actualImageWidth, actualImageHeight, BufferedImage.TYPE_INT_RGB);

		for(int y=0;y<actualImageHeight;y++)
		{
			for(int x=0;x<actualImageWidth;x++)
			{
				localTexture.setRGB(x, y, imageInfo.texture.getRGB(x, y));
			}
		}

		imageInfo.texture = localTexture;
		
		return true;
	}	

	public boolean RemoveAlphaLayerIfNoDataLose()
	{
		if(HasTransparency() == false)
		{
			return false;
		}
		
		int actualImageWidth = imageInfo.texture.getWidth();
		int actualImageHeight = imageInfo.texture.getHeight();

		for(int y=0;y<actualImageHeight;y++)
		{
			for(int x=0;x<actualImageWidth;x++)
			{
				int pixel = imageInfo.texture.getRGB(x, y);
				int alpha = (pixel >> 24) & 0xff;
				
				if(alpha < 250)
				{
					return false;
				}
			}
		}

		BufferedImage localTexture = new BufferedImage(actualImageWidth, actualImageHeight, BufferedImage.TYPE_INT_RGB);

		for(int y=0;y<actualImageHeight;y++)
		{
			for(int x=0;x<actualImageWidth;x++)
			{
				localTexture.setRGB(x, y, imageInfo.texture.getRGB(x, y));
			}
		}

		imageInfo.texture = localTexture;
		
		return true;
	}


	public boolean AutoCropByFirstPixelColor() throws Exception
	{
		int p1 = imageInfo.texture.getRGB(0, 0);
		int a1 = (p1 >> 24) & 0xff;
		int r1 = (p1 >> 16) & 0xff;
		int g1 = (p1 >> 8) & 0xff;
		int b1 = (p1) & 0xff;
	
		int textureWidth = imageInfo.texture.getWidth();
		int textureHeight = imageInfo.texture.getHeight();

		int minX = -1;
		int maxX = -1;
	
		int minY = -1;
		int maxY = -1;
	
		for(int y=0;y<textureHeight;y++)
		{
			for(int x=0;x<textureWidth;x++)
			{
				int pixel = imageInfo.texture.getRGB(x, y);
				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				if(alpha != a1 || red != r1 || green != g1 || blue != b1)
				{
					if(x < minX || minX == -1)
					{
						minX = x;
					}
					break;
				}
			}
		}

		for(int x=0;x<textureWidth;x++)
		{
			for(int y=0;y<textureHeight;y++)
			{
				int pixel = imageInfo.texture.getRGB(x, y);
				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				if(alpha != a1 || red != r1 || green != g1 || blue != b1)
				{
					if(y < minY || minY == -1)
					{
						minY = y;
					}
					break;
				}
			}
		}

		for(int y=0;y<textureHeight;y++)
		{
			for(int x=textureWidth-1; x>=0; x--)
			{
				int pixel = imageInfo.texture.getRGB(x, y);
				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				if(alpha != a1 || red != r1 || green != g1 || blue != b1)
				{
					if(x > maxX || maxX == -1)
						maxX = x;
					break;
				}
			}
		}

		for(int x=0;x<textureWidth;x++)
		{
			for(int y=textureHeight-1; y>=0; y--)
			{
				int pixel = imageInfo.texture.getRGB(x, y);
				int alpha = (pixel >> 24) & 0xff;
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				
				if(alpha != a1 || red != r1 || green != g1 || blue != b1)
				{
					if(y > maxY || maxY == -1)
						maxY = y;
					break;
				}
			}
		}

		if(minX == -1)	minX = 0;
		if(minY == -1)	minY = 0;

		if(maxX == -1)	maxX = textureWidth;
		if(maxY == -1)	maxY = textureHeight;

		int finalWidth = (maxX-minX)+1;
		int finalHeight = (maxY-minY)+1;

		if(finalWidth > textureWidth)
			finalWidth = textureWidth;

		if(finalHeight > textureHeight)
			finalHeight = textureHeight;
			
		if(textureWidth != finalWidth || textureHeight != finalHeight)
		{
			imageInfo.texture = imageInfo.texture.getSubimage(minX, minY, finalWidth, finalHeight);
			return true;
		}
		return false;
	}
	
	

	public Rectangle AutoCrop() throws Exception
	{
		if(HasTransparency() == false)
			return new Rectangle(0,0,0,0);
	
		int textureWidth = imageInfo.texture.getWidth();
		int textureHeight = imageInfo.texture.getHeight();

		int minX = -1;
		int maxX = -1;
	
		int minY = -1;
		int maxY = -1;
	
		for(int y=0;y<textureHeight;y++)
		{
			for(int x=0;x<textureWidth;x++)
			{
				int pixel = imageInfo.texture.getRGB(x, y);
				int alpha = (pixel >> 24) & 0xff;
				if(alpha != 0)
				{
					if(x < minX || minX == -1)
					{
						minX = x;
					}
					break;
				}
			}
		}

		for(int x=0;x<textureWidth;x++)
		{
			for(int y=0;y<textureHeight;y++)
			{
				int pixel = imageInfo.texture.getRGB(x, y);
				int alpha = (pixel >> 24) & 0xff;
				if(alpha != 0)
				{
					if(y < minY || minY == -1)
					{
						minY = y;
					}
					break;
				}
			}
		}

		for(int y=0;y<textureHeight;y++)
		{
			for(int x=textureWidth-1; x>=0; x--)
			{
				int pixel = imageInfo.texture.getRGB(x, y);
				int alpha = (pixel >> 24) & 0xff;
				if(alpha != 0)
				{
					if(x > maxX || maxX == -1)
						maxX = x;
					break;
				}
			}
		}

		for(int x=0;x<textureWidth;x++)
		{
			for(int y=textureHeight-1; y>=0; y--)
			{
				int pixel = imageInfo.texture.getRGB(x, y);
				int alpha = (pixel >> 24) & 0xff;
				if(alpha != 0)
				{
					if(y > maxY || maxY == -1)
						maxY = y;
					break;
				}
			}
		}

		if(minX == -1)	minX = 0;
		if(minY == -1)	minY = 0;

		if(maxX == -1)	maxX = textureWidth;
		if(maxY == -1)	maxY = textureHeight;

		int finalWidth = (maxX-minX)+1;
		int finalHeight = (maxY-minY)+1;

		if(finalWidth > textureWidth)
			finalWidth = textureWidth;

		if(finalHeight > textureHeight)
			finalHeight = textureHeight;
			
		if(textureWidth != finalWidth || textureHeight != finalHeight)
		{
			imageInfo.texture = imageInfo.texture.getSubimage(minX, minY, finalWidth, finalHeight);
			return new Rectangle(minX, minY, finalWidth, finalHeight);
		}
		return new Rectangle(0,0,0,0);
	}

	public void WriteAs(String filePath, String extension) throws Exception
	{
		ImageIO.write(imageInfo.texture, extension, new FileOutputStream(filePath));
	}

	public int GetHeapMemory()
	{
		//int width = MyUtil.GetNextAndNearest2Power(GetWidth());
		//int height = MyUtil.GetNextAndNearest2Power(GetHeight());

		//int bytesPerPixel = 4;
		//if(HasTransparency() == false)
		//	bytesPerPixel = 3;

		//return (width*height*bytesPerPixel);
		return 0;
	}


	public void MakeBlackAndWhite()
	{
		int imageWidth = imageInfo.texture.getWidth();
		int imageHeight = imageInfo.texture.getHeight();

		for(int y=0;y<imageHeight;y++)
		{
			for(int x=0;x<imageWidth;x++)
			{
				int p = imageInfo.texture.getRGB(x, y);

				int alpha = (p >> 24) & 255;
				int red = (p >> 16) & 255;
				int green = (p >> 8) & 255;
				int blue = (p) & 255;

				int value = (int) (0.2126*(float)red + 0.7152*(float)green + 0.0722*(float)blue);
				int color = ((alpha<<24) + (value<<16) + (value<<8) + value);
			
				imageInfo.texture.setRGB(x, y, color);
			}
		}
	}
	
	public void SetPixelColor(int x, int y, int r, int g, int b, int a)
	{
		int color = ((a<<24) + (r<<16) + (g<<8) + b);
		imageInfo.texture.setRGB(x, y, color);
	}
	
	public int GetPixelColor(int x, int y)
	{
		return imageInfo.texture.getRGB(x, y);		
	}
	
	public int GetPixelAlpha(int x, int y)
	{
		int p = imageInfo.texture.getRGB(x, y);
		int a = (p >> 24) & 255;
		return a;
	}
		
	public int GetPixelRed(int x, int y)
	{
		int p = imageInfo.texture.getRGB(x, y);
		int r = (p >> 16) & 255;
		return r;
	}
	
	public int GetPixelGreen(int x, int y)
	{
		int p = imageInfo.texture.getRGB(x, y);
		int g = (p >> 8) & 255;
		return g;
	}

	public int GetPixelBlue(int x, int y)
	{
		int p = imageInfo.texture.getRGB(x, y);
		int b = (p) & 255;
		return b;
	}

	public int ReplacePixelsWithPosColor(int pixelX, int pixelY, int toR, int toG, int toB, int toA, int accuracy)
	{
            int p = imageInfo.texture.getRGB(pixelX, pixelY);

            int fromA = (p >> 24) & 255;
            int fromR = (p >> 16) & 255;
            int fromG = (p >> 8) & 255;
            int fromB = (p) & 255;
		
            return (ReplacePixels(fromR, fromG, fromB, fromA, toR, toG, toB, toA, accuracy));
	}

    public int ReplacePixels(int fromR, int fromG, int fromB, int fromA, int toR, int toG, int toB, int toA, int accuracy)
    {
        return 0;
    }
	
	//TGA image related...
	
	BufferedImagePlus GetBufferedImagePlusFromRawData(byte[] raw, int w, int h, int bytesPP, boolean flipVertical)
	{
		BufferedImagePlus bufferedImage = null;

		if(bytesPP == 4)
		{
			bufferedImage = new BufferedImagePlus(w, h, BufferedImage.TYPE_INT_ARGB);

			if(flipVertical)
			{
				for(int y=0; y<h; y++)
					for(int x=0; x<w; x++)
					{
						int pos = (y*w + x)*bytesPP;
						bufferedImage.SetPixelColor(x, h-y-1, raw[pos+2]&0xFF, raw[pos+1]&0xFF, raw[pos+0]&0xFF, raw[pos+3]&0xFF);
					}
			}
			else
			{
				for(int y=0; y<h; y++)
					for(int x=0; x<w; x++)
					{
						int pos = (y*w + x)*bytesPP;
						bufferedImage.SetPixelColor(x, y, raw[pos+2]&0xFF, raw[pos+1]&0xFF, raw[pos+0]&0xFF, raw[pos+3]&0xFF);
					}			
			}
		}
		else
		{
			bufferedImage = new BufferedImagePlus(w, h, BufferedImage.TYPE_INT_RGB);

			if(flipVertical)
			{
				for(int y=0; y<h; y++)
					for(int x=0; x<w; x++)
					{
						int pos = (y*w + x)*bytesPP;
						bufferedImage.SetPixelColor(x, h-y-1, raw[pos+2]&0xFF, raw[pos+1]&0xFF, raw[pos+0]&0xFF, 0);
					}
			}
			else
			{
				for(int y=0; y<h; y++)
					for(int x=0; x<w; x++)
					{
						int pos = (y*w + x)*bytesPP;
						bufferedImage.SetPixelColor(x, y, raw[pos+2]&0xFF, raw[pos+1]&0xFF, raw[pos+0]&0xFF, 0);
					}			
			}
		}
		
		return bufferedImage;
	}
	
	int tga_read(String filename, TgaInfo tgaInfo) throws Exception
	{
		DataInputStream fp = new DataInputStream(new FileInputStream(filename));
				
		byte[] header = new byte[18];
		fp.read(header);

		int image_id_length = header[0];
		int image_type = header[2];
		int origin_x = 0;//header[8] + header[9]*256;
		int origin_y = 0;//header[10] + header[11]*256;
		int width = (header[12]&0xFF) + (header[13]&0xFF)*256;
		int height = (header[14]&0xFF) + (header[15]&0xFF)*256;
		int bytesPerPixel = header[16]/8;
		int image_descriptor = header[17];
		
		//System.out.println("width = "+width);
		//System.out.println("height = "+height);
		//System.out.println("bytesPerPixel ="+bytesPerPixel);		
		
		if(image_type != 10 && image_type != 2)
			return -1;

		byte[] image_id = null;

		if(image_id_length > 0)
		{
			image_id = new byte[image_id_length];
			fp.read(image_id);
		}

		byte[] imgRawData = new byte[width * height * bytesPerPixel];

		if(tga_read_rle(fp, width, height, bytesPerPixel, imgRawData) == 0)
			return -2;
		
		tgaInfo.width = width;
		tgaInfo.height = height;
		tgaInfo.bytesPP = bytesPerPixel;
		tgaInfo.rawData = imgRawData;
		
		return 1;
	}
	
	int tga_read_rle(DataInputStream fp, int width, int height, int bytesPerPixel, byte[] rawData) throws Exception
	{
		short RLE_BIT = (1 << 7);
		long p_loaded = 0;
		long p_expected = width * height;
		byte bpp = (byte)bytesPerPixel;
		int index = 0;

		byte[] b = new byte[1];

		while ((p_loaded < p_expected))
		{
			if(fp.read(b) < 0)
				break;
			
			if( (b[0] & RLE_BIT) > 0)	// is an RLE packet
			{
				short count;
				short i;
				byte[] tmp = new byte[bpp];

				count = (short)(((b[0]&0xFF) & ~RLE_BIT) + 1);
				fp.read(tmp);

				for(i=0; i<count; i++)
				{
					p_loaded++;
					if(p_loaded > p_expected)
						return 0;
					
					for(int j=0;j<bpp;j++)
						rawData[index+j] = tmp[j];
					
					index += bpp;
				}
			}
			else // RAW packet
			{
				short count = (short)((b[0] & ~RLE_BIT) + 1);
				if (p_loaded + count > p_expected)
					return 0;

				p_loaded += count;
				fp.read(rawData, index, bpp*count);
				index += count * bpp;
			}
		}
		
		return 1;
	}
}


class ImageInfo
{
	public BufferedImage texture=null;
	public String filePath="";
	
	public ImageInfo(String filePath, BufferedImage texture)
	{
		this.filePath = filePath;
		this.texture = texture;
	}
}


class TgaInfo
{
	public int width = 0;
	public int height = 0;
	public int bytesPP = 0;
	public byte[] rawData = null;
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
