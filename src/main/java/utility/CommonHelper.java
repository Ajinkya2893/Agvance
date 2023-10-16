package utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Pause;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.SkipException;

import com.aventstack.extentreports.ExtentTest;

import io.appium.java_client.AppiumDriver;

public class CommonHelper extends AppiumIntegration {

	private AppiumDriver driverInstanceInstance;
	private Properties elementProp;
	private Duration timeout = Duration.ofSeconds(Constants.TIMEOUT);
	private Duration pooltime = Duration.ofMillis(Constants.POOLTIME);

	public CommonHelper(ExtentTest logger) {
		super();
		this.logger = logger;
	}

	public AppiumDriver getDriverInstance() {
		return driverInstanceInstance;
	}

	public String getPropValue(String key) throws NullPointerException {
		return elementProp.getProperty(key);
	}

	public void launchApplication(HashMap<String, String> data) {
		try (FileInputStream fs = new FileInputStream(
				Constants.PROPERTERYFILE.replace("${var}",
						data.get("DeviceDetails").startsWith("i") ? "IOS" : "Android"))) {
			elementProp = new Properties();
			elementProp.load(fs);
			driverInstanceInstance = isCouldEnable() ? launch_BRInstance(data)
					: launch_Instance(data.get("DeviceDetails"));
			log("Successfully Started the Session");
		} catch (IOException e) {
			throw new AssertionError("File Not Found or Exception occurred");
		}
	}

	public void closeApp() {
		if (isCouldEnable())
			updateBrowserStack("passed", "Successfully : Completed the Test");
		driverInstanceInstance.quit();
	}

	public void click(By element) {
		waitForElementToBeClickable(element).click();
	}

	public void click(String element) {
		waitForElementToBeClickable(getBy(element)).click();
	}

	public void click(String element, String key, String var) {
		waitForElementToBeClickable(getBy(getPropValue(element).replace(key, var))).click();
	}

	public void multiClick(String element, int clickCount) {
		for (int i = 0; i < clickCount; i++)
			click(element);
	}

	public String getText(By by) {
		return waitForElementToBePresence(by, timeout).getText();
	}

	public String getText(String element) {
		return waitForElementToBeVisible(getBy(element), timeout).getText();
	}

	public void clearTypeText(String locator, String data) {
		waitForElementToBeVisible(getBy(locator)).clear();
		waitForElementToBeVisible(getBy(locator)).sendKeys(data);
	}
	
	public void clearText(String locator) {
		waitForElementToBeVisible(getBy(locator)).clear();
	}


	public void typeText(String locator, String data) {
		waitForElementToBeVisible(getBy(locator)).sendKeys(data);
	}

	public void typeText(String locator, String data, Duration duration) {
		waitForElementToBeVisible(getBy(locator), duration).sendKeys(data);
	}

	public void clearTypeText(String locator, String data, Duration duration) {
		waitForElementToBeVisible(getBy(locator), duration).clear();
		waitForElementToBeVisible(getBy(locator), duration).sendKeys(data);
	}

	public boolean isVisible(String locator, int timeout) {
		try {
			return waitForAllElementToBeVisible(locator, Duration.ofSeconds(timeout)).size() > 0;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isVisible(WebElement locator, int timeout) {
		try {
			return waitForAllElementToBeVisible(locator, Duration.ofSeconds(timeout)).size() > 0;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isChecked(String locator) {
		try {
			return waitForElementToBeVisible(getBy(locator)).getAttribute("checked").equals("true");
		} catch (Exception e) {
			return false;
		}
	}
	
	public List<WebElement> isVisible(String locator) {
		try {
			return waitForAllElementToBeVisible(locator, timeout);
		} catch (Exception e) {
			return new ArrayList<WebElement>();
		}
	}

	public WebElement getElement(String locator) {
		return waitForElementToBePresence(getBy(locator), pooltime);
	}

	public List<WebElement> getElements(String locator) {
		return waitForAllElementToBeVisible(locator, pooltime);
	}
	
	public void drawingSign(String locator) {
			WebElement element = waitForElementToBePresence(getBy(locator), pooltime);
			Actions builder = new Actions(getDriverInstance());
			builder.moveToElement(element).perform();
			builder.clickAndHold(element).perform();
			builder.moveByOffset(20, 50).perform();
			builder.moveToElement(element).perform();
			builder.clickAndHold(element).perform();
			builder.moveByOffset(100, 50).perform();
			builder.moveToElement(element).perform();
			builder.moveByOffset(10, 50).perform();
			builder.moveToElement(element).perform();
			builder.clickAndHold(element).perform();
			builder.moveByOffset(200, 50).perform();
			builder.moveToElement(element).perform();
	}

	private By getBy(String locator) {
		try {
			switch (locator.substring(locator.lastIndexOf('_') + 1)) {
			case "xpath":
				return By.xpath(getPropValue(locator));
			case "id":
				return By.id(getPropValue(locator));
			case "name":
				return By.name(getPropValue(locator));
			case "className":
				return By.className(getPropValue(locator));
			case "css":
				return By.cssSelector(getPropValue(locator));
			case "tagName":
				return By.tagName(getPropValue(locator));
			default:
				return By.xpath(locator);
			}
		} catch (Exception e) {
			error(e.toString());
			throw new SkipException("Skipped because of no locator found");
		}
	}

	private WebElement waitForElementToBeClickable(By element) {
		return new WebDriverWait(driverInstanceInstance, timeout, pooltime)
				.until(ExpectedConditions.elementToBeClickable(element));
	}

	private WebElement waitForElementToBePresence(By by, Duration duration) {
		return new WebDriverWait(getDriverInstance(), duration, pooltime)
				.until(ExpectedConditions.presenceOfElementLocated(by));
	}

	private WebElement waitForElementToBeVisible(By elementLocator) {
		return new WebDriverWait(driverInstanceInstance, timeout, pooltime)
				.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
	}
	
	public Boolean waitForElementToBeInVisible(String elementLocator) {
		return new WebDriverWait(driverInstanceInstance, timeout, pooltime)
				.until(ExpectedConditions.invisibilityOfElementLocated(getBy(elementLocator)));
	}
	
	public Boolean waitForElementToBeInVisible(String elementLocator, int duration) {
		return new WebDriverWait(driverInstanceInstance, Duration.ofSeconds(duration), pooltime)
				.until(ExpectedConditions.invisibilityOfElementLocated(getBy(elementLocator)));
	}

	private WebElement waitForElementToBeVisible(By elementLocator, Duration duration) {
		return new WebDriverWait(driverInstanceInstance, duration, pooltime)
				.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));
	}

	private List<WebElement> waitForAllElementToBeVisible(String elementLocator, Duration duration) {
		return new WebDriverWait(driverInstanceInstance, duration, pooltime)
				.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(getBy(elementLocator)));
	}
	
	private List<WebElement> waitForAllElementToBeVisible(WebElement elementLocator, Duration duration) {
		return new WebDriverWait(driverInstanceInstance, duration, pooltime)
				.until(ExpectedConditions.visibilityOfAllElements(elementLocator));
	}

	public void switchToFrameUsingSwitchTo(String locator) {
		try {
			getDriverInstance().switchTo().frame(locator);
			waitForElementToBePresence(getBy(locator), pooltime);
		} catch (Exception e) {
			error(e.toString());
		}
	} 

	public void swipeUp() {
	    Dimension size = getDriverInstance().manage().window().getSize();
	    int startX = size.getWidth() / 2;
	    int startY = size.getHeight() / 2;
	    int endX = startX;
	    int endY = (int) (size.getHeight() * 0.25);
	    PointerInput finger1 = new PointerInput(PointerInput.Kind.TOUCH, "finger1");
	    
	    Sequence sequence = new Sequence(finger1, 1)
	        .addAction(finger1.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
	        .addAction(finger1.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
	        .addAction(new Pause(finger1, Duration.ofMillis(200)))
	        .addAction(finger1.createPointerMove(Duration.ofMillis(100), PointerInput.Origin.viewport(), endX, endY))
	        .addAction(finger1.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
	    getDriverInstance().perform(Collections.singletonList(sequence));
	}
	
	public void swipeUntilFound(String elementLocator) {
		for(int i=0; i<10; i++) {
			if(!isVisible(elementLocator, 5))
				swipeUp();
			else
				break;
		}
	}

	public String getScreenShot(WebDriver driverInstance, String screenshotName) {
		try {
			TakesScreenshot ts = (TakesScreenshot) driverInstance;
			File source = ts.getScreenshotAs(OutputType.FILE);
			int randomNumb = random.nextInt(1000);
			String destination = Constants.filePath + "/Screenshot/" + screenshotName + randomNumb + ".png";
			String pathDestination = "./Screenshot/" + screenshotName + randomNumb + ".png";
			File finalDestination = new File(destination);
			FileUtils.copyFile(source, finalDestination);
			return pathDestination;
		} catch (IOException | WebDriverException e) {
			System.err.println("Failed to capture screenshot or copy the file: " + e.getMessage());
			return null;
		}
	}

	public void captureAndLogScreenshot(String Title) {
		TakesScreenshot ts = (TakesScreenshot) driverInstanceInstance;
		byte[] screenshotBytes = ts.getScreenshotAs(OutputType.BYTES);
		String base64Screenshot = Base64.getEncoder().encodeToString(screenshotBytes);
		pass_ScreenShot(base64Screenshot, Title);
	}

	private void updateBrowserStack(String status, String reason) {
		if (isCouldEnable()) {
			((JavascriptExecutor) driverInstanceInstance).executeScript(
					"browserstack_executor: {\"action\": \"setSessionStatus\", \"arguments\": {\"status\": \""
							+ status + "\", \"reason\": \"" + reason.replaceAll("\"", "'") + "\"}}");
		}
	}

	public void threadSleep(int i) throws InterruptedException {
		Thread.sleep(i);
	}
}
