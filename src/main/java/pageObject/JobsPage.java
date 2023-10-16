package pageObject;

import java.util.HashMap;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import com.aventstack.extentreports.ExtentTest;

public class JobsPage extends Dashboard{

	public JobsPage(ExtentTest logger) {
		super(logger);
	}

	public JobsPage getCountOfJobs(String JobCount) {
		String count = getText("verifyCountOfJobs_xpath").replaceAll("[^0-9]", "");
		Assert.assertEquals(JobCount, count);
		pass("Verified the job Count");
		return this;
	}

	public JobsPage getJobDetails(String TicketName) {
		Assert.assertEquals(getText(getPropValue("ticketName_xpath").replace("${var}", TicketName)), TicketName);
		pass("Matched the ticketName :" + TicketName);
		captureAndLogScreenshot("Matched the ticketName :" + TicketName);
		return this;
	}

	public JobsPage verifyJobDetails(HashMap<String, String> data) {
		for(WebElement el: getElements("cardTextView_xpath")) {
			System.out.println(data.containsValue(el.getText()));
			log("Job details "+ el.getText());
		}
		return this;
	}

	public JobsPage selectJobByName(String locator, String JobName) {
		swipeUntilFound(getPropValue(locator).replace("${var}", JobName));
		pass("Scrolled and found the ticket");
		click(getPropValue(locator).replace("${var}", JobName));
		log("Clicked on JobName :"+JobName);
		return this;
	}

	public JobsPage verifyTicketDetails(String JobName) {
		waitForElementToBeInVisible("loaderPopUp_id", 120);
		for(WebElement el: isVisible("recylerViewText_xpath")) {
			System.out.println(JobName.contains(el.getText()));
			log("Ticket details "+ el.getText());
		}
		return this;
	}

	public JobsPage startJob() {
		waitForElementToBeInVisible("loaderPopUp_id", 120);
		click("startJobButton_xpath");
		captureAndLogScreenshot("Clicked on Start Job");
		for(WebElement el: isVisible("getTextViewElement_xpath")) {
			System.out.println(el.getText());
		}
		return this;
	}

	public JobsPage startAndCapture() {
		waitForElementToBeInVisible("loaderPopUp_id", 120);
		click("startJobAndCaptureButton_xpath");
		captureAndLogScreenshot("Clicked on Start Job And Capture");
		return this;
	}

	public JobsPage completeJob() {
		waitForElementToBeInVisible("loaderPopUp_id", 120);
		click("completeJobButton_xpath");
		captureAndLogScreenshot("Clicked on Complete Job And Capture");
		for(WebElement el: isVisible("getTextViewElement_xpath")) {
			System.out.println(el.getText());
		}
		return this;
	}

	public JobsPage captureAndReview() {
		waitForElementToBeInVisible("loaderPopUp_id", 120);
		click("reviewAndCapture_xpath");
		captureAndLogScreenshot("Clicked on Review and Capture");
		waitForElementToBeInVisible("loaderPopUp_id", 120);
		return this;
	}

	public JobsPage drawSign() {
		swipeUp();
		click("submitDrawing_xpath");
		captureAndLogScreenshot("Submited the Draw Sign");
		waitForElementToBeInVisible("loaderPopUp_id", 120);
		return this;
	}

	public JobsPage closeOpenOptions(String OptionName) {
		click(getPropValue("closeIcon_xpath").replace("${var}", OptionName));
		captureAndLogScreenshot("Closed a Open Option");
		return this;
	}

	public JobsPage startJobDataUpdate(HashMap<String, String> data) {
		log("Started updated Job Data");
		//Date Applied
		click("dateSelect_xpath");
		click("dateYear_id");
		click(getPropValue("selectYear_xpath").replace("${var}", "2023"));
		click("okButton_xpath");
		log("Updated the date");

		//Start Time
		click("timeSelect_xpath");
		click("okButton_xpath");
		log("Entered the time");
		
		//Tempature Change
		click("tempature_xpath");
		typeText("TextField_id", data.get("tempature"));
		click("submitButton_xpath");
		log("Updated Tempature Change "+data.get("tempature"));

		//Wind Speed
		click("WindSpeedSelect_xpath");
		typeText("TextField_id", data.get("WindSpeed"));
		click("submitButton_xpath");
		log("Updated WindSpeed "+data.get("WindSpeed"));

		//WindSpeed Direct
		click("WindSpeedDirectionSelect_xpath");
		click(getPropValue("selectYear_xpath").replace("${var}", data.get("WindSpeedDirection")));
		log("Selected the WindSpeed Direction "+data.get("WindSpeedDirection"));

		//Wind Meter
		if(!isChecked("windBooleanButton_xpath"))
			click("windBooleanButton_xpath");
		log("Updated the Wind Boolean Button");
		
		if(isVisible("windMeterBrandWithoutText_xpath", 5))
			typeText("windMeterBrandWithoutText_xpath", data.get("WindMeterBrand"));
		log("Updated the Wind Meter Brand Without Text");

		if(isVisible("windMeterAlreadyBrandText_xpath", 5))
			typeText("windMeterAlreadyBrandText_xpath", data.get("WindMeterBrand"));

		click(getPropValue("closeIcon_xpath").replace("${var}", "Start of Job"));
		pass("Completed the Start Job Data");
		return this;
	}

	public void clearField(String section) {
		for(WebElement el : getElements(getPropValue("clearInputField_xpath").replace("${var}", section))) {
			el.clear();
		}	
	}

	public JobsPage fieldConditionUpdate(HashMap<String, String> data) {
		clearField("Field Conditions");
		log("Cleared all fields");
		//Field Condition
		typeText("fieldConditiontext_xpath", data.get("FieldCondition"));
		log("Entered Field Conditions");
		//Soil Moisture
		typeText("soilMositure_xpath", data.get("SoilMoisture"));
		log("Entered Soil Moisture");
		//Soil texture
		typeText("soilTexture_xpath", data.get("Soiltexture"));
		log("Entered Soil texture");
		//Humidity 
		click("humiditySection_xpath");
		typeText("TextField_id", data.get("humidity"));
		click("submitButton_xpath");
		log("Entered Humidity Data");

		//weed height
		click("WeedHeightSection_xpath");
		typeText("TextField_id", data.get("WeedHeight"));
		click("submitButton_xpath");
		log("Entered Weed Height Data");

		//crop stage
		typeText("cropStageSection_xpath", data.get("CropStage"));
		log("Entered Crop Stage Data");

		//senstive crop date boolean
		if(isChecked("SenstiveCropCheckSection_xpath")) {
			multiClick("SenstiveCropCheckSection_xpath", 2);
		}else
			click("SenstiveCropCheckSection_xpath");
		log("Changed Senstive Crop Check to current date");

		//Tank Partner Site date boolean
		if(isChecked("TankPartnerSiteSection_xpath")) {
			multiClick("TankPartnerSiteSection_xpath", 2);
		}else
			click("TankPartnerSiteSection_xpath");
		log("Changed Tank Partner Site Section to current date");

		//Susceptible crops
		typeText("SusceptibleCropsText_xpath", data.get("SusceptableCrops"));
		log("Updated the crops text");

		click(getPropValue("closeIcon_xpath").replace("${var}", "Field Conditions"));
		pass("Completed the Field Job Data");
		return this;
	}

	public JobsPage vehicleUpdation(HashMap<String, String> data) {
		clearField("Vehicle");
		//applicationSpeedtext
		click("applicationSpeedtext_xpath");
		typeText("TextField_id", data.get("ApplicationSpeed"));
		click("submitButton_xpath");
		log("Entered Application Speed Data");

		//Application direction
		click("ApplicationDirectiontext_xpath");
		click(getPropValue("selectYear_xpath").replace("${var}", data.get("ApplicationDirection")));
		log("Entered Application Direction Data");

		//Nozzle Text
		typeText("nozzletext_xpath", data.get("Nozzle"));
		log("Updated the Nozzle text");

		//NozzleSpacing
		typeText("NozzleSpacing_xpath", data.get("NozzleSpacing"));
		log("Updated the Nozzle Spacing text");

		//Nozzle Pressure
		click("NozzlePressuretext_xpath");
		typeText("TextField_id", data.get("NozzlePressure"));
		click("submitButton_xpath");
		log("Entered Application Speed Data");

		//BoomHeight
		click("Boomheighttext_xpath");
		typeText("TextField_id", data.get("BoomHeight"));
		click("submitButton_xpath");
		log("Entered Application Speed Data");

		click(getPropValue("closeIcon_xpath").replace("${var}", "Vehicle"));
		pass("Completed the Vehicle Job Data");
		return this;
	}

	public JobsPage regulationUpdation(HashMap<String, String> data) throws InterruptedException {
		threadSleep(3000);
		clearField("Regulations");
		log("Cleared all the regulation Data");

		//Grower Notified
		if(isChecked("SenstiveCropCheckSection_xpath"))
			click("SenstiveCropCheckSection_xpath");
		log("Verified Grower Notified Radio button");
		
		//growed contacted
		typeText("growerContacted_xpath", data.get("growerContacted"));
		log("Updated the Grower Contacted text");
		
		//Product Supplied by
		typeText("productSuppliedBy_xpath", data.get("ProductSupplied"));
		log("Updated the Product SuppliedBy text");

		//Product Chem Job
		typeText("perviousChemJob_xpath", data.get("PreviousChemJob"));
		log("Updated the pervious Chem Job text");

		//Previous Grower
		typeText("perviousGrower_xpath", data.get("PreviousGrower"));
		log("Updated the previous Grower text");

		//Product remaining boolean
		if(!isChecked("productRemainingBoolean_xpath"))
			click("productRemainingBoolean_xpath");

		//Product Remaining textField
		typeText("productRemaining_xpath", data.get("ProductRemainig"));
		log("Updated the product Remainig reason text");

		//Crop Rotation Boolean
		if(!isChecked("cropRotationBoolean_xpath"))
			click("cropRotationBoolean_xpath");
		log("Updated the Crop Rotation Boolean");
		
		swipeUp();

		//Buffer Section
		click("bufferWidth_xpath");
		typeText("TextField_id", data.get("bufferwidth"));
		click("submitButton_xpath");
		log("Updated the BufferWidth text");

		//Rinse Boolean
		if(!isChecked("rinseBoolean_xpath"))
			click("rinseBoolean_xpath");

		//Actual Applied Comment Section
		clearTypeText("actualAppliedComment_id", data.get("actualAppliedComment"));
		log("Actual Applied Comment section Updated");

		//Precipition Boolean
		if(!isChecked("precipitationBoolean_xpath"))
			click("precipitationBoolean_xpath");

		//24 hours Max temp
		click("24hourMaxTemp_xpath");
		typeText("TextField_id", data.get("24hourMaxTemp"));
		click("submitButton_xpath");
		log("Updated 24hours Max Temp");

		//24 hours Min temp
		click("24hourMinTemp_xpath");
		typeText("TextField_id", data.get("24hourMinTemp"));
		click("submitButton_xpath");
		log("Updated 24hours Min Temp");

		//24 hours Forcast per
		click("24hourPrePerc_xpath");
		typeText("TextField_id", data.get("24hourPrePerc"));
		click("submitButton_xpath");
		log("Updated 24hours ForCast percentage");

		//24 hours Forcast Amount
		click("24hourPreAmount_xpath");
		typeText("TextField_id", data.get("24hourPreAmount"));
		click("submitButton_xpath");
		log("Updated 24hours ForCast Amount");

		//Soil Frozen
		if(!isChecked("soilFrozenBoolean_xpath"))
			click("soilFrozenBoolean_xpath");
		log("Updated Soil Frozen Boolean value");

		//Soil Covered
		if(!isChecked("soilSnowCoveredBoolean_xpath"))
			click("soilSnowCoveredBoolean_xpath");
		log("Updated Soil Covered Boolean value");
		
		swipeUp();

		//Planting date
		click("plantingDate_xpath");
		click("okButton_xpath");

		//Number of days after planning
		click("numberOfDays_xpath");
		typeText("TextField_id", data.get("NumberOFDays"));
		click("submitButton_xpath");
		log("Updated Number of days after planning");

		//before Application Clean out date
		click("beforeApplicationCleanOutDate_xpath");
		click("okButton_xpath");
		log("Updated Before Application Clean Out Date");

		//before application close
		if(isVisible("beforeApplicationCloseOut_xpath", 60))
			clearText("beforeApplicationCloseOut_xpath");
		typeText("beforeApplicationCloseOutType_xpath", data.get("beforeAppClose"));
		log("Before Application Close Out type");
		
		//After applicationCleanOutDate
		click("afterapplicationCleanDate_xpath");
		click("okButton_xpath");
		log("After Application Clean out Date updated");

		//After application close
		if(isVisible("afterapplicationCleanOutMethod_xpath", 60))
			clearText("afterapplicationCleanOutMethod_xpath");
		typeText("afterapplicationCleanOutMethodType_xpath", data.get("AfterAppClose"));
		log("Updated After App Close section");
		
		swipeUp();
		pass("Completed the Regulation Job Data");
		return this;
	}

	public JobsPage endOfJob(HashMap<String, String> data) {
		swipeUntilFound("EndOfWindDirection_xpath");
		//Time Change
		click("EndOfEndTime_xpath");
		click("okButton_xpath");

		//Tempature Change
		click("EndOfTemperature_xpath");
		typeText("TextField_id", data.get("tempature"));
		click("submitButton_xpath");
		log("Updated Tempature Change section");

		//Wind Speed
		click("EndOfWindSpeed_xpath");
		typeText("TextField_id", data.get("WindSpeed"));
		click("submitButton_xpath");
		log("Updated Wind Speed");

		//WindSpeed Direct
		click("EndOfWindDirection_xpath");
		click(getPropValue("selectYear_xpath").replace("${var}", data.get("WindSpeedDirection")));
		log("Updated Wind Speed Direction");
		
		pass("Completed the End Job Data");
		return this;
	}

	public JobsPage finaliseConditions() {
		waitForElementToBeInVisible("loaderPopUp_id", 120);
		click("finaliseButton_xpath");
		return this;
	}

	public JobsPage gotoCondition() {
		click("conditionButton_xpath");
		log("Clicked on Condition Button");
		return this;
	}

	public JobsPage gotoOverview() {
		click("overViewButton_xpath");
		log("Clicked on OverView Button");
		return this;
	}

	public JobsPage startOrComplete() {
		if(isVisible("startJobButton_xpath", 60)) {
			startJob().startAndCapture();
		}else if (isVisible("completeJobButton_xpath", 60)) {
			completeJob().captureAndReview();
		}
		return this;
	}
}
