package com.gac.easyokhttp.cookie;


import android.text.TextUtils;
import android.util.Log;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import static okhttp3.internal.Util.UTC;

public class CookiesManager implements CookieJar {

      //  private final PersistentCookieStore cookieStore;
       // private Context context;

        public CookiesManager(){
         //   cookieStore = new PersistentCookieStore(context);
           // this.context = context;
        }
        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
//            if(url.toString().contains("login")){
                   StringBuilder sb = new StringBuilder();


                    if(cookies != null && cookies.size() == 1){
                        sb.append(cookies.get(0).name()+","+cookies.get(0).value());
                       // CookieSave.setJseesionId(sb.toString());
                        Log.d("httputils","cookie0:"+toString(cookies.get(0)));
                    }
                    if(cookies != null && cookies.size() == 2){
                        String cookie = cookies.get(0).name()+","+cookies.get(0).value();
                        String cookie1 = cookies.get(1).name()+","+cookies.get(1).value();
                        if(cookies.get(0).name().equals("remember-me")){
//                            CookieSave.setRememberMe(cookie);
//                            CookieSave.setJseesionId(cookie1);
                        }else{
//                            CookieSave.setJseesionId(cookie);
//                            CookieSave.setRememberMe(cookie1);
                        }
                        sb.append(cookies.get(0).name()+","+cookies.get(0).value()+",");
                        sb.append(cookies.get(1).name()+","+cookies.get(1).value());
                        Log.d("httputils","receive cookie0:"+toString(cookies.get(0)));

                        Log.d("httputils","receive cookie1:"+toString(cookies.get(1)));
                       // CookieSave.setCookie(sb.toString());
                    }


//                    System.out.println("cookies:"+sb.toString());
//
//                    SPUtils.setCookie(sb.toString(),context);
                }


//: JSESSIONID=8B8B7E4D8A9674EC37B68690AA5F81D9;
    public String toString(Cookie cookie) {
        if(cookie == null){
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(cookie.name());
        result.append('=');
        result.append(cookie.value());

        if (cookie.persistent()) {
            if (cookie.expiresAt() == Long.MIN_VALUE) {
                result.append("; max-age=0");
            } else {
                DateFormat rfc1123 = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss 'GMT'", Locale.US);
                rfc1123.setLenient(false);
                rfc1123.setTimeZone(UTC);

                result.append("; Expires=").append(rfc1123.format(cookie.expiresAt()));
            }
        }

        if (!cookie.hostOnly()) {
            result.append("; domain=").append(cookie.domain());
        }

        result.append("; Path=").append(cookie.path());

        if (cookie.secure()) {
            result.append("; secure");
        }

        if (cookie.hostOnly()) {
            result.append("; HttpOnly");
        }

        return result.toString();
    }
      List<Cookie> cookies  = new ArrayList<>();
        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            cookies.clear();
            String nowdate = getDateStr(new Date());
            Calendar dd = Calendar.getInstance();//定义日期实例
            dd.setTime(new Date());//设置日期起始时间
            dd.add(Calendar.YEAR,3);
            Date date = calendar2Date(dd);
            String[] remember = null;
          //  L.d("httpUtils","save rememberme:"+CookieSave.getRememberMe());
//            if(!TextUtils.isEmpty(CookieSave.getRememberMe())){
//                remember = CookieSave.getRememberMe().split(",");
//            }
          //  String[]
            Cookie.Builder builder_remember = new Cookie.Builder();

            if(remember != null && remember.length ==2){
                builder_remember.name(remember[0]).value(remember[1]).domain(url.host());
                builder_remember.expiresAt(date.getTime());

            }else{
                builder_remember.name("remember-me").value("ffdadafddaff").domain(url.host());
                builder_remember.expiresAt(date.getTime());
            }

            Cookie.Builder builder_sessionId = new Cookie.Builder();
            String[] seesionId = null;
//            String str_session = CookieSave.getSessionId();
//            L.d("httpUtils","save sessionId:"+str_session);
//            if(!TextUtils.isEmpty(str_session)){
//                seesionId = str_session.split(",");
//            }

            if(seesionId != null && seesionId.length ==2){
                builder_sessionId.name(seesionId[0]).value(seesionId[1]);
                builder_sessionId.domain(url.host());
                builder_sessionId.expiresAt(date.getTime());
            }else{
                builder_sessionId.name("JSESSIONID").value("ffdadafddaff");
                builder_sessionId.domain(url.host());
                builder_sessionId.expiresAt(date.getTime());
               // builder1.name().value().domain(url.host());
            }
             cookies.add(builder_remember.build());
             cookies.add(builder_sessionId.build());

//            L.d("httputils","send cookies cookie0:"+toString(cookies.get(0))+" cookie1:"+toString(cookies.get(1)));
            return cookies;
        }
        public static String DATEFORMAT = "yyyy-MM-dd HH:mm:ss";
        public static String getDateStr(Date date){
             return new SimpleDateFormat(DATEFORMAT).format(date);
        }
        public static Date calendar2Date(Calendar calendar){
            return calendar.getTime();
        }

}