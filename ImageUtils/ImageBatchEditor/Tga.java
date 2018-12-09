import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Tga {

	public Tga(String file)
	{
		try
		{
			loadTGA(file);
		}
		catch (Exception e)
		{
			So.pln("Loading tga error...");
			e.printStackTrace();
		}
	}
	
	public static final int TGA_RGB = 2;
	public static final int TGA_A = 3;
	public static final int TGA_RLE = 10;
	public static final int TGA_FILE_NOT_FOUND = 13; /* file was not found */
	public static final int TGA_BAD_IMAGE_TYPE = 14; /* color mapped image or image is not uncompressed */
	public static final int TGA_BAD_DIMENSION = 15; /* dimension is not a power of 2 */
	public static final int TGA_BAD_BITS = 16; /* image bits is not 8, 24 or 32 */
	public static final int TGA_BAD_DATA = 17; /* image data could not be loaded */

	public static final int BOTTOM_LEFT_ORIGIN = 0;
	public static final int BOTTOM_RIGHT_ORIGIN = 1;
	public static final int TOP_LEFT_ORIGIN = 2;
	public static final int TOP_RIGHT_ORIGIN = 3;

	public byte[] imageData;
	public int iBits;
	public int texFormat;

	public int imageWidth;
	public int imageHeight;
	/*
	 =============
	 checkSize
	 
	 Make sure its a power of 2.
	 =============
	 */
	int checkSize(int x) {
		//if (x == 2 || x == 4 || x == 8 || x == 16 || x == 32 || x == 64 || x == 128 || x == 256 || x == 512 || x == 1024 || x == 2048)
			return 1;
		//else
		//	return 0;
	}
	/*
	 =============
	 getRGBA
	 
	 Reads in RGBA data for a 32bit image.
	 =============
	 */
	byte[] getRGBA(byte[] data, int size) throws IOException {
		byte[] rgba = data; //new byte[size*4];
		byte temp;
		int i;
		int numOfBytes = size * 4;

		/* TGA is stored in BGRA, make it RGBA */

		for (i = 0; i < numOfBytes; i += 4) {
			temp = rgba[i];
			rgba[i] = rgba[i + 2];
			rgba[i + 2] = temp;
		}
		
		return rgba;
	}
	/*
	 =============
	 getRGB
	 
	 Reads in RGB data for a 24bit image.
	 =============
	 */
	byte[] getRGB(byte[] data, int size) throws IOException {
		byte[] rgb = data; //new byte[size *3];
		byte temp;
		int i;

		/* TGA is stored in BGR, make it RGB */
		for (i = 0; i < size * 3; i += 3) {
			temp = rgb[i];
			rgb[i] = rgb[i + 2];
			rgb[i + 2] = temp;
		}

		return rgb;
	}
	/*
	 =============
	 getGray
	 
	 Gets the grayscale image data.  Used as an alpha channel.
	 =============
	 */
	byte[] getGray(byte[] data, int size) throws IOException {
		//unsigned char *grayData;
		byte[] grayData = data;
		int bread;

		return grayData;
	}
	/*
	 =============
	 getData
	 
	 Gets the image data for the specified bit depth.
	 =============
	 */
	byte[] getData(byte[] s, int sz) throws IOException {
		if (iBits == 32)
			return getRGBA(s, sz);
		else if (iBits == 24)
			return getRGB(s, sz);
		else if (iBits == 8)
			return getGray(s, sz);
		return null;
	}
	/*
	 =============
	 returnError
	 =============
	 Called when there is an error loading the .tga file.
	 */
	int returnError(InputStream s, int error) throws IOException {
		So.pln("error is: " + error);
		s.close();
		return error;
	}
	/*
	 =============
	 loadTGA
	 
	 Loads up a targa file.  Supported types are 8,24 and 32 uncompressed images.
	 id is the texture ID to bind too.
	 =============
	 */


	//int loadTGA(URL url) {
	int loadTGA(String filePath)
	{
		int type[] = new int[4]; // unsigned char
		int info[] = new int[6]; // unsigned char
		int size;
		//InputStream stream;
		BufferedInputStream s;

		try {

			//stream = url.openStream();
			//s = new BufferedInputStream(stream);

			s = new BufferedInputStream(new FileInputStream(filePath));

			// read in identification field length, colormap info and image type
			type[0] = s.read();
			type[1] = s.read();
			type[2] = s.read();

			// seek past the header and useless info
			s.skip(9);

			info[0] = s.read();
			info[1] = s.read();
			info[2] = s.read();
			info[3] = s.read();
			info[4] = s.read();
			info[5] = s.read();

			if (type[1] != 0 || (type[2] != TGA_RGB && type[2] != TGA_A && type[2] != TGA_RLE)) {
				returnError(s, TGA_BAD_IMAGE_TYPE);
			}

			imageWidth = info[0] + info[1] * 256;
			imageHeight = info[2] + info[3] * 256;
			iBits = info[4];

			// origin info stored in bits 4 and 5, 6 and 7 are always 0
			texFormat = info[5] >>> 4;

			size = imageWidth * imageHeight;

			/* make sure dimension is a power of 2 */
			if (checkSize(imageWidth) == 0 || checkSize(imageHeight) == 0) {
				returnError(s, TGA_BAD_DIMENSION);
			}

			/* make sure we are loading a supported type */
			if (iBits != 32 && iBits != 24 && iBits != 8) {
				returnError(s, TGA_BAD_BITS);
			}

			// skip size of header to get to pixels
			int skipAmount = type[0];
			if(skipAmount < 0) {
				skipAmount += 256;
			}
			
			if(skipAmount != 0) {
				s.skip(skipAmount);
			}

			if (type[2] == TGA_RLE) {
				imageData = getRLEData(s);
			} else {
				byte[] buf = new byte[size * (iBits / 8)];
				s.read(buf, 0, buf.length);
				imageData = getData(buf, size);
			}
			
			//stream.close();
			s.close();

			/* no image data */
			if (imageData == null) {
				returnError(s, TGA_BAD_DATA);
			}
			
			return 1;
		} catch (IOException e) {
			e.printStackTrace();
			return TGA_FILE_NOT_FOUND;
		}

	}
	Image getImage() {
		int pixel[] = new int[imageWidth * imageHeight];
		int z = 0;
		int red, green, blue, alpha;

		int startX = 0, maxX = 0, startY = 0, maxY = 0, xInc = 1, yInc = 1;

		// set start and end values based on origin data
		switch (texFormat) {
			case BOTTOM_LEFT_ORIGIN :
				{
					startX = 0;
					startY = 0;
					maxX = imageWidth - 1;
					maxY = imageHeight - 1;
					break;
				}
			case BOTTOM_RIGHT_ORIGIN :
				{
					startX = imageWidth - 1;
					startY = 0;
					maxX = -1;
					maxY = imageHeight - 1;
					xInc = -1;
					break;
				}
			case TOP_LEFT_ORIGIN :
				{
					startX = 0;
					startY = imageHeight - 1;
					maxX = imageWidth - 1;
					maxY = -1;
					yInc = -1;
					break;
				}
			case TOP_RIGHT_ORIGIN :
				{
					startX = imageWidth - 1;
					startY = imageHeight - 1;
					maxX = -1;
					maxY = -1;
					xInc = -1;
					yInc = -1;
					break;
				}
		}

		if (iBits == 32)
			for (int i = startX; i != maxX; i += xInc)
				for (int j = startY; j != maxY; j += yInc) {
					red = imageData[(j * 4) + (i * (maxY * 4))] & 0xff;
					green = imageData[(j * 4) + (i * (maxY * 4)) + 1] & 0xff;
					blue = imageData[(j * 4) + (i * (maxY * 4)) + 2] & 0xff;
					alpha = imageData[(j * 4) + (i * (maxY * 4)) + 3] & 0xff;
					pixel[z++] = (alpha << 24) | (red << 16) | (green << 8) | blue;
				}
		if (iBits == 24)
			for (int i = startX; i != maxX; i += xInc)
				for (int j = startY; j != maxY; j += yInc) {
					red = imageData[(j * 3) + (i * (maxY * 3))] & 0xff;
					green = imageData[(j * 3) + (i * (maxY * 3)) + 1] & 0xff;
					blue = imageData[(j * 3) + (i * (maxY * 3)) + 2] & 0xff;
					pixel[z++] = (255 << 24) | (red << 16) | (green << 8) | blue;
				}
		if (iBits == 8)
			for (int i = startX; i != maxX; i += xInc)
				for (int j = startY; j != maxY; j += yInc) {
					red = imageData[(j * 3) + (i * (maxY * 3))] & 0xff;
					green = imageData[(j * 3) + (i * (maxY * 3)) + 1] & 0xff;
					blue = imageData[(j * 3) + (i * (maxY * 3)) + 2] & 0xff;
					pixel[z++] = (255 << 24) | (red << 16) | (green << 8) | blue;
				}
		Toolkit tk = Toolkit.getDefaultToolkit();
		return tk.createImage(new MemoryImageSource(imageWidth, imageHeight, pixel, 0, imageWidth));
	}

	byte[] getRLEData(InputStream stream) throws IOException {
		// Create some variables to hold the rleID, current colors read, channels, & stride.
		int rleID = 0;
		int colorsRead = 0;
		int channels = iBits / 8;
		int stride = channels * imageWidth;
		int i = 0;

		// Next we want to allocate the memory for the pixels and create an array,
		// depending on the channel count, to read in for each pixel.
		byte[] data = new byte[stride * imageHeight];
		byte[] pColors = new byte[channels];

		// Load in all the pixel data
		int size = imageWidth * imageHeight;
		while (i < size) {
			// Read in the current color count + 1
			rleID = stream.read();

			// Check if we don't have an encoded string of colors
			if (rleID >> 7 == 0) {
				// Increase the count by 1
				rleID++;

				// Go through and read all the unique colors found
				while (rleID > 0) {
					// Read in the current color
					stream.read(pColors, 0, pColors.length);

					// Store the current pixel in our image array
					data[colorsRead + 0] = pColors[2];
					data[colorsRead + 1] = pColors[1];
					data[colorsRead + 2] = pColors[0];

					// If we have a 4 channel 32-bit image, assign one more for the alpha
					if (iBits == 32) {
						data[colorsRead + 3] = pColors[3];
					}

					// Increase the current pixels read, decrease the amount
					// of pixels left, and increase the starting index for the next pixel.
					i++;
					rleID--;
					colorsRead += channels;
				}
			}
			// Else, let's read in a string of the same character
			else {
				// Minus the 128 ID + 1 (127) to get the color count that needs to be read
				rleID -= 127;

				// Read in the current color, which is the same for a while
				stream.read(pColors, 0, pColors.length);

				// Go and read as many pixels as are the same
				while (rleID > 0) {
					// Assign the current pixel to the current index in our pixel array
					data[colorsRead + 0] = pColors[2];
					data[colorsRead + 1] = pColors[1];
					data[colorsRead + 2] = pColors[0];

					// If we have a 4 channel 32-bit image, assign one more for the alpha
					if (iBits == 32) {
						data[colorsRead + 3] = pColors[3];
					}

					// Increase the current pixels read, decrease the amount
					// of pixels left, and increase the starting index for the next pixel.
					i++;
					rleID--;
					colorsRead += channels;
				}

			}

		}

		return data;
	}
}