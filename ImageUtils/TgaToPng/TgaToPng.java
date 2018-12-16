import java.io.*;
import java.awt.image.*;
import java.awt.*;


class TgaInfo
{
	public int width = 0;
	public int height = 0;
	public int bytesPP = 0;
	public byte[] rawData = null;
}

public class TgaToPng
{
    public static void main(String[] args) throws Exception
	{
		new TgaToPng();
    }
	
	TgaToPng() throws Exception
	{
		TgaInfo tgaInfo = new TgaInfo();

		File runFolder = new File("run");
		File[] runFilesArr = runFolder.listFiles();

		File runPNGFolder = new File("runPNG");
		runPNGFolder.mkdir();
		
		BufferedReader br = new BufferedReader(new FileReader("RunList.txt"));

		String line = null;
		
		while((line = br.readLine()) != null)
		{
			String filePath = "run/"+line.replace(".png", ".tga");
			String descrip = br.readLine();
			
			tga_read(filePath, tgaInfo);	
			BufferedImagePlus bufferedImage = GetBufferedImagePlusFromRawData(tgaInfo.rawData, tgaInfo.width, tgaInfo.height, tgaInfo.bytesPP, true);

			BufferedImagePlus finalImg = new BufferedImagePlus(tgaInfo.width, tgaInfo.height*2,  BufferedImage.TYPE_INT_ARGB);
			finalImg.DrawImage(bufferedImage, 0,0,bufferedImage.GetWidth(), bufferedImage.GetHeight());
			finalImg.DrawString(descrip, 0, bufferedImage.GetHeight(), Color.GRAY, new Font("Arial", Font.BOLD, 32), true);
			
			finalImg.WriteAs(runPNGFolder.getPath()+"/"+line, "png");
			
			br.readLine();
			br.readLine();
		}
	}
	
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
		
		System.out.println("width = "+width);
		System.out.println("height = "+height);
		System.out.println("bytesPerPixel ="+bytesPerPixel);		
		
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
