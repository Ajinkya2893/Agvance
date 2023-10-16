package utility;

import java.util.HashMap;

import org.openqa.selenium.Capabilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;

public class AppiumIntegration extends AppiumService {

	private AppiumDriver mobileDriver;
	
	public AppiumIntegration() {
		super();
	}

	public AppiumDriver launch_Instance(String deviceType) {
		//setStartService();
		if(deviceType.startsWith("i"))
			setDriverInstance(new IOSDriver(getUrl(), setOptions(deviceType)));
		else 
			setDriverInstance(new AndroidDriver(getUrl(), setOptions(deviceType)));
		return mobileDriver;
	}
	
	public AppiumDriver launch_BRInstance(HashMap<String, String> data) {
		//setStartService();
		if(data.get("DeviceDetails").startsWith("i"))
			setDriverInstance(new IOSDriver(getUrl(), setOptions(data)));
		else 
			setDriverInstance(new AndroidDriver(getUrl(), setOptions(data)));
		return mobileDriver;
	}

	private Capabilities setOptions(String deviceType) {
		if(deviceType.startsWith("i")) {
			return new XCUITestOptions()
					.autoAcceptAlerts()
					.setDeviceName(getEnvProp().getProperty("IOS_devicename"))
					.setPlatformVersion(getEnvProp().getProperty("IOS_platformName"))
					.setBundleId(getEnvProp().getProperty("bundleId"));
		}else {
			return new UiAutomator2Options()
					.autoGrantPermissions()
					.setAutoGrantPermissions(true)
					.setDeviceName(getEnvProp().getProperty("Android_devicename"))
					.setPlatformVersion(getEnvProp().getProperty("Android_platformName"))
					.setAppPackage(getEnvProp().getProperty("AppPackage"))
					.setAppActivity(getEnvProp().getProperty("AppActivity"));
		}
	}

	private void setDriverInstance(IOSDriver iosDriver) {
		this.mobileDriver = iosDriver;
	}

	private void setDriverInstance(AndroidDriver androidDriver) {
		this.mobileDriver = androidDriver;
	}

	public AppiumDriver getDriverInstance() {
		return this.mobileDriver;
	}
}
