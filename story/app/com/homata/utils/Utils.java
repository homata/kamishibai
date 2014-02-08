package com.homata.utils;

//import java.net.URLEncoder;

//import play.Play;
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
}
