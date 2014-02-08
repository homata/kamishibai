package com.homata.controllers;

import play.*;
import play.mvc.*;
import views.html.*;

import play.Logger;
import play.mvc.Controller;
import play.mvc.Result;

//import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.homata.utils.Utils;


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
   * GETメソッド: オープンデータ一覧
   * @return メソッドの戻り値
   */
  public static Result get() {
    Logger.debug("get()");
    return ok();
  }
}
