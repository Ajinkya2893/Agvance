package com.agvance.verifyDashboard;

import java.util.HashMap;

import org.testng.Reporter;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.mobile.base.BaseTest;

import pageObject.JobsPage;

public class JobType extends BaseTest{
    
    @Test(dataProvider = "getData")
	public void selectVehicleType(HashMap<String, String> data) throws Exception {
		if (!data.get("RunMode").equalsIgnoreCase("N") && isTestSkipped(testName)) {
			JobsPage jobs = new JobsPage(setNodeReporter(data));
			Reporter.getCurrentTestResult().setAttribute("PageInstance", jobs);
			jobs.launchApplication(data);
			jobs.getLogin(data);
			jobs
				.selectVechileType(data.get("VechileType"))
				.selectJobType(data.get("JobType"));
			jobs
				.getCountOfJobs(data.get("deliveryJobCount"))
				.getJobDetails(data.get("Ticket"))
				.changeJobDashBoard(data.get("ChangedJobType"));
			jobs.getCountOfJobs(data.get("ChangedJobCount"))
				.getJobDetails(data.get("ChangedTicket"));
			jobs.closeApp();
		} else {
			node = setNodeReporter(data).log(Status.SKIP,
                    "Skipping the test as runMode is NO in the Excel Sheet");
			throw new SkipException("Skipping the test as runMode is NO in the Excel Sheet");
        }
    }
}
