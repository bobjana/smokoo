package za.co.zynafin.smokoo.io;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Constants;

@Component
public class SmokooConnector {

	private static final Logger log = Logger.getLogger(SmokooConnector.class);

	private HttpClient httpClient;
	private String sessionId;
	private boolean loggedIn;

	// NOTE: Used for testing purposes
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public SmokooConnector() {
		MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
		httpConnectionManager.getParams().setConnectionTimeout(5000);
		this.httpClient = new HttpClient(httpConnectionManager);
		httpClient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
		
		Executors.newSingleThreadExecutor().execute(new InitializeTask());
	}

	public SmokooConnector(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public String get(String url) {
		GetMethod getMethod = new GetMethod(url);
		Map<String,String> defaultGetRequestHeaders = getDefaultGetRequestHeaders();
		for (String property : defaultGetRequestHeaders.keySet()){
			getMethod.setRequestHeader(property, defaultGetRequestHeaders.get(property));
		}

		getMethod.setRequestHeader("Referer", url);
		getMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		getMethod.setRequestHeader("Cookie",
						"__utmz=1.1293638289.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); refererstr=http%3A%2F%2Fwww.smokoo.co.za%2F; __utma=1.255417079.1293671363.1295037722.1295068168.43; __utmc=1; __utmb=1.11.10.1295068168; "
								+ getSessionIdCookie());
		int status;
		try {
			log.debug("Before execute of http get: " + url);
			status = httpClient.executeMethod(getMethod);
			log.debug("after execute of http get - " + status);
			if (HttpStatus.SC_OK != status) {
				throw new RuntimeException("Unable to get smokoo content - " + status);
			}
			return getMethod.getResponseBodyAsString();
		} catch (Exception e) {
			log.error("Get request failed", e);
			return null;
		} finally {
			getMethod.releaseConnection();
		}

	}

	private Map<String, String> getDefaultGetRequestHeaders() {
		Map<String,String> headers = new HashMap<String, String>();
		headers.put("Connection", "keep-alive");
		headers.put("Keep-Alive", "115");
		headers.put("X-Prototype-Version", "1.6.1");
		headers.put("X-Requested-With", "XMLHttpRequest");
		headers.put("Accept", "text/javascript, text/html, application/xml, text/xml, */*");
		headers.put("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.224 Safari/534.10");
		headers.put("Accept-Encoding", "gzip,deflate,sdch");
		headers.put("Accept-Language", "en-US,en;q=0.8");
		headers.put("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		return headers;
	}

	public String post(String url, NameValuePair[] parameters) {
		PostMethod postMethod = null;
		try {
			while (!isLoggedIn(url)) {
				log.debug("Not yet logged in, waiting.....");
				Thread.sleep(2000);
			}

			postMethod = new PostMethod(url);
			postMethod.addParameters(parameters);

			postMethod.setRequestHeader("Connection", "keep-alive");
			postMethod.setRequestHeader("Referer", Constants.DEFAULT_URL);
			postMethod.setRequestHeader("Cache-Control", "max-age=0");
			postMethod.setRequestHeader("Origin", Constants.DEFAULT_URL);
			postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			postMethod.setRequestHeader("Accept",
					"application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
			postMethod
					.setRequestHeader(
							"User-Agent",
							"Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.224 Safari/534.10");
			postMethod.setRequestHeader("Accept-Encoding", "gzip,deflate,sdch");
			postMethod.setRequestHeader("Accept-Language", "en-US,en;q=0.8");
			postMethod.setRequestHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
			postMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
			postMethod
					.setRequestHeader(
							"Cookie",
							"__utmz=1.1293638289.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); refererstr=http%3A%2F%2Fwww.smokoo.co.za%2F; __utma=1.255417079.1293671363.1295037722.1295068168.43; __utmc=1; __utmb=1.11.10.1295068168; "
									+ getSessionIdCookie());

			int status = httpClient.executeMethod(postMethod);
		} catch (Exception e) {
			log.error("Post request failed", e);
			return null;
		} finally {
			if (postMethod != null) {
				postMethod.releaseConnection();
			}
		}
		return null;
	}

	private boolean isLoggedIn(String url) {
		if (Constants.LOGIN_URL.equals(url)) {
			return true;
		}
		return loggedIn;
	}

	private void login() {
		if (StringUtils.isEmpty(sessionId)){
			log.debug("Session ID is null thus unable to login....");
			return;
		}
		log.debug("Performing login process....");
		get(Constants.LOGOUT_URL);
		NameValuePair[] parameters = new NameValuePair[] { new NameValuePair("href_from", "http://www.smokoo.co.za"),
				new NameValuePair("email", "bobjana@gmail.com"), new NameValuePair("pwd", "neojl1v") };
		post(Constants.LOGIN_URL, parameters);
		loggedIn = true;
		log.debug("Login process completed");
	}

	private void initSessionId() {
		try {
			log.debug("Init session id");
			if (sessionId == null) {
				GetMethod initGet = new GetMethod(Constants.DEFAULT_URL);
				Map<String,String> defaultGetRequestHeaders = getDefaultGetRequestHeaders();
				for (String property : defaultGetRequestHeaders.keySet()){
					initGet.setRequestHeader(property, defaultGetRequestHeaders.get(property));
				}
				httpClient.executeMethod(initGet);
				Cookie[] cookies = httpClient.getState().getCookies();
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("PHPSESSID")) {
						sessionId = cookie.getValue();
						log.debug("Session ID = " + sessionId);
						long expiry = 12*60*1000;
						if (cookie.getExpiryDate() != null){
							expiry = cookie.getExpiryDate().getTime() - new Date().getTime();
						}
						Executors.newScheduledThreadPool(1).schedule(new InitializeTask(),expiry,TimeUnit.MILLISECONDS);
					}
				}
			}
		} catch (Exception e) {
			log.error("Unable to init session id, retry in 60 seconds....",e);
			Executors.newScheduledThreadPool(1).schedule(new InitializeTask(),60,TimeUnit.SECONDS);
		}
	}

	private String getSessionIdCookie() {
		if (sessionId == null) {
			return "";
		}
		return "PHPSESSID=" + sessionId + ";";
	}

	private class InitializeTask implements Runnable{

		@Override
		public void run() {
			initSessionId();
			login();
		}
		
	}
}
