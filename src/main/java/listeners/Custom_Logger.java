package listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

public class Custom_Logger{
	
	protected ExtentTest logger;
	protected Logger log = LogManager.getLogger();

	public void log(String Message) {
		log.info(Message);
		logger.log(Status.INFO, Message);
	}

	public void pass(String Message) {
		log.info(Message);
		logger.log(Status.PASS, Message);
	}
	
	public void pass_ScreenShot(String ScreenShotPath) {
		logger.log(Status.PASS, MediaEntityBuilder.createScreenCaptureFromPath(ScreenShotPath).build());
	}
	
	public void pass_ScreenShot(String Base64Screenshot, String Title) {
		logger.log(Status.PASS, MediaEntityBuilder.createScreenCaptureFromBase64String(Base64Screenshot, Title).build());
	}
	
	public void error(String Message) {
		log.error(Message);
		logger.log(Status.WARNING, Message);
	}

	public void debug(String Message) {
		log.debug(Message);
		logger.log(Status.INFO, Message);
	}

	public void fatal(String Message) {
		log.fatal(Message);
		logger.log(Status.INFO, Message);
	}

	public void warn(String Message) {
		log.warn(Message);
		logger.log(Status.WARNING, Message);
	}
}
