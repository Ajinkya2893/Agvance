package pageObject;

import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;

public class Dashboard extends LoginPage {

	public Dashboard(ExtentTest logger) {
		super(logger);
	}

	public Dashboard acceptPermissions(){
		if(isVisible("allowPermission_id", 5)){
			click("allowPermission_id");
			log("Clicked on accept Permission");
		}
		return this;
	}

	public Dashboard denyPermissions(){
		if(isVisible("denyPermission_id", 5)){
			click("denyPermission_id");
			log("Clicked on Deny Permission");
		}
		return this;
	}

	public Dashboard acceptLocationPermission(){
		if(isVisible("onlythisPermission_id", 5)){
			click("onlythisPermission_id");
			log("Clicked on only this time Permission");
		}
		return this;
	}

	public Dashboard permissionLogic1(){
		if(isVisible("allowPermission_id", 5) || isVisible("denyPermission_id", 5) || isVisible("onlythisPermission_id", 5))
			acceptPermissions().acceptLocationPermission().acceptPermissions();
		return this;
	}

	public Dashboard selectVechileType(String VechileType){
		Assert.assertEquals(getText("SelectedVehicleText_xpath"), "Selected Vehicle");
		click("SelectedVehicleDropDown_xpath");
		log("Clicked on Vehicle DropDown button");
		typeText("SearchVehicle_id", VechileType);
		log("Entered the Vehicle Type");
		captureAndLogScreenshot("Entered VechileType "+VechileType);
		click(getPropValue("SearchedVichleValue_xpath").replace("${var}", VechileType));
		log("Select the Vehicle type : "+VechileType);
		return this;
	}

	//Selecting and changing the job type
	public Dashboard selectJobType(String VechileType){
		Assert.assertEquals(getText("JobTypeText_xpath"), "Job Type");
		click("JobTypeDropDown_xpath");
		log("Clicked on Job Type Drop Down");
		if(VechileType.contains("Blend")) {
			click("BlendJobSelect_xpath");
			log("Clicked on Blend Job section");
			captureAndLogScreenshot("Blend Job");
		}else {
			click("DeliveryJobSelect_xpath");
			log("Clicked on Delivery Job section");
			captureAndLogScreenshot("Delivery Job");
		}
		click("continueButton_xpath");
		log("Clicked on Continue Button");
		return this;
	}

	//Just changing the job type
	public Dashboard changeJobType(String VechileType){
		if(VechileType.contains("Blend")) {
			click("BlendJobSelect_xpath");
			log("Clicked on Blend Job section");
		} else {
			click("DeliveryJobSelect_xpath");
			log("Clicked on Delivery Job section");
		}
		return this;
	}

	public Dashboard changeJobDashBoard(String VechileType) {
		click("changeJobType_xpath");
		log("Change Job Type "+VechileType);
		click(getPropValue("dropDownJobChangeFrame_xpath").replace("${var}", VechileType));
		return this;
	}
}
