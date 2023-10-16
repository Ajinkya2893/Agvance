package pageObject;

import java.util.HashMap;

import com.aventstack.extentreports.ExtentTest;

public class LoginPage extends AppGeneric{

	public LoginPage(ExtentTest logger) {
		super(logger);
	}

	public LoginPage getLogin(HashMap<String, String> data){
		verifyDatabase(data).enterUserName(data.get("Username")).
		enterPassword(data.get("Password")).loginSubmit();
		return this;
	}
	
	public LoginPage enterUserName(String userName) {
		typeText("userName_xpath", userName);
		captureAndLogScreenshot("Entered Username");
		log("Entered the UserName "+userName);
		return this;
	}
	
	public LoginPage enterPassword(String password) {
		typeText("password_xpath", password);
		captureAndLogScreenshot("Entered Password");
		log("Entered the Password "+password);
		return this;
	}
	
	public LoginPage loginSubmit() {
		click("loginButton_xpath");
		captureAndLogScreenshot("Login Submitted");
		log("Clicked on Login Button");
		waitForElementToBeInVisible("loaderPopUp_id");
		return this;
	}

    public LoginPage verifyDatabase(HashMap<String, String> data) {
		multiClick("logoButton_xpath", 5);
		log("Clicked on logo icon for 5 times");
		typeText("emailRequired_id", data.get("CredentialEmail"));
		log("Entered the Credential Email Details");
		typeText("passwordRequired_id", data.get("CredentialPassword"));
		log("Entered the Credential Password Details");
		captureAndLogScreenshot("Entered the Credentials");
		click("okButton_xpath");
		log("Clicked on Ok Button");
		
		clearTypeText("skyDentity_xpath", data.get("skyDentity"));
		log("Entered the skyDentity details");
		clearTypeText("apiBase_xpath", data.get("apiBase"));
		log("Entered the skyDentity details");
		captureAndLogScreenshot("Entered the Sky Dentity details");
		click("submitButton_xpath");
		waitForElementToBeInVisible("loaderPopUp_id");
		return this;
    }
}
