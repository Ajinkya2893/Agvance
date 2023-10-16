package apiHelper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import java.net.http.HttpResponse;

import junit.framework.Assert;

public class HttpRequestHelper {
	
	Builder builder;
	HttpResponse<String> response;
	ExtentTest node;
	public HttpRequestHelper(String URL, ExtentTest node) {
		this.node = node;
		builder = HttpRequest.newBuilder().uri(URI.create(URL));
	}
	
	public HttpRequestHelper header(String Key, String Value) {
		builder = builder.header(Key, Value);
		node.log(Status.INFO, "Added the header ( "+Key+" ) token = "+Value);
		return this;
	}
	
	public HttpRequestHelper method(String method) {
		builder = builder.method(method, HttpRequest.BodyPublishers.noBody());
		node.log(Status.INFO, "Method :: "+method);
		return this;
	}
	
	
	public HttpRequestHelper getResponse() throws Throwable {
		response = HttpClient.newHttpClient().send(builder.build(), HttpResponse.BodyHandlers.ofString());
		node.log(Status.INFO, "Response Received");
		return this;
	}
	
	public HttpRequestHelper assertStatusCode(int expected) {
		Assert.assertEquals(expected, response.statusCode());
		node.pass("Status Code Verified :: "+expected);
		return this;
	}
	
	public int getStatusCode() {
		int statusCode = response.statusCode();
		return statusCode;
	}
	
	public String getBody() {
		node.log(Status.INFO, "Response Body Received");
		return response.body().toString();
	}
	
	
		 

}
