package com.homata.utils;

//import java.net.URLEncoder;

//import play.Play;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javassist.bytecode.Descriptor.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import play.Logger;
import play.i18n.Lang;
import play.i18n.Messages;
//import play.libs.Crypto;

/**
 * 汎用ユーティリティクラス
 *
 * @author omata
 *
 */
public class StoryImage {

	//-----------------------------------
	// Image
	//-----------------------------------

	// http://www.ne.jp/asahi/hishidama/home/tech/java/image.html#ImageIO
	public static BufferedImage loadImageFile(String fileName) {
		InputStream is = null;
		try {
			  is = new FileInputStream(fileName);
			  BufferedImage bimg = ImageIO.read(is);
			  return bimg;
		      //byte[] bData = getBytes(bimg);
			  //return bData;
		  } catch (IOException e) {
			  throw new RuntimeException(e);
		  } finally {
			  if (is != null) try { is.close(); } catch (IOException e) {}
		  }
	  }
	  public static BufferedImage loadImageUrl(String url) {
		  ByteArrayOutputStream bais = new ByteArrayOutputStream();
		  InputStream is = null;

		  try {
			  URL u = new URL(url);
			  is = u.openStream ();
			  byte[] byteChunk = new byte[4096]; // Or whatever size you want to read in at a time.
			  int n;

			  while ( (n = is.read(byteChunk)) > 0 ) {
			    bais.write(byteChunk, 0, n);
			  }
			  return convertByteToImage(bais.toByteArray());
			  //return bais.toByteArray() ;
		  } catch (IOException e) {
			  e.printStackTrace ();
			  throw new RuntimeException(e);
		  }
		  finally {
			  if (is != null) try { is.close(); } catch (IOException e) {}
		}
	  }

	  /*
	   * convert byte array back to BufferedImage
	   */
	  // http://www.mkyong.com/java/how-to-convert-byte-to-bufferedimage-in-java/
	  public static BufferedImage convertByteToImage(byte[] bytes) {
		  InputStream in = null;

		  try {
			  // convert byte array back to BufferedImage
			  in = new ByteArrayInputStream(bytes);
			  BufferedImage bImageFromConvert = ImageIO.read(in);
			  return bImageFromConvert;
		  } catch (IOException e) {
			  e.printStackTrace ();
			  throw new RuntimeException(e);
		  }
		  finally {
			  if (in != null) try { in.close(); } catch (IOException e) {}
		}
	  }

	  // http://www.ne.jp/asahi/hishidama/home/tech/java/image.html#h2_write
	   public static BufferedImage createImage(int width, int height) {
	      return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	   }
	   public static BufferedImage createBufferedImage(Image img) {
	      BufferedImage bimg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);

	      Graphics g = bimg.getGraphics();
	      g.drawImage(img, 0, 0, null);
	      g.dispose();
	      return bimg;
		}
		
	  //http://www.javadrive.jp/java2d/graphics2d/index1.html
	  //http://www.ne.jp/asahi/hishidama/home/tech/java/image.html
	  //http://www.javadrive.jp/applet/graphics/
	  //http://sourceforge.jp/projects/opengion/scm/svn/blobs/568/trunk/uap/webapps/gf/src/org/opengion/hayabusa/servlet/MakeImage.java
	  public static BufferedImage makeImage(BufferedImage bimg, String str, float xx, float yy, float width, float height) throws Exception {

		  int x = (int)((float)bimg.getWidth()  * (xx     / 100.0f) + 0.5f);
		  int y = (int)((float)bimg.getHeight() * (yy     / 100.0f) + 0.5f);
		  int w = (int)((float)bimg.getWidth()  * (width  / 100.0f) + 0.5f);
		  int h = (int)((float)bimg.getHeight() * (height / 100.0f) + 0.5f);
		  
	      Graphics g = bimg.getGraphics();
	      //g.setColor(Color.BLACK);	//黒
	      g.setColor(Color.WHITE);		//白
	      g.fillRect(x, y, w, h);
	      
	      g.setColor(Color.BLACK);	//黒
	      g.setFont(new Font(Font.DIALOG_INPUT, Font.PLAIN, 14));
	      //g.drawString(str, x, y + 16);
	      drawStringCenter(g, x, y, w, h, str);
	      g.dispose();
	      return bimg;
	  }
	  
	  /*
	   * 文字列を中央に描画する
	   */
	  // http://yu1rows.blogspot.jp/2011/08/blog-post_25.html
	    public static void drawStringCenter(Graphics g, int xx, int hh, int ww, int yy, String text) {
	        // Calc draw position
	        FontMetrics fm = g.getFontMetrics();
	        Rectangle rectText = fm.getStringBounds(text, g).getBounds();
	        int x = (ww - rectText.width) / 2;
	        int y = (hh - rectText.height) / 2 + fm.getMaxAscent();
	        // Draw text
	        g.drawString(text, x + xx, y + yy);
	    }
	  
	  
		/**
		 * BufferedImageをバイト配列に変換する例. [2010-01-08]
		 * @param img イメージ
		 * @return バイト配列
		 * @throws IOException 変換できなかった場合
		 */
		public static byte[] getBytes(BufferedImage img) throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			if (!ImageIO.write(img, "png", bos)) {
				throw new IOException("unsupported format");
			}
			return bos.toByteArray();
		}
		/*
		 * 画像ファイル保存
		 */
		// http://www.ne.jp/asahi/hishidama/home/tech/java/image.html
		public static void saveImage(BufferedImage bimg, File file) throws IOException {
			if (!ImageIO.write(bimg, "png", file)) {
				throw new IOException("unsupported format");
			}
		}

}
