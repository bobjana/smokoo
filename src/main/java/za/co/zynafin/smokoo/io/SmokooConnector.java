package za.co.zynafin.smokoo.io;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.stereotype.Component;

import za.co.zynafin.smokoo.Constants;

@Component
public class SmokooConnector {

	private HttpClient httpClient;
	private String sessionId;
	
	//NOTE: Used for testing purposes
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}
	
	public SmokooConnector(){
		this.httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
		initSessionId();
		login();
	}

	public SmokooConnector(HttpClient httpClient){
		this.httpClient = httpClient;
	}
	
	public String get(String url){
		GetMethod getMethod = new GetMethod(url);
		
		getMethod.setRequestHeader("Connection", "keep-alive");
		getMethod.setRequestHeader("Keep-Alive", "115");
		getMethod.setRequestHeader("X-Prototype-Version", "1.6.1");
		getMethod.setRequestHeader("X-Requested-With", "XMLHttpRequest");
		getMethod.setRequestHeader("Referer", url);
		getMethod.setRequestHeader("Accept", "text/javascript, text/html, application/xml, text/xml, */*");
		getMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.224 Safari/534.10");
		getMethod.setRequestHeader("Accept-Encoding", "gzip,deflate,sdch");
		getMethod.setRequestHeader("Accept-Language", "en-US,en;q=0.8");
		getMethod.setRequestHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		getMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		getMethod.setRequestHeader("Cookie", "__utmz=1.1293638289.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); refererstr=http%3A%2F%2Fwww.smokoo.co.za%2F; __utma=1.255417079.1293671363.1295037722.1295068168.43; __utmc=1; __utmb=1.11.10.1295068168; " + getSessionIdCookie());
		int status;
		try {
			status = httpClient.executeMethod(getMethod);
			if (HttpStatus.SC_OK != status) {
				throw new RuntimeException("Unable to get smokoo content - " + status);
			}
			return getMethod.getResponseBodyAsString();
		} catch (NoRouteToHostException e){
			return null;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		finally{
			getMethod.releaseConnection();
		}
		
	}
	
	public String post(String url, NameValuePair[] parameters){
		PostMethod postMethod = new PostMethod(url);
		postMethod.addParameters(parameters);
		
		postMethod.setRequestHeader("Connection", "keep-alive");
		postMethod.setRequestHeader("Referer", Constants.DEFAULT_URL);
		postMethod.setRequestHeader("Cache-Control", "max-age=0");
		postMethod.setRequestHeader("Origin", Constants.DEFAULT_URL);
		postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		postMethod.setRequestHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
		postMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.224 Safari/534.10");
		postMethod.setRequestHeader("Accept-Encoding", "gzip,deflate,sdch");
		postMethod.setRequestHeader("Accept-Language", "en-US,en;q=0.8");
		postMethod.setRequestHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
		postMethod.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
		postMethod.setRequestHeader("Cookie", "__utmz=1.1293638289.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); refererstr=http%3A%2F%2Fwww.smokoo.co.za%2F; __utma=1.255417079.1293671363.1295037722.1295068168.43; __utmc=1; __utmb=1.11.10.1295068168; " + getSessionIdCookie());
		
		try {
			int status = httpClient.executeMethod(postMethod);
//			String response = postMethod.getResponseBodyAsString();
//			System.out.println(response);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
		finally{
			postMethod.releaseConnection();
		}
		return null;
	}
	
	private void login() {
		get(Constants.LOGOUT_URL);
		NameValuePair[] parameters = new NameValuePair[]{new NameValuePair("href_from","http://www.smokoo.co.za"),
				new NameValuePair("email","bobjana@gmail.com"),
				new NameValuePair("pwd","neojl1v")};
		post(Constants.LOGIN_URL, parameters);
	}
	
	private void initSessionId() {
		try {
			if (sessionId == null){
				GetMethod initGet = new GetMethod(Constants.DEFAULT_URL);
				httpClient.executeMethod(initGet);
				 Cookie[] cookies = httpClient.getState().getCookies();
				 for (Cookie cookie : cookies){
					 if (cookie.getName().equals("PHPSESSID")){
						 sessionId = cookie.getValue();
					 }
				 }
			}
		} catch (Exception e) {
			throw new RuntimeException("Unable to establish session id",e);
		}
	}

	private String getSessionIdCookie() {
		if (sessionId == null){
			return "";
		}
		return "PHPSESSID=" + sessionId + ";";
	}

	public static void main(String[] args) throws Exception{
		SmokooConnector connector = new SmokooConnector();
//		String content = connector.get(Constants.AUCTION_BID_HISTORY_URL + "smokoo_auction_50c_220920_e16adc");
//		System.out.println(content);
		

		NameValuePair[] parameters = new NameValuePair[]{new NameValuePair("href_from","http://www.smokoo.co.za"),
				new NameValuePair("email","bobjana@gmail.com"),
				new NameValuePair("pwd","neojl1v")};
		connector.post(Constants.DEFAULT_URL + "/login.php", parameters);
		
		parameters = new NameValuePair[]{new NameValuePair("auction_id","10737")};
		String content = connector.post(Constants.DEFAULT_URL + "/make_bid.php", parameters);
		System.out.println(content);
		
//	   // Get initial state object
//    HttpState initialState = new HttpState();
//    
//    // Initial set of cookies can be retrieved from persistent storage 
//    // and re-created, using a persistence mechanism of choice,
//    Cookie mycookie = new Cookie(".foobar.com", "mycookie", "stuff", 
//            "/", null, false);
//    
//    // and then added to your HTTP state instance
//    initialState.addCookie(mycookie);
//    
//    // Get HTTP client instance
//    HttpClient httpclient = new HttpClient();
//    httpclient.getHttpConnectionManager().
//            getParams().setConnectionTimeout(30000);
//    httpclient.setState(initialState);
//    
//    // RFC 2101 cookie management spec is used per default
//    // to parse, validate, format & match cookies
//    httpclient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
//    
//    // A different cookie management spec can be selected
//    // when desired
//    
//    //httpclient.getParams().setCookiePolicy(CookiePolicy.NETSCAPE);
//    // Netscape Cookie Draft spec is provided for completeness
//    // You would hardly want to use this spec in real life situations
//    // httppclient.getParams().setCookiePolicy(
//    //   CookiePolicy.BROWSER_COMPATIBILITY);
//    // Compatibility policy is provided in order to mimic cookie
//    // management of popular web browsers that is in some areas
//    // not 100% standards compliant
//    
//    // Get HTTP GET method
//    GetMethod httpget = new GetMethod("http://www.smokoo.co.za");
//    
//    // Execute HTTP GET
//    int result = httpclient.executeMethod(httpget);
//    
//    // Display status code
//    System.out.println("Response status code: " + result);
//    
//    // Get all the cookies
//    Cookie[] cookies = httpclient.getState().getCookies();
//    
//    // Display the cookies
//    System.out.println("Present cookies: ");
//    for (int i = 0; i < cookies.length; i++) {
//        System.out.println(" - " + cookies[i].toExternalForm());
//    }
//    
//    // Release current connection to the connection pool 
//    // once you are done
//    httpget.releaseConnection();

	}
	
	
//Adapted from the Basic Auth example in the Apache Commonds HTTPClient examples with mfantcook's addRequestHeader thrown in the mix
//	client = new Packages.org.apache.commons.httpclient.HttpClient;
//	usrpsw = new Packages.org.apache.commons.httpclient.UsernamePasswordCredentials("myReallyLongSuperSecureApiTokenThingy", "myReallyLongSuperSecureApiTokenThingy");
//	authsc = new Packages.org.apache.commons.httpclient.auth.AuthScope("mysubdomain.mysaasapp.com", 80, "My Realm - Use CURL to discover");
//	// create a new GET method using basic authentication from above.
//	method = new Packages.org.apache.commons.httpclient.methods.GetMethod("http://mysubdomain.mysaasapp.com/myresource.xml");
//
//	// pass our credentials to HttpClient
//	client.getState().setCredentials(authsc,usrpsw);
//
//	// Tell the GET method to automatically handle authentication.
//	method.setDoAuthentication( true );
//
//	// Add an "If-None-Match" header to pass an ETag for change checking 
//	// This can MASSIVELY reduce wire transfer if the host supports it
//	// Note the embedded double quotes in the etag string, this is essential.
//	method.addRequestHeader("If-None-Match",'"4e6993f454dc19b5246cceb38376b546"');
//
//	// Add an "If-Modified-Since" header to pass an ETag for change checking 
//	// This can also reduce wire transfer but carries the added burden of time zones, etc.
//	method.addRequestHeader("If-Modified-Since","Thu, 26 Aug 2010 08:23:02 GMT");
//
//	// Add "Accept" and "Content-Type" headers, may be necessary for some APIs.
//	method.addRequestHeader("Accept","application/xml");
//	method.addRequestHeader("Content-Type","application/xml");
//
//	var status = client.executeMethod( method );
//	var message = method.getStatusText();
//	var response = method.getResponseBodyAsString();
//	var request = method.getRequestHeaders().toSource()
//	//var query = method.getQueryString(); 
//	//var path = method.getPath();
//
//	method.releaseConnection();
	
}
