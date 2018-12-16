import java.io.*;
import java.awt.*;
import javax.imageio.*;
import java.awt.image.*;

public class BufferedImagePlus implements Cloneable
{
	public ImageInfo imageInfo;
	
	public BufferedImagePlus(ImageInfo imageInfo)
	{
		this.imageInfo = imageInfo;
	}

	public BufferedImagePlus(String filePath) throws Exception
	{
		imageInfo = new ImageInfo(filePath, ImageIO.read(new File(filePath)));
	}
	
	public BufferedImagePlus(int width, int height, int type)
	{
		BufferedImage bufferedImage = new BufferedImage(width, height, type);
		imageInfo = new ImageInfo("", bufferedImage);
	}
	
	public BufferedImagePlus(BufferedImage bufferedImg)
	{
		imageInfo = new ImageInfo("", bufferedImg);
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
			//print("No alpha layer");
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
					//print("can't remove alpha layer");
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

				//0.2126×red + 0.7152×green + 0.0722×blue
				int value = (int) (0.2126*(float)red + 0.7152*(float)green + 0.0722*(float)blue); /// 3;
				int color = ((alpha<<24) + (value<<16) + (value<<8) + value);
			
/*			
				int rr = (int)(0.2126*(float)red);
				int gg = (int)(0.7152*(float)green);
				int bb = (int)(0.0722*(float)blue);
				
				int val = rr + gg + bb;
								
				int grayR = val;
				int grayG = val;
				int grayB = val;
				
				int color = ((alpha<<24) + grayR + grayG + grayB);
*/

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
