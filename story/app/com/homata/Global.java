package com.homata;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.mvc.Action;
import play.mvc.Http.Cookie;
import play.mvc.Http.Request;

import com.avaje.ebean.Ebean;

import play.mvc.Http;
import play.mvc.SimpleResult;
import play.mvc.Results;
import play.libs.F;

public class Global extends GlobalSettings {
	@Override
	public void onStart(Application app) {
	}

	@Override
	public void onStop(Application app) {
	}

	@Override
	public Action<?> onRequest(Request request, Method actionMethod) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS Z");
		Cookie cookie = request.cookies().get("DS");
		Logger.info(String.format("%s\t%s %s\t%s\t%s\t%s",
				dateFormat.format(new Date()),
				request.method(),
				request.uri(),
				request.getHeader("User-Agent"),
				request.remoteAddress(),
				cookie != null ? cookie.value() : "-"
				));
		return super.onRequest(request, actionMethod);
	}

    @Override
    public F.Promise<SimpleResult> onHandlerNotFound(Http.RequestHeader requestHeader) {
        // This is here to make sure that the context is set, there is a test that asserts
        // that this is true
        //Http.Context.current().session().put("onHandlerNotFound", "true");
        return F.Promise.<SimpleResult>pure(Results.notFound());
    }
}
