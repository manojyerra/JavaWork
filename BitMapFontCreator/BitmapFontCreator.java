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
	private int _fontSize = 13;
	private String _fontName = "Monaco";
	private int _fontStyle = Font.PLAIN;
	
	private Font _font = null;
	private int _descent = 0;
	
	private String _letters1 = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private String _letters2 = "abcdefghijklmnopqrstuvwxyz";
	private String _letters3 = "0123456789";
	private String _letters4 = "`!\"$%^&*()-_=+[{]};:'@#\\|,<.>/?~";
	
	private int _offSetX1 = 3;
	private int _offSetX2 = 3;
	private int _horGap = 3;
	private int _verGap = 4;
	
	private String _fontImgName = "img.png";
	private String _uvFileName = "uv.txt";
	
	private BufferedWriter _bw = new BufferedWriter(new FileWriter(_uvFileName));
	private Graphics _globalGraphics = null;

	public static void main(String[] args) throws Exception
	{
		new BitmapFontCreator();
	}
	
	BitmapFontCreator() throws Exception
	{
		BufferedReader br = new BufferedReader(new FileReader("FontInfo.txt"));
		_fontName = br.readLine();
		_fontSize = Integer.parseInt(br.readLine());
		br.close();
		
		System.out.println("FontName:"+_fontName);
		System.out.println("FontSize:"+_fontSize);
		
		_font = new Font(_fontName, _fontStyle, _fontSize);
		
		BufferedImage tempImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		_globalGraphics = tempImg.getGraphics();

		FontMetrics metrics = _globalGraphics.getFontMetrics(_font);
		_descent = metrics.getDescent();
		
		int imgWidth = GetMaxTextureWidth();
		int imgHeight = GetMaxTextureHeight();
		
		BufferedImage bufImg = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g = bufImg.createGraphics();
		g.setFont(_font);
		g.setColor(Color.black);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		int x = _offSetX1;
		int y = _verGap+_fontSize;

		DrawOnTexture(_letters1, g, x, y, _horGap);
		
		y += _verGap+_fontSize;
		DrawOnTexture(_letters2, g, x, y, _horGap);

		y += _verGap+_fontSize;
		DrawOnTexture(_letters3, g, x, y, _horGap);

		y += _verGap+_fontSize;
		DrawOnTexture(_letters4, g, x, y, _horGap);
		
		g.dispose();
		
		ImageIO.write(bufImg, "png", new File(_fontImgName));
		
		_bw.close();
	}

	void DrawOnTexture(String str, Graphics2D g, int startX, int startY, int horGap) throws Exception
	{
		int x = startX;
		int y = startY;
		
		g.setColor(Color.WHITE);
		
		for(int i=0; i<str.length(); i++)
		{
			String charStr = ""+str.charAt(i);
			int letterWidth = TextWidth(charStr, _font);
			
			g.drawString(charStr, x, y);

			String line = charStr +" "+(x-1)+" "+(y-_fontSize+_descent)+" "+(letterWidth+2)+" "+_fontSize;
			_bw.write(line, 0, line.length());
			_bw.newLine();
			
			x += letterWidth+horGap;
		}
	}
	
	int GetMaxTextureWidth()
	{
		int maxWidth = 0;
		
		int textWidth1 = TextWidth(_letters1, _offSetX1, _offSetX2, _horGap);
		int textWidth2 = TextWidth(_letters2, _offSetX1, _offSetX2, _horGap);
		int textWidth3 = TextWidth(_letters3, _offSetX1, _offSetX2, _horGap);
		int textWidth4 = TextWidth(_letters4, _offSetX1, _offSetX2, _horGap);
		
		if(textWidth1 > maxWidth) maxWidth = textWidth1;
		if(textWidth2 > maxWidth) maxWidth = textWidth2;
		if(textWidth3 > maxWidth) maxWidth = textWidth3;
		if(textWidth4 > maxWidth) maxWidth = textWidth4;
		
		return maxWidth;
	}
	
	int GetMaxTextureHeight()
	{
		return 4*(_verGap+_fontSize) + 3*_verGap;
	}

	int TextWidth(String text, Font font)
	{
		//AffineTransform affinetransform = new AffineTransform();
		//FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
		//int textwidth = (int)(font.getStringBounds(text, frc).getWidth());
		//return textwidth;
		
		FontMetrics metrics = _globalGraphics.getFontMetrics(font);
		int hgt = metrics.getHeight();
		return metrics.stringWidth(text);
	}
	
	int TextWidth(String text, int offSetX1, int offSetX2, int horGap)
	{
		int textLen = TextWidth(text, _font);
		return offSetX1 + offSetX2 + textLen + (text.length() * horGap);
	}
}

