package com.homata.utils;

//import java.net.URLEncoder;

//import play.Play;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
public class Utils {

    private static String local = "ja";       // アプリケーションのロケール

    /**
     * アプリケーションのロケールを取得
     * @return ロケール文字列
     */
    public static String getLocal() {
        return local;
    }
    /**
     * アプリケーションのロケールを日本語に設定
     */
    public static void setLocalJa() {
        local = "ja";       // 日本語
    }
    /**
     * アプリケーションのロケールを英語に設定
     */
    public static void setLocalEn() {
        local = "en";       // 英語
    }

	/**
	 * リソースからメッセージを取得
	 * @param key メッセージのキー名
	 * @param obj 引数データ
	 * @return メッセージ
	 */
	public static String getMessagesEn(String key, Object... obj) {
		return Messages.get(new Lang(Lang.forCode(local)), key, obj);
	}

	/**
	 * 数値変換可能な文字列かをチェックする
	 * @param val 数値文字列
	 * @return boolen
	 */
    /*
    public static boolean isNumber(String val) {
        try {
           Integer.parseInt(val);
           return true;
        } catch (NumberFormatException e) {
           return false;
    	} catch (Exception e) { 
           return false;
        } 
    }
    */
	/**
	 * 浮動小数点型数値変換可能な文字列かをチェックする
	 * @param val 数値文字列
	 * @return boolen
	 */
    /*
    public static boolean isDoubleNumber(String val) {
        try {
           Double.parseDouble(val);
           return true;
        } catch (NumberFormatException e) {
           return false;
    	} catch (Exception e) { 
           return false;
        }
    }
    */
	
	/**
	 * カレントディレクトリを取得する
	 * @return カレントディレクトリ文字列
	 */
	  public static String getCurrentDirectory() {
		  Logger.debug("currentDirectory()");
		  //カレントディレクトリを取得する
		  // http://www.geocities.co.jp/AnimeComic-Ink/2723/tips/java/4.html
		  String currentDirectory = new File(".").getAbsoluteFile().getParent();
		  if (!currentDirectory.endsWith(File.separator)) {
			  currentDirectory = currentDirectory + File.separator;
		  } else if (currentDirectory.endsWith(File.separator)) {
			  currentDirectory = currentDirectory.substring(0, currentDirectory.length() - 1);
		  }
		  Logger.debug("current=" + currentDirectory);
		  return currentDirectory;
	  }
  
	  /*
	   * tempディレクトリ作成
	   * @return tempディレクトリ文字列
	   */
	  /*
	  public static File createTempDirectory(String prefix, String suffix, File directory) throws IOException {
		  final File temp = File.createTempFile(prefix, suffix, directory);

		  if (!temp.delete()) {
			  throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
		  }

		  if (!temp.mkdir()) {
			  throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
		  }
		  return temp;
	  }
	  */
	  
	  /*
	   * 現在時刻
	   */
	  public static String getCurrentDateString()
	  {
	      //==== 現在時刻を取得 ====//
	      Date date = new Date();
	      //==== 表示形式を設定 ====//
	      //SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	      return sdf.format(date);
	  }

}
