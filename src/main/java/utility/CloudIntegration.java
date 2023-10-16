package utility;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Random;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import listeners.Custom_Logger;

public class CloudIntegration extends Custom_Logger{

	private DesiredCapabilities browserStackCap, perfectoCap;
	private MutableCapabilities sauceLabCap;
	private Properties envProp;
	protected Random random = new Random();
	
	public Properties getEnvProp() {
		return envProp;
	}
	
	public CloudIntegration() {
		try (FileInputStream fs = new FileInputStream(Constants.PROPERTERYFILE.replace("${var}", "environment"))) {
			envProp = new Properties();
			envProp.load(fs);
		} catch (FileNotFoundException e) {
			System.err.println("File Not Found");
			throw new AssertionError("File Not Found");
		} catch (IOException e) {
			throw new AssertionError("Execption occured");
		}
	}

	public DesiredCapabilities getBrowserStackCap() {
		return browserStackCap;
	}

	public DesiredCapabilities getPerfectoCap() {
		return perfectoCap;
	}

	public MutableCapabilities getSauceLabCap() {
		return sauceLabCap;
	}

	public boolean isCouldEnable() {
		if (envProp.get("CLOUD").toString().equalsIgnoreCase("Y"))
			return true;
		else
			return false;
	}

	public boolean isGridEnable() {
		if (envProp.get("Grid").toString().equalsIgnoreCase("Y"))
			return true;
		else
			return false;
	}

	public DesiredCapabilities setBrowserStackCap(Hashtable<String, String> data, String projectName, String testRunName) {
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("browserName", data.get("browser"));
		caps.setCapability("browserVersion", data.get("browserVersion"));
		caps.setCapability("projectName", projectName);
		caps.setCapability("sessionName", testRunName);
		caps.setCapability("autoAcceptAlerts", "true");
		if (data.get("browser").equalsIgnoreCase("chrome")) {
			ChromeOptions options = new ChromeOptions();
			options.addArguments("use-fake-device-for-media-stream");
			options.addArguments("use-fake-ui-for-media-stream");
			options.addArguments("--disable-notifications");
			caps.setCapability(ChromeOptions.CAPABILITY, options);
		} else if (data.get("browser").equalsIgnoreCase("Edge")) {
			EdgeOptions options = new EdgeOptions();
			caps.setCapability("edge", options);
			caps.setCapability("browserstack.edge.enablePopups", "false");
		} else if (data.get("browser").equalsIgnoreCase("Safari")) {
			HashMap<String, Object> safariOptions = new HashMap<String, Object>();
			safariOptions.put("enablePopups", "false");
			safariOptions.put("allowAllCookies", "false");
			caps.setCapability("browserstack.safari.enablePopups", "false");
			caps.setCapability("safari", safariOptions);
			caps.setCapability("autoGrantPermissions", "true");
		}
		HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
		if (data.get("isMobile").equalsIgnoreCase("N"))
			browserstackOptions.put("os", data.get("os"));
		else {
			browserstackOptions.put("deviceName", data.get("os"));
			browserstackOptions.put("realMobile", "true");
			browserstackOptions.put("appiumVersion", "1.21.0");
		}
		browserstackOptions.put("osVersion", data.get("os_version"));
		browserstackOptions.put("seleniumVersion", "4.2.2");
		browserstackOptions.put("telemetryLogs", "true");
		browserstackOptions.put("networkLogs", "true");
		browserstackOptions.put("local", false);
		browserstackOptions.put("resolution", "1600x1200");
		browserstackOptions.put("idleTimeout", Constants.BSTACK_IDLE_TIMEOUT.toString());
		browserstackOptions.put("autoWait", Constants.BSTACK_IDLE_AUTOWAIT.toString());
		browserstackOptions.put("wsLocalSupport", true);
		caps.setCapability("bstack:options", browserstackOptions);
		browserStackCap = caps;
		return browserStackCap;
	}

	public MutableCapabilities setSauceLabsCaps(Hashtable<String, String> data, String projectName,
			String testRunName) {
		MutableCapabilities sauceOptions = new MutableCapabilities();
		sauceOptions.setCapability("build", projectName);
		sauceOptions.setCapability("username", Constants.SAUCE_USERNAME);
		sauceOptions.setCapability("accessKey", Constants.SAUCE_ACCESS_KEY);
		sauceOptions.setCapability("name", testRunName);
		MutableCapabilities capabilities = new MutableCapabilities();
		capabilities.setCapability("browserName", data.get("browser"));
		capabilities.setCapability("browserVersion", data.get("browserVersion"));
		capabilities.setCapability("platformVersion", data.get("os") + " " + data.get("os_version"));
		capabilities.setCapability("sauce:options", sauceOptions);
		sauceLabCap = capabilities;
		return sauceLabCap;
	}

	public DesiredCapabilities setPerfectoCaps() {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("platformName", "Windows");
		capabilities.setCapability("platformVersion", "10");
		capabilities.setCapability("browserName", "Chrome");
		capabilities.setCapability("browserVersion", "latest");
		capabilities.setCapability("resolution", "1280x1024");
		capabilities.setCapability("securityToken", Constants.PERFECTO_TOKEN);
		perfectoCap = capabilities;
		return perfectoCap;
	}

	public CloudIntegration addCapabilities(String key, String values) {
		if (browserStackCap != null)
			browserStackCap.setCapability(key, values);
		return this;
	}


	public DesiredCapabilities setOptions(HashMap<String, String> data) {
		DesiredCapabilities capabilities = new DesiredCapabilities();
		HashMap<String, Object> browserstackOptions = new HashMap<String, Object>();
		browserstackOptions.put("projectName", "App Version 0.1");
		browserstackOptions.put("buildName", data.get("TestName"));
		browserstackOptions.put("sessionName", data.get("DeviceDetails").replace("./", ""));
		browserstackOptions.put("appiumVersion", "2.0.1");
		browserstackOptions.put("idleTimeout", "60");
		browserstackOptions.put("autoAcceptAlerts", "true"); //to accept all alerts

		capabilities.setCapability("bstack:options", browserstackOptions);
		if(data.get("DeviceDetails").startsWith("i")) {
			capabilities.setCapability("platformName", "ios");
			capabilities.setCapability("app", envProp.get("IOS_AppUrl"));
			capabilities.setCapability("automationName", "XCUITest");
			
		} else {
			capabilities.setCapability("platformName", "android");
			capabilities.setCapability("app", envProp.get("Android_AppUrl"));
			capabilities.setCapability("automationName", "UIAutomator2");
			capabilities.setCapability("autoGrantPermissions", "true");
		}
		capabilities.setCapability("platformVersion", data.get("DeviceDetails").split("./")[1].trim());
		capabilities.setCapability("deviceName", data.get("DeviceDetails").trim().split("./")[0].trim());
		return capabilities;
	}
}
