package com.homata.controllers;

import play.*;
import play.mvc.*;
import views.html.*;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.Json;

import play.mvc.Http.RequestBody;
import play.mvc.Http.Request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

//import java.util.Map;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.homata.utils.Utils;

// https://github.com/boatmeme/microsoft-translator-java-api
import com.memetix.mst.translate.Translate;
import com.memetix.mst.speak.Speak;
import com.memetix.mst.language.Language;
import com.memetix.mst.language.SpokenDialect;

// http://www.javadrive.jp/java2d/graphics2d/index1.html
//import javax.swing.*;
//import java.awt.Graphics;
//import java.awt.Color;
//import java.io.IOException;
import java.util.Iterator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

// https://datamarket.azure.com/developer/applications/edit/story
public class RestServer extends Controller {

  /**
   * nullも判断できるisEmpty
   *
   * @param str 検索する文字列
   * @return bool値
   */
  protected static boolean isEmpty(String str) {
    if (str == null) {
      return true;
    } else if (str.isEmpty()) {
      return true;
    } else {
      return false;
    }
  }
 
  /**
   * 
   */
  public static Result get() {
    Logger.debug("RestServer.get()");
    String sourceText = "ほげ";
    int xx = 10, yy = 10, width = 400, height = 400;
    return imageData(sourceText, xx, yy, width, height);
  }
  
  /**
   * 
   */
  public static Result post() {
    Logger.debug("RestServer.post()");
    
    /*
     * http://gihyo.jp/dev/serial/01/engineer_toolbox/0028
     * http://www.crystal-creation.com/software/technical-information/programming/java/package/json/jackson.htm
    {"name":"sho322","age":28,"interests":["programming","basketball"]} 
    {"source":"おはようございます"} 
     */
    RequestBody body = request().body();
    String sourceText = "";
   
    try {
    	JsonNode rootNode = body.asJson();
        // 方法1:オブジェクトのフィールドを直接取得
    	/*
        for (JsonNode node : rootNode) {
            System.out.println(node);
        }
        */
        // 方法2:オブジェクトのフィールド名からフィールドを取得
        Iterator<String> fieldNames = rootNode.fieldNames();
        while (fieldNames.hasNext()) {
            String name  = fieldNames.next();
            String value = rootNode.path(name).toString();
            
      	    Logger.debug(name + ":" + value);
            System.out.println(name + ":" + value);
            
            if (name.equalsIgnoreCase("source")) {
                sourceText = value;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    int xx = 10, yy = 10, width = 400, height = 400;
    return imageData(sourceText, xx, yy, width, height);
  }
    /**
     * 
     * @return
     */
    public static Result imageData(String sourceText, int xx, int yy, int width, int height) {
      String translatedText = "";

      // 翻訳
      if (!isEmpty(sourceText)){
    	try {
    		translatedText = translate(sourceText);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return badRequest(e.getMessage());
    	}
      } else {
    	return badRequest("error");
      }

      BufferedImage bimg;
      byte[] bData;
    
	  try {
		bimg = makeImage(translatedText, xx, yy, width, height);
		bData = getBytes(bimg);
	  } catch (Exception e) {
		e.printStackTrace();
		return badRequest(e.getMessage());
	  }
    
	// http://www.kyoto-su.ac.jp/ccinfo/use_web/mine_contenttype/
    //response().setContentType("text/plain; charset=utf-8");
    //https://stackoverflow.com/questions/17873074/how-to-show-images-in-play-framework-2-1
    //response().setContentType("image/png");
    return ok(bData).as("image/png");
  }
  /*
   * 
   */
  public static String getTempFilename() {
	  String tmpfilename = "temp.png";
	  return tmpfilename;
  }
  private static String clientId = "story";
  private static String clientSecret = "sP4z5neCWSXwshK5ByAYgHIOx0654DzLhcMm9Z06w2U=";
  private static String apiKey = ""; // ????


  public static String translate(String sourceText) throws Exception {
	  return "translated: " + sourceText;
/*
	  // Set your Windows Azure Marketplace client info - See http://msdn.microsoft.com/en-us/library/hh454950.aspx
	  Translate.setClientId(clientId);
	  Translate.setClientSecret(clientSecret);
	  //String translatedText = Translate.execute("Bonjour le monde", Language.FRENCH, Language.ENGLISH);
	  //System.out.println(translatedText);
	  Logger.debug(sourceText);
	  String translatedText = Translate.execute(sourceText, Language.JAPANESE, Language.ENGLISH);
	  Logger.debug(translatedText);
	  //System.out.println(translatedText);
	  return translatedText;
*/
  }

  /*
  public static void speak(String text) throws Exception {
	 
      Speak.setKey(apiKey);
      Speak.setClientSecret(clientSecret);
      Speak.setClientId(clientId);

	  Speak.execute(text,SpokenDialect.JAPANESE_JAPAN);
  }
  */

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
  public static BufferedImage makeImage(String str, int xx, int yy, int width, int height) throws Exception {
      BufferedImage bimg = createImage(width, height);

      Graphics g = bimg.getGraphics();
      //g.setColor(Color.BLACK);	//黒
      g.setColor(Color.WHITE);		//白
      g.fillRect(0, 0, width, height);
      
      g.setColor(Color.BLACK);	//黒
      //g.setFont(new Font("Serif", Font.BOLD, 14));
      g.drawString(str, xx, yy);
      g.dispose();
      return bimg;
  }
	/**
	 * BufferedImageをバイト配列に変換する例. [2010-01-08]
	 * @param img イメージ
	 * @return バイト配列
	 * @throws IOException 変換できなかった場合
	 */
	public static byte[] getBytes(BufferedImage img) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		if (!ImageIO.write(img, "PNG", bos)) {
			throw new IOException("フォーマットが対象外");
		}
		return bos.toByteArray();
	}
}
