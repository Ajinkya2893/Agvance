package utility;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.time.Duration;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

public class AppiumService extends CloudIntegration{

	private AppiumDriverLocalService service;
	protected String URL = Constants.URL;
	protected int PORT = Constants.PORT;
	public URL url;

	public AppiumService() {
		super();
	}
	
	private void startServer() {
		try {
			service=new AppiumServiceBuilder().withAppiumJS(new File("/Users/ajinkyagangawane/node_modules/appium/build/lib/main.js"))
					.withIPAddress(URL)
					.usingPort(PORT)
					.withTimeout(Duration.ofSeconds(120))
					.build();
			service.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(service.isRunning());
	}

	private void stopServer() {
		if (!checkIfServerIsRunning(PORT)) {
			service.stop();
		} else {
			System.out.println("Stopping Appium Server - " + PORT);
		}
	}

	public boolean setStopService() {
		try {
			stopServer();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean setStartService() {
		try {
			if (!checkIfServerIsRunning(PORT)) {
				startServer();
			} else {
				System.out.println("Appium Server already running on Port - " + PORT);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private boolean checkIfServerIsRunning(int port) {
		boolean isServerRunning = false;
		try (ServerSocket serverSocket = new ServerSocket(port)) {
		} catch (IOException e) {
			isServerRunning = true;
		}
		return isServerRunning;
	}	

	public URL getUrl() {
		try {
			if (isCouldEnable()) {
				url = new URL("http://" + Constants.BSTACK_USERNAME + ":" + Constants.BSTACK_ACCESS_KEY + "@hub-cloud.browserstack.com/wd/hub");
			} else {
				url = new URL("http://" + URL + ":" + PORT);
			}
			return url;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
