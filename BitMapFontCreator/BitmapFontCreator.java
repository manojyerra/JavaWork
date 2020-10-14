import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.awt.*;
import javax.imageio.*;
import java.awt.image.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

class BitmapFontCreator
{
	int fontSize = 18;
	String fontName = "Helvetica";
	int fontStyle = Font.PLAIN;
	
	Font font = new Font(fontName, fontStyle, fontSize);
	
	String letters1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	String letters2 = "abcdefghijklmnopqrstuvwxyz";
	String letters3 = "0123456789";
	String letters4 = "`!\"$%^&*()-_=+[{]};:'@#\\|,<.>/?~";
	
	String fontImgName = fontName+"_"+fontSize+".png";
	String uvFileName = fontName+"_"+fontSize+".txt";
	
	BufferedWriter bw = new BufferedWriter(new FileWriter(uvFileName));
	Graphics globalGraphics = null;

	public static void main(String[] args) throws Exception
	{
		new BitmapFontCreator();
	}
	
	BitmapFontCreator() throws Exception
	{
		BufferedImage tempImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		globalGraphics = tempImg.getGraphics();
		
		int imgWidth = TextWidth("ABCDEFGHIJKLMNOPQRSTUVWXYZ", font) + 26*5;
		
		BufferedImage bufImg = new BufferedImage(imgWidth, fontSize*7, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = bufImg.createGraphics();
		g.setFont(font);
		g.setColor(Color.black);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		

		int horGap = 3;
		int verGap = 4;
		int x = 3;
		int y = verGap+fontSize;

		DrawOnTexture(letters1, g, x, y, horGap);
		
		y += verGap+fontSize;
		DrawOnTexture(letters2, g, x, y, horGap);

		y += verGap+fontSize;
		DrawOnTexture(letters3, g, x, y, horGap);

		y += verGap+fontSize;
		DrawOnTexture(letters4, g, x, y, horGap);
		
		g.dispose();
		
		ImageIO.write(bufImg, "png", new File(fontImgName));
		
		bw.close();
	}

	void DrawOnTexture(String str, Graphics2D g, int startX, int startY, int horGap) throws Exception
	{
		int x = startX;
		int y = startY;
		
		for(int i=0; i<str.length(); i++)
		{
			String charStr = ""+str.charAt(i);
			int letterWidth = TextWidth(charStr, font);
			
			g.drawString(charStr, x, y);

			String line = charStr +" "+(x-1)+" "+(y-fontSize)+" "+(letterWidth+2)+" "+fontSize;
			bw.write(line, 0, line.length());
			bw.newLine();
			
			x += letterWidth+horGap;
		}
		
	}

	int TextWidth(String text, Font font)
	{
		//AffineTransform affinetransform = new AffineTransform();
		//FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
		//int textwidth = (int)(font.getStringBounds(text, frc).getWidth());
		//return textwidth;
		
		FontMetrics metrics = globalGraphics.getFontMetrics(font);
		int hgt = metrics.getHeight();
		return metrics.stringWidth(text);
	}
}

