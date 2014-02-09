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

import com.homata.utils.Utils;
import com.homata.utils.StoryImage;

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
import java.util.Date;
import java.util.Iterator;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

//https://commons.apache.org/proper/commons-lang/
import org.apache.commons.lang3.StringUtils;

/*
初期画像取得
------------
メソッド
	GET  /rest/v1/translate?ehon="http://hoge.com/ehon/ehon.png"
    ※  UR文字列はURLエンコードしてください
戻り値 
   画像データ(image/png)

画像合成
------------
メソッド
    POST /rest/v1/
    {
      "url" : "http://hoge.com/image.png",
      "script"  :  [
         {"text":"あいうえお", "x,"10","y,"10","width,"50","height,"50 },
         {"text":"かきくけこ", "x,"10","y,"30","width,"50","height,"50 },
     ]
     }
    ※  x,y,width,height: 数値はパーセント(%) 
戻り値
    URL 文字列 (text/plain) → "http://hoge.com/hoge.png"
    ※  UR文字列はURLエンコードされます

 http://localhost:9000/rest/v1/translate?ehon=/ehon/ehon1.png
 */


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
  /*
  public static String getTempFilename() {
	  String tmpfilename = "temp.png";
	  return tmpfilename;
  }
  */
 
  //------------------------------------------------
  // URL Parameter
  //------------------------------------------------

  /**
   * リクエストからパラメータのマップを取得する
   *
   * @return パラメータマップ
   *
   * @see <a href="http://netmark.jp/2012/12/play-framework-getpost.html">Play frameworkでGET/POSTでデータを受け取る</a>
   */
  protected static Map<String, String[]> getRequestParameter() {

    Map<String, String[]> params = null;

    if (request().method().equalsIgnoreCase("GET")) {
      //System.out.println("GET");
      params = request().queryString();
    } else if (request().method().equalsIgnoreCase("POST")) {
      //System.out.println("POST");
      params = request().body().asFormUrlEncoded();
    } else {
      return null;
    }
    return params;
  }

  /**
   * パラメータのマップから該当する値を返します
   *
   * @param key 検索するキー
   * @return パラメータ値
   * @throws Exception 不正なパラメータあり
   */
  protected static String getParameter(String key) throws Exception {
    return getParameter(getRequestParameter(), key);
  }

  /**
   * パラメータのマップから該当する値を返します (URLデコード付き)
   *
   * @param key 検索するキー
   * @return パラメータ値
   * @throws Exception 不正なパラメータあり
   */
  protected static String getParameterWithDecoding(String key) throws Exception {
    return getParameterWithDecoding(getRequestParameter(), key);
  }
  /**
   * パラメータのマップから該当する値を返します
   *
   * @param params パラメータのマップ
   * @param key 検索するキー
   * @return パラメータ値
   * @throws Exception 不正なパラメータあり
   */
  protected static String getParameter(Map<String, String[]> params, String key) throws Exception {
    if (params != null && params.containsKey(key)) {
      String v = params.get(key)[0];
      if (v != null && !v.isEmpty()) {
        return v;
      } else {
        throw new Exception("parameter get error: " + key);
      }
    }
    return null;
  }

  /**
   * パラメータのマップから該当する値を返します (URLデコード付き)
   *
   * @param params パラメータのマップ
   * @param key 検索するキー
   * @return パラメータ値
   */
  protected static String getParameterWithDecoding(Map<String, String[]> params, String key) throws Exception {
    String v;
    try {
        v = getParameter(params, key);
    } catch(Exception e) {
      throw e;
    }
    if (v == null) {
      return null;
    }
    try {
      return URLDecoder.decode(v, "utf-8");
    } catch (UnsupportedEncodingException e) {
    }
    return null;
  }

  /**
   * パラメータ名の正当性チェック
   * 
   * @param plist パラメータ名文字列リスト
   * @throws Exception 不正なパラメータあり
   */
  protected static void checkParameterString(String[] plist) throws Exception {
    Map<String, String[]> params = getRequestParameter();

    int length = plist.length;
    for (String key : params.keySet()) {
      //Logger.debug("paramete check=" + key);

      int ii;
      for (ii=0; ii<length; ii++) {
        String str = plist[ii];
        if (str.equalsIgnoreCase(key)) {
            break;
        }
      }
      if (ii == length) {
          throw new Exception("parameter error: " + key);
      }
    }
  }

  /**
   * 同じパラメータ名があるかチェック
   * 
   * @throws Exception 不正なパラメータあり
   */
/*
  protected static void checkDuplicateParameter() throws Exception {
    Map<String, String[]> params1 = getRequestParameter();
    Map<String, String[]> params2 = getRequestParameter();
    for (String key : params.keySet()) {
      //Logger.debug("paramete check=" + key);

      int ii;
      for (ii=0; ii<length; ii++) {
        String str = plist[ii];
        if (str.equalsIgnoreCase(key)) {
            break;
        }
      }
      if (ii == length) {
          throw new Exception("parameter error: " + key);
      }
    }
  }
*/
  
  //------------------------------------------------
  // configuration file
  //------------------------------------------------

  /**
   * コンフィグレーションファイル(conf/application.conf)から文字列を取得する
   *
   * @param key 検索するキー
   * @return 取得した文字列
   */
  protected static String getConfString(String key) {
    String str = play.Play.application().configuration().getString(key);
    //Logger.debug("get url=" + str);
    if (isEmpty(str) ) {
      return null;
    } else if (str.equalsIgnoreCase("null")) {
      return null;
    } else {
      return str;
    }
  }

  //-----------------------------------
  // GET
  //-----------------------------------
  /**
   * loadBufferedImage()
   */
  public static BufferedImage loadBufferedImage(String filename) {
    Logger.debug("loadBufferedImage()");
    BufferedImage bimg = null;

    try {
        if (filename.startsWith("http") || filename.startsWith("https")) { 
        	bimg = StoryImage.loadImageUrl(filename);
        } else {
        	// カレントディレクトリを取得する
        	String currentDirectory = Utils.getCurrentDirectory();
        	// フルパス名を作成
        	String fullname = currentDirectory + "public/images/ehon/" + filename;
        	Logger.debug("fullname=" + fullname);
        	bimg = StoryImage.loadImageFile(fullname);
        }
    } catch (Exception e) {
		e.printStackTrace();
	    bimg = null;
    }
    return bimg;
  }  
  
  /**
   * get()
   */
  public static Result get() {
    Logger.debug("RestServer.get()");
    byte[] bData = null;
 
    try {
    	String filename = getParameterWithDecoding("ehon");
        // 文字列の両端にある"(クオータ文字)を削除する
    	filename = StringUtils.strip(filename, "\"");
        Logger.debug("filenmae=" + filename);

    	BufferedImage bimg = loadBufferedImage(filename);
	    bData = StoryImage.getBytes(bimg);

	    //https://stackoverflow.com/questions/17873074/how-to-show-images-in-play-framework-2-1
        //response().setContentType("image/png");
        return ok(bData).as("image/png");
    } catch (Exception e) {
		e.printStackTrace();
		return badRequest(e.getMessage());
    }
  }
  
  //-----------------------------------
  // POST
  //-----------------------------------
  /**
   * post()
   */
  public static Result post() {
    Logger.debug("RestServer.post()");
    
    /*
     * http://gihyo.jp/dev/serial/01/engineer_toolbox/0028
     * http://www.crystal-creation.com/software/technical-information/programming/java/package/json/jackson.htm
    {"name":"sho322","age":28,"interests":["programming","basketball"]} 
    {"source":"おはようございます"} 
{
"url":"http://hoge.com/image.png",
"callback":"hoge",
"script":[
  {"text":"あいうえお", "x":10,"y":20,"width":30,"height":40},
  {"text":"かきくけこ", "x":11.1,"y":21.2,"width":31.3,"height":41.4}
]
}
     */
    RequestBody body = request().body();
    String sourceText = "";
   
    try {
    	JsonNode rootNode = body.asJson();

    	// URL
    	String url = rootNode.path("url").toString();
  	    url = StringUtils.strip(url, "\"");
  	    Logger.debug("url:" + url);

  	    BufferedImage canvas = loadBufferedImage(url);
 
  	    // callback
  	    String callback = rootNode.path("callback").toString();
  	    callback = StringUtils.strip(callback, "\"");
  	    Logger.debug("callback:" + callback);

  	    // script
  	    float x=0.0f;
  	    float y=0.0f;
  	    float width=100.0f;
  	    float height=100.0f;
  	 	JsonNode scriptNode = rootNode.get("script");
    	JsonNode current;
    	for (int ii=0; (current = scriptNode.get(ii)) != null; ii++) {
            Iterator<String> fieldNames = current.fieldNames();
            while (fieldNames.hasNext()) {
                String name  = fieldNames.next();
                String value = current.path(name).toString();
                
          	    Logger.debug(name + ":" + value);
          	    value = StringUtils.strip(value, "\"");
                
                if (name.equalsIgnoreCase("text")) {
                    sourceText = value;
                } else if (name.equalsIgnoreCase("x")) {
                	x = Float.parseFloat(value);
                } else if (name.equalsIgnoreCase("y")) {
                   	y = Float.parseFloat(value);
                } else if (name.equalsIgnoreCase("width")) {
                   	width = Float.parseFloat(value);
                } else if (name.equalsIgnoreCase("height")) {
                   	height = Float.parseFloat(value);
                }
            }
      	    Logger.debug("sourceText:" + sourceText);
      	    Logger.debug("x:" + x);
      	    Logger.debug("y:" + y);
      	    Logger.debug("width:" + width);
      	    Logger.debug("height:" + height);    		

      	    imageData(canvas, sourceText, x, y, width, height);
    	}
 	    String saveUrl = imageSave(canvas, url);
  	    
  	    if (isEmpty(callback)) {
      	    return ok(saveUrl).as("text/plain");
  	    } else {
      		//callbackFunc( {"url":[1,2,3]} );
  	        String text = callback + "({\"url\":\"" + saveUrl + "\"})"; 	
      	    return ok(text).as("application/json");
      	    //return ok(text).as("text/plain");
  	    }
    } catch (Exception e) {
        e.printStackTrace();
    }
	return badRequest();
  }
  //-----------------------------------
  // Util
  //-----------------------------------
    /**
     * @return
     */
    public static void imageData(BufferedImage canvas, String sourceText, float xx, float yy, float width, float height) {
      String translatedText = "";
      // 翻訳
      if (!isEmpty(sourceText)){
    	try {
    		translatedText = translate(sourceText);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return;
    	}
      } else {
  		return;
      }

	  try {
		  StoryImage.makeImage(canvas, translatedText, xx, yy, width, height);
	  } catch (Exception e) {
		e.printStackTrace();
	  }
    }

    /**
     * @return
     */
    public static String imageSave(BufferedImage canvas, String url) {
      String translatedText = "";

      String prefix   = "ehon_";
      String suffix   = ".png";
      String imagepath = "public/images/ehon/";
      String urlpath   = "assets/images/ehon/";
      String host      = request().host();
      // カレントディレクトリを取得する
      String currentDirectory = Utils.getCurrentDirectory();
      String imgDir = currentDirectory + imagepath;
      //String urlDir = currentDirectory + urlpath;
	  Logger.debug("imgDir:" + imgDir);
	  //Logger.debug("urlDir:" + urlDir);
	  Logger.debug("request().host():" + host);

	  String date = Utils.getCurrentDateString();

	  String filename = prefix + date + suffix;
	  String tempfile = imgDir + filename;
	  String saveUrl  = "http://" + host + "/" + urlpath + filename;

	  Logger.debug("filename:" + filename);
	  Logger.debug("tempfile:" + tempfile);
	  Logger.debug("saveUrl:" + saveUrl);

      try {
      	  StoryImage.saveImage(canvas, new File(tempfile));  
      } catch (IOException e) {
          e.printStackTrace();
      }
     
      return saveUrl;
  }
    
  public static String translate(String sourceText) throws Exception {
	  return sourceText;
/*
      private static String clientId = "story";
      private static String clientSecret = "sP4z5neCWSXwshK5ByAYgHIOx0654DzLhcMm9Z06w2U=";
      private static String apiKey = ""; // ????

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
}
